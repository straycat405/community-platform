// 백엔드 통신용 API 설정 (AXIOS)

// 모든 API 요청의 기본 URL 설정
// 요청 인터셉터: 모든 요청에 JWT 토큰 자동 추가 (헤더에)
// 응답 인터셉터: 401 에러(인증 실패) 시 토큰 갱신 시도 또는 로그인 페이지로 이동

import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 토큰 갱신 중복 요청 방지를 위한 플래그
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

// 요청 인터셉터 - JWT 토큰 자동 추가
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 - 토큰 만료 시 자동 갱신
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // 401 에러이고, 재시도하지 않은 요청인 경우
        if (error.response?.status === 401 && !originalRequest._retry) {
            // 로그인/회원가입/토큰갱신 API는 재시도하지 않음
            if (originalRequest.url.includes('/login') ||
                originalRequest.url.includes('/register') ||
                originalRequest.url.includes('/auth/refresh')) {
                return Promise.reject(error);
            }

            if (isRefreshing) {
                // 이미 토큰 갱신 중이면 큐에 추가
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return api(originalRequest);
                }).catch(err => {
                    return Promise.reject(err);
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            const refreshToken = localStorage.getItem('refreshToken');

            if (!refreshToken) {
                // RefreshToken이 없으면 로그인 페이지로
                localStorage.removeItem('accessToken');
                window.location.href = '/login';
                return Promise.reject(error);
            }

            try {
                // 토큰 갱신 시도
                const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
                    refreshToken
                });

                const { accessToken } = response.data;
                localStorage.setItem('accessToken', accessToken);

                // 대기 중인 요청들에게 새 토큰 전달
                processQueue(null, accessToken);

                // 원래 요청 재시도
                originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                return api(originalRequest);

            } catch (refreshError) {
                // 토큰 갱신 실패 시 로그아웃 처리
                processQueue(refreshError, null);
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);

export default api;
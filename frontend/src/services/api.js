// 백엔드 통신용 API 설정 (AXIOS)

// 모든 API 요청의 기본 URL 설정
// 요청 인터셉터: 모든 요청에 JWT 토큰 자동 추가 (헤더에)
// 응답 인터셉터: 401 에러(인증 실패) 시 자동으로 로그인 페이지로 이동

import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터 - JWT 토큰 자동 추가 (TBD)
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = 'Bearer ${token}';
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 - 에러 처리
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
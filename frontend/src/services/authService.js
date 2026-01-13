// 인증 API 함수

//register(): 회원가입 API 호출
//login(): 로그인 API 호출 + 토큰을 localStorage에 저장
//logout(): 토큰 삭제
//getCurrentUser(): 현재 로그인한 사용자 정보 가져오기
// -> 백엔드 API와 실제로 통신하는 부분

import api from './api';

export const authService = {
  // 회원가입
  register: async (userData) => {
    const response = await api.post('/users/register', userData);
    return response.data;
  },

  // 로그인
  login: async (credentials) => {
    const response = await api.post('/users/login', credentials);
    const { accessToken, refreshToken, user } = response.data;

    if (accessToken && refreshToken) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    }

    return response.data;
  },

  // 로그아웃
  logout: async () => {
    const refreshToken = localStorage.getItem('refreshToken');

    // 백엔드에 로그아웃 요청 (RefreshToken DB에서 삭제)
    if (refreshToken) {
      try {
        await api.post('/auth/logout', { refreshToken });
      } catch (error) {
        console.error('로그아웃 API 호출 실패:', error);
      }
    }

    // 로컬 스토리지에서 토큰 제거
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  },

  // 토큰 갱신
  refreshToken: async () => {
    const refreshToken = localStorage.getItem('refreshToken');

    if (!refreshToken) {
      throw new Error('Refresh token not found');
    }

    const response = await api.post('/auth/refresh', { refreshToken });
    const { accessToken } = response.data;

    if (accessToken) {
      localStorage.setItem('accessToken', accessToken);
    }

    return accessToken;
  },

  // 현재 사용자 정보
  getCurrentUser: async () => {
    const response = await api.get('/users/me');
    return response.data;
  },
};
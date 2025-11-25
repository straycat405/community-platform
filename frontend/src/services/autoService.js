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
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
    }
    return response.data;
  },

  // 로그아웃
  logout: () => {
    localStorage.removeItem('token');
  },

  // 현재 사용자 정보
  getCurrentUser: async () => {
    const response = await api.get('/users/me');
    return response.data;
  },
};
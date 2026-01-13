// 상태관리 (Zustand) - Redux 대안용 React 상태 관리 라이브러리

//user, isAuthenticated, isLoading, error 같은 상태 관리
//여러 컴포넌트에서 사용자 로그인 정보를 공유
//login(), register(), logout() 액션 함수 제공
//컴포넌트 어디서든 useAuthStore()로 사용자 정보 접근 가능

import { create } from 'zustand';
import { authService } from '../services/authService';

export const useAuthStore = create((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,

  login: async (credentials) => {
    set({ isLoading: true, error: null });
    try {
      const data = await authService.login(credentials);
      set({ user: data.user, isAuthenticated: true, isLoading: false });
      return data;
    } catch (error) {
      set({ error: error.response?.data?.message || '로그인 실패', isLoading: false });
      throw error;
    }
  },

  register: async (userData) => {
    set({ isLoading: true, error: null });
    try {
      const data = await authService.register(userData);
      set({ isLoading: false });
      return data;
    } catch (error) {
      set({ error: error.response?.data?.message || '회원가입 실패', isLoading: false });
      throw error;
    }
  },

  logout: async () => {
    set({ isLoading: true });
    try {
      await authService.logout();
      set({ user: null, isAuthenticated: false, isLoading: false });
    } catch (error) {
      console.error('로그아웃 실패:', error);
      // 로그아웃은 실패해도 로컬 상태는 초기화
      set({ user: null, isAuthenticated: false, isLoading: false });
    }
  },

  clearError: () => set({ error: null }),
}));
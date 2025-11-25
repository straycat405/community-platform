import { useAuthStore } from '../store/authStore';
import { useNavigate } from 'react-router-dom';

function Home() {
  const { user, isAuthenticated, logout } = useAuthStore();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900">커뮤니티 플랫폼</h1>
          {isAuthenticated ? (
            <div className="flex items-center gap-4">
              <span className="text-gray-700">{user?.nickname}님 환영합니다!</span>
              <button
                onClick={handleLogout}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
              >
                로그아웃
              </button>
            </div>
          ) : (
            <button
              onClick={() => navigate('/login')}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              로그인
            </button>
          )}
        </div>
      </nav>
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        <h2 className="text-3xl font-bold text-gray-900 mb-4">
          개발자 커뮤니티에 오신 것을 환영합니다!
        </h2>
        <p className="text-gray-600">
          게시글 기능은 곧 추가될 예정입니다.
        </p>
      </div>
    </div>
  );
}

export default Home;
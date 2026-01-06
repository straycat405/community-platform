import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import api from '../services/api';

function Register() {
  const navigate = useNavigate();
  const { register, isLoading, error } = useAuthStore();
  
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    passwordConfirm: '',
    nickname: '',
  });

  const [validationError, setValidationError] = useState('');

  const [emailStatus, setEmailStatus] = useState({
    checking : false, //중복 사용 중인지
    available : null, //사용 가능 여부 (true/false/null)
    message : '' // 피드백 메세지
  })

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setValidationError('');
  };

  const checkEmailAvailability = async (email) => {
    //이메일 형식이 유효하지 않으면 체크하지 않음
    if (!email || !email.includes('@')) {
      setEmailStatus({ checking: false, available: null, message: '' })
      return;
    }

    setEmailStatus({ checking : true, available : null, message: '확인 중...'});

    try {
      const response = await api.get(`/users/check-email?email=${email}`);
      const { exists, message } = response.data.data;

      setEmailStatus({
        checking: false,
        available: !exists,
        message: message
      });
    } catch (err) {
      console.error('이메일 중복 확인 실패', err);
      setEmailStatus({
        checking: false,
        available: null,
        message: '확인 중 오류가 발생했습니다.'
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 비밀번호 확인 검증
    if (formData.password !== formData.passwordConfirm) {
      setValidationError('비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      await register({
        email: formData.email,
        password: formData.password,
        nickname: formData.nickname,
      });
      alert('회원가입이 완료되었습니다!');
      navigate('/login');
    } catch (err) {
      console.error('회원가입 실패:', err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="text-center text-3xl font-bold text-gray-900">
            회원가입
          </h2>
        </div>
        
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm space-y-4">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                이메일
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleChange}
                onBlur={(e) => checkEmailAvailability(e.target.value)}
                className="appearance-none rounded-lg relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="your@email.com"
              />
              {emailStatus.message && (
                <p className={`text-sm mt-1 ${
                  emailStatus.checking
                    ? 'text-gray-500'
                    : emailStatus.available
                      ? 'text-green-600'
                      : 'text-red-600'
                }`}>
                  {emailStatus.message}
                </p>
              )}
            </div>
            
            <div>
              <label htmlFor="nickname" className="block text-sm font-medium text-gray-700 mb-1">
                닉네임
              </label>
              <input
                id="nickname"
                name="nickname"
                type="text"
                required
                value={formData.nickname}
                onChange={handleChange}
                className="appearance-none rounded-lg relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="닉네임"
              />
            </div>
            
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                비밀번호
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleChange}
                className="appearance-none rounded-lg relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="비밀번호"
              />
            </div>
            
            <div>
              <label htmlFor="passwordConfirm" className="block text-sm font-medium text-gray-700 mb-1">
                비밀번호 확인
              </label>
              <input
                id="passwordConfirm"
                name="passwordConfirm"
                type="password"
                required
                value={formData.passwordConfirm}
                onChange={handleChange}
                className="appearance-none rounded-lg relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="비밀번호 확인"
              />
            </div>
          </div>

          {(validationError || error) && (
            <div className="text-red-600 text-sm text-center">
              {validationError || error}
            </div>
          )}

          <div>
            <button
              type="submit"
              disabled={isLoading || emailStatus.available === false}
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:bg-gray-400"
            >
              {isLoading ? '처리중...' : '회원가입'}
            </button>
          </div>
          
          <div className="text-center">
            <Link to="/login" className="text-sm text-blue-600 hover:text-blue-500">
              이미 계정이 있으신가요? 로그인
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Register;
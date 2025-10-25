import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AiOutlineMail, AiOutlineLock } from 'react-icons/ai'; // Icons for email & password
import LoginIllustration from '../../assets/vecteezy_modern-electric-vehicle-charging-station_47067449.png'; // Add your image in src/assets
import api from '../../services/api';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email || !password) {
      setError('Please enter email and password');
      return;
    }

    try {
      const { data } = await api.post('/User/login', {
        Email: email,
        Password: password,
      });

      const token = data.token;
      localStorage.setItem('jwtToken', token);

      const payload = JSON.parse(atob(token.split('.')[1]));
      const role = payload['http://schemas.microsoft.com/ws/2008/06/identity/claims/role'];

      if (role.toLowerCase() === 'backoffice') navigate('/backoffice');
      else if (role.toLowerCase() === 'operator') navigate('/operator');
      else setError('Unknown user role');
    } catch (err) {
      setError('Server error: ' + err.message);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="flex flex-col md:flex-row bg-white rounded-xl shadow-lg overflow-hidden w-full max-w-4xl">
        
       {/* Left: Illustration */}
<div className="hidden md:flex md:w-1/2">
  <img
    src={LoginIllustration}
    alt="EV Login Illustration"
    className="object-cover w-full h-full"
  />
</div>


        {/* Right: Login Form */}
        <div className="w-full md:w-1/2 p-10">
          <h1 className="text-3xl font-bold mb-6 text-center text-gray-700">
            EV Charging Login
          </h1>
          {error && <p className="text-red-500 mb-4 text-center">{error}</p>}

          {/* Email Input */}
          <div className="flex items-center border rounded mb-4 p-2 focus-within:ring-2 focus-within:ring-blue-400">
            <AiOutlineMail className="text-gray-400 mr-2" size={24} />
            <input
              type="email"
              placeholder="Email"
              className="w-full outline-none"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          {/* Password Input */}
          <div className="flex items-center border rounded mb-4 p-2 focus-within:ring-2 focus-within:ring-blue-400">
            <AiOutlineLock className="text-gray-400 mr-2" size={24} />
            <input
              type="password"
              placeholder="Password"
              className="w-full outline-none"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button
            className="w-full bg-blue-500 text-white p-3 rounded-lg hover:bg-blue-600 transition mb-4"
            onClick={handleLogin}
          >
            Login
          </button>

          <p className="text-sm text-gray-600 text-center">
            Don't have an account?{' '}
            <span
              onClick={() => navigate('/signup')}
              className="text-green-500 cursor-pointer font-semibold"
            >
              Sign Up
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;

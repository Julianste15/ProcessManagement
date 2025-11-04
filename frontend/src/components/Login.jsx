import React, { useState } from 'react';
import { authService } from '../services/api';
import { authUtils } from '../utils/auth';

// Asegúrate de que el logo esté en la ruta correcta
// Por ejemplo: import logoUniversidad from '../assets/logo-unicauca.png';
// Si no tienes uno, podemos usar un placeholder o que tú lo añadas.
import logoUniversidad from '../assets/logo-universidad.png'; // ASUME QUE TIENES ESTE ARCHIVO

const Login = ({ onLoginSuccess, onNavigateToRegister }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authService.login(formData.email, formData.password);
      authUtils.setToken(response.token);
      onLoginSuccess(response); // Notificamos a App.jsx
    } catch (error) {
      setError('Error al iniciar sesión: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-lg">
      <div className="flex flex-col items-center justify-center space-y-4">
        {/* Logo de la Universidad */}
        {logoUniversidad && (
          <img src={logoUniversidad} alt="Logo Universidad del Cauca" className="h-24 mx-auto mb-4" />
        )}
        
        <h1 className="text-3xl font-bold text-gray-800 uppercase tracking-wide">BIENVENIDO</h1>
        <p className="text-sm text-gray-600 text-center">Sistema de Gestión de Trabajos de Grado</p>
      </div>

      {error && (
        <div className="p-3 text-sm font-medium text-center text-red-800 bg-red-100 rounded-lg">
          {error}
        </div>
      )}

      <form className="space-y-5" onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email" className="block text-sm font-medium text-gray-700">
            Correo electrónico <span className="text-red-500">*</span>
          </label>
          <input
            id="email"
            name="email"
            type="email"
            required
            className="w-full px-4 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            value={formData.email}
            onChange={handleChange}
          />
        </div>

        <div>
          <label htmlFor="password" className="block text-sm font-medium text-gray-700">
            Contraseña <span className="text-red-500">*</span>
          </label>
          <input
            id="password"
            name="password"
            type="password"
            required
            className="w-full px-4 py-2 mt-1 text-gray-900 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            value={formData.password}
            onChange={handleChange}
          />
        </div>

        <div className="flex flex-col space-y-3 sm:flex-row sm:space-y-0 sm:space-x-4">
          <button
            type="button" // Cambiado a 'button' para no enviar el formulario
            onClick={onNavigateToRegister}
            className="w-full px-4 py-2 text-sm font-medium text-blue-700 bg-blue-100 border border-transparent rounded-md hover:bg-blue-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Registrarse
          </button>
          <button
            type="submit"
            disabled={loading}
            className="w-full px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default Login;
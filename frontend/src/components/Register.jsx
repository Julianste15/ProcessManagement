// src/components/Register.jsx
import React, { useState } from 'react';
import { userService } from '../services/api';

import logoUniversidad from '../assets/logo-universidad.png';

const Register = ({ onNavigateToLogin }) => {
  const [formData, setFormData] = useState({
    names: '',
    surnames: '',
    phone: '',
    email: '',
    password: '',
    career: 'SISTEMAS',
    role: 'ESTUDIANTE',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

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
    setSuccess('');

    try {
      const userData = { ...formData };
      await userService.register(userData);
      setSuccess('¡Registro exitoso! Ahora puedes volver e iniciar sesión.');
      
      setTimeout(() => {
        onNavigateToLogin();
      }, 3000);
    } catch (err) {
      setError('Error en el registro: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    // 3. El componente de Registro TAMBIÉN ES TRANSPARENTE.
    // App.jsx (el padre) ya le da el fondo gris oscuro.
    <div className="w-full p-4 sm:p-6 lg:p-8">
      {/* Cabecera */}
      <header className="flex flex-col sm:flex-row justify-between items-center text-white mb-6">
        {/* Botón Volver */}
        <button
          onClick={onNavigateToLogin}
          className="bg-white text-gray-800 font-semibold px-4 py-2 rounded-lg shadow-md hover:bg-gray-100 transition duration-300 self-start"
        >
          &larr; Volver al Login
        </button>

        {/* Logo y Título */}
        <div className="flex flex-col items-center my-4 sm:my-0">
          <img src={logoUniversidad} alt="Logo Universidad" className="h-20" />
          <h1 className="text-3xl font-bold mt-2">Universidad del Cauca</h1>
          <p className="text-xl">Registro de Usuario</p>
        </div>

        {/* Espaciador (para centrar el logo) */}
        <div className="hidden sm:block w-36"></div>
      </header>

      {/* Tarjeta del Formulario */}
      <main className="flex justify-center">
        <div className="w-full max-w-3xl p-8 bg-white rounded-lg shadow-2xl">
          <h2 className="text-2xl font-bold text-center text-gray-900 mb-6"> {/* Texto oscuro */}
            Complete sus datos
          </h2>

          <form onSubmit={handleSubmit}>
            {/* Contenedor de la Grilla */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
              
              {/* Nombres */}
              <div>
                <label htmlFor="names" className="block text-sm font-medium text-gray-700">
                  Nombres <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  id="names"
                  name="names"
                  value={formData.names}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Apellidos */}
              <div>
                <label htmlFor="surnames" className="block text-sm font-medium text-gray-700">
                  Apellidos <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  id="surnames"
                  name="surnames"
                  value={formData.surnames}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Teléfono (Opcional) */}
              <div>
                <label htmlFor="phone" className="block text-sm font-medium text-gray-700">
                  Teléfono
                </label>
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Correo electrónico */}
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                  Correo electrónico <span className="text-red-500">*</span>
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Contraseña */}
              <div className="md:col-span-2">
                <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                  Contraseña <span className="text-red-500">*</span>
                </label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              {/* Carrera */}
              <div>
                <label htmlFor="career" className="block text-sm font-medium text-gray-700">
                  Carrera <span className="text-red-500">*</span>
                </label>
                <select
                  id="career"
                  name="career"
                  value={formData.career}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="SISTEMAS">Ingeniería de Sistemas</option>
                  <option value="ELECTRONICA_TELECOM">Ingeniería Electrónica y Telecomunicaciones</option>
                  <option value="AUTOMATICA">Ingeniería en Automática Industrial</option>
                  <option value="TELEMATICA">Tecnología en Telemática</option>
                </select>
              </div>

              {/* Rol */}
              <div>
                <label htmlFor="role" className="block text-sm font-medium text-gray-700">
                  Rol <span className="text-red-500">*</span>
                </label>
                <select
                  id="role"
                  name="role"
                  value={formData.role}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 mt-1 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="ESTUDIANTE">Estudiante</option>
                  <option value="PROFESOR">Profesor</option>
                  <option value="ADMIN">Administrador</option>
                </select>
              </div>
            </div>

            {/* Mensajes de Error o Éxito */}
            {error && (
              <div className="mt-6 p-3 text-center text-sm font-medium text-red-800 bg-red-100 rounded-lg">
                {error}
              </div>
            )}
            {success && (
              <div className="mt-6 p-3 text-center text-sm font-medium text-green-800 bg-green-100 rounded-lg">
                {success}
              </div>
            )}

            {/* Botón de Registrarse */}
            <div className="mt-8 text-center">
              <button
                type="submit"
                disabled={loading}
                className="w-full sm:w-auto px-8 py-3 text-lg font-medium text-white bg-blue-600 border border-transparent rounded-md shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Registrando...' : 'Registrarse'}
              </button>
            </div>

            {/* Campos Obligatorios */}
            <p className="mt-4 text-sm text-center text-red-600">
              * Campos obligatorios
            </p>

          </form>
        </div>
      </main>
    </div>
  );
};

export default Register;


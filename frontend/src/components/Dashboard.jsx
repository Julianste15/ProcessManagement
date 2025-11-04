import React, { useState, useEffect } from 'react';
import { userService, authService } from '../services/api';
// No necesitas importar authUtils aquí, App.js se encargará del logout

const Dashboard = ({ onLogout }) => {
  const [userProfile, setUserProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadUserProfile();
  }, []); // El array vacío asegura que se ejecute solo al montar

  const loadUserProfile = async () => {
    try {
      const profile = await userService.getProfile();
      setUserProfile(profile);
    } catch (error) {
      setError('Error cargando perfil: ' + error.message);
      if (error.message.includes('401') || error.message.includes('403')) {
        // Si el token es inválido, le decimos a App.js que cierre sesión
        onLogout();
      }
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      // Notificamos al backend (opcional pero recomendado)
      await authService.logout();
    } catch (err) {
      console.error("Error notificando al backend el logout:", err);
    } finally {
      // Le decimos a App.js que ejecute el logout
      onLogout();
    }
  };

  return (
    <div className="w-full max-w-5xl p-6 mx-auto bg-white rounded-lg shadow-xl sm:p-8">
      <div className="flex flex-col items-start justify-between pb-4 border-b border-gray-200 sm:flex-row sm:items-center">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard - FIET</h1>
        <button
          onClick={handleLogout}
          className="w-full px-4 py-2 mt-4 text-sm font-medium text-white bg-red-600 rounded-md shadow-sm sm:w-auto sm:mt-0 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
        >
          Cerrar Sesión
        </button>
      </div>
      
      <div className="mt-6">
        {loading && (
          <div className="text-center text-gray-600">Cargando perfil...</div>
        )}
        {error && (
          <div className="p-4 text-center text-red-800 bg-red-100 rounded-lg">{error}</div>
        )}
        
        {userProfile && (
          <div>
            <div className="p-6 bg-gray-50 border border-gray-200 rounded-lg">
              <h3 className="text-xl font-semibold text-gray-800">Información del Usuario</h3>
              <div className="grid grid-cols-1 gap-4 mt-4 md:grid-cols-2">
                <p><strong>Nombre:</strong> {userProfile.names} {userProfile.surnames}</p>
                <p><strong>Email:</strong> {userProfile.email}</p>
                <p><strong>Rol:</strong> <span className="px-2 py-0.5 text-sm font-medium text-blue-800 bg-blue-100 rounded-full">{userProfile.role}</span></p>
                <p><strong>Carrera:</strong> {userProfile.career}</p>
              </div>
            </div>
            
            {userProfile?.role === 'ADMIN' && (
              <div className="mt-8">
                <h3 className="text-xl font-semibold text-gray-800">Panel de Administración</h3>
                <div className="mt-4">
                  <button className="px-5 py-2 text-white bg-gray-800 rounded-md shadow-sm hover:bg-gray-700">
                    Gestionar Usuarios
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
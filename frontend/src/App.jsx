// src/App.jsx
import React, { useState, useEffect } from 'react';

// Importamos los componentes
import Login from './components/Login.jsx';
import Dashboard from './components/Dashboard.jsx';
import Register from './components/Register.jsx';

// Importamos los servicios y utilidades
import { authService } from './services/api'; // <--- Usamos el servicio de API
import { authUtils } from './utils/auth';

const App = () => {
  const [view, setView] =useState('loading'); // 'loading', 'login', 'register', 'dashboard'
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Función para validar el token al cargar la app (Versión segura)
    const checkAuthStatus = async () => {
      const token = authUtils.getToken();
      if (token) {
        try {
          // 1. Validamos el token contra el backend (más seguro)
          const validationResponse = await authService.validateToken();
          // 2. Si es válido, guardamos los datos del usuario
          setUser(validationResponse.user); 
          setView('dashboard');
        } catch (error) {
          // 3. Si el token es inválido (expirado, etc.)
          console.error("Token validation failed:", error);
          authUtils.removeToken();
          setView('login');
        }
      } else {
        setView('login');
      }
    };

    checkAuthStatus();
  }, []);

  // Navegación
  const handleLoginSuccess = (loginResponse) => {
    setUser(loginResponse.user); // Guardamos los datos del usuario del login
    setView('dashboard');
  };

  const handleLogout = async () => {
    try {
      // Le avisamos al backend que cerramos sesión (opcional pero bueno)
      await authService.logout();
    } catch (error) {
      console.error("Error during logout:", error);
    } finally {
      authUtils.removeToken();
      setUser(null);
      setView('login');
    }
  };

  const renderView = () => {
    switch (view) {
      case 'login':
        return (
          <Login 
            onLoginSuccess={handleLoginSuccess}
            onNavigateToRegister={() => setView('register')}
          />
        );
      case 'register':
        return (
          <Register 
            onNavigateToLogin={() => setView('login')}
          />
        );
      case 'dashboard':
        return (
          <Dashboard 
            user={user} // Pasamos el usuario al Dashboard
            onLogout={handleLogout} 
          />
        );
      default:
        return null; // No mostrar nada mientras carga (el 'loading' se maneja abajo)
    }
  };

  return (
    // ESTA ES LA ARQUITECTURA CORRECTA:
    // 1. El fondo azul brillante (el que te gustó) se pone AQUÍ, en el App.jsx
    <div className="flex items-center justify-center min-h-screen bg-blue-900">
      
      {/* 2. Mostramos "Cargando..." o la vista correspondiente */}
      {view === 'loading' ? (
         <div className="text-xl font-medium text-gray-400">Cargando...</div>
      ) : (
         renderView()
      )}

    </div>
  );
};

export default App;



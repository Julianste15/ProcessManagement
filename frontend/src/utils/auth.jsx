export const authUtils = {
  // Guardar token en localStorage
  setToken: (token) => {
    localStorage.setItem('authToken', token);
  },
  
  // Obtener token
  getToken: () => {
    return localStorage.getItem('authToken');
  },
  
  // Remover token (logout)
  removeToken: () => {
    localStorage.removeItem('authToken');
  },
  
  // Verificar si el usuario está autenticado
  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },
  
  // Decodificar el token para obtener información del usuario
  getUserFromToken: () => {
    const token = localStorage.getItem('authToken');
    if (!token) return null;
    
    try {
      // Decodifica la parte 'payload' (la segunda) del token JWT
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        email: payload.sub,
        role: payload.role,
        exp: payload.exp,
      };
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  },
};
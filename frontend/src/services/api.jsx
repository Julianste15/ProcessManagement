// src/services/api.js
const API_BASE_URL = 'http://localhost:8080';

// Helper para hacer requests con auth
const apiRequest = async (endpoint, options = {}) => {
  const token = localStorage.getItem('authToken');
  
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };
  
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
    
    // --- INICIO DE LA CORRECCIÓN ---

    // Primero, leemos la respuesta como TEXTO, no como JSON directo
    const responseText = await response.text();

    if (!response.ok) {
      // Si hay un error, intentamos usar el texto como mensaje
      // Si el texto está vacío, usamos el statusText
      // --- CORRECCIÓN AQUÍ: Se añadieron backticks (`) ---
      throw new Error(`Error ${response.status}: ${responseText || response.statusText}`);
    }

    // Ahora, verificamos si el texto de la respuesta está vacío
    if (responseText.length === 0) {
      // Si está vacío (como en tu caso de registro), 
      // devolvemos 'null' o un objeto de éxito.
      return null; 
    }

    // Si NO está vacío, entonces sí lo parseamos como JSON
    return JSON.parse(responseText);

    // --- FIN DE LA CORRECCIÓN ---

  } catch (error) {
    console.error('API Request failed:', error);
    throw error;
  }
};

// Servicios de Autenticación
export const authService = {
  login: async (email, password) => {
    return apiRequest('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  },
  
  validateToken: async () => {
    return apiRequest('/api/auth/validate', {
      method: 'GET',
    });
  },
  
  logout: async () => {
    return apiRequest('/api/auth/logout', {
      method: 'POST',
    });
  },
};

// Servicios de Usuario
export const userService = {
  register: async (userData) => {
    return apiRequest('/api/users/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  },
  
  getProfile: async () => {
    return apiRequest('/api/users/profile');
  },
  
  getAllUsers: async () => {
    return apiRequest('/api/users/all');
  },
};


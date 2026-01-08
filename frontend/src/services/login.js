import apiClient from '@/services/axios'

// Nombre de la clave en localStorage donde se guarda el token JWT
const TOKEN_KEY = 'token' 

 // Clase que agrupa los métodos de autenticación (login, logout, register, me)
class AuthService {
  async login(request) { // Método asíncrono para iniciar sesión; recibe el objeto request (ej. { username, password })
    const { data } = await apiClient.post("auth/login", request) // Envía POST a /auth/signin con las credenciales y extrae la respuesta (data)
    // Si el backend devuelve token en la respuesta, lo guardas en localStorage
    if (data.token) { // Comprueba si la respuesta contiene un token
      localStorage.setItem(TOKEN_KEY, data.token) // Guarda el token en localStorage bajo TOKEN_KEY
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${data.token}` // Añade el header Authorization por defecto para futuras peticiones
    }
    return data // Devuelve los datos completos de la respuesta (puede incluir usuario, roles, etc.)
  }
  // Logout solo en frontend: limpia el token local y elimina el header Authorization de axios
  logout() {
    localStorage.removeItem(TOKEN_KEY) // Elimina el token del localStorage
    if (apiClient?.defaults?.headers?.common) {
      delete apiClient.defaults.headers.common['Authorization'] // Quita el header Authorization por defecto de Axios
    }
  }

  async register(request) { // Método asíncrono para registrar un nuevo usuario
    const { data } = await apiClient.post("/auth/signup", request) // Envía POST a /auth/signup con los datos de registro
    return data // Devuelve la respuesta del backend (confirmación, usuario creado, etc.)
  }


}

export default new AuthService() // Exporta una instancia de AuthService para usarla directamente en la aplicación

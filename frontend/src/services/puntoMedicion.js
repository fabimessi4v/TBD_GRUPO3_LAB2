import apiClient from '@/services/axios'

export default {
  getInvalidos() {
    return apiClient.get('/puntos/invalidos')
  },
}

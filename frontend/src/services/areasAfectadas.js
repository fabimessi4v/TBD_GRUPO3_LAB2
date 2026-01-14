import apiClient from '@/services/axios'

export default {
    // Obtener todas las áreas afectadas con geometría
    // Usa paginación con size grande para obtener todas las áreas para el mapa
    getAll(page = 0, size = 1000, nombre = '') {
        return apiClient.get('/areas', {
            params: { page, size, nombre }
        })
    },

    // Obtener áreas paginadas (útil para tablas)
    getPaginated(page = 0, size = 10, nombre = '') {
        return apiClient.get('/areas', {
            params: { page, size, nombre }
        })
    },

    // Obtener una área específica por ID (requiere endpoint en backend)
    getById(id) {
        return apiClient.get(`/areas/${id}`)
    }
}


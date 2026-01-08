<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import {
  CCard, CCardHeader, CCardBody,
  CSpinner, CTable, CTableHead, CTableHeaderCell,
  CTableRow, CTableBody, CTableDataCell
} from '@coreui/vue'

const loading = ref(true)
const puntos = ref([])

const obtenerDatos = async () => {
  try {
    const response = await apiClient.get('/puntos/georreferencia')
    datos.value = response.data
  } catch (err) {
    console.error(err)
  }
}

onMounted(async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/puntos/georreferencia')
    console.log( data)
    puntos.value = data
  } catch (error) {
    console.error( error)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="p-4">
    <CCard class="shadow-sm">
      <CCardHeader>
        <h4 class="mb-0">Georreferenciación nula</h4>
      </CCardHeader>

      <CCardBody>
        <div v-if="loading" class="text-center my-4">
          <CSpinner color="primary" grow />
          <p class="mt-2">Cargando puntos...</p>
        </div>

        <div v-else>
          <CTable hover responsive bordered>
            <CTableHead color="light">
              <CTableRow>
                <CTableHeaderCell>ID Punto</CTableHeaderCell>
                <CTableHeaderCell>Nombre del Punto</CTableHeaderCell>
                <CTableHeaderCell>Última Medición</CTableHeaderCell>
              </CTableRow>
            </CTableHead>

            <CTableBody>
              <CTableRow v-for="(punto, index) in puntos" :key="index">
                <CTableDataCell>{{ punto.idPunto }}</CTableDataCell>
                <CTableDataCell>{{ punto.nombrePunto }}</CTableDataCell>
                <CTableDataCell>
                  {{ punto.ultimaMedicion ? new Date(punto.ultimaMedicion).toLocaleString() : 'Sin medición' }}
                </CTableDataCell>
              </CTableRow>
            </CTableBody>
          </CTable>
        </div>
      </CCardBody>
    </CCard>
  </div>
</template>

<style scoped>
h4 {
  font-weight: 600;
}
</style>
<script setup>
import apiClient from '@/services/axios'
import { ref, onMounted } from 'vue'
import {
  CCard, CCardHeader, CCardBody,
  CSpinner, CTable, CTableHead, CTableHeaderCell,
  CTableRow, CTableBody, CTableDataCell,
  CButton
} from '@coreui/vue'

const loading = ref(true)
const puntos = ref([])

const obtenerDatos = async () => {
  loading.value = true
  try {
    const response = await apiClient.get('/deteccionzonasderiesgo/obtenerpunto')
    puntos.value = response.data
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  obtenerDatos()
})
</script>

<template>
  <div class="p-4">
    <CCard class="shadow-sm">
      <CCardHeader class="d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Zonas de riesgo</h4>

        <CButton color="primary" variant="outline" size="sm" @click="obtenerDatos" :disabled="loading">
          Actualizar
        </CButton>
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
                <CTableHeaderCell>Nombre Punto</CTableHeaderCell>
                <CTableHeaderCell>Tipo Sensor</CTableHeaderCell>
                <CTableHeaderCell>ID Area</CTableHeaderCell>
                <CTableHeaderCell>Nombre Area</CTableHeaderCell>
                <CTableHeaderCell>Tipo Riesgo</CTableHeaderCell>
              </CTableRow>
            </CTableHead>

            <CTableBody>
              <CTableRow v-for="(punto, index) in puntos" :key="index">
                <CTableDataCell>{{ punto.idPunto }}</CTableDataCell>
                <CTableDataCell>{{ punto.nombrePunto }}</CTableDataCell>
                <CTableDataCell>{{ punto.tipoSensor }}</CTableDataCell>
                <CTableDataCell>{{ punto.idArea }}</CTableDataCell>
                <CTableDataCell>{{ punto.nombreArea }}</CTableDataCell>
                <CTableDataCell>{{ punto.tipoRiesgo }}</CTableDataCell>
              </CTableRow>

              <CTableRow v-if="puntos.length === 0">
                <CTableDataCell colspan="6" class="text-center text-muted">
                  No hay datos para mostrar
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



<template>
  <CRow class="justify-content-center">
    <CCol :md="8">
      <CCard class="mb-4">
        <CCardHeader>
          Eventos Extremos de Temperatura
        </CCardHeader>
        <CCardBody>
          <CTable v-if="eventos.length > 0" bordered hover small>
            <CTableHead>
              <CTableRow>
                <CTableHeaderCell>Fecha</CTableHeaderCell>
                <CTableHeaderCell>Máxima Temperatura (°C)</CTableHeaderCell>
              </CTableRow>
            </CTableHead>
            <CTableBody>
              <CTableRow v-for="evento in eventos" :key="evento.fecha">
                <CTableDataCell>{{ evento.fecha }}</CTableDataCell>
                <CTableDataCell>{{ evento.maximaTemperatura }}</CTableDataCell>
              </CTableRow>
            </CTableBody>
          </CTable>
          <div v-else class="text-muted small">
            No hay datos de eventos extremos de temperatura.
          </div>
        </CCardBody>
      </CCard>
    </CCol>
  </CRow>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { CRow, CCol, CCard, CCardBody, CCardHeader, CTable, CTableHead, CTableRow, CTableHeaderCell, CTableBody, CTableDataCell } from '@coreui/vue'

const eventos = ref([])
const baseUrl = import.meta.env.VITE_BASE_URL

const fetchEventos = async () => {
  try {
    const res = await fetch(`${baseUrl}/api/mediciones/eventos-extremos-temperatura`)
    if (!res.ok) throw new Error('Error en la petición')
    eventos.value = await res.json()
  } catch (e) {
    eventos.value = []
  }
}

onMounted(() => {
  fetchEventos()
})
</script>
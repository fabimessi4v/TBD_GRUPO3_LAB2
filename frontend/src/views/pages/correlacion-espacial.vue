<template>
  <CRow class="justify-content-center">
    <CCol :md="8">
      <CCard class="mb-4">
        <CCardHeader>
          Consulta de correlación
        </CCardHeader>
        <CCardBody>
          <div class="mb-4">
            <CFormLabel for="idSelect">Selecciona idPuntoTemperatura</CFormLabel>
            <CFormSelect id="idSelect" v-model="selectedId">
              <option v-for="id in ids" :key="id" :value="id">
                {{ id }}
              </option>
            </CFormSelect>
          </div>
          <div>
            <CTable v-if="correlaciones.length > 0" bordered hover small>
              <CTableHead>
                <CTableRow>
                  <CTableHeaderCell># Temp</CTableHeaderCell>
                  <CTableHeaderCell># CO2</CTableHeaderCell>
                  <CTableHeaderCell>Distancia (km)</CTableHeaderCell>
                </CTableRow>
              </CTableHead>
              <CTableBody>
                <CTableRow v-for="item in correlaciones" :key="item.idPuntoCO2">
                  <CTableDataCell>{{ item.idPuntoTemperatura }}</CTableDataCell>
                  <CTableDataCell>{{ item.idPuntoCO2 }}</CTableDataCell>
                  <CTableDataCell>{{ item.distanciaKm }}</CTableDataCell>
                </CTableRow>
              </CTableBody>
            </CTable>
            <div v-else class="text-muted small">
              No hay datos para el id seleccionado.
            </div>
          </div>
        </CCardBody>
      </CCard>
    </CCol>
  </CRow>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue' // Importa watch para observar cambios
import { CRow, CCol, CCard, CCardBody, CCardHeader, CFormSelect, CFormLabel, CTable, CTableHead, CTableRow, CTableHeaderCell, CTableBody, CTableDataCell } from '@coreui/vue'

const ids = [1, 2, 3, 4, 5] // Puedes cambiar los ids disponibles aquí
const selectedId = ref(ids[0]) // ID seleccionado
const correlaciones = ref([]) // Datos de correlaciones
const baseUrl = import.meta.env.VITE_BASE_URL // URL base de la API

const fetchCorrelacion = async () => { // Función para obtener correlaciones
  try {
    const res = await fetch(`${baseUrl}/api/puntos/correlacion?idPuntoTemperatura=${selectedId.value}`) // Llama a la API con el ID seleccionado
    if (!res.ok) throw new Error('Error en la petición') // Manejo de errores
    correlaciones.value = await res.json() // Almacena la respuesta en correlaciones
  } catch (e) {
    correlaciones.value = [] // Si hay un error, establece correlaciones como un array vacío
  }
}

onMounted(() => { // Al montar el componente
  fetchCorrelacion() // Llama a la función para obtener correlaciones
})

// Observa cambios en selectedId y llama a fetchCorrelacion cuando cambie
watch(selectedId, (newId) => {
  fetchCorrelacion() // Llama a la función para obtener correlaciones con el nuevo ID
})
</script>
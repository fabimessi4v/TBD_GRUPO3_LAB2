<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import apiClient from '@/services/axios' 
import {
  CCard,
  CCardHeader,
  CCardBody,
  CFormSelect,
  CButton,
  CSpinner,
  CTable,
  CTableHead,
  CTableHeaderCell,
  CTableBody,
  CTableRow,
  CTableDataCell
} from '@coreui/vue'

const loading = ref(false)
const datos = ref([])

const filtroPrincipal = ref('')
const filtroSecundario = ref('')
const ordenFechas = ref('desc')

const datasetsDisponibles = ref([])
const sensoresDisponibles = ref([])
const estacionesDisponibles = ref([])

const obtenerTendencia = async (params = {}) => {
  loading.value = true
  try {
    const response = await apiClient.get('/mediciones/tendencia-mensual', { params })
    datos.value = response.data

    datasetsDisponibles.value = [...new Set(response.data.map(i => i.nombreDataset))].filter(Boolean)
    sensoresDisponibles.value = [...new Set(response.data.map(i => i.tipoSensor))].filter(Boolean)
    estacionesDisponibles.value = [...new Set(response.data.map(i => i.nombrePunto))].filter(Boolean)
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const aplicarFiltro = () => {
  const params = {}
  if (filtroPrincipal.value === 'dataset' && filtroSecundario.value)
    params.dataset = filtroSecundario.value
  else if (filtroPrincipal.value === 'sensor' && filtroSecundario.value)
    params.sensor = filtroSecundario.value
  else if (filtroPrincipal.value === 'estacion' && filtroSecundario.value)
    params.estacion = filtroSecundario.value

  obtenerTendencia(params)
}

watch(filtroSecundario, (nuevoValor) => {
  if (nuevoValor) aplicarFiltro()
})

const grupos = computed(() => {
  if (!filtroPrincipal.value) return { 'Datos': datos.value }

  let campo = ''
  if (filtroPrincipal.value === 'dataset') campo = 'nombreDataset'
  else if (filtroPrincipal.value === 'sensor') campo = 'tipoSensor'
  else if (filtroPrincipal.value === 'estacion') campo = 'nombrePunto'

  const agrupado = {}
  datos.value.forEach((item) => {
    const clave = item[campo] || 'Sin categoría'
    if (!agrupado[clave]) agrupado[clave] = []
    agrupado[clave].push(item)
  })

  for (const grupo in agrupado) {
    agrupado[grupo].sort((a, b) =>
      ordenFechas.value === 'desc'
        ? b.mes.localeCompare(a.mes)
        : a.mes.localeCompare(b.mes)
    )
  }

  return agrupado
})

onMounted(() => obtenerTendencia())
</script>

<template>
  <div class="p-4">
    <CCard class="shadow-sm">
      <CCardHeader>
        <h4 class="mb-0">Tendencia Mensual</h4>
      </CCardHeader>

      <CCardBody>
        <div class="row mb-4">
          <div class="col-md-3">
            <CFormSelect
              v-model="filtroPrincipal"
              label="Filtrar por:"
              :options="[
                { label: 'Seleccionar', value: '' },
                { label: 'Dataset', value: 'dataset' },
                { label: 'Sensor', value: 'sensor' },
                { label: 'Estación', value: 'estacion' }
              ]"
            />
          </div>

          <div class="col-md-3" v-if="filtroPrincipal">
            <CFormSelect
              v-model="filtroSecundario"
              label="Selecciona una opción:"
              :options="[
                { label: 'Seleccionar...', value: '' },
                ...(filtroPrincipal === 'dataset'
                  ? datasetsDisponibles
                  : filtroPrincipal === 'sensor'
                  ? sensoresDisponibles
                  : estacionesDisponibles
                ).map(o => ({ label: o, value: o }))
              ]"
            />
          </div>

          <div class="col-md-3">
            <CFormSelect
              v-model="ordenFechas"
              label="Ordenar por fecha:"
              :options="[
                { label: 'Más reciente', value: 'desc' },
                { label: 'Más antiguo', value: 'asc' }
              ]"
            />
          </div>

          <div class="col-md-3 d-flex align-items-end">
            <CButton color="secondary" class="w-100" @click="() => { filtroPrincipal = ''; filtroSecundario = ''; obtenerTendencia(); }">
              Limpiar filtros
            </CButton>
          </div>
        </div>

        <div v-if="loading" class="text-center my-4">
          <CSpinner color="primary" grow />
          <p class="mt-2">Cargando datos...</p>
        </div>

        <div v-else>
          <div v-for="(items, grupo) in grupos" :key="grupo" class="mb-5">
            <h5 class="fw-bold mb-3">{{ grupo }}</h5>

            <CTable hover responsive bordered>
              <CTableHead color="light">
                <CTableRow>
                  <CTableHeaderCell>Sensor</CTableHeaderCell>
                  <CTableHeaderCell>Dataset</CTableHeaderCell>
                  <CTableHeaderCell>Estación</CTableHeaderCell>
                  <CTableHeaderCell>Fecha</CTableHeaderCell>
                  <CTableHeaderCell>Promedio Mensual</CTableHeaderCell>
                </CTableRow>
              </CTableHead>

              <CTableBody>
                <CTableRow v-for="(item, index) in items" :key="index">
                  <CTableDataCell>{{ item.tipoSensor }}</CTableDataCell>
                  <CTableDataCell>{{ item.nombreDataset }}</CTableDataCell>
                  <CTableDataCell>{{ item.nombrePunto }}</CTableDataCell>
                  <CTableDataCell>{{ item.mes }}</CTableDataCell>
                  <CTableDataCell>{{ item.promedioMensual }}</CTableDataCell>
                </CTableRow>
              </CTableBody>
            </CTable>
          </div>

          <div v-if="Object.keys(grupos).length === 0" class="text-center text-muted">
            <p>No hay datos.</p>
          </div>
        </div>
      </CCardBody>
    </CCard>
  </div>
</template>

<style scoped>
h4 {
  font-weight: 600;
}
h5 {
  font-weight: 600;
  margin-top: 1rem;
  color: #333;
}
label {
  font-weight: 500;
}
</style>

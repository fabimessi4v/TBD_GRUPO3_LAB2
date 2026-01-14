<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import areasAfectadasService from '@/services/areasAfectadas'
import {
  CCard,
  CCardBody,
  CCardHeader,
  CRow,
  CCol,
  CSpinner,
  CListGroup,
  CListGroupItem
} from '@coreui/vue'
// State
const map = ref(null)
const loading = ref(true)
const areas = ref([])
const selectedArea = ref(null)
const error = ref(null)

// Color mapping for different risk types
const riskColors = {
  'Inundación': '#3b82f6',    // Blue
  'Bajo': '#4ade80',          // Green
  'Medio': '#fbbf24',         // Yellow
  'Alto': '#fb923c',          // Orange
  'Crítico': '#ef4444',       // Red
  'Muy Alto': '#dc2626'       // Dark Red
}


// Initialize map
const initMap = () => {
  try {
    // Create map centered on Chile
    map.value = L.map('map-container').setView([-33.4489, -70.6693], 6)
    // Add OpenStreetMap tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(map.value)
  } catch (err) {
    console.error('Error inicializando mapa:', err)
    error.value = 'Error al inicializar el mapa'
  }
}

// Load areas from backend
const loadAreas = async () => {
  try {
    loading.value = true
    error.value = null
    
    const response = await areasAfectadasService.getAll()
    
    areas.value = response.data
    
    // Add polygons to map
    addPolygonsToMap()
  } catch (err) {
    console.error('Error loading areas:', err)
    console.error('Error details:', err.response?.data || err.message)
    error.value = 'Error al cargar las áreas afectadas: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
    
    if (map.value) {
      await nextTick() // Esperar a que v-show actualice el DOM
      map.value.invalidateSize()
    }
  }
}

// Add polygons to map
const addPolygonsToMap = () => {
  if (!map.value) {
    console.warn('Mapa no disponible para agregar polígonos')
    return
  }


  areas.value.forEach((area, index) => {
    console.log(`   Área ${index + 1}:`, {
      id: area.id,
      nombre: area.nombre,
      tipoRiesgo: area.tipoRiesgo,
      geomType: typeof area.geom,
      hasGeom: !!area.geom,
      geomPreview: area.geom?.substring(0, 50) + '...'
    })

    if (area.geom) {
      try {
        let geojson
        
        if (typeof area.geom === 'string') {
          try {
            geojson = JSON.parse(area.geom)
          } catch (jsonErr) {
            if (/^(POLYGON|POINT|LINESTRING|MULTIPOLYGON)/i.test(area.geom)) {
              console.log(`Detectado WKT, convirtiendo a GeoJSON...`)
              geojson = wktToGeoJSON(area.geom)
              if (!geojson) {
                throw new Error('No se pudo convertir WKT a GeoJSON')
              }
            } else {
              throw new Error('Formato de geometría no reconocido: ' + jsonErr.message)
            }
          }
        } 
        // Si ya es un objeto, usar directamente
        else if (typeof area.geom === 'object') {
          geojson = area.geom
          console.log(`GeoJSON directo (objeto)`)
        } else {
          throw new Error('Tipo de geometría no soportado')
        }

        // Determine color based on risk type
        const color = riskColors[area.tipoRiesgo] || '#6B7280'
        console.log(`Color asignado: ${color} para riesgo: ${area.tipoRiesgo}`)

        // Create polygon layer
        const polygon = L.geoJSON(geojson, {
          style: {
            color: color,
            weight: 2,
            opacity: 0.8,
            fillColor: color,
            fillOpacity: 0.3
          }
        })

        // Add click event
        polygon.on('click', () => {
          selectArea(area)
        })

        // Add to map
        polygon.addTo(map.value)
        console.log(`Polígono agregado al mapa`)

        // Store reference
        area.layer = polygon
      } catch (err) {
        console.error(`Error procesando área ${area.id}:`, err)
        console.error(`Geometría problemática:`, area.geom)
      }
    } else {
      console.warn(`Área sin geometría (geom es null/undefined)`)
    }
  })

  // Fit map to show all polygons if we have areas
  if (areas.value.length > 0) {
    const layersWithGeom = areas.value.filter(a => a.layer).map(a => a.layer)
    console.log(`Ajustando vista del mapa para ${layersWithGeom.length} polígonos`)
    
    if (layersWithGeom.length > 0) {
      const group = new L.featureGroup(layersWithGeom)
      if (group.getBounds().isValid()) {
        map.value.fitBounds(group.getBounds(), { padding: [50, 50] })
        console.log('Vista del mapa ajustada a los límites de los polígonos')
      } else {
        console.warn('Límites del grupo no son válidos')
      }
    }
  }
  
  console.log('Proceso de agregar polígonos completado')
}

// Select an area
const selectArea = (area) => {
  // Reset previous selection
  if (selectedArea.value && selectedArea.value.layer) {
    selectedArea.value.layer.setStyle({
      weight: 2,
      opacity: 0.8
    })
  }

  // Highlight selected area
  selectedArea.value = area
  if (area.layer) {
    area.layer.setStyle({
      weight: 4,
      opacity: 1
    })
    area.layer.bringToFront()
  }
}

// Lifecycle hooks
onMounted(async () => {
  console.log('Component mounted')
  
  // Usar nextTick es más eficiente que setTimeout
  await nextTick()
  
  // Verificar que el contenedor existe
  const container = document.getElementById('map-container')
  if (!container) {
    console.error('Contenedor del mapa no encontrado')
    error.value = 'No se pudo encontrar el contenedor del mapa'
    loading.value = false
    return
  }
  
  // Inicializar mapa inmediatamente
  initMap()
  
  // Cargar áreas en paralelo (no bloquear la UI)
  loadAreas()
})

onUnmounted(() => {
  if (map.value) {
    map.value.remove()
  }
})
</script>

<template>
  <div>
    <CRow>
      <!-- Main column: Map (8 of 12 columns) -->
      <CCol :md="8" class="order-md-1">
        <CCard class="mb-4">
          <CCardBody>
            <h4 class="card-title mb-3">Mapa de Áreas Afectadas</h4>
            
            <!-- Loading spinner -->
            <div v-if="loading" class="text-center my-5">
              <CSpinner color="primary" />
              <p class="mt-2">Cargando áreas...</p>
            </div>

            <!-- Error message -->
            <div v-if="error && !loading" class="alert alert-danger">
              {{ error }}
            </div>

            <!-- Map container - siempre renderizado para que Leaflet pueda inicializarse -->
            <div 
              id="map-container" 
              style="height: 500px; border-radius: 8px; z-index: 1;"
              v-show="!loading && !error"
            ></div>
          </CCardBody>
        </CCard>
      </CCol>

      <!-- Side panel: Area information (4 of 12 columns) -->
      <CCol :md="4" class="order-md-2">
        <CCard class="mb-4">
          <CCardHeader>
            <strong>Panel de Información</strong>
          </CCardHeader>
          <CCardBody>
            <!-- No area selected -->
            <div v-if="!selectedArea" class="text-muted text-center py-4">
              <i class="cil-info" style="font-size: 2rem;"></i>
              <p class="mt-3">
                Haz clic en un área del mapa para ver sus detalles
              </p>
            </div>

            <!-- Selected area details -->
            <div v-else>
              <h5 class="mb-3">{{ selectedArea.nombre }}</h5>
              
              <CListGroup flush>
                <CListGroupItem>
                  <strong>ID:</strong> {{ selectedArea.id }}
                </CListGroupItem>
                <CListGroupItem>
                  <strong>Descripción:</strong><br>
                  {{ selectedArea.descripcion || 'Sin descripción' }}
                </CListGroupItem>
                <CListGroupItem>
                  <strong>Tipo de Riesgo:</strong><br>
                  <span 
                    class="badge"
                    :style="{ 
                      backgroundColor: riskColors[selectedArea.tipoRiesgo] || '#6B7280',
                      color: 'white',
                      padding: '0.5rem 1rem',
                      fontSize: '0.9rem',
                      borderRadius: '0.375rem'
                    }"
                  >
                    {{ selectedArea.tipoRiesgo || 'No especificado' }}
                  </span>
                </CListGroupItem>
              </CListGroup>
            </div>

            <!-- Areas count -->
            <div class="mt-3 pt-3 border-top text-center">
              <small class="text-muted">
                Total de áreas: <strong>{{ areas.length }}</strong>
              </small>
            </div>
          </CCardBody>
        </CCard>
      </CCol>
    </CRow>
  </div>
</template>

<style scoped>
.card-title {
  font-weight: 600;
  color: var(--cui-heading-color);
}

#map-container {
  position: relative;
}

.gap-2 {
  gap: 0.5rem;
}
</style>
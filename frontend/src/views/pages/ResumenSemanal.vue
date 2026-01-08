<script setup>
import axios from 'axios'
import { ref } from 'vue'

const availableDataset = ref([])
const mostrarLista = ref(false)
const mostrarIngresarId = ref(false)
const idDataset = ref('')

const limpiarPantalla = () => {
  mostrarLista.value = false
  availableDataset.value = []
}

const fetchListReportSummary = async () => {
  limpiarPantalla()
  mostrarIngresarId.value = false
  try {
    const url = import.meta.env.VITE_BASE_URL + '/api/resumensemanal/obtenerListaResumen'
    const response = await axios.get(url)
    availableDataset.value = response.data
    mostrarLista.value = true
  } catch (error) {
    alert('Error al recuperar los datos del reporte semanal: ' + error.message)
  }
}

const fetchListById = async () => {
  limpiarPantalla()
  try {
    if (!idDataset.value) {
      alert('Debes ingresar un ID de dataset.')
      return
    }
    const url = import.meta.env.VITE_BASE_URL + '/api/resumensemanal/obtenerListaResumenEspecifico/' + idDataset.value
    const response = await axios.get(url)
    availableDataset.value = response.data
    mostrarLista.value = true
  } catch (error) {
    alert('Error al recuperar el dataset: ' + error.message)
  }
}

const updateListById = async () => {
  limpiarPantalla()
  try {
    if (!idDataset.value) {
      alert('Debes ingresar un ID de dataset.')
      return
    }
    const url = import.meta.env.VITE_BASE_URL + '/api/resumensemanal/recalcularResumen/' + idDataset.value
    await axios.post(url)
    alert('Resumen recalculado exitosamente para el dataset ' + idDataset.value)
  } catch (error) {
    alert('Error al recalcular el resumen: ' + error.message)
  }
}

const updateListByIdAndShow = async () => {
  limpiarPantalla()
  try {
    if (!idDataset.value) {
      alert('Debes ingresar un ID de dataset.')
      return
    }
    const url = import.meta.env.VITE_BASE_URL + '/api/resumensemanal/recalcularResumenyObtener/' + idDataset.value
    const response = await axios.post(url)
    availableDataset.value = response.data
    mostrarLista.value = true
  } catch (error) {
    alert('Error al recalcular y obtener el resumen: ' + error.message)
  }
}

const mostrarCampoId = () => {
  limpiarPantalla()
  mostrarIngresarId.value = true
}

const ocultarTodo = () => {
  mostrarIngresarId.value = false
  mostrarLista.value = false
  availableDataset.value = []
}
</script>

<template>
  <div>
    <h3>Reporte semanal</h3>

    <div class="button-container">
      <button @click="mostrarCampoId" class="btn-cargar">
        Calcular y obtener resumen semanal por ID
      </button>

      <button @click="fetchListReportSummary" class="btn-cargar">
        Reporte de todos los datasets
      </button>

      <button 
        v-if="mostrarIngresarId || mostrarLista" 
        @click="ocultarTodo" 
        class="btn-ocultar">
        Ocultar
      </button>
    </div>

    <div v-if="mostrarIngresarId" class="input-container">
      <input v-model="idDataset" type="number" placeholder="Ingrese ID del dataset" class="input-id" />
      <button @click="updateListById" class="btn-cargar">Calcular resumen semanal</button>
      <button @click="updateListByIdAndShow" class="btn-cargar">Calcular y Mostrar resumen semanal</button>
      <button @click="fetchListById" class="btn-cargar">Buscar</button>
    </div>

    <ul v-if="mostrarLista && availableDataset.length">
      <li v-for="objeto in availableDataset" :key="objeto.idDataset" class="report-item">
        <div class="report-details">
          <span class="report-info">ID Dataset: {{ objeto.idDataset }}</span>
          <span class="report-info">Semana: {{ objeto.semana }}</span>
          <span class="report-info">Promedio: {{ objeto.promedio }}</span>
        </div>
      </li>
    </ul>

    <router-link to="/seleccion" class="btn-volver">
      Volver al menu de seleccion
    </router-link>
  </div>
</template>

<style scoped>
.button-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
  margin-top: 30px;
  margin-bottom: 20px;
}

.btn-cargar {
  background-color: #3c3d3f;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.btn-cargar:hover {
  background-color: #0056b3;
}

.btn-ocultar {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.btn-ocultar:hover {
  background-color: #a71d2a;
}

.btn-volver {
  position: fixed;
  top: 40px;
  left: 20px;
  background-color: #007bff;
  color: white;
  padding: 10px 16px;
  border-radius: 6px;
  text-decoration: none;
}

.input-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.input-id {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 220px;
}

.report-item {
  background: #f8f9fa;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ddd;
  list-style: none;
}

.report-info {
  display: block;
  color: #333;
}

h3 {
  text-align: center;
  margin-top: 20px;
}
</style>

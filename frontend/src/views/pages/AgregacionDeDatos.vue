<script setup>
import axios from 'axios'
import { ref } from 'vue'

const availableDataset = ref([])
const idDataset = ref('')
const beginDate = ref('')
const endDate = ref('')
const noDataFound = ref(false)

const addData = async () => {
  try {
    if (!idDataset.value || !beginDate.value || !endDate.value) {
      alert('Debes ingresar un ID de dataset, una fecha de inicio y una fecha de término.')
      return
    }

    const url = `${import.meta.env.VITE_BASE_URL}/api/agregaciondedatos/obtenerserietemporal`
    const body = {
      idDataset: parseInt(idDataset.value),
      fechaComienzo: beginDate.value,
      fechaTermino: endDate.value
    }

    const response = await axios.post(url, body)
    availableDataset.value = response.data

    noDataFound.value = !availableDataset.value.length
  } catch (error) {
    console.error(error)
    alert(
      'Error al obtener la serie temporal: ' +
        (error.response?.status
          ? `Request failed with status code ${error.response.status}`
          : error.message)
    )
  }
}
</script>

<template>
  <div>
    <h3>Obtener serie temporal</h3>

    <div class="input-container">
      <input
        type="number"
        v-model="idDataset"
        placeholder="Ingrese ID del dataset"
        class="input-id"
      />

      <label>Fecha de inicio:</label>
      <input type="date" v-model="beginDate" class="input-date" />

      <label>Fecha de término:</label>
      <input type="date" v-model="endDate" class="input-date" />

      <button @click="addData" class="btn-cargar">Enviar</button>
    </div>

    <ul v-if="availableDataset.length">
      <li
        v-for="objeto in availableDataset"
        :key="objeto.idDataset + objeto.periodo"
        class="serie-item"
      >
        <div class="report-details">
          <span class="report-info">ID Dataset: {{ objeto.idDataset }}</span><br />
          <span class="report-info">Periodo: {{ objeto.periodo }}</span><br />
          <span class="report-info">Valor: {{ objeto.valor }}</span>
        </div>
      </li>
    </ul>

    <p v-else-if="noDataFound" class="no-data">
      No se encontraron datos para el rango de fechas seleccionado.
    </p>

    <router-link to="/seleccion" class="btn-volver">
      Volver al menú de selección
    </router-link>
  </div>
</template>

<style scoped>
.input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

.input-id,
.input-date {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 220px;
}

.btn-volver {
  position: fixed;
  top: 40px;
  left: 20px;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.btn-cargar {
  background-color: #3c3d3f;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;
}

h3 {
  text-align: center;
  margin-top: 20px;
}

.btn-cargar:hover {
  background-color: #0056b3;
}

.serie-item {
  list-style: none;
  background: #f8f9fa;
  margin: 8px 0;
  padding: 10px 15px;
  border-radius: 8px;
  width: 300px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.report-info {
  font-size: 15px;
  color: #000000;
}

.report-details {
  display: flex;
  flex-direction: column;
  gap: 1px; 
  background: #d3edf2;
  padding: 10px;
  border-radius: 8px;
}

.no-data {
  text-align: center;
  color: #dc3545;
  font-weight: 500;
  margin-top: 20px;
}
</style>
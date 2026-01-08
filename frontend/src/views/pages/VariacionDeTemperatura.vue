<script setup>
import { ref, onMounted } from "vue";
import { getTopVariacionTemperatura } from "@/services/mediciones";

const data = ref([]);
const error = ref("");
const loading = ref(true);

const formatNumber = (value) => {
  if (value === null || value === undefined || isNaN(value)) return "—";
  return Number(value).toFixed(2);
};

onMounted(async () => {
  try {
    const res = await getTopVariacionTemperatura();
    data.value = res.data ?? [];
  } catch (e) {
    error.value = "No se pudo cargar la variación de temperatura";
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="metric-container">
    <h2 class="metric-title">
      Top 10 variación de temperatura
      <span class="subtitle">(últimos 5 años)</span>
    </h2>

    <p v-if="loading" class="info">Cargando datos…</p>
    <p v-else-if="error" class="error">{{ error }}</p>

    <div v-else class="table-wrapper">
      <table v-if="data.length" class="metric-table">
        <thead>
          <tr>
            <th>#</th>
            <th>ID Punto</th>
            <th>Desviación estándar (°C)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in data" :key="row.idPunto">
            <td>{{ index + 1 }}</td>
            <td>{{ row.idPunto }}</td>
            <td class="numeric">
              {{ formatNumber(row.desviacionStd) }}
            </td>
          </tr>
        </tbody>
      </table>

      <p v-else class="info">No hay datos disponibles.</p>
    </div>
  </div>
</template>

<style scoped>
.metric-container {
  max-width: 720px;
  margin: 40px auto;
  background: #ffffff;
  padding: 24px 28px;
  border-radius: 10px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

.metric-title {
  text-align: center;
  margin-bottom: 20px;
}

.subtitle {
  display: block;
  font-size: 0.85em;
  color: #666;
  margin-top: 4px;
}

.table-wrapper {
  overflow-x: auto;
}

.metric-table {
  width: 100%;
  border-collapse: collapse;
}

.metric-table thead {
  background-color: #2c3e50;
  color: white;
}

.metric-table th,
.metric-table td {
  padding: 10px 12px;
  text-align: center;
}

.metric-table tbody tr:nth-child(even) {
  background-color: #f4f6f8;
}

.metric-table tbody tr:hover {
  background-color: #e9f0f7;
}

.numeric {
  font-weight: bold;
}

.info {
  text-align: center;
  color: #555;
}

.error {
  text-align: center;
  color: #c0392b;
  font-weight: bold;
}
</style>

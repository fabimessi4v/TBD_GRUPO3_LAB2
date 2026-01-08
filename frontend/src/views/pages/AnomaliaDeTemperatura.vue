<script setup>
import { ref, onMounted } from "vue";
import { getAnomaliasTemperatura } from "@/services/mediciones";

const data = ref([]);
const error = ref("");
const loading = ref(true);

const formatNumber = (value) => {
  if (value === null || value === undefined || isNaN(value)) return "—";
  return Number(value).toFixed(2);
};

onMounted(async () => {
  try {
    const res = await getAnomaliasTemperatura();
    data.value = res.data ?? [];
  } catch (e) {
    error.value = "No se pudo cargar la anomalía de temperatura";
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="metric-container">
    <h2 class="metric-title">
      Anomalía de temperatura
      <span class="subtitle">(último año vs histórico)</span>
    </h2>

    <p v-if="loading" class="info">Cargando datos…</p>
    <p v-else-if="error" class="error">{{ error }}</p>

    <table v-else-if="data.length" class="metric-table">
      <thead>
        <tr>
          <th>ID Punto</th>
          <th>Anomalía (°C)</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in data" :key="row.idPunto">
          <td>{{ row.idPunto }}</td>
          <td
            :class="{
              positive: row.anomalia > 0,
              negative: row.anomalia < 0
            }"
          >
            {{ formatNumber(row.anomalia) }}
          </td>
        </tr>
      </tbody>
    </table>

    <p v-else class="info">No hay datos disponibles.</p>
  </div>
</template>

<style scoped>
.metric-container {
  max-width: 600px;
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
}

.metric-table {
  width: 100%;
  border-collapse: collapse;
}

.metric-table th,
.metric-table td {
  padding: 10px;
  text-align: center;
}

.metric-table thead {
  background-color: #2c3e50;
  color: white;
}

.metric-table tbody tr:nth-child(even) {
  background-color: #f4f6f8;
}

.positive {
  color: #27ae60;
  font-weight: bold;
}

.negative {
  color: #c0392b;
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

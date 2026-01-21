<template>
  <div class="page">
    <h1>Interpolación climática</h1>

    <!-- Botones -->
    <div class="card">
      <div class="actions">
        <button class="btn-outline" @click="aplicarInterpolacion" :disabled="loading">
          Interpolación
        </button>

        <button class="btn-ghost" @click="limpiarInterpolacion" :disabled="loading">
          Limpiar interpolación
        </button>
      </div>
    </div>

    <!-- Resumen -->
    <div v-if="puntos.length" class="card">
      <h2>Resumen</h2>
      <div class="grid">
        <div><b>Total puntos:</b> {{ puntos.length }}</div>
        <div><b>Nulos:</b> {{ nulos }}</div>
        <div><b>Interpolados:</b> {{ interpolados }}</div>
      </div>
    </div>

    <!-- Tabla -->
    <div class="card">
      <h2>Puntos</h2>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Sensor</th>
              <th>Longitud</th>
              <th>Latitud</th>
              <th>Valor</th>
              <th>Interpolación</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="(p, idx) in puntos"
              :key="idx"
              :class="{ interp: p.interpolacion }"
            >
              <td>{{ p.nombre }}</td>
              <td>{{ p.sensor }}</td>
              <td>{{ fmtCoord(p.longitud) }}</td>
              <td>{{ fmtCoord(p.latitud) }}</td>
              <td>
                <span v-if="p.valor !== null">{{ p.valor }}</span>
                <span v-else class="blank">—</span>
              </td>
              <td>
                <span v-if="p.interpolacion" class="pill">Sí</span>
                <span v-else class="muted">No</span>
              </td>
            </tr>

          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import apiClient from "@/services/axios";

const puntos = ref([]);
const loading = ref(false);
const error = ref("");
const mensaje = ref("");

function fmtCoord(n) {
  if (n === null || n === undefined) return "";
  return Number(n).toFixed(6);
}

const nulos = computed(() =>
  puntos.value.filter(p => p.valor === null).length
);

const interpolados = computed(() =>
  puntos.value.filter(p => p.interpolacion).length
);

async function cargarPuntos() {
  loading.value = true;
  error.value = "";
  try {
    const { data } = await apiClient.get("/consultas/puntos", {
      params: { t: Date.now() }
    });

    puntos.value = (data.data ?? []).map(p => ({
      nombre: p.nombre,
      sensor: p.sensor,
      longitud: p.longitud,
      latitud: p.latitud,
      valor: p.valor,
      interpolacion: p.interpolacion
    }));
  } catch (e) {
    error.value = "Error al cargar puntos";
    puntos.value = [];
  } finally {
    loading.value = false;
  }
}

async function aplicarInterpolacion() {
  loading.value = true;
  try {
    const { data } = await apiClient.post("/consultas/interpolacion/aplicar");
    mensaje.value = data.mensaje;
    await cargarPuntos();
  } finally {
    loading.value = false;
  }
}

async function limpiarInterpolacion() {
  loading.value = true;
  try {
    const { data } = await apiClient.post("/consultas/interpolacion/limpiar");
    mensaje.value = data.mensaje;
    await cargarPuntos();
  } finally {
    loading.value = false;
  }
}

onMounted(async () => {
  await cargarPuntos();
});
</script>


<style scoped>
.page { max-width: 1100px; margin: 32px auto; padding: 0 16px; font-family: system-ui, Arial; }
.card { border: 1px solid #ddd; border-radius: 12px; padding: 16px; margin-top: 16px; background: #fff; }

.top-row { display: flex; gap: 16px; align-items: end; justify-content: space-between; flex-wrap: wrap; }

.filter { display: grid; gap: 8px; min-width: 320px; }
.label { font-weight: 700; }
select { padding: 10px; border-radius: 10px; border: 1px solid #ccc; font-weight: 500; background: #fff; }

.actions { display: flex; gap: 10px; flex-wrap: wrap; }
button { padding: 10px 14px; border-radius: 10px; border: 1px solid #111; background: #111; color: #fff; cursor: pointer; }
button:disabled { opacity: 0.7; cursor: not-allowed; }
.btn-outline { background: #fff; color: #111; }
.btn-ghost { background: #f6f6f6; color: #111; border-color: #ddd; }

.status { margin-top: 10px; }
.error { color: #b00020; font-weight: 600; margin: 6px 0 0; }
.ok { color: #0a7a2f; font-weight: 600; margin: 6px 0 0; }
.muted { color: #666; margin: 6px 0 0; }

.grid { display: grid; gap: 8px; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); margin-top: 10px; }

.table-wrap { overflow: auto; margin-top: 10px; border-radius: 12px; border: 1px solid #eee; }
table { width: 100%; border-collapse: collapse; min-width: 800px; }
th, td { padding: 10px 12px; border-bottom: 1px solid #eee; text-align: left; vertical-align: middle; }
thead th { position: sticky; top: 0; background: #fafafa; z-index: 1; }

tr.interp { background: #fff9e6; }
.blank { color: #aaa; }
.center { text-align: center; }
.pill { display: inline-block; padding: 2px 10px; border-radius: 999px; background: #111; color: #fff; font-size: 12px; }
</style>

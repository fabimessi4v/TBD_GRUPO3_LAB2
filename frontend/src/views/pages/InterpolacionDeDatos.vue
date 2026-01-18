<template>
  <div class="page">
    <h1>Interpolación climática</h1>

    <!-- Controles -->
    <form class="card" @submit.prevent="cargarPuntos">

      <div class="actions">

        <button type="button" class="btn-outline" @click="aplicarInterpolacion" :disabled="loading">
          Interpolar
        </button>

        <button type="button" class="btn-ghost" @click="limpiarInterpolacion" :disabled="loading">
          Limpiar interpolacion
        </button>
      </div>
    </form>

    <!-- Resumen -->
    <div v-if="puntos.length" class="card">
      <h2>Resumen</h2>
      <div class="grid">
        <div><b>Total puntos:</b> {{ puntos.length }}</div>
        <div><b>Nulos:</b> {{ enBlanco }}</div>
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
              :key="pKey(p, idx)"
              :class="{ interp: !!p.interpolacion }"
            >
              <td>{{ p.nombre ?? "-" }}</td>
              <td>{{ p.sensor ?? "-" }}</td>
              <td>{{ fmtCoord(p.longitud) }}</td>
              <td>{{ fmtCoord(p.latitud) }}</td>

              <td>
                <span v-if="p.valor !== null && p.valor !== undefined">{{ p.valor }}</span>
                <span v-else class="blank">—</span>
              </td>

              <td>
                <span v-if="p.interpolacion" class="pill">Sí</span>
                <span v-else class="muted">No</span>
              </td>
            </tr>

            <tr v-if="!loading && puntos.length === 0">
              <td colspan="6" class="muted center">No hay puntos cargados aún.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from "vue";
import apiClient from "@/services/axios";

const iddataset = ref(1);

const puntos = ref([]);
const loading = ref(false);
const error = ref("");
const mensaje = ref("");

function fmtCoord(n) {
  if (n === null || n === undefined) return "";
  const x = Number(n);
  return Number.isFinite(x) ? x.toFixed(6) : String(n);
}

function pKey(p, idx) {
  // key estable aunque no venga idpunto
  return `${p.nombre ?? "?"}-${p.longitud ?? "?"}-${p.latitud ?? "?"}-${idx}`;
}

const enBlanco = computed(() =>
  puntos.value.filter(p => p.valor === null || p.valor === undefined).length
);

const interpolados = computed(() =>
  puntos.value.filter(p => !!p.interpolacion).length
);

async function cargarPuntos() {
  error.value = "";
  mensaje.value = "";

  if (!Number.isInteger(iddataset.value) || iddataset.value <= 0) {
    error.value = "iddataset debe ser un entero >= 1";
    return;
  }

  loading.value = true;
  try {
    const { data } = await apiClient.get("/consultas/puntos", {
      params: { t: Date.now() },
    });

    const arr = Array.isArray(data?.data) ? data.data : (Array.isArray(data) ? data : null);

    if (!arr) {
      puntos.value = [];
      console.log("Respuesta backend:", data);
      error.value = "Respuesta OK pero no trae lista. Revisa consola.";
      return;
    }

    puntos.value = arr.map((p) => ({
      nombre: p.nombre ?? p.Nombre,
      sensor: p.sensor ?? p.tiposensor ?? p.TipoSensor,
      longitud: p.longitud ?? p.lon ?? p.Longitud,
      latitud: p.latitud ?? p.lat ?? p.Latitud,
      valor: p.valor ?? p.valor_final ?? p.ValorFinal,
      interpolacion: p.interpolacion ?? p.es_interpolado ?? p.EsInterpolado,
    }));

    mensaje.value = `Puntos cargados: ${puntos.value.length}`;
  } catch (e) {
    puntos.value = [];
    const status = e?.response?.status;
    if (status === 401) error.value = "401: No autorizado. Inicia sesión.";
    else error.value = e?.response?.data?.error ?? e?.message ?? "Error al cargar puntos.";
  } finally {
    loading.value = false;
  }
}

async function aplicarInterpolacion() {
  error.value = "";
  mensaje.value = "";
  loading.value = true;
  try {
    const { data } = await apiClient.post("/consultas/interpolacion/aplicar");
    mensaje.value = data?.mensaje ?? "Interpolación aplicada.";
    await cargarPuntos();
  } catch (e) {
    const status = e?.response?.status;
    if (status === 401) error.value = "401: No autorizado. Inicia sesión.";
    else error.value = e?.response?.data?.error ?? e?.message ?? "Error al aplicar interpolación.";
  } finally {
    loading.value = false;
  }
}

async function limpiarInterpolacion() {
  error.value = "";
  mensaje.value = "";
  loading.value = true;
  try {
    const { data } = await apiClient.post("/consultas/interpolacion/limpiar");
    mensaje.value = data?.mensaje ?? "Interpolación limpiada.";
    await cargarPuntos();
  } catch (e) {
    const status = e?.response?.status;
    if (status === 401) error.value = "401: No autorizado. Inicia sesión.";
    else error.value = e?.response?.data?.error ?? e?.message ?? "Error al limpiar interpolación.";
  } finally {
    loading.value = false;
  }
}
onMounted(async () => {
  await limpiarInterpolacion();   // apaga flag + refresh MVs
  // limpiarInterpolacion() ya llama cargarPuntos() al final en tu código,
  // así que esto puede ser opcional:
  // await cargarPuntos();
});

</script>

<style scoped>
.page { max-width: 1100px; margin: 32px auto; padding: 0 16px; font-family: system-ui, Arial; }
.card { border: 1px solid #ddd; border-radius: 12px; padding: 16px; margin-top: 16px; background: #fff; }
.actions { display: flex; gap: 10px; flex-wrap: wrap; margin-top: 8px; }
label { display: grid; gap: 6px; font-weight: 600; }
input { padding: 10px; border-radius: 10px; border: 1px solid #ccc; font-weight: 500; min-width: 220px; }

button { padding: 10px 14px; border-radius: 10px; border: 1px solid #111; background: #111; color: #fff; cursor: pointer; }
button:disabled { opacity: 0.7; cursor: not-allowed; }
.btn-outline { background: #fff; color: #111; }
.btn-ghost { background: #f6f6f6; color: #111; border-color: #ddd; }

.error { color: #b00020; margin: 10px 0 0; font-weight: 600; }
.ok { color: #0a7a2f; margin: 10px 0 0; font-weight: 600; }

.grid { display: grid; gap: 8px; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); margin-top: 10px; }

.table-wrap { overflow: auto; margin-top: 10px; border-radius: 12px; border: 1px solid #eee; }
table { width: 100%; border-collapse: collapse; min-width: 800px; }
th, td { padding: 10px 12px; border-bottom: 1px solid #eee; text-align: left; vertical-align: middle; }
thead th { position: sticky; top: 0; background: #fafafa; z-index: 1; }

tr.interp { background: #fff9e6; }
.blank { color: #aaa; }
.center { text-align: center; }
.muted { color: #666; }

.pill { display: inline-block; padding: 2px 10px; border-radius: 999px; background: #111; color: #fff; font-size: 12px; }
</style>

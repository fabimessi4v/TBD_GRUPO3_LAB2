<template>
  <div class="p-4">
    <CCard class="shadow-sm">
      <CCardHeader class="d-flex align-items-center justify-content-between">
        <h4 class="mb-0">Georreferenciación nula</h4>

        <button
          class="btn btn-sm btn-dark"
          type="button"
          @click="cargarPuntos"
          :disabled="loading"
        >
          {{ loading ? "Actualizando..." : "Actualizar" }}
        </button>

      </CCardHeader>

      <CCardBody>
        <div v-if="loading" class="text-center my-4">
          <CSpinner color="primary" grow />
          <p class="mt-2">Cargando puntos...</p>
        </div>

        <div v-else>
          <p v-if="errorMsg" class="text-danger fw-semibold mb-3">
            {{ errorMsg }}
          </p>

          <CTable hover responsive bordered>
            <CTableHead color="light">
              <CTableRow>
                <CTableHeaderCell>ID Punto</CTableHeaderCell>
                <CTableHeaderCell>Nombre del Punto</CTableHeaderCell>
                <CTableHeaderCell>Latitud</CTableHeaderCell>
                <CTableHeaderCell>Longitud</CTableHeaderCell>
                <CTableHeaderCell>Última Medición</CTableHeaderCell>
              </CTableRow>
            </CTableHead>

            <CTableBody>
              <CTableRow v-for="(p, index) in puntos" :key="p.idPunto ?? index">
                <CTableDataCell>{{ p.idPunto ?? "-" }}</CTableDataCell>
                <CTableDataCell>{{ p.nombrePunto ?? "-" }}</CTableDataCell>
                <CTableDataCell>{{ fmtCoord(p.latitud) }}</CTableDataCell>
                <CTableDataCell>{{ fmtCoord(p.longitud) }}</CTableDataCell>
                <CTableDataCell>
                  {{
                    p.ultimaMedicion
                      ? new Date(p.ultimaMedicion).toLocaleString()
                      : "Sin medición"
                  }}
                </CTableDataCell>
              </CTableRow>

              <CTableRow v-if="puntos.length === 0 && !errorMsg">
                <CTableDataCell colspan="5" class="text-center text-muted">
                  No hay puntos sin georreferenciación.
                </CTableDataCell>
              </CTableRow>
            </CTableBody>
          </CTable>
        </div>
      </CCardBody>
    </CCard>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import apiClient from "@/services/axios";

import {
  CCard,
  CCardHeader,
  CCardBody,
  CSpinner,
  CTable,
  CTableHead,
  CTableHeaderCell,
  CTableRow,
  CTableBody,
  CTableDataCell,
} from "@coreui/vue";

const loading = ref(true);
const puntos = ref([]);
const errorMsg = ref("");

function fmtCoord(n) {
  if (n === null || n === undefined) return "—";
  const x = Number(n);
  return Number.isFinite(x) ? x.toFixed(6) : String(n);
}

async function cargarPuntos() {
  loading.value = true;
  errorMsg.value = "";

  try {
    const { data } = await apiClient.get("/puntos/georreferencia", {
      params: { t: Date.now() },
    });

    // el controller devuelve List<DTO>, o sea un array directo
    puntos.value = Array.isArray(data?.data) ? data.data : (Array.isArray(data) ? data : []);
  } catch (err) {
    console.error(err);
    const status = err?.response?.status;

    if (status === 401) {
      errorMsg.value = "401: No autorizado. Inicia sesión (token faltante o expirado).";
    } else {
      errorMsg.value =
        err?.response?.data?.error ?? err?.message ?? "Error al cargar puntos.";
    }

    puntos.value = [];
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  cargarPuntos();
});
</script>

<style scoped>
h4 {
  font-weight: 600;
}
</style>


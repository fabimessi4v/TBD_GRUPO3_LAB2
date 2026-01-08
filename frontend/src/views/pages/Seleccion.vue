<template>
  <div class="text-center mt-3">
    <h1>Página de selección</h1>
    <div class="mt-4">
      <router-link to="/resumenSemanal" class="alsoButton" style="margin-right: 12px">
        Visualizar Resumen Semanal
      </router-link>

      <router-link to="/agregaciondedatos" class="alsoButton">
        Obtener serie temporal
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Seleccion'
}
</script>

<style scoped>
.alsoButton {
  display: inline-block;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-weight: 500;
  transition: background-color 0.3s;
}
.alsoButton:hover {
  background-color: #0056b3;
}
</style>
<template>
  <div>
    <WidgetsStatsA class="mb-4" />
    <CRow>
      <!-- Columna principal: mapa y stats (8 de 12 columnas) -->
      <CCol :md="8" class="order-md-1">
        <CCard class="mb-4">
          <CCardBody>
            <h4 class="card-title mb-3">Mapa Interactivo</h4>
            <div class="bg-body" style="height: 400px; border: 2px dashed #adb5bd; display: flex; align-items: center; justify-content: center; border-radius: 8px;">
              <span style="color: var(--cui-secondary-color);">[Espacio reservado para el mapa]</span>
            </div>
          </CCardBody>
        </CCard>
      </CCol>

      <!-- Panel lateral: menú de navegación (4 de 12 columnas) -->
      <CCol :md="4" class="order-md-2">
        <CCard class="mb-4">
          <CCardHeader>Panel lateral</CCardHeader>
          <CCardBody>
            <div class="mb-4">
              <h6>Datasets</h6>
              <CForm>
                <div class="mb-3">
                  <CFormSelect id="DatasetSelect">
                    <option>Dataset 1</option>
                    <option>Dataset 2</option>
                    <option>Dataset 3</option>
                  </CFormSelect>
                </div>
              </CForm>
            </div>
            <div class="mb-4">
              <h6>Filtros</h6>
              <CForm>
                <div class="mb-3">
                  <CFormSelect id="filtro1">
                    <option>Opción 1</option>
                    <option>Opción 2</option>
                    <option>Opción 3</option>
                  </CFormSelect>
                </div>
              </CForm>
            </div>
          </CCardBody>
        </CCard>
      </CCol>
    </CRow>
  </div>
</template>
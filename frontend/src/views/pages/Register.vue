<template>
  <div class="bwrapper min-vh-100 d-flex flex-row align-items-center">
    <CContainer>
      <CRow class="justify-content-center">
        <CCol :md="9" :lg="7" :xl="6">
          <CCard class="mx-4">
            <CCardBody class="p-4">
              <CForm @submit.prevent="submit">
                <h1>Register</h1>
                <p class="text-body-secondary">Create your account</p>

                <div v-if="error" class="mb-3">
                  <div class="text-danger">{{ error }}</div>
                </div>

                <CInputGroup class="mb-3">
                  <CInputGroupText>
                    <CIcon icon="cil-user" />
                  </CInputGroupText>
                  <CFormInput placeholder="Nombre" v-model="nombre" autocomplete="given-name" />
                </CInputGroup>

                <CInputGroup class="mb-3">
                  <CInputGroupText>
                    <CIcon icon="cil-user" />
                  </CInputGroupText>
                  <CFormInput placeholder="Apellido" v-model="apellido" autocomplete="family-name" />
                </CInputGroup>

                <CInputGroup class="mb-3">
                  <CInputGroupText>@</CInputGroupText>
                  <CFormInput placeholder="Email" v-model="email" type="email" autocomplete="email" />
                </CInputGroup>

                <CInputGroup class="mb-3">
                  <CInputGroupText>
                    <CIcon icon="cil-lock-locked" />
                  </CInputGroupText>
                  <CFormInput
                    type="password"
                    placeholder="Password"
                    v-model="password"
                    autocomplete="new-password"
                  />
                </CInputGroup>

                <CInputGroup class="mb-3">
                  <CInputGroupText>
                    <CIcon icon="cil-lock-locked" />
                  </CInputGroupText>
                  <CFormInput
                    type="password"
                    placeholder="Repeat password"
                    v-model="password2"
                    autocomplete="new-password"
                  />
                </CInputGroup>

                <CInputGroup class="mb-4">
                  <CInputGroupText>Rol</CInputGroupText>
                  <CFormSelect v-model="rol">
                    <option value="user">user</option>
                    <option value="admin">admin</option>
                  </CFormSelect>
                </CInputGroup>

                <div class="d-grid">
                  <CButton :disabled="loading" color="success" type="submit">
                    {{ loading ? 'Creating...' : 'Create Account' }}
                  </CButton>
                </div>
              </CForm>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </CContainer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { CRow, CCol, CCard, CCardBody, CInputGroup, CInputGroupText, CFormInput, CFormSelect, CForm, CButton } from '@coreui/vue'
import CIcon from '@coreui/icons-vue'
import AuthService from '@/services/login'

const router = useRouter()
const nombre = ref('')
const apellido = ref('')
const email = ref('')
const password = ref('')
const password2 = ref('')
const rol = ref('user')
const loading = ref(false)
const error = ref(null)
/* Función submit ejecutada al enviar el formulario */
async function submit() {
  error.value = null
    // Validaciones básicas de campos requeridos
  if (!nombre.value || !apellido.value || !email.value || !password.value || !rol.value) {
    error.value = 'Todos los campos son obligatorios'
    return
  }
    // Validación de coincidencia de contraseñas
  if (password.value !== password2.value) {
    error.value = 'Las contraseñas no coinciden'
    return
  }
  loading.value = true
  try {
        // Llama al servicio de autenticación para registrar el usuario
    await AuthService.register({
      email: email.value,
      password: password.value,
      nombre: nombre.value,
      apellido: apellido.value,
      rol: rol.value,
    })
    // redirige al login después de registrar
    router.push({ path: '/' })
  } catch (err) {
    error.value = err.response?.data?.message || err.message || 'Error al crear la cuenta'
  } finally {
    loading.value = false
  }
}
</script>


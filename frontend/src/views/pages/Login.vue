<template>
  <!-- ...existing code... -->
  <div class="wrapper min-vh-100 d-flex flex-row align-items-center">
    <CContainer>
      <CRow class="justify-content-center">
        <CCol :md="4">
          <CCardGroup>
            <!-- Agregamos un width del 100% para que ocupe el espacio completo -->
            <CCard class="p-4" style="width: 100%">
              <CCardBody>
                <CForm @submit.prevent="submit">
                  <h1>Login</h1>
                  <p class="text-body-secondary">Sign In to your account</p>

                  <div v-if="error" class="mb-3">
                    <div class="text-danger">{{ error }}</div>
                  </div>

                  <CInputGroup class="mb-3">
                    <CInputGroupText>
                      <CIcon icon="cil-user" />
                    </CInputGroupText>
                    <CFormInput
                      placeholder="Username"
                      autocomplete="username"
                      v-model="username"
                    />
                  </CInputGroup>
                  <CInputGroup class="mb-4">
                    <CInputGroupText>
                      <CIcon icon="cil-lock-locked" />
                    </CInputGroupText>
                    <CFormInput
                      type="password"
                      placeholder="Password"
                      autocomplete="current-password"
                      v-model="password"
                    />
                  </CInputGroup>
                  <CRow>
                    <CCol :xs="6">
                      <CButton color="primary" class="px-4" :disabled="loading" type="submit">
                        {{ loading ? 'Logging...' : 'Login' }}
                      </CButton>
                    </CCol>
                    <CCol :xs="6" class="text-end">
                      <CButton color="secondary" class="px-4" :disabled="loading" type="button" variant="outline"   @click="router.push({ path: '/register' })">
                        Sign Up
                      </CButton>
                    </CCol>
                  </CRow>
                </CForm>
                <div class="text-center mt-3">
                    <router-link to="/seleccion" class="alsoButton">
                      Ingresar sin iniciar sesión
                    </router-link>
                </div>
              </CCardBody>
            </CCard>
          </CCardGroup>
        </CCol>
      </CRow>
    </CContainer>
  </div>
  <!-- ...existing code... -->
</template>



<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AuthService from '@/services/login' // instancia exportada en services/login.js

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

async function submit() {
  loading.value = true
  error.value = null
  try {
    const resp = await AuthService.login({
      email: username.value,
      password: password.value,
    })
    // Redirige a la ruta principal
    await router.push({ path: '/Dashboard' })
  } catch (err) {
    error.value = err.response?.data?.message || err.message || 'Login failed'
  } finally {
    loading.value = false
  }
}
</script>
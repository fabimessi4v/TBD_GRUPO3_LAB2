<script setup>
import avatar from '@/assets/images/avatars/8.svg'
import { useRouter } from 'vue-router'
import AuthService from '@/services/login'

const itemsCount = 42
const router = useRouter()

async function handleLogout() {
  try {
    await AuthService.logout() // limpia token y headers en frontend
  } finally {
    // redirige al login y reemplaza historial para evitar volver con back
    router.replace({ path: '/' })
  }
}
</script>

<template>
  <CDropdown placement="bottom-end" variant="nav-item">
    <CDropdownToggle class="py-0 pe-0" :caret="false">
      <CAvatar :src="avatar" size="md" />
    </CDropdownToggle>
    <CDropdownMenu class="pt-0">
      <CDropdownHeader
        component="h6"
        class="bg-body-secondary text-body-secondary fw-semibold my-2"
      >
        Settings
      </CDropdownHeader>
      <CDropdownItem @click="handleLogout"> <CIcon icon="cil-lock-locked" /> Logout </CDropdownItem>
    </CDropdownMenu>
  </CDropdown>
</template>

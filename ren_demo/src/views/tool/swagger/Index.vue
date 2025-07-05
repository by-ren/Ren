<template>
  <IFrame :src="src" />
</template>

<script setup lang="ts" name="Swagger">
import { ref} from 'vue'

const src = ref(import.meta.env.VITE_APP_SWAGGER_URL)
</script>

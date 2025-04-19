<template>
  <div class="app-container">
    <div class="content">
      <h2>Welcome to My App</h2>
      <p v-if="isAuthenticated">Hello, {{ authStore.user?.username }}!</p>
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup lang="ts" name="home">
  import { onMounted } from 'vue'
  import { useAuthStore } from '@/stores/authStore'
  import { useRouter } from 'vue-router'
  import { storeToRefs } from 'pinia'

  const authStore = useAuthStore()
  const router = useRouter()

  // 使用 storeToRefs 保持响应式
  const { isAuthenticated } = storeToRefs(authStore)

  onMounted(async () => {
    try {
      //获取用户信息，如果可以获取到，用户信息和AccessToken和User会存储在Pinia中，而refreshToken会存储在localStorage中
      await authStore.getUserInfo()
      
      // 如果未认证，带当前路径跳转到登录页
      if (!isAuthenticated) {
        router.replace({
          path: '/login',
          query: { redirect: router.currentRoute.value.fullPath }
        })
      }
    } catch (error) {
      console.error('认证检查失败:', error)
      router.replace('/login')
    }
  })
</script>

<style scoped>
  .app-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }

  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 2rem;
    background-color: #ffffff;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }

  .logo {
    color: #2c3e50;
    margin: 0;
  }

  .nav-links {
    display: flex;
    gap: 1.5rem;
    align-items: center;
  }

  .nav-item {
    color: #2c3e50;
    text-decoration: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    transition: background-color 0.3s;
  }

  .nav-item:hover {
    background-color: #f0f0f0;
  }

  .login-btn, .logout-btn {
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: opacity 0.3s;
  }

  .login-btn {
    background-color: #3498db;
    color: white;
  }

  .logout-btn {
    background-color: #e74c3c;
    color: white;
  }

  button:hover {
    opacity: 0.9;
  }

  .content {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 1rem;
    background-color: white;
    border-radius: 8px;
    padding: 2rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }

  h2 {
    color: #2c3e50;
    margin-bottom: 1.5rem;
  }
</style>
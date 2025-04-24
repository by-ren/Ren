<template>
  <div class="app-container">
    <div class="content">
      <h2>Welcome to My App</h2>
      <p v-if="isAuthenticated">Hello, {{ authStore.user?.username }}!</p>
      <button @click="loginOut" class="submit-btn" v-if="isAuthenticated">
        login out
      </button>
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup lang="ts" name="home">
  import { onMounted } from 'vue'
  import { useAuthStore } from '@/stores/authStore'
  import router from '@/router';
  import { storeToRefs } from 'pinia'

  const authStore = useAuthStore()

  // 使用 storeToRefs 保持响应式
  const { isAuthenticated } = storeToRefs(authStore)

  onMounted(async () => {
    try {
      //获取用户信息，如果可以获取到，用户信息和AccessToken和User会存储在Pinia中，而refreshToken会存储在localStorage中
      await authStore.getUserInfo()
      
      // 如果未认证(其实也就是没有得到用户信息)，跳转到登录页
      if (!isAuthenticated) {
        router.replace({
          path: '/login'
        })
      }
    } catch (error) {
      console.error('认证检查失败:', error)
      router.replace('/login')
    }
  })

  function loginOut(){
    authStore.logout();
  }
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

  .submit-btn {
    background-color: #3498db;
    color: white;
    padding: 0.8rem;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .submit-btn:hover {
    background-color: #2980b9;
  }
</style>
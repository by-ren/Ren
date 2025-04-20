<template>
    <div class="login-container">
      <div class="login-box">
        <h2>Welcome Back</h2>
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label>Username</label>
            <input 
              v-model="username"
              type="text" 
              required
              placeholder="Enter your username"
            >
          </div>
  
          <div class="form-group">
            <label>Password</label>
            <input
              v-model="password"
              type="password"
              required
              placeholder="Enter your password"
            >
          </div>
  
          <button type="submit" class="submit-btn">
            {{ isLoading ? 'Logging in...' : 'Sign In' }}
          </button>
  
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        </form>
      </div>
    </div>
  </template>
  
  <script setup lang="ts" name="login">
  import { ref } from 'vue'
  import router from '@/router';
  import { useAuthStore } from '@/stores/authStore'
  import { isAxiosError } from 'axios'
  
  const authStore = useAuthStore()
  
  const username = ref('')
  const password = ref('')
  const isLoading = ref(false)
  const errorMessage = ref('')
  
  const handleLogin = async () => {
    try {
      isLoading.value = true
      errorMessage.value = ''
      
      await authStore.login(username.value, password.value)
      const redirectPath = router.currentRoute.value.query.redirect
      router.replace(
        typeof redirectPath === 'string' ? redirectPath : '/'
      )
    } catch (error) {
      // 处理不同错误类型
      if (isAxiosError(error)) {
        errorMessage.value = error.response?.data?.message 
          || `网络错误：${error.message}`
      } else if (error instanceof Error) {
        errorMessage.value = error.message
      } else {
        errorMessage.value = '系统发生未知错误'
      }
    } finally {
      isLoading.value = false
    }
}
  </script>
  
  <style scoped>
  .login-container {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f8f9fa;
  }
  
  .login-box {
    background-color: white;
    padding: 2rem 3rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    width: 100%;
    max-width: 400px;
  }
  
  h2 {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 2rem;
  }
  
  .login-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  label {
    font-weight: 500;
    color: #34495e;
  }
  
  input {
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
    transition: border-color 0.3s;
  }
  
  input:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 0 2px rgba(52,152,219,0.2);
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
  
  .error-message {
    color: #e74c3c;
    text-align: center;
    margin: 0;
    font-size: 0.9rem;
  }
  </style>
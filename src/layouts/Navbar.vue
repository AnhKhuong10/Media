<template>
  <header class="navbar">
    <!-- Logo + tiêu đề: nhấn về trang Photos -->
    <div class="left" @click="goHome" title="Go to Photos">
      <i class="pi pi-images logo"></i>
      <span class="title">Photo Zamura</span>
    </div>

    <div class="center">
      <div class="search-wrapper">
        <i class="pi pi-search search-icon"></i>
        <input
          class="search-input"
          type="text"
          placeholder="Search your photos..."
        />
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()

function goHome() {
  router.push('/photos')
}
</script>

<style scoped>
/* 🧭 Thanh navbar tổng thể */
.navbar {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #ddd;
  padding: 0 1rem;
  background: #fff;
}

/* 🌈 Logo + tiêu đề */
.left {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: 0.25s ease;
}
.left:hover {
  opacity: 0.85;
  transform: scale(1.02);
}
.logo {
  font-size: 1.6rem;
  margin-right: 0.5rem;
  color: #1976d2;
}
.title {
  font-weight: 700;
  font-size: 1.25rem;
  color: #333;
}

/* 🔍 Ô tìm kiếm giữa */
.center {
  flex: 1;
  max-width: 500px;
  margin: 0 2rem;
  display: flex;
  justify-content: center;
}

.search-wrapper {
  position: relative;
  width: 100%;
  max-width: 380px;
  transition: all 0.3s ease;
}

/* Icon tìm kiếm */
.search-icon {
  position: absolute;
  top: 50%;
  left: 12px;
  transform: translateY(-50%);
  color: #666;
  transition: 0.3s ease;
}

/* Ô input */
.search-input {
  width: 100%;
  padding: 10px 14px 10px 38px;
  border: 1px solid #ccc;
  border-radius: 25px;
  font-size: 0.95rem;
  background-color: #f9f9f9;
  transition: all 0.3s ease;
}

/* 🪄 Hiệu ứng khi focus */
.search-input:focus {
  border-color: #1976d2;
  background-color: #fff;
  box-shadow: 0 0 8px rgba(25, 118, 210, 0.3);
  outline: none;
  transform: scale(1.02);
}

.search-input:focus + .search-icon,
.search-wrapper:hover .search-icon {
  color: #1976d2;
}
</style>

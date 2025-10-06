<template>
  <div class="favorites-page">
    <h2 class="page-title">Favorites</h2>

    <div class="photo-grid">
      <PhotoListFormLineXn
        :photos="favorites"
        :type_component="{ type: 'favorites' }"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PhotoListFormLineXn from '../components/photo/PhotoListFormLineXn.vue'

// Dữ liệu chung của toàn app
const allPhotos = JSON.parse(localStorage.getItem('my_photos_gallery') || '[]')

// ⚡️ favorites chỉ là “bản copy tĩnh” khi trang này load
const favorites = ref([])

onMounted(() => {
  favorites.value = allPhotos.filter(p => p.liked).map(p => ({ ...p })) // tạo bản sao, không reactive
})
</script>

<style scoped>
.page-title {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  font-weight: 600;
}
.photo-grid {
  width: 100%;
}
</style>

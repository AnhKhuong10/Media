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
import { ref, onMounted, onBeforeUnmount } from 'vue'
import PhotoListFormLineXn from '../components/photo/PhotoListFormLineXn.vue'

const STORAGE_PHOTOS = 'my_photos_gallery'
const favorites = ref([])

function loadFavorites() {
  const allPhotos = JSON.parse(localStorage.getItem(STORAGE_PHOTOS) || '[]')
  favorites.value = allPhotos.filter(p => p.liked)
}

onMounted(() => {
  loadFavorites()

  // 🔁 Lắng nghe cả hai loại sự kiện
  window.addEventListener('storage', loadFavorites)
  window.addEventListener('photos-updated', loadFavorites)
})

onBeforeUnmount(() => {
  window.removeEventListener('storage', loadFavorites)
  window.removeEventListener('photos-updated', loadFavorites)
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

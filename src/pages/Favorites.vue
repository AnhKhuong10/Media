<template>
  <div class="favorites-page">
    <h2 class="page-title">💖 Favorites</h2>

    <div v-if="favorites.length" class="photo-grid">
      <PhotoListFormLineXn
        :photos="favorites"
        :type_component="{ type: 'favorites' }"
      />
    </div>
    <p v-else>📭 Không có ảnh yêu thích nào</p>
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

  // 🔁 Lắng nghe sự kiện cập nhật ảnh yêu thích (custom event)
  window.addEventListener('photos-updated', loadFavorites)
})

onBeforeUnmount(() => {
  window.removeEventListener('photos-updated', loadFavorites)
})
</script>

<style scoped>
.page-title {
  font-size: 1.6rem;
  margin-bottom: 1rem;
  font-weight: 700;
  text-transform: uppercase;
  text-align: left; /* 🟢 canh trái */

  /* 🌈 Hiệu ứng gradient */
  background-image: linear-gradient(90deg, #ff7b00, #ff00c8, #00bfff, #00ff88);
  background-size: 300%;
  color: transparent;
  background-clip: text;
  -webkit-background-clip: text;

  animation: textGradient 4s linear infinite, popIn 0.4s ease;
}

/* 🌈 Hiệu ứng chữ */
@keyframes textGradient {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes popIn {
  0% { transform: scale(0.8); opacity: 0; }
  60% { transform: scale(1.05); opacity: 1; }
  100% { transform: scale(1); }
}

p {
  text-align: left; /* 🟢 canh trái */
  font-size: 1.2rem;
  font-weight: 600;
  margin-top: 40px;

  background-image: linear-gradient(90deg, #00ff88, #00bfff, #ff00c8, #ff7b00);
  background-size: 300%;
  color: transparent;
  background-clip: text;
  -webkit-background-clip: text;

  animation: textGradient 4s linear infinite, popIn 0.5s ease;
}

/* Grid ảnh */
.photo-grid {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-start; /* 🟢 ảnh dồn trái */
  align-items: flex-start;
}
</style>

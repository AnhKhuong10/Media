<template>
  <div class="favorites-page">
    <h2 class="page-title">Your photos</h2>

    <div class="photo-grid">

      <PhotoListFormLineXn :photos="favorites" :type_component="{ type: 'photos' }" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { photoData } from '../graphql/FakeData.js'
import PhotoListFormLineXn from '../components/photo/PhotoListFormLineXn.vue'

const favorites = ref(photoData.data.getUserPhotos)
</script>

<style scoped>
.page-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1rem;

  background: linear-gradient(90deg, #4285f4, #ea4335, #fbbc05, #34a853);
  background-clip: text;
  color: transparent;

  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  letter-spacing: 0.5px;
}


.photo-grid {
  width: 100%;
}

.photo-card img {
  width: 100%;
  aspect-ratio: 2/2;
  border-radius: 8px;
}
</style>

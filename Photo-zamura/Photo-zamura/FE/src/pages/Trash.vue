<template>
  <div class="trash-page">
    <h2 class="page-title">Trash</h2>

    <div v-if="trash.length" class="photo-grid">
      <div v-for="(photo, index) in trash" :key="index" class="photo-card">
        <img :src="photo.url" :alt="photo.name" />
      </div>
    </div>
    <p v-else>No items in trash</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const trash = ref([
  { name: 'Deleted 1', url: 'https://picsum.photos/id/1050/400/300' },
  { name: 'Deleted 2', url: 'https://picsum.photos/id/1052/400/300' }
])
</script>

<style scoped>
.page-title {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  font-weight: 600;
}
.photo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 10px;
}
.photo-card img {
  width: 100%;
  border-radius: 8px;
}
</style>

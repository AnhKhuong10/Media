<template>
  <aside class="sidebar">
    <Button label="Photos" icon="pi pi-images" class="menu-btn" @click="$router.push('/photos')" />
    <Button label="Albums" icon="pi pi-book" class="menu-btn" @click="$router.push('/albums')" />
    <Button label="Favorites" icon="pi pi-star" class="menu-btn" @click="$router.push('/favorites')" />
    <Button label="Trash" icon="pi pi-trash" class="menu-btn" @click="$router.push('/trash')" />
  </aside>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
</script>

<style scoped>
.sidebar {
  width: 240px;
  border-right: 1px solid #ddd;
  padding: 0.5rem;
  background: #fafafa;

  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  /* khoảng cách giữa các nút */
}

.menu-btn {
  /* width: 100%;
  justify-content: flex-start; */
  /* icon + text căn trái */
  border-radius: 28px;

  border-radius: 28px;
  background-color: #d9e8ff;
  color: black;
}

.menu-btn:hover {
  border-radius: 28px;

  border-radius: 28px;
  background-color: #dedede;
  color: black;
}
</style>

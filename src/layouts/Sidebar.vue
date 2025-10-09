<template>
  <aside class="sidebar">
    <Button
      label="Photos"
      icon="pi pi-images"
      class="menu-btn photos"
      :class="{ active: isActive('/photos') }"
      @click="$router.push('/photos')"
    />
    <Button
      label="Albums"
      icon="pi pi-book"
      class="menu-btn albums"
      :class="{ active: isActive('/albums') }"
      @click="$router.push('/albums')"
    />
    <Button
      label="Favorites"
      icon="pi pi-star"
      class="menu-btn favorites"
      :class="{ active: isActive('/favorites') }"
      @click="$router.push('/favorites')"
    />
    <Button
      label="Trash"
      icon="pi pi-trash"
      class="menu-btn trash"
      :class="{ active: isActive('/trash') }"
      @click="$router.push('/trash')"
    />
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import Button from 'primevue/button'

const route = useRoute()
function isActive(path: string) {
  return route.path === path
}
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
}

/* Base style */
.menu-btn {
  border-radius: 28px;
  width: 100%;
  justify-content: flex-start;
  font-weight: 500;
  color: black;
  transition: all 0.25s ease;
}

/* 🔵 Photos */
.menu-btn.photos {
  background: #e3f2fd;
}
.menu-btn.photos:hover {
  background: #bbdefb;
}
.menu-btn.photos.active {
  background: #64b5f6;
  color: white;
  font-weight: 600;
}

/* 🟢 Albums */
.menu-btn.albums {
  background: #e8f5e9;
}
.menu-btn.albums:hover {
  background: #c8e6c9;
}
.menu-btn.albums.active {
  background: #81c784;
  color: white;
  font-weight: 600;
}

/* 🟡 Favorites */
.menu-btn.favorites {
  background: #fff9e6;
}
.menu-btn.favorites:hover {
  background: #fff3cd;
}
.menu-btn.favorites.active {
  background: #ffd54f;
  color: #4e342e;
  font-weight: 600;
}

/* 🔴 Trash */
.menu-btn.trash {
  background: #fdecea;
}
.menu-btn.trash:hover {
  background: #f8d7da;
}
.menu-btn.trash.active {
  background: #ef5350;
  color: white;
  font-weight: 600;
}
</style>

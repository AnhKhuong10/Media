<template>
  <div
    class="photo-list-compo"
    :class="{ 'drop-zone': dragFileStatus }"
    @dragenter.prevent="onDragEnter"
    @dragleave.prevent="onDragLeave"
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <div v-if="displayedPhotos.length === 0" class="no-data">
      No data
    </div>

    <div v-else class="grids">
      <div v-for="(item, i) in displayedPhotos" :key="i" class="photo-card">
        <PhotoItem
          :photo="item"
          v-if="type_component.type === 'favorites' ? item.liked : true"
          @delete-photo="handleDeletePhoto"
          @toggle-like="handleToggleFavorite"
        />
      </div>
    </div>

    <!-- Overlay hiển thị khi đang kéo file -->
    <div v-if="dragFileStatus" class="drop-overlay">
      <p>Thả ảnh vào đây để tải lên</p>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import PhotoItem from './PhotoItem.vue'

const STORAGE_PHOTOS = 'my_photos_gallery'
const STORAGE_FAVORITES = 'my_favorites_gallery'

const props = defineProps({
  photos: Array,
  type_component: Object,
})

const localPhotos = ref([])
const localFavorites = ref([])
const dragFileStatus = ref(false)

// 🧠 Khi mount, đọc cả 2 storage
onMounted(() => {
  const photos = localStorage.getItem(STORAGE_PHOTOS)
  const favorites = localStorage.getItem(STORAGE_FAVORITES)

  localPhotos.value = photos ? JSON.parse(photos) : [...props.photos]
  localFavorites.value = favorites ? JSON.parse(favorites) : []
})

// 📸 Tùy vào component mà hiển thị danh sách tương ứng
const displayedPhotos = computed(() => {
  if (props.type_component.type === 'favorites') {
    return localFavorites.value
  } else {
    return localPhotos.value
  }
})

// 💾 Lưu vào localStorage
function savePhotos() {
  localStorage.setItem(STORAGE_PHOTOS, JSON.stringify(localPhotos.value))
}
function saveFavorites() {
  localStorage.setItem(STORAGE_FAVORITES, JSON.stringify(localFavorites.value))
}

// 🗑️ Xoá ảnh
function handleDeletePhoto(photo) {
  localPhotos.value = localPhotos.value.filter(p => p.id !== photo.id)
  localFavorites.value = localFavorites.value.filter(p => p.id !== photo.id)
  savePhotos()
  saveFavorites()
}

// ❤️ Thích / bỏ thích
function handleToggleFavorite(photo) {
  const p = localPhotos.value.find(p => p.id === photo.id)
  if (p) p.liked = !p.liked

  if (p?.liked) {
    // thêm mới lên đầu Favorites
    const exist = localFavorites.value.find(f => f.id === photo.id)
    if (!exist) localFavorites.value.unshift(p)
  } else {
    // bỏ khỏi Favorites
    localFavorites.value = localFavorites.value.filter(f => f.id !== photo.id)
  }

  savePhotos()
  saveFavorites()
}

// 📤 Kéo thả ảnh
function handleDrop(e) {
  dragFileStatus.value = false
  const files = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/'))
  files.forEach(file => {
    const reader = new FileReader()
    reader.onload = ev => {
      const newPhoto = {
        id: Date.now() + Math.random().toString(36).slice(2),
        url: ev.target.result,
        name: file.name,
        liked: false,
      }
      localPhotos.value.unshift(newPhoto)
      savePhotos()
    }
    reader.readAsDataURL(file)
  })
}

const onDragEnter = () => (dragFileStatus.value = true)
const onDragLeave = (e) => {
  if (e.target === e.currentTarget) dragFileStatus.value = false
}
</script>

<style scoped>
.photo-list-compo {
  position: relative;
  width: 100%;
  min-height: 200px;
}

.no-data {
  color: #888;
  font-size: 1.1rem;
  text-align: center;
  padding: 2rem 0;
}

.grids {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.photo-card {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
}
</style>


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
          :type="type_component.type"
          @delete-photo="handleDeletePhoto"
          @toggle-like="handleToggleFavorite"
          @restore-photo="handleRestorePhoto"
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
import { ref, onMounted, computed } from 'vue'
import PhotoItem from './PhotoItem.vue'

const STORAGE_PHOTOS = 'my_photos_gallery'
const STORAGE_FAVORITES = 'my_favorites_gallery'
const STORAGE_TRASH = 'my_trash_gallery'

const props = defineProps({
  photos: Array,
  type_component: Object,
})

const localPhotos = ref([])
const localFavorites = ref([])
const localTrash = ref([])
const dragFileStatus = ref(false)

// 🧠 Khi mount, đọc dữ liệu từ localStorage
onMounted(() => {
  const photos = localStorage.getItem(STORAGE_PHOTOS)
  const favorites = localStorage.getItem(STORAGE_FAVORITES)
  const trash = localStorage.getItem(STORAGE_TRASH)

  localPhotos.value = photos ? JSON.parse(photos) : [...props.photos]
  localFavorites.value = favorites ? JSON.parse(favorites) : []
  localTrash.value = trash ? JSON.parse(trash) : []
})

// 📸 Tùy vào component mà hiển thị danh sách tương ứng
const displayedPhotos = computed(() => {
  switch (props.type_component.type) {
    case 'favorites':
      return localFavorites.value
    case 'trash':
      return localTrash.value
    default:
      return localPhotos.value
  }
})

// 💾 Lưu dữ liệu
function saveAll() {
  localStorage.setItem(STORAGE_PHOTOS, JSON.stringify(localPhotos.value))
}
function saveFavorites() {
  localStorage.setItem(STORAGE_FAVORITES, JSON.stringify(localFavorites.value))
  localStorage.setItem(STORAGE_TRASH, JSON.stringify(localTrash.value))
}

// 🗑️ Xóa ảnh
function handleDeletePhoto(photo) {
  // Chuyển sang thùng rác
  localTrash.value.unshift(photo)

  // Xóa khỏi Photos & Favorites
  localPhotos.value = localPhotos.value.filter(p => p.id !== photo.id)
  localFavorites.value = localFavorites.value.filter(p => p.id !== photo.id)
  saveAll()
  saveFavorites()
  window.dispatchEvent(new Event('photos-updated'))
}

// ♻️ Khôi phục ảnh
function handleRestorePhoto(photo) {
  // Reset liked = false khi khôi phục
  const restoredPhoto = { ...photo, liked: false }

  // Thêm vào Photos
  const exist = localPhotos.value.find(p => p.id === photo.id)
  if (!exist) localPhotos.value.unshift(restoredPhoto)

  // Xóa khỏi Trash và Favorites
  localTrash.value = localTrash.value.filter(p => p.id !== photo.id)
  localFavorites.value = localFavorites.value.filter(f => f.id !== photo.id)
  saveAll()
  saveFavorites()
  window.dispatchEvent(new Event('photos-updated'))
}

// 💖 Yêu thích / Bỏ yêu thích
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

  saveAll()
  saveFavorites()
  window.dispatchEvent(new Event('photos-updated')) // 🔔 Cập nhật Favorites.vue ngay
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
      saveAll()
      window.dispatchEvent(new Event('photos-updated'))
    }
    reader.readAsDataURL(file)
  })
}

const onDragEnter = () => (dragFileStatus.value = true)
const onDragLeave = e => {
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


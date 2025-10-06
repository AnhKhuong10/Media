<template>
  <div class="trash-page">
    <h2 class="page-title">🗑️ Trash</h2>

    <div v-if="trash.length" class="photo-grid">
      <div v-for="(photo, index) in trash" :key="index" class="photo-card">
        <img :src="photo.url" :alt="photo.name" />
        <div class="action-buttons">
          <button @click="restorePhoto(photo)">♻️ Khôi phục</button>
          <button @click="deleteForever(photo)">❌ Xóa vĩnh viễn</button>
        </div>
      </div>
    </div>
    <p v-else>🕳️ Thùng rác trống</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const STORAGE_PHOTOS = 'my_photos_gallery'
const STORAGE_TRASH = 'my_trash_gallery'

const trash = ref([])
const photos = ref([])

onMounted(() => {
  const t = localStorage.getItem(STORAGE_TRASH)
  const p = localStorage.getItem(STORAGE_PHOTOS)

  trash.value = t ? JSON.parse(t) : []
  photos.value = p ? JSON.parse(p) : []
})

// ♻️ Khôi phục ảnh
function restorePhoto(photo) {
  // Xóa ảnh khỏi thùng rác
  trash.value = trash.value.filter(p => p.id !== photo.id)

  // Xóa ảnh cũ (nếu tồn tại trong danh sách chính)
  photos.value = photos.value.filter(p => p.id !== photo.id)

  // ✅ Giữ nguyên trạng thái yêu thích (không reset liked)
  photos.value.unshift({
    ...photo,
    deletedAt: null
  })

  saveAll()
  alert(`Đã khôi phục ảnh "${photo.name}"`)

  // 🔁 Báo cho các trang khác (Favorites.vue) cập nhật
  window.dispatchEvent(new Event('storage'))
}

// ❌ Xóa vĩnh viễn
function deleteForever(photo) {
  if (confirm(`Bạn có chắc muốn xóa vĩnh viễn "${photo.name}"?`)) {
    trash.value = trash.value.filter(p => p.id !== photo.id)
    saveAll()
    window.dispatchEvent(new Event('storage')) // cập nhật luôn
  }
}

// 💾 Lưu lại vào localStorage
function saveAll() {
  localStorage.setItem(STORAGE_PHOTOS, JSON.stringify(photos.value))
  localStorage.setItem(STORAGE_TRASH, JSON.stringify(trash.value))
}
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

.photo-card {
  position: relative;
}

.photo-card img {
  width: 100%;
  border-radius: 8px;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
}

.action-buttons button {
  background: #f0f0f0;
  border: none;
  border-radius: 6px;
  padding: 4px 8px;
  cursor: pointer;
  transition: background 0.2s ease;
}

.action-buttons button:hover {
  background: #e0e0e0;
}
</style>

```vue
<template>
  <div class="trash-page">
    <h2 class="page-title">🗑️ Trash</h2>

    <!-- Nếu có ảnh trong thùng rác -->
    <div v-if="trash.length" class="photo-grid">
      <div v-for="(photo, index) in trash" :key="index" class="photo-card">
        <img :src="photo.url" :alt="photo.name" />
        <div class="action-buttons">
          <button @click="restorePhoto(photo)">♻️ Khôi phục</button>
          <button @click="askDelete(photo)">❌ Xóa vĩnh viễn</button>
        </div>
      </div>
    </div>

    <!-- Nếu KHÔNG có ảnh -->
    <div v-else class="no-data">
      <div class="empty-animation">
        <img
          src="https://cdn-icons-png.flaticon.com/512/3096/3096687.png"
          alt="Empty trash"
        />
        <p>Thùng rác trống 🗑️</p>
      </div>
    </div>

    <!-- 🌟 Toast thông báo -->
    <transition-group name="toast-fade" tag="div" class="toast-container">
      <div
        v-for="(msg, i) in toasts"
        :key="i"
        class="toast"
        :class="msg.type"
      >
        {{ msg.text }}
      </div>
    </transition-group>

    <!-- 💥 Modal xác nhận xóa -->
    <div v-if="confirmDialog.visible" class="confirm-overlay">
      <div class="confirm-box">
        <h3>⚠️ Xác nhận xóa</h3>
        <p>Bạn có chắc muốn xóa vĩnh viễn ảnh "<strong>{{ confirmDialog.photo.name }}</strong>"?</p>
        <div class="confirm-actions">
          <button class="btn-cancel" @click="cancelDelete">Hủy</button>
          <button class="btn-confirm" @click="confirmDelete">Xóa</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const STORAGE_PHOTOS = 'my_photos_gallery'
const STORAGE_TRASH = 'my_trash_gallery'

const trash = ref([])
const photos = ref([])
const toasts = ref([])
const confirmDialog = ref({ visible: false, photo: null })

onMounted(() => {
  const t = localStorage.getItem(STORAGE_TRASH)
  const p = localStorage.getItem(STORAGE_PHOTOS)

  trash.value = t ? JSON.parse(t) : []
  photos.value = p ? JSON.parse(p) : []
})

function showToast(text, type = 'info') {
  const id = Date.now()
  toasts.value.push({ id, text, type })
  setTimeout(() => {
    toasts.value = toasts.value.filter(t => t.id !== id)
  }, 3000)
}

function restorePhoto(photo) {
  trash.value = trash.value.filter(p => p.id !== photo.id)
  photos.value = photos.value.filter(p => p.id !== photo.id)
  photos.value.unshift({ ...photo, liked: false, deletedAt: null })
  saveAll()
  showToast(`✅ Đã khôi phục ảnh "${photo.name}"`, 'success')
  window.dispatchEvent(new Event('storage'))
}

function askDelete(photo) {
  confirmDialog.value = { visible: true, photo }
}

function cancelDelete() {
  confirmDialog.value.visible = false
}

function confirmDelete() {
  const photo = confirmDialog.value.photo
  trash.value = trash.value.filter(p => p.id !== photo.id)
  saveAll()
  showToast(`🗑️ Đã xóa vĩnh viễn "${photo.name}"`, 'error')
  confirmDialog.value.visible = false
  window.dispatchEvent(new Event('storage'))
}

function saveAll() {
  localStorage.setItem(STORAGE_PHOTOS, JSON.stringify(photos.value))
  localStorage.setItem(STORAGE_TRASH, JSON.stringify(trash.value))
}
</script>

<style scoped>
.trash-page {
  text-align: left;
  padding-left: 10px;
}

.page-title {
  font-size: 1.6rem;
  margin-bottom: 1rem;
  font-weight: 700;
  text-transform: uppercase;
  text-align: left;
  background-image: linear-gradient(90deg, #ff7b00, #ff00c8, #00bfff, #00ff88);
  background-size: 300%;
  color: transparent;
  background-clip: text;
  -webkit-background-clip: text;
  animation: textGradient 4s linear infinite, popIn 0.4s ease;
}

/* 🕳️ Khi không có dữ liệu */
.no-data {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  color: #888;
  animation: fadeIn 0.5s ease;
  text-align: center;
  margin: 0 auto; /* ✅ giúp căn giữa khối */
}


.empty-animation img {
  width: 100px;
  height: 100px;
  opacity: 0.8;
  margin-bottom: 16px;
  filter: drop-shadow(0 2px 6px rgba(0,0,0,0.2));
}
.empty-animation p {
  font-size: 1.2rem;
  font-weight: 600;
  background-image: linear-gradient(90deg, #00ff88, #00bfff, #ff00c8, #ff7b00);
  background-size: 300%;
  color: transparent;
  background-clip: text;
  -webkit-background-clip: text;
  animation: textGradient 4s linear infinite;
}

/* Grid ảnh */
.photo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 10px;
  justify-items: start;
}

.photo-card {
  position: relative;
  width: 180px;
}

.photo-card img {
  width: 100%;
  border-radius: 8px;
}

.action-buttons {
  display: flex;
  justify-content: flex-start;
  gap: 8px;
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

/* 🌈 Toast container */
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 999;
}

/* ✨ Toast box */
.toast {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid #ddd;
  padding: 10px 18px;
  border-radius: 12px;
  font-weight: 600;
  backdrop-filter: blur(8px);
  box-shadow: 0 4px 14px rgba(0,0,0,0.2);
  background-image: linear-gradient(90deg, #ff7b00, #ff00c8, #00bfff, #00ff88);
  background-size: 400%;
  color: transparent;
  background-clip: text;
  -webkit-background-clip: text;
  animation: glowText 3s linear infinite, slideIn 0.4s ease;
}

.toast.success { border-color: #4caf50; }
.toast.error { border-color: #f44336; }

/* 💥 Modal xác nhận */
.confirm-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.confirm-box {
  background: white;
  padding: 24px;
  border-radius: 16px;
  width: 320px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.25);
  animation: fadeIn 0.25s ease;
  text-align: center;
}

.confirm-box h3 {
  color: #ff4d4f;
  margin-bottom: 10px;
}

.confirm-box p {
  color: #333;
  margin-bottom: 20px;
}

.confirm-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.confirm-actions button {
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  font-weight: 600;
  cursor: pointer;
  transition: 0.2s;
}

.btn-confirm {
  background: #ff4d4f;
  color: white;
}
.btn-confirm:hover {
  background: #e33e3f;
}

.btn-cancel {
  background: #ddd;
}
.btn-cancel:hover {
  background: #ccc;
}

/* Hiệu ứng */
@keyframes textGradient {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}
@keyframes glowText {
  0% { background-position: 0% 50%; }
  100% { background-position: 100% 50%; }
}
@keyframes popIn {
  0% { transform: scale(0.8); opacity: 0; }
  60% { transform: scale(1.05); opacity: 1; }
  100% { transform: scale(1); }
}
@keyframes slideIn {
  from { opacity: 0; transform: translateX(40px); }
  to { opacity: 1; transform: translateX(0); }
}
@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 1; transform: scale(1); }
}
.toast-fade-enter-active, .toast-fade-leave-active {
  transition: all 0.3s ease;
}
.toast-fade-enter-from, .toast-fade-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
```

<template>
  <div class="photo-item">
    <!-- Ảnh -->
    <img :src="photo.url" :alt="photo.name" class="photo-img" />

    <!-- Góc dưới phải -->
    <div class="action-group">
      <!-- Nút yêu thích -->
      <button
        class="icon-btn heart-btn"
        @click.stop="toggleFavorite(photo)"
        :title="photo.liked ? 'Bỏ yêu thích' : 'Yêu thích'"
      >
        <span v-if="photo.liked">💖</span>
        <span v-else>🤍</span>
      </button>

      <!-- Nút 3 chấm -->
      <div class="menu-container">
        <button class="icon-btn menu-btn" @click.stop="toggleMenu" title="Tùy chọn">
          ⋮
        </button>

        <transition name="fade">
          <div v-if="menuVisible" class="menu">
            <div class="menu-item" @click="() => { emitShare(photo); closeMenu() }">
              <span class="mi">📤</span>
              <span class="ml">Chia sẻ</span>
            </div>

            <!-- Album -->
            <div class="menu-item" @click="() => { openAlbumModal(photo); closeMenu() }">
              <span class="mi">📁</span>
              <span class="ml">Album</span>
            </div>

            <div class="menu-item delete" @click="() => { emitDelete(photo); closeMenu() }">
              <span class="mi">🗑️</span>
              <span class="ml">Xóa</span>
            </div>
          </div>
        </transition>
      </div>
    </div>

    <!-- 💿 Popup chọn / tạo album -->
    <div v-if="showAlbumModal" class="modal-overlay" @click.self="closeAlbumModal">
      <div class="modal">
        <h3>Thêm vào Album</h3>

        <div class="album-list">
          <div
            v-for="album in albums"
            :key="album.id"
            class="album-item"
            @click="addPhotoToAlbum(album)"
          >
            📁 {{ album.name }}
          </div>
        </div>

        <div class="new-album">
          <input v-model="newAlbumName" placeholder="Tên album mới..." />
          <button @click="createNewAlbum">Tạo Album</button>
        </div>

        <button class="close-btn" @click="closeAlbumModal">Đóng</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const emit = defineEmits(['delete-photo', 'toggle-like', 'share-photo'])

const props = defineProps({
  photo: {
    type: Object,
    required: true,
  },
})

const menuVisible = ref(false)
const showAlbumModal = ref(false)
const selectedPhoto = ref(null)
const newAlbumName = ref('')
const albums = ref([
  { id: 1, name: 'Kỷ niệm' },
  { id: 2, name: 'Gia đình' },
  { id: 3, name: 'Chuyến đi' },
])

function toggleMenu() {
  menuVisible.value = !menuVisible.value
}
function closeMenu() {
  menuVisible.value = false
}

// Đóng menu khi click ra ngoài
function handleClickOutside(e) {
  if (!e.target.closest('.menu-container')) {
    menuVisible.value = false
  }
}
onMounted(() => document.addEventListener('click', handleClickOutside))
onBeforeUnmount(() => document.removeEventListener('click', handleClickOutside))

// Emit các hành động
function emitDelete(photo) {
  emit('delete-photo', photo)
}
function emitShare(photo) {
  emit('share-photo', photo)
}
function toggleFavorite(photo) {
  emit('toggle-like', photo)
}

// Album modal
function openAlbumModal(photo) {
  selectedPhoto.value = photo
  showAlbumModal.value = true
}
function closeAlbumModal() {
  showAlbumModal.value = false
  newAlbumName.value = ''
}
function addPhotoToAlbum(album) {
  alert(`Đã thêm ảnh "${selectedPhoto.value.name}" vào album "${album.name}"`)
  closeAlbumModal()
}
function createNewAlbum() {
  if (!newAlbumName.value.trim()) return alert('Vui lòng nhập tên album!')
  const newAlbum = { id: Date.now(), name: newAlbumName.value.trim() }
  albums.value.push(newAlbum)
  alert(`Đã tạo album "${newAlbum.name}" và thêm ảnh vào đó`)
  closeAlbumModal()
}
</script>

<style scoped>
.photo-item {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}

.photo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* góc dưới phải */
.action-group {
  position: absolute;
  right: 8px;
  bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 20;
}

/* nút icon chung */
.icon-btn {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 8px;
  padding: 6px;
  min-width: 34px;
  min-height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.12);
  transition: background 0.15s;
}
.icon-btn:hover { background: white; }

.heart-btn {
  color: #ff4b91;
}

/* menu container */
.menu-container { position: relative; }

.menu-btn {
  border-radius: 8px;
  font-size: 18px;
  line-height: 1;
}

/* menu dropdown */
.menu {
  position: absolute;
  right: 0;
  bottom: 42px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 6px 18px rgba(0,0,0,0.12);
  padding: 6px;
  min-width: 120px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  z-index: 30;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 8px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.95rem;
}
.menu-item:hover { background: #f7f7f8; }
.menu-item.delete { color: #dc2626; }
.mi { width: 22px; text-align: center; }

/* modal album */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 50;
}

.modal {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  width: 360px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.album-list {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #eee;
  border-radius: 8px;
}

.album-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f3f3f3;
}
.album-item:hover {
  background: #f6f8ff;
}

.new-album {
  display: flex;
  gap: 8px;
}

.new-album input {
  flex: 1;
  padding: 6px 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.new-album button {
  background: #007bff;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 10px;
  cursor: pointer;
}
.new-album button:hover {
  background: #0056b3;
}

.close-btn {
  align-self: flex-end;
  background: #ddd;
  border: none;
  border-radius: 6px;
  padding: 6px 10px;
  cursor: pointer;
}
.close-btn:hover {
  background: #ccc;
}

/* fade animation */
.fade-enter-active, .fade-leave-active { transition: opacity .12s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>

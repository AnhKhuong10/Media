<template>
  <div
    class="photo-list-compo"
    :class="{ 'drop-zone': dragFileStatus }"
    @dragenter.prevent="onDragEnter"
    @dragleave.prevent="onDragLeave"
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <div v-if="!album || album?.photos?.length === 0" class="no-data">
      No data
    </div>

    <div v-else class="grids">
      <div v-for="(item, i) in album?.photos" :key="i" class="photo-card">
        <PhotoItem
          :photo="item"
          @delete-photo="handleDeletePhoto"
          @add-to-album="openAlbumModal"
        />
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
import { ref } from 'vue'
import PhotoItem from './PhotoItem.vue'

defineProps({
  album: Object,
})

const dragFileStatus = ref(false)
const showAlbumModal = ref(false)
const selectedPhoto = ref(null)
const newAlbumName = ref('')
const albums = ref([
  { id: 1, name: 'Kỷ niệm' },
  { id: 2, name: 'Gia đình' },
  { id: 3, name: 'Chuyến đi' },
])

// 🗂 Khi nhấn "Album"
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

// 🔥 Xử lý xóa ảnh trực tiếp (không reload trang)
function handleDeletePhoto(photo) {
  const index = album.photos.indexOf(photo)
  if (index !== -1) {
    album.photos.splice(index, 1)
  }
}

// Kéo thả upload (có sẵn)
const onDragEnter = () => (dragFileStatus.value = true)
const onDragLeave = () => (dragFileStatus.value = false)
const handleDrop = (event) => {
  dragFileStatus.value = false
  const file = event.dataTransfer.files[0]
  if (file && file.type.startsWith('image/')) {
    alert('Giả lập upload ảnh...')
  } else {
    alert('Vui lòng chọn đúng định dạng ảnh!')
  }
}
</script>

<style scoped>
/* === layout gốc === */
.photo-list-compo {
  width: 100%;
  position: relative;
}

.no-data {
  color: #888;
  font-size: 1.1rem;
  text-align: center;
}

.grids {
  display: flex;
  flex-wrap: wrap;
  width: fit-content;
}

.photo-card {
  border-radius: 6px;
  object-fit: cover;
  width: 14vw;
  padding: 5px;
}

/* === modal album === */
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
</style>

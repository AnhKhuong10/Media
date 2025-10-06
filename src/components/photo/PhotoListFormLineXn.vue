<template>
  <div
    class="photo-list-compo"
    :class="{ 'drop-zone': dragFileStatus }"
    @dragenter.prevent="onDragEnter"
    @dragleave.prevent="onDragLeave"
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <div v-if="localPhotos.length === 0" class="no-data">
      No data
    </div>

    <div v-else class="grids">
      <div
        v-for="(item, i) in localPhotos"
        :key="i"
        class="photo-card"
      >
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
import { ref, watch, onMounted } from 'vue'
import PhotoItem from './PhotoItem.vue'

const STORAGE_KEY = 'my_photos_gallery'

const props = defineProps({
  photos: {
    type: Array,
    default: () => [],
  },
  type_component: {
    type: Object,
    default: () => ({ type: 'photos' }),
  },
})

// ✅ localPhotos lưu cả dữ liệu props và ảnh trong localStorage
const localPhotos = ref([])

onMounted(() => {
  // 1️⃣ Lấy ảnh từ localStorage (nếu có)
  const saved = localStorage.getItem(STORAGE_KEY)
  if (saved) {
    try {
      localPhotos.value = JSON.parse(saved)
    } catch {
      localPhotos.value = []
    }
  } else {
    localPhotos.value = [...props.photos]
  }
})

// 2️⃣ Đồng bộ lại khi props.photos thay đổi
watch(
  () => props.photos,
  (newVal) => {
    localPhotos.value = [...newVal]
    saveToLocalStorage()
  },
  { deep: true }
)

// 3️⃣ Tự động lưu vào localStorage mỗi khi localPhotos thay đổi
watch(
  localPhotos,
  (newVal) => {
    saveToLocalStorage()
  },
  { deep: true }
)

const dragFileStatus = ref(false)

const onDragEnter = () => {
  dragFileStatus.value = true
}

const onDragLeave = (event) => {
  if (event.target === event.currentTarget) {
    dragFileStatus.value = false
  }
}

// ✅ Khi thả ảnh, đọc file và thêm trực tiếp vào gallery
const handleDrop = (event) => {
  dragFileStatus.value = false

  const files = Array.from(event.dataTransfer.files).filter((f) =>
    f.type.startsWith('image/')
  )

  if (files.length === 0) {
    alert('Vui lòng chọn đúng định dạng ảnh!')
    return
  }

  files.forEach((file) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      const newPhoto = {
        id: Date.now() + Math.random().toString(36).slice(2), // 🆔 id duy nhất
        url: e.target.result,
        name: file.name,
        liked: false,
      }
      localPhotos.value.unshift(newPhoto)
    }
    reader.readAsDataURL(file)
  })
}

// 🧠 Hàm lưu vào localStorage
function saveToLocalStorage() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(localPhotos.value))
}

// 🗑️ Nhận sự kiện xoá ảnh từ PhotoItem
function handleDeletePhoto(photo) {
  localPhotos.value = localPhotos.value.filter(p => p.id !== photo.id)
  saveToLocalStorage()
}

// ❤️ Nhận sự kiện toggle favorite
function handleToggleFavorite(photo) {
  const target = localPhotos.value.find(p => p.id === photo.id)
  if (target) target.liked = !target.liked
  saveToLocalStorage()
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
  width: 200px;        /* 👈 tất cả ảnh có cùng kích thước */
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
}
</style>


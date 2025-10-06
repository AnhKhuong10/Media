<template>
  <div
    class="photo-list-compo"
    :class="{ 'drop-zone': dragFileStatus }"
    @dragenter.prevent="onDragEnter"
    @dragleave.prevent="onDragLeave"
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <div v-if="!localAlbum || localAlbum.photos.length === 0" class="no-data">
      No data
    </div>

    <div v-else class="grids">
      <div
        v-for="(item, i) in localAlbum.photos"
        :key="item.id || i"
        class="photo-card"
      >
        <PhotoItem
          :photo="item"
          :id_album="localAlbum.id"
          @delete-photo="handleDeletePhoto"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import PhotoItem from './PhotoItem.vue'

const props = defineProps({
  album: {
    type: Object,
    default: () => ({ id: '1', photos: [] }),
  },
})

// ✅ Dùng bản sao của album để thao tác, tránh ảnh hưởng props gốc
const localAlbum = ref({ id: '', photos: [] })

onMounted(() => {
  localAlbum.value = JSON.parse(JSON.stringify(props.album))
})

// Đồng bộ lại khi album thay đổi từ ngoài
watch(
  () => props.album,
  (newVal) => {
    localAlbum.value = JSON.parse(JSON.stringify(newVal))
  },
  { deep: true }
)

// 🧠 Xóa ảnh trực tiếp trong localAlbum mà không reload
function handleDeletePhoto(photo) {
  localAlbum.value.photos = localAlbum.value.photos.filter(
    (p) => p.id !== photo.id
  )
}

// 🧲 Kéo thả upload ảnh
const dragFileStatus = ref(false)
const onDragEnter = () => (dragFileStatus.value = true)
const onDragLeave = () => (dragFileStatus.value = false)

function handleDrop(event) {
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
        id: Date.now() + Math.random().toString(36).slice(2),
        url: e.target.result,
        name: file.name,
        liked: false,
      }
      localAlbum.value.photos.unshift(newPhoto)
    }
    reader.readAsDataURL(file)
  })
}
</script>

<style scoped>
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

.drop-zone::before {
  content: "Thả file ảnh vô đây";
  position: absolute;
  inset: 0;
  border: 2px dashed #11d7ff;
  background-color: rgba(0, 153, 255, 0.34);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #004d66;
  font-weight: bold;
  z-index: 10;
}
</style>

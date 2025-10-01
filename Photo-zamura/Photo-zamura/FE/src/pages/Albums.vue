<template>
  <div class="albums-page">
    <h2 class="page-title" v-if="status === 0">Albums</h2>



    <!-- Danh sách album -->
    <div class="album-list" v-if="status === 0">
      <Card v-for="(album, i) in albums" :key="i" @click="albumDetail(album)" class="album-card">
        <template #title>{{ album.name }}</template>
        <template #content>
          <img :src="album.photos[0] ? album.photos[0].url : imgNull" class="album-cover" :alt="album.name" />
        </template>
      </Card>
    </div>

    <!-- Chi tiết album -->
    <div v-else class="album-detail">
      <div class="detail-header">
        <Button label="Quay lại" icon="pi pi-arrow-left" class="p-button-text" @click="status = 0"></Button>
        <!-- <h2>{{ currentAlbum.name }}</h2> -->
      </div>

      <div class="photo-list">
        <!-- PhotoList :photos="album.photos" /> -->
        <PhotoListFormLineXn :album="currentAlbum" />
        <!-- <div v-for="(photo, j) in currentAlbum" :key="j" class="photo-card">
          <img :src="photo" alt="photo" />
        </div> -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Card from "primevue/card";
import Button from "primevue/button";
import PhotoListFormLineXn from "../components/album/PhotoListFormLineXn.vue";
import { DataAlbumUser } from "../graphql/FakeData.js"

const imgNull = 'https://t3.ftcdn.net/jpg/03/08/79/70/360_F_308797039_9fsJmkRwEcLJT73bk0EbGIqKpQiibfVa.jpg'
// STATE
const status = ref(0); // 0 = list, khác 0 = detail
const currentAlbum = ref(null);
const dataDetailAlbum = ref({})
const albums = ref([
  {
    name: "Vacation",
    cover: "https://picsum.photos/id/1018/300/200",
    photos: [
      "https://picsum.photos/id/1018/400/300",
      "https://picsum.photos/id/1015/400/300",
      "https://picsum.photos/id/1016/400/300",
      "https://picsum.photos/id/1018/400/300",
      "https://picsum.photos/id/1015/400/300",
      "https://picsum.photos/id/1016/400/300",
      "https://picsum.photos/id/1018/400/300",
      "https://picsum.photos/id/1015/400/300",
      "https://picsum.photos/id/1016/400/300",
    ],
  },
  {
    name: "Family",
    cover: "https://picsum.photos/id/1025/300/200",
    photos: [
      "https://picsum.photos/id/1025/400/300",
      "https://picsum.photos/id/1021/400/300",
    ],
  },
  {
    name: "Work",
    cover: "https://picsum.photos/id/1033/300/200",
    photos: [
      "https://picsum.photos/id/1033/400/300",
      "https://picsum.photos/id/1035/400/300",
    ],
  },
]);

albums.value = DataAlbumUser.data.getUserAlbums

// METHODS
const albumDetail = (album) => {
  currentAlbum.value = album;
  status.value = 1;
  // console.log('album check: ', DataAlbumUser)

};
</script>

<style scoped>
.albums-page {
  min-height: calc(100vh - 80px);
  padding: 1rem;
}

.album-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1rem;
  margin-top: 1.5rem;
}

.album-cover {
  width: 100%;
  aspect-ratio: 3/2;
  object-fit: cover;
  border-radius: 6px;
}

.album-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.album-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* DETAIL PAGE */
.album-detail {
  margin-top: 1rem;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.photo-list {
  /* display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1rem;
  margin-top: 1rem; */
}

.photo-card img {
  width: 100%;
  border-radius: 6px;
  object-fit: cover;
}
</style>

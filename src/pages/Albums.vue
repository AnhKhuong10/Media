<template>
  <div class="album-page">
    <!-- 🔹 Header gồm 2 nút chế độ + nút tạo album -->
    <div class="album-header">
      <div class="mode-group">
        <Button
          :label="allLabel"
          :class="['mode-btn', { active: albumMode === 'all' }]"
          @click="changeAlbumMode('all')"
        />
        <Button
          :label="sharedLabel"
          :class="['mode-btn', { active: albumMode === 'shared' }]"
          @click="changeAlbumMode('shared')"
        />
      </div>
      <Button label="➕ Tạo Album" class="create-btn" @click="createAlbum" />
    </div>

    <!-- 🔹 Danh sách album -->
    <div class="album-list">
      <template v-if="filteredAlbums.length > 0">
        <Card
          v-for="(album, index) in filteredAlbums"
          :key="album.id"
          v-if="album && album.name"
          class="album-card"
        >
          <div class="album-card-content" @click="openAlbumDetail(album)">
            <img
              :src="album.cover || defaultAlbumImage"
              alt="cover"
              class="album-cover"
            />
            <h3 class="album-name">{{ album.name }}</h3>
            <p class="album-count">{{ album.photos?.length || 0 }} ảnh</p>
          </div>

          <Button
            icon="pi pi-ellipsis-v"
            class="menu-btn"
            @click.stop="toggleMenu(index, $event)"
          />

          <div v-if="openMenuIndex === index" class="album-menu">
            <Button label="Sửa" @click="handleEditAlbum(album)" />
            <Button label="Chia sẻ" @click="handleShareAlbum(album)" />
            <Button label="Xóa" severity="danger" @click="handleDeleteAlbum(album)" />
          </div>
        </Card>
      </template>

      <p v-else class="no-album-text">Chưa có album nào, hãy tạo album mới 🎵</p>
    </div>

    <!-- 🔹 Các dialog chia sẻ, quyền, xóa -->
    <Dialog v-model:visible="showShareDialog" header="Chia sẻ album">
      <p>Link chia sẻ: <strong>{{ albumShareLink }}</strong></p>
      <Button label="Sao chép liên kết" @click="handleCopyLink(albumShareLink)" />
      <hr />
      <h4>Chia sẻ với người dùng:</h4>
      <ul>
        <li
          v-for="user in userList"
          :key="user.id"
          class="share-user"
          @click="selectUserToShare(user)"
        >
          👤 {{ user.name }}
        </li>
      </ul>
    </Dialog>

    <Dialog v-model:visible="showPermissionDialog" header="Chọn quyền chia sẻ">
      <Dropdown
        v-model="selectedPermission"
        :options="permissionOptions"
        optionLabel="name"
        placeholder="Chọn quyền"
      />
      <Button label="Xác nhận" @click="confirmShareAlbum" />
    </Dialog>

    <Dialog v-model:visible="showDeleteAlbumDialog" header="Xác nhận xóa album">
      <p>Bạn có chắc muốn xóa album này?</p>
      <Button label="Xóa" severity="danger" @click="confirmDeleteAlbum" />
    </Dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { albumShare } from "../graphql/FakeData.js";
import { DataAlbumUser, userList as mockUserList } from "../graphql/FakeData.js";
import Button from "primevue/button";
import Card from "primevue/card";
import Dialog from "primevue/dialog";
import Dropdown from "primevue/dropdown";

const defaultAlbumImage =
  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEaYTaC-q-QWUu2g7QgVvRKkJkqXjXtjBU2w&s";

const albumList = ref(JSON.parse(localStorage.getItem("albumList") || "[]"));
if (albumList.value.length === 0) {
  albumList.value = DataAlbumUser.data.getUserAlbums || [];
  localStorage.setItem("albumList", JSON.stringify(albumList.value));
}
watch(albumList, (val) => localStorage.setItem("albumList", JSON.stringify(val)), {
  deep: true,
});

const albumMode = ref("all");
const allLabel = "Tất cả album";
const sharedLabel = "Album đã chia sẻ";

const filteredAlbums = computed(() => {
  if (albumMode.value === "shared") {
    const sharedAlbums = albumList.value.filter((a) => a.isShared);
    return [...albumShare, ...sharedAlbums];
  }
  return albumList.value;
});

const changeAlbumMode = (mode) => (albumMode.value = mode);

const selectedAlbum = ref(null);
const showShareDialog = ref(false);
const showPermissionDialog = ref(false);
const showDeleteAlbumDialog = ref(false);
const albumShareLink = ref("");
const openMenuIndex = ref(null);
const userList = ref(mockUserList.data.getUsers || []);
const selectedShareInfo = ref({ albumId: "", user: {} });
const selectedPermission = ref(null);
const permissionOptions = ref([
  { name: "Chỉ được xem", code: "VIEW" },
  { name: "Được xem và sửa đổi", code: "EDIT" },
  { name: "Công khai với mọi người", code: "PUBLIC" },
]);

const createAlbum = () => {
  const name = prompt("Nhập tên album mới:");
  if (!name) return;
  const newAlbum = {
    id: Date.now().toString(),
    name,
    photos: [],
    isShared: false,
    cover: defaultAlbumImage,
  };
  albumList.value.push(newAlbum);
  localStorage.setItem("albumList", JSON.stringify(albumList.value));
  alert(`Đã tạo album "${name}"`);
};

const handleEditAlbum = (album) => {
  closeMenu();
  alert(`Mở form sửa album: ${album.name}`);
};

const handleShareAlbum = (album) => {
  closeMenu();
  selectedAlbum.value = album;
  albumShareLink.value = `${window.location.origin}/share/album/${album.id}`;
  showShareDialog.value = true;
};

const handleDeleteAlbum = (album) => {
  closeMenu();
  const ok = confirm(`Bạn có chắc muốn xóa album "${album.name}"?`);
  if (!ok) return;
  albumList.value = albumList.value.filter((a) => a.id !== album.id);
  localStorage.setItem("albumList", JSON.stringify(albumList.value));
  alert("Đã xóa album!");
};

const selectUserToShare = (user) => {
  selectedShareInfo.value.user = user;
  selectedShareInfo.value.albumId = selectedAlbum.value?.id || "";
  showPermissionDialog.value = true;
};

const confirmShareAlbum = () => {
  showPermissionDialog.value = false;
  showShareDialog.value = false;
  alert(
    `Chia sẻ album id=${selectedShareInfo.value.albumId} với ${
      selectedShareInfo.value.user?.name || "—"
    } (quyền: ${selectedPermission.value?.name || "VIEW"})`
  );
  selectedShareInfo.value = { albumId: "", user: {} };
  selectedPermission.value = null;
};

const handleCopyLink = (url) => {
  if (!url) return;
  navigator.clipboard.writeText(url);
  alert("Đã sao chép liên kết!");
};

const toggleMenu = (index) => {
  openMenuIndex.value = openMenuIndex.value === index ? null : index;
};
const closeMenu = () => (openMenuIndex.value = null);

onMounted(() => {
  window.addEventListener("keydown", (e) => {
    if (e.key === "Escape") openMenuIndex.value = null;
  });
});
</script>

<style scoped>
.album-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  background: #f7f7f7;
}

/* 🔹 Header */
.album-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 12px 16px;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.mode-group {
  display: flex;
  gap: 10px;
}

.mode-btn {
  padding: 6px 18px;
  border-radius: 6px;
  background: #f3f4f6;
  color: #222;
  border: 1px solid #e5e7eb;
  font-weight: 500;
  cursor: pointer;
}

.mode-btn.active {
  background: #6366f1;
  color: #fff;
  border-color: #6366f1;
}

.create-btn {
  background: #22c55e;
  color: white;
  border: none;
  font-weight: 600;
  border-radius: 8px;
  padding: 8px 16px;
}

/* 🔹 Album grid */
.album-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 18px;
}

.album-card {
  position: relative;
  background: #fff;
  border-radius: 8px;
  padding: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s;
}
.album-card:hover {
  transform: scale(1.02);
}
.album-cover {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 6px;
}
.album-name {
  margin-top: 8px;
  font-weight: 600;
  font-size: 14px;
}
.album-count {
  font-size: 12px;
  color: #666;
}

.album-menu {
  position: absolute;
  right: 8px;
  top: 36px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 4px;
  z-index: 10;
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.menu-btn {
  position: absolute;
  top: 8px;
  right: 8px;
}

/* 🔹 Khi không có album */
.no-album-text {
  text-align: center;
  color: #777;
  font-size: 15px;
  grid-column: 1 / -1;
  margin-top: 30px;
}
</style>

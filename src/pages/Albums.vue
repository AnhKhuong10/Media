<template>
  <div class="albums-page">
    <!-- Danh sách album (và header nằm trong v-if) -->
    <div class="album-list-wrap" v-if="viewMode === 'list'">
      <!-- Header (chỉ hiện khi ở chế độ list) -->
      <div class="header">
        <div class="header-left">
          <h2 class="page-title">Albums</h2>
          <p class="subtle">Tổng {{ filteredAlbums.length }} album</p>
        </div>
        <div class="actions">
          <Button label="Tạo album" icon="pi pi-plus" class="p-button-outlined create-album-btn" @click="createAlbum" />
        </div>
      </div>
      <AlbumListHeader :mode="albumMode" @changeMode="changeAlbumMode" />
      <div class="album-list">
        <Card v-for="(album, index) in filteredAlbums" :key="album.id ?? index" class="album-card"
          :style="{ '--accent': album.accent || '#6c5ce7' }">
          <!-- card title area -->
          <template #title>
            <div class="title-row" @click.stop="">
              <div class="title-left">
                <div class="album-title">{{ album.name }}</div>
                <div class="album-meta">{{ album.photos?.length || 0 }} ảnh</div>
              </div>

              <!-- three-dot menu -->
              <div class="menu-wrap" @click.stop>
                <button class="icon-btn" @click="toggleMenu(index, $event)" :aria-expanded="openMenuIndex === index"
                  :aria-controls="`album-menu-${index}`" title="Tùy chọn album">
                  <i class="pi pi-ellipsis-v"></i>
                </button>

                <div v-if="openMenuIndex === index" :id="`album-menu-${index}`" class="album-menu" @click.stop>
                  <ul>
                    <li @click="handleEditAlbum(album)">Sửa</li>
                    <li @click="handleShareAlbum(album)">Chia sẻ</li>
                    <li class="danger" @click="handleDeleteAlbum(album)">Xóa</li>
                  </ul>
                </div>
              </div>
            </div>
          </template>

          <!-- card body / cover -->
          <template #content>
            <div class="cover-wrap" @click="openAlbumDetail(album)">
              <img :src="album.photos?.[0]?.url || defaultAlbumImage" class="album-cover" :alt="album.name" />
              <div class="badge shared" v-if="isAlbumShared(album)">
                <i class="pi pi-share-alt"></i> Đã được chia sẻ
              </div>
              <!-- cover-overlay và view-button đã bị loại bỏ để giao diện gọn hơn -->
            </div>
          </template>
        </Card>
      </div>
    </div>

    <!-- Chi tiết album -->
    <div v-else class="album-detail">
      <div class="detail-header">
        <Button label="Quay lại" icon="pi pi-arrow-left" class="p-button-text" @click="viewMode = 'list'" />
        <div class="detail-main-row">
          <div class="detail-title">
            <h3>{{ selectedAlbum?.name }}</h3>
            <p class="muted">{{ selectedAlbum?.photos?.length || 0 }} ảnh</p>
          </div>
          <div class="detail-actions">
            <Button label="Thêm ảnh" icon="pi pi-plus" class="p-button-outlined" @click="showAddPhotoDialog = true" />
            <Button label="Share" icon="pi pi-share-alt" class="p-button-outlined" @click="showShareDialog = true" />
            <div class="menu-wrap" style="display: inline-block; position: relative;">
              <button class="icon-btn" @click="openDetailMenu = !openDetailMenu" :aria-expanded="openDetailMenu"
                aria-label="Tùy chọn album">
                <i class="pi pi-ellipsis-v"></i>
              </button>
              <div v-if="openDetailMenu" class="album-menu" style="position: absolute; right: 0; z-index: 10;">
                <ul>
                  <li @click="showEditAlbumDialog = true; openDetailMenu = false">Sửa album</li>
                  <li class="danger" @click="showDeleteAlbumDialog = true; openDetailMenu = false">Xóa album</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="photo-list">
        <PhotoListFormLineXn :album="selectedAlbum" />
      </div>
      <!-- Dialog thêm ảnh vào album -->
      <Dialog v-model:visible="showAddPhotoDialog" modal header="Thêm ảnh vào album" :style="{ width: '32rem' }">
        <div>
          <h4>Chọn ảnh để thêm vào album</h4>
          <ul class="photo-select-list">
            <li v-for="photo in allPhotos" :key="photo.id"
              style="display: flex; align-items: center; margin-bottom: 8px;">
              <img :src="photo.url" alt="photo"
                style="width: 48px; height: 48px; object-fit: cover; margin-right: 12px;" />
              <span>{{ photo.name }}</span>
              <Button label="Thêm" size="small" style="margin-left: auto;" @click="addPhotoToAlbum(photo)" />
            </li>
          </ul>
        </div>
        <template #footer>
          <Button label="Đóng" severity="secondary" @click="showAddPhotoDialog = false" />
        </template>
      </Dialog>
      <!-- Dialog sửa album -->
      <Dialog v-model:visible="showEditAlbumDialog" modal header="Sửa album" :style="{ width: '28rem' }">
        <div>
          <label>Tên album</label>
          <input v-model="editAlbumName" class="p-inputtext" style="width: 100%; margin-bottom: 1rem;" />
        </div>
        <template #footer>
          <Button label="Hủy" severity="secondary" @click="showEditAlbumDialog = false" />
          <Button label="Lưu" @click="saveEditAlbum"></Button>
        </template>
      </Dialog>
      <!-- Dialog xác nhận xóa album -->
      <Dialog v-model:visible="showDeleteAlbumDialog" modal header="Xóa album" :style="{ width: '22rem' }">
        <div>Bạn có chắc muốn xóa album này không?</div>
        <template #footer>
          <Button label="Hủy" severity="secondary" @click="showDeleteAlbumDialog = false" />
          <Button label="Xóa" severity="danger" @click="confirmDeleteAlbum" />
        </template>
      </Dialog>
    </div>

    <!-- Dialog chia sẻ album -->
    <Dialog v-model:visible="showShareDialog" modal :style="{ width: '34rem' }" class="custom-dialog">
      <template #header>
        <div class="dialog-header">
          <img :src="selectedAlbum?.photos?.[0]?.url || defaultAlbumImage" class="thumb" />
          <div class="info">
            <h3 class="album-name">{{ selectedAlbum?.name }}</h3>
            <p class="muted">{{ selectedAlbum?.photos?.length || 0 }} ảnh</p>
          </div>
        </div>
      </template>

      <div class="dialog-body">
        <h4>Chọn người dùng để chia sẻ</h4>
        <hr class="thin-sep" />
        <ul class="folder-list">
          <li v-for="(user, idx) in userList" :key="user.id ?? idx" class="album-item" @click="selectUserToShare(user)">
            <i class="pi pi-user"></i>
            <div class="user-info">
              <div class="user-name">{{ user.name }}</div>
              <div class="user-email muted">{{ user.email }}</div>
            </div>
            <div class="action">
              <Button icon="pi pi-chevron-right" class="p-button-text" @click.stop="selectUserToShare(user)" />
            </div>
          </li>
        </ul>

        <div class="share-options">
          <Button icon="pi pi-link" @click="handleCopyLink(albumShareLink)" label="Tạo liên kết" text />
          <Button icon="pi pi-facebook" label="Facebook" text />
          <Button icon="pi pi-envelope" label="Email" text />
        </div>
      </div>

      <template #footer>
        <div class="flex justify-end gap-2">
          <Button label="Đóng" severity="secondary" @click="showShareDialog = false" />
          <Button label="Chia sẻ" @click="confirmShareAlbum" />
        </div>
      </template>
    </Dialog>

    <!-- Dialog phân quyền chia sẻ -->
    <Dialog v-model:visible="showPermissionDialog" modal header="Phân quyền chia sẻ" :style="{ width: '25rem' }">
      <span class="muted block mb-4">Chọn quyền cho người dùng</span>
      <Dropdown v-model="selectedPermission" :options="permissionOptions" optionLabel="name" placeholder="Chọn quyền"
        class="w-full md:w-14rem" />
      <div class="flex justify-end gap-2 mt-4">
        <Button label="Thoát" severity="secondary" @click="showPermissionDialog = false" />
        <Button label="Xác nhận" @click="confirmShareAlbum" />
      </div>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from "vue";
import AlbumListHeader from "../components/album/AlbumListHeader.vue";
import { albumShare } from "../graphql/FakeData.js";
import Card from "primevue/card";
import Button from "primevue/button";
import Dropdown from "primevue/dropdown";
import Dialog from "primevue/dialog";

import PhotoListFormLineXn from "../components/album/PhotoListFormLineXn.vue";
import { DataAlbumUser, userList as mockUserList } from "../graphql/FakeData.js";

// Constants
const defaultAlbumImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEaYTaC-q-QWUu2g7QgVvRKkJkqXjXtjBU2w&s";

// State
const viewMode = ref("list"); // 'list' or 'detail'
const albumList = ref(DataAlbumUser.data.getUserAlbums || []);
const albumMode = ref('all'); // 'all' | 'shared'
const filteredAlbums = computed(() => {
  if (albumMode.value === 'shared') {
    // Kết hợp albumShare và các album có isShared
    const sharedAlbums = albumList.value.filter(a => a.isShared);
    return [...albumShare, ...sharedAlbums];
  }
  return albumList.value;
});
function changeAlbumMode(mode) {
  albumMode.value = mode;
}
const selectedAlbum = ref(null);


const showShareDialog = ref(false);
const showPermissionDialog = ref(false);
const showAddPhotoDialog = ref(false);
const showEditAlbumDialog = ref(false);
const showDeleteAlbumDialog = ref(false);
const openDetailMenu = ref(false);
const editAlbumName = ref("");

// Giả lập danh sách ảnh (cần thay bằng API thực tế)
const allPhotos = ref([
  { id: 1, name: "Ảnh 1", url: "https://picsum.photos/id/101/200/200" },
  { id: 2, name: "Ảnh 2", url: "https://picsum.photos/id/102/200/200" },
  { id: 3, name: "Ảnh 3", url: "https://picsum.photos/id/103/200/200" },
]);

const userList = ref(mockUserList.data.getUsers || []);
const selectedShareInfo = ref({
  albumId: "",
  user: {},
});

const selectedPermission = ref(null);
const permissionOptions = ref([
  { name: "Chỉ được xem", code: "VIEW" },
  { name: "Được xem và sửa đổi", code: "EDIT" },
  { name: "Công khai với mọi người", code: "PUBLIC" },
]);

// menu state
const openMenuIndex = ref(null);
const albumShareLink = ref("");

// Methods
const createAlbum = () => {
  // stub: mở modal tạo album hoặc điều hướng tới form - cắm logic ở đây
  alert("Mở form tạo album (mock).");
};

const addPhotoToAlbum = (photo) => {
  if (selectedAlbum.value) {
    selectedAlbum.value.photos = selectedAlbum.value.photos || [];
    selectedAlbum.value.photos.push(photo);
  }
};

const saveEditAlbum = () => {
  if (selectedAlbum.value && editAlbumName.value) {
    selectedAlbum.value.name = editAlbumName.value;
    showEditAlbumDialog.value = false;
  }
};

const confirmDeleteAlbum = () => {
  // Xóa album khỏi danh sách
  const idx = albumList.value.findIndex(a => a.id === selectedAlbum.value.id);
  if (idx !== -1) {
    albumList.value.splice(idx, 1);
    viewMode.value = 'list';
    showDeleteAlbumDialog.value = false;
  }
};

const openAlbumDetail = (album) => {
  selectedAlbum.value = album;
  viewMode.value = "detail";
  closeMenu();
};

const toggleMenu = (index, event) => {
  if (openMenuIndex.value === index) openMenuIndex.value = null;
  else {
    openMenuIndex.value = index;
    // Optional: close menu on outside click
    nextTick(() => {
      window.addEventListener("click", onWindowClick);
    });
  }
};

const onWindowClick = () => {
  closeMenu();
};

const closeMenu = () => {
  openMenuIndex.value = null;
  window.removeEventListener("click", onWindowClick);
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
  alert("Đã xóa album (mock).");
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
    `Chia sẻ album id=${selectedShareInfo.value.albumId} với ${selectedShareInfo.value.user?.name || "—"} (quyền: ${selectedPermission.value?.name || "VIEW"})`
  );
  selectedShareInfo.value = { albumId: "", user: {} };
  selectedPermission.value = null;
};

const handleCopyLink = (url) => {
  if (!url) return;
  navigator.clipboard.writeText(url);
  alert("Đã sao chép liên kết!");
};

const isAlbumShared = (album) => {
  return !!album.isShared;
};

onMounted(() => {
  window.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeMenu();
  });
});
</script>

<style scoped>
.albums-page {
  min-height: calc(100vh - 80px);
  padding: 1.25rem;
  background: linear-gradient(180deg, #f7f8fc 0%, #ffffff 100%);
  color: #222;
}

/* header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.9rem;
  padding: 0.25rem 0;
}

.header-left {
  display: flex;
  flex-direction: column;
}

.page-title {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 700;
  color: #2d2d2d;
}

.subtle {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 0.85rem;
}

.actions {
  display: flex;
  align-items: center;
}

/* small tweak for create button so it doesn't look crowded on small screens */
.create-album-btn {
  white-space: nowrap;
}

/* grid wrapper */
.album-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 1rem;
}

/* card */
.album-card {
  cursor: default;
  overflow: visible;
  border-radius: 10px;
  transition: transform 0.18s ease, box-shadow 0.18s ease;
}

.album-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 25px rgba(40, 40, 60, 0.06);
}

/* title row */
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 0.5rem;
  width: 100%;
}

.title-left {
  display: flex;
  flex-direction: column;
}

.album-title {
  font-weight: 600;
  font-size: 0.98rem;
  color: #111827;
}

.album-meta {
  font-size: 0.78rem;
  color: #6b7280;
  margin-top: 2px;
}

/* three-dot icon */
.icon-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 6px;
  border-radius: 6px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.icon-btn:hover {
  background: rgba(0, 0, 0, 0.04);
}

/* menu */
.menu-wrap {
  position: relative;
}

.album-menu {
  position: absolute;
  right: 0;
  top: 34px;
  background: white;
  border: 1px solid rgba(15, 23, 42, 0.06);
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.06);
  border-radius: 8px;
  min-width: 160px;
  z-index: 30;
}

.album-menu ul {
  list-style: none;
  margin: 0;
  padding: 6px 0;
}

.album-menu li {
  padding: 10px 14px;
  cursor: pointer;
  font-size: 0.95rem;
  color: #111827;
}

.album-menu li:hover {
  background: rgba(15, 23, 42, 0.03);
}

.album-menu li.danger {
  color: #dc2626;
}

/* cover */
.cover-wrap {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
}

.album-cover {
  width: 100%;
  aspect-ratio: 3/2;
  object-fit: cover;
  display: block;
  transition: transform 0.2s ease;
}

.cover-wrap:hover .album-cover {
  transform: scale(1.03);
}

/* overlay */
.cover-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: end;
  justify-content: center;
  pointer-events: none;
}

.view-button {
  margin: 12px;
  pointer-events: auto;
}

/* shared badge */
.badge.shared {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(34, 197, 94, 0.12);
  color: #16a34a;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 0.78rem;
  display: inline-flex;
  gap: 6px;
  align-items: center;
}

/* dialog */
.custom-dialog .dialog-header {
  display: flex;
  gap: 12px;
  align-items: center;
}

.thumb {
  width: 56px;
  height: 56px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 6px 18px rgba(16, 24, 40, 0.06);
}

.info .album-name {
  margin: 0;
  font-size: 1.05rem;
}

.muted {
  color: #6b7280;
  font-size: 0.9rem;
}

/* user list in dialog */
.folder-list {
  list-style: none;
  padding: 0;
  margin: 0.5rem 0 1rem 0;
  max-height: 220px;
  overflow: auto;
}

.album-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  cursor: pointer;
  border-radius: 8px;
}

.album-item:hover {
  background: rgba(15, 23, 42, 0.03);
}

.user-info {
  flex: 1;
}

.user-name {
  font-weight: 600;
}

.user-email {
  font-size: 0.82rem;
}

/* separator */
.thin-sep {
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  margin: 8px 0 12px;
}

/* share options */
.share-options {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-top: 8px;
}

/* detail view */
.album-detail {
  margin-top: 1rem;
}

.detail-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 0.5rem;
  margin-bottom: 12px;
}

.detail-main-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  gap: 1.5rem;
}

.detail-title {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.detail-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}


.detail-title h3 {
  margin: 0;
}

/* responsive tweaks */
@media (max-width: 640px) {
  .header {
    flex-direction: column;
    align-items: stretch;
    gap: 0.5rem;
  }

  .actions {
    justify-content: flex-end;
  }
}
</style>

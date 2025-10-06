<script setup>
import { ref, computed, onMounted, nextTick, watch } from "vue";
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

// 🔹 1. Load album từ localStorage (nếu có)
const savedAlbums = JSON.parse(localStorage.getItem("albumList"));
const albumList = ref(savedAlbums || DataAlbumUser.data.getUserAlbums || []);

// 🔹 2. Theo dõi và lưu lại localStorage khi có thay đổi
watch(
  albumList,
  (newVal) => {
    localStorage.setItem("albumList", JSON.stringify(newVal));
  },
  { deep: true }
);

const viewMode = ref("list"); // 'list' or 'detail'
const albumMode = ref('all'); // 'all' | 'shared'

const filteredAlbums = computed(() => {
  if (albumMode.value === 'shared') {
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

const openMenuIndex = ref(null);
const albumShareLink = ref("");

// ---- Methods ----

const createAlbum = () => {
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

// ✅ Cập nhật xóa album (sẽ tự lưu vào localStorage nhờ watch)
const confirmDeleteAlbum = () => {
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

// ✅ Xóa album trong danh sách (tự lưu localStorage)
const handleDeleteAlbum = (album) => {
  closeMenu();
  const ok = confirm(`Bạn có chắc muốn xóa album "${album.name}"?`);
  if (!ok) return;
  albumList.value = albumList.value.filter((a) => a.id !== album.id);
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

<template>
  <section class="card">
    <!-- Header -->
    <header class="card-hd">
      <div class="hd-left">
        <h2>Danh sách Poster</h2>
        <span class="muted">{{ totalElements }} bản ghi</span>
      </div>
      <div class="hd-right">
        <button class="btn primary" @click="showModalCreate = true">Tạo mới</button>
      </div>
    </header>

    <!-- Toolbar -->
    <div class="toolbar">
      <input v-model="q" type="search" class="input" placeholder="Tìm theo tiêu đề, nội dung, người tạo…"
        @input="onSearch" />
      <!-- <label class="check"><input type="checkbox" v-model="draftOnly" /><span>Draft</span></label>
      <label class="check"><input type="checkbox" v-model="deletedOnly" /><span>Deleted</span></label> -->
      <select v-model="styleFilter" class="select" style="width: 35%;">
        <option value="">Tất cả kiểu</option>
        <option v-for="s in styleOptions" :key="s" :value="s">{{ s }}</option>
      </select>
    </div>

    <!-- Table -->
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>STT</th>
            <th>Poster ID</th>
            <th>Title</th>
            <th>Content</th>
            <th>Created at</th>
            <th>Created By</th>
            <th>Draft</th>
            <th>Type</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <poster v-for="(p, index) in posters" :key="p.posterId" :poster="p" :index="index + 1" @view="onView"
            @edit="onEdit" @delete="onDelete" />
          <tr v-if="posters.length === 0">
            <td colspan="13" class="empty">Không có dữ liệu phù hợp.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <footer class="pager">
      <div class="spacer"></div>

      <button class="btn sm" :disabled="pageNumber === 0" @click="pageNumber = 0">
        «
      </button>

      <button class="btn sm" :disabled="pageNumber === 0" @click="pageNumber--">
        ‹
      </button>

      <span class="muted">Trang {{ pageNumber + 1 }} / {{ totalPages }}</span>

      <button class="btn sm" :disabled="pageNumber >= totalPages - 1" @click="pageNumber++">
        ›
      </button>

      <button class="btn sm" :disabled="pageNumber >= totalPages - 1" @click="pageNumber = totalPages - 1">
        »
      </button>
    </footer>

    <!-- modal preview -->
    <div v-if="showModalPreview" class="modal-backdrop" @click="showModalPreview = false">
      <div class="modal-preview" @click.stop>
        <div class="modal-hd">
          <button class="btn" @click="showModalPreview = false">Đóng</button>
        </div>
        <div class="modal-body">
          <!-- Khung preview (tỉ lệ poster) -->
          <div class="poster-preview-shell">
            <div class="poster-preview-surface">
              <component v-if="previewForm" :is="previewComponent" :form="previewForm" :preview-photo="previewPhoto" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- modal edit -->
    <div v-if="showModalEdit" class="modal-backdrop" @click="showModalEdit = false">
      <div class="modal-edit" @click.stop>
        <div class="modal-hd">
          <button class="btn" @click="showModalEdit = false">Đóng</button>
        </div>
        <div class="modal-body">
          <PostersPage :posterData="editingPoster" mode="edit" @update-success="onUpdateSuccess" />
        </div>
      </div>
    </div>

    <!-- modal create -->
    <div v-if="showModalCreate" class="modal-backdrop" @click="showModalCreate = false">
      <div class="modal-edit" @click.stop>
        <div class="modal-hd">
          <button class="btn" @click="showModalCreate = false">Đóng</button>
        </div>
        <div class="modal-body">
          <PostersPage mode="create" />
        </div>
      </div>
    </div>

  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import poster from "../components/Poster.vue";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import PostersPage from "../components/PostersPage.vue";
import type { Poster, PosterDTO, PosterPage, PosterUpdateDTO } from "../model/poster"; // nếu chưa có, xem chú thích ở cuối
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
import { IMAGE_URL } from "../api/configService";

// lấy danh sách poster
import { getAllPoster } from "../api/graphql/poster-service-graphql";
import { PosterType } from "../model/poster";
const pageNumber = ref(0);
const pageSize = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);
const posters = ref<PosterDTO[]>([]);

onMounted(fetchPosters);
const loading = ref(false);
const q = ref("");
async function fetchPosters() {
  try {
    loading.value = true;
    const posterPage: PosterPage = await getAllPoster(
      pageNumber.value,
      pageSize.value,
      q.value.trim()
    );
    posters.value = posterPage.content;
    totalPages.value = posterPage.totalPages;
    totalElements.value = posterPage.totalElements;
    pageNumber.value = posterPage.pageNumber;
    pageSize.value = posterPage.pageSize;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách Poster:", error);
  } finally {
    loading.value = false;
  }
}
let searchTimer: number | undefined;
function onSearch() {
  clearTimeout(searchTimer);
  searchTimer = window.setTimeout(() => {
    pageNumber.value = 0; // reset về trang đầu khi tìm
    fetchPosters();
  }, 200);
}
// Khi người dùng chuyển trang
watch(pageNumber, () => {
  fetchPosters();
});
// end

const previewPhoto = ref<string>(defaultLogo);
const showModalEdit = ref(false);
const showModalPreview = ref(false);
const showModalCreate = ref(false);

const editingPoster = ref<PosterDTO | null>(null); // dữ liệu poster đang edit

const selectedPoster = ref<PosterDTO | null>(null);
function resolvePreviewComponent(p?: PosterDTO | null) {
  if (!p) return PosterNewHire;

  const type = p.posterType || "";
  if (type === PosterType.new_employee.toString()) return PosterNewHire;
  if (type === PosterType.honor.toString()) return PosterRecognition;

  return PosterNewHire; // fallback
}
const previewComponent = computed(() => resolvePreviewComponent(selectedPoster.value));

// Adapter dữ liệu từ Poster -> form/props mà poster component cần
const previewForm = computed(() => {
  const p = selectedPoster.value;
  if (!p) return null;
  return {
    posterType:
      resolvePreviewComponent(p) === PosterNewHire
        ? PosterType.new_employee
        : PosterType.honor,
    title: p.title,
    companyName: p.companyName,
    logo: defaultLogo,
    user: p.user,
  };
});

// search & filters
const draftOnly = ref(false);
const deletedOnly = ref(false);
const styleFilter = ref<string>("");

// select type
const styleOptions = computed(() => {
  // lấy ra 1 mảng các posterType (loại bỏ null/undefined)
  const types = posters.value.map((p) => p.posterType).filter(Boolean);
  // trả về 1 set kh bị trùng lặp các type
  return Array.from(new Set(types));
});

// actions (stub)
function onCreate() {
  alert("Mở form tạo mới");
}

// Hàm onView sẽ gán formNewHire vào selectedPoster
function onView(p: PosterDTO) {
  console.log("Poster detail:", p); // ✅ log ra để xem toàn bộ object
  previewPhoto.value = `${IMAGE_URL}${p.user.avatar}`;
  selectedPoster.value = p; // gán poster đang chọn
  showModalPreview.value = true; // mở modal
}
function onEdit(p: PosterDTO) {
  editingPoster.value = { ...p };
  showModalEdit.value = true;
}
function onUpdateSuccess() {
  console.log("✅ Nhận emit thành công từ con!");
  showModalEdit.value = false;
  fetchPosters();
}
function onDelete(p: Poster) {
  alert(`Xóa ${p.posterId}`);
}
</script>

<style scoped>
/* ===== Layout & text (đơn giản, sáng, dễ đọc) ===== */
.card {
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
}

.card-hd {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #eef2f7;
}

.card-hd h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.muted {
  color: #6b7280;
  font-size: 13px;
  font-weight: bold;
}

/* Buttons: đơn giản */
.btn {
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.btn:hover {
  background: #f9fafb;
}

.btn.primary {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.btn.primary:hover {
  background: #1d4ed8;
}

.btn.sm {
  padding: 6px 10px;
}

/* Toolbar gọn */
.toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  padding: 12px 0;
  padding-right: 830px;
}

.input {
  min-width: 260px;
}

.check {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #374151;
}

.check input {
  width: 16px;
  height: 16px;
}

/* ===== Table đơn giản ===== */
.table-wrap {
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  overflow: auto;
  /* scroll ngang khi chật */
}

.table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 14px;
}

.table thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f8fafc;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
  text-align: left;
  font-weight: bold;
  padding: 10px 12px;
  color: #111;
}

.table tbody td {
  padding: 10px 12px;
  border-bottom: 1px solid #f2f4f7;
  color: #111;
}

/* Zebra + hover */
.table tbody tr:nth-child(odd) {
  background: #fff;
}

.table tbody tr:nth-child(even) {
  background: #fcfdff;
}

.table tbody tr:hover {
  background: #eef6ff;
}

/* Cột rộng hẹp hợp lý */
.table th:nth-child(1),
.table td:nth-child(1) {
  width: 50px;
}

.table th:nth-child(2),
.table td:nth-child(2) {
  width: 90px;
}

.table th:nth-child(3),
.table td:nth-child(3) {
  width: 200px;
}

.table th:nth-child(4),
.table td:nth-child(4) {
  width: 200px;
}

.table th:nth-child(5),
.table td:nth-child(5) {
  width: 120px;
}

.table th:nth-child(6),
.table td:nth-child(6) {
  width: 90px;
  text-transform: capitalize;
}

.table th:nth-child(7),
.table td:nth-child(7) {
  width: 90px;
}

.table th:nth-child(8),
.table td:nth-child(8) {
  width: 110px;
}

.table th:last-child,
.table td:last-child {
  width: 100px;
}

/* Rút gọn text dài ở Title/Content */
.table td:nth-child(2),
.table td:nth-child(3) {
  max-width: 420px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Empty state */
.empty {
  text-align: center;
  color: #6b7280;
  padding: 28px 12px;
}

/* ===== Pagination ===== */
.pager {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-top: 12px;
  border-top: 1px solid #eef2f7;
}

.pager .spacer {
  display: f;
}

.select.sm {
  height: 32px;
  padding: 0 8px;
  font-size: 13px;
}

/* Responsive nhỏ gọn */
@media (max-width: 900px) {
  .input {
    min-width: unset;
    flex: 1;
  }

  .table td:nth-child(2),
  .table td:nth-child(3) {
    max-width: 260px;
  }
}

/* thu nhỏ ô select */
.select {
  height: 32px;
  /* thấp hơn */
  max-width: 180px;
  /* giới hạn ngang */
  font-size: 14px;
  /* chữ vừa */
  padding: 0 8px;
}

/* menu xổ xuống cũng nhỏ lại */
.select option {
  font-size: 14px;
  padding: 4px 8px;
}

.input,
.select {
  height: 36px;
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 0 10px;
  outline: none;
}

.input {
  height: 32px;
  /* thấp hơn */
  max-width: 180px;
  /* giới hạn ngang */
  font-size: 14px;
  /* chữ vừa */
}

/* Nền tối phủ toàn màn hình */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(2, 6, 23, 0.55);
  display: grid;
  place-items: center;
  z-index: 9999;
  /* luôn nổi trên cùng */
}

/* Hộp modal */
.modal-preview {
  width: min(90vw, 930px);
  max-height: 90vh;
  background: #fff;
  color: #111;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(2, 6, 23, 0.45);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-edit {
  width: min(90vw, 1400px);
  max-height: 100vh;
  background: #fff;
  color: #111;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(2, 6, 23, 0.45);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* Header modal */
.modal-hd {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

/* Nút đóng */
.btn {
  background: #e5e7eb;
  border: none;
  color: #111;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn:hover {
  background: #d1d5db;
}

/* Body modal */
.modal-body {
  flex: 1;
  overflow: auto;
  padding: 16 px;
  background: #fff;
}

/* Vỏ bọc preview poster */
.poster-preview-shell {
  display: grid;
  place-items: center;
}

/* Khung preview poster (tỉ lệ cố định) */
.poster-preview-surface {
  width: min(100%, 850px);
  aspect-ratio: 3 / 4;
  /* tỉ lệ đứng 3:4 */
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

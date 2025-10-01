<template>
  <section class="card">
    <!-- Header -->
    <header class="card-hd">
      <div class="hd-left">
        <h2>Danh sách Poster</h2>
        <span class="muted">{{ total }} bản ghi</span>
      </div>
      <div class="hd-right">
        <button class="btn primary" @click="onCreate">Tạo mới</button>
        <button class="btn" @click="null">Export CSV</button>
      </div>
    </header>

    <!-- Toolbar -->
    <div class="toolbar">
      <input v-model="q" type="search" class="input" placeholder="Tìm theo tiêu đề, nội dung, người tạo…" />
      <label class="check"><input type="checkbox" v-model="draftOnly" /><span>Draft</span></label>
      <label class="check"><input type="checkbox" v-model="deletedOnly" /><span>Deleted</span></label>
      <select v-model="styleFilter" class="select">
        <option value="">Tất cả style</option>
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
            <th>Style</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <poster v-for="(p, index) in paginated" :key="p.posterId" :poster="p" :index="index + 1" @view="onView"
            @edit="onEdit" @delete="onDelete" />
          <tr v-if="paginated.length === 0">
            <td colspan="13" class="empty">Không có dữ liệu phù hợp.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <footer class="pager">
      <div class="spacer"></div>
      <button class="btn sm" :disabled="page === 1" @click="page = 1">«</button>
      <button class="btn sm" :disabled="page === 1" @click="page--">‹</button>
      <span class="muted">Trang {{ page }} / {{ totalPages }}</span>
      <button class="btn sm" :disabled="page === totalPages" @click="page++">›</button>
      <button class="btn sm" :disabled="page === totalPages" @click="page = totalPages">
        »
      </button>
    </footer>
    <div v-if="showPreview" class="modal-backdrop" @click="closePreview">
      <div class="modal" @click.stop>
        <div class="modal-hd">
          <button class="btn" @click="closePreview">Đóng</button>
        </div>
        <div class="modal-body">
          <!-- Khung preview (tỉ lệ poster) -->
          <div class="poster-preview-shell">
            <div class="poster-preview-surface">
              <component v-if="previewForm" :is="previewComponent" :form="previewForm" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, reactive, onBeforeUnmount } from "vue";
import poster from "../components/Poster.vue";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import type { Poster } from "../model/poster"; // nếu chưa có, xem chú thích ở cuối
import type { User } from "../model/user"; // nếu chưa có, xem chú thích ở cuối
import type { Role } from "../model/role";
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
function closePreview() {
  showPreview.value = false;   // Ẩn modal
  selectedPoster.value = null; // Xoá poster đang chọn
}

const showPreview = ref(false);
const selectedPoster = ref<Poster | null>(null);
function resolvePreviewComponent(p?: Poster | null) {
  if (!p) return PosterNewHire;

  const id = (p.postStyleId || "").toLowerCase();
  if (id === "1") return PosterNewHire;
  if (id === "2") return PosterRecognition;

  return PosterNewHire; // fallback
}
const previewComponent = computed(() => resolvePreviewComponent(selectedPoster.value));

// Adapter dữ liệu từ Poster -> form/props mà poster component cần
const previewForm = computed(() => {
  const p = selectedPoster.value;
  if (!p) return null;

  return {
    postStyleId: resolvePreviewComponent(p) === PosterNewHire ? "1" : "2",
    title: p.title || "CHÀO MỪNG BẠN ĐẾN VỚI",
    companyName: p.companyName || "TÊN CÔNG TY",
    user: p.user,
    logo: defaultLogo,
  };
});
// demo role
const roleDemo: Role = {
  roleId: 1, // Chỉ số vai trò (Role ID)
  roleName: "Tester", // Tên vai trò
};
// demo user
const userDemo: User = {
  userId: 1,
  username: "user1",
  password: "password1",
  phone: "0123456789",
  email: "user1@example.com",
  fullName: "Nguyen Van A",
  gender: "Male",
  dob: "1990-01-01",
  statusUser: "Active",
  createDate: "2024-01-01T00:00:00Z",
  updateDate: "2024-01-01T00:00:00Z",
  homeTown: "Hà Nội",
  role: {
    roleId: 1,
    roleName: "HR"
  },
  photo: "/src/assets/image-poster-banner/photo-test.png",
};
// seed demo
const posters: Poster[] = [
  {
    posterId: "p001",
    title: "CHÀO MỪNG BẠN ĐẾN VỚI",
    content: "Chào mừng bạn đến với công ty!",
    filePath: "/images/p001.png",
    createdAt: "2025-09-01",
    updatedAt: "2025-09-02",
    createdBy: "Admin",
    updatedBy: "Admin",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "1",
    companyName: "Revotech",
  },
  {
    posterId: "p002",
    title: "Vinh danh",
    content: "Vinh danh nhân viên xuất sắc tháng 9!",
    filePath: "/images/p002.png",
    createdAt: "2025-09-05",
    updatedAt: "2025-09-06",
    createdBy: "Manager1",
    updatedBy: "Manager1",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style02",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p003",
    title: "Thông báo",
    content: "Thông báo nghỉ lễ 2/9",
    filePath: "/images/p003.png",
    createdAt: "2025-08-28",
    updatedAt: "2025-08-30",
    createdBy: "Hr",
    updatedBy: "Hr",
    isDraft: true,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style03",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p004",
    title: "Tuyển dụng",
    content: "Tuyển dụng thực tập sinh IT",
    filePath: "/images/p004.png",
    createdAt: "2025-07-12",
    updatedAt: "2025-07-13",
    createdBy: "Recruiter1",
    updatedBy: "Recruiter2",
    isDraft: false,
    isDeleted: true,
    user: userDemo,
    postStyleId: "style04",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p005",
    title: "Chúc mừng sinh nhật",
    content: "Chúc mừng sinh nhật các thành viên tháng 9!",
    filePath: "/images/p005.png",
    createdAt: "2025-09-15",
    updatedAt: "2025-09-16",
    createdBy: "Hr",
    updatedBy: "Admin",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style05",
    companyName: "Công ty XYZ",
  },
  // 8 thêm bản ghi mới
  {
    posterId: "p006",
    title: "Thông báo nghỉ lễ 30/4",
    content: "Thông báo nghỉ lễ 30/4 và 1/5",
    filePath: "/images/p006.png",
    createdAt: "2025-04-25",
    updatedAt: "2025-04-26",
    createdBy: "Hr",
    updatedBy: "Hr",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style06",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p007",
    title: "Mở rộng đội ngũ IT",
    content: "Tuyển dụng lập trình viên và kỹ sư phần mềm.",
    filePath: "/images/p007.png",
    createdAt: "2025-05-10",
    updatedAt: "2025-05-11",
    createdBy: "Recruiter2",
    updatedBy: "Recruiter2",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style07",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p008",
    title: "Chúc mừng ngày Quốc tế Phụ nữ",
    content: "Chúc mừng các chị em nhân ngày Quốc tế Phụ nữ 8/3.",
    filePath: "/images/p008.png",
    createdAt: "2025-03-08",
    updatedAt: "2025-03-09",
    createdBy: "Admin",
    updatedBy: "Admin",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style08",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p009",
    title: "Thưởng tháng 13",
    content: "Thông báo về chế độ thưởng tháng 13 cho toàn thể nhân viên.",
    filePath: "/images/p009.png",
    createdAt: "2025-12-10",
    updatedAt: "2025-12-11",
    createdBy: "Admin",
    updatedBy: "Admin",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style09",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p010",
    title: "Đào tạo nhân viên mới",
    content: "Chương trình đào tạo nhân viên mới vào tháng 6/2025.",
    filePath: "/images/p010.png",
    createdAt: "2025-06-01",
    updatedAt: "2025-06-02",
    createdBy: "Hr",
    updatedBy: "Hr",
    isDraft: true,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style10",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p011",
    title: "Chính sách lương thưởng năm 2025",
    content: "Cập nhật chính sách lương thưởng cho năm 2025.",
    filePath: "/images/p011.png",
    createdAt: "2025-01-01",
    updatedAt: "2025-01-02",
    createdBy: "Hr",
    updatedBy: "Hr",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style11",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p012",
    title: "Thông báo vắng mặt",
    content: "Thông báo về việc vắng mặt của một số nhân viên trong tháng 7/2025.",
    filePath: "/images/p012.png",
    createdAt: "2025-07-05",
    updatedAt: "2025-07-06",
    createdBy: "Hr",
    updatedBy: "Hr",
    isDraft: false,
    isDeleted: true,
    user: userDemo,
    postStyleId: "style12",
    companyName: "Công ty XYZ",
  },
  {
    posterId: "p013",
    title: "Chính sách bảo hiểm",
    content: "Thông báo về chính sách bảo hiểm mới cho toàn thể nhân viên.",
    filePath: "/images/p013.png",
    createdAt: "2025-11-20",
    updatedAt: "2025-11-21",
    createdBy: "Admin",
    updatedBy: "Admin",
    isDraft: false,
    isDeleted: false,
    user: userDemo,
    postStyleId: "style13",
    companyName: "Công ty XYZ",
  },
];

// search & filters
const q = ref("");
const draftOnly = ref(false);
const deletedOnly = ref(false);
const styleFilter = ref<string>("");

const styleOptions = Array.from(new Set(posters.map((p) => p.postStyleId)));

const filtered = computed(() => {
  const kw = q.value.trim().toLowerCase();
  return posters.filter((p) => {
    if (draftOnly.value && !p.isDraft) return false;
    if (deletedOnly.value && !p.isDeleted) return false;
    if (styleFilter.value && p.postStyleId !== styleFilter.value) return false;
    if (!kw) return true;
    return [p.posterId, p.title, p.content, p.createdBy, p.updatedBy, p.user]
      .join(" ")
      .toLowerCase()
      .includes(kw);
  });
});

// pagination
const page = ref(1);
const pageSize = ref(13);
const total = computed(() => filtered.value.length);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)));
const paginated = computed(() => {
  if (page.value > totalPages.value) page.value = totalPages.value;
  const start = (page.value - 1) * pageSize.value;
  return filtered.value.slice(start, start + pageSize.value);
});

// actions (stub)
function onCreate() {
  alert("Mở form tạo mới");
}
const formNewHire = reactive({
  template: "new-hire",
  headline: "CHÀO MỪNG BẠN ĐẾN VỚI",
  subheadline: "TÊN CÔNG TY",
  name: "TÊN NHÂN VIÊN",
  role: "VỊ TRÍ CÔNG VIỆC",
  yob: "NĂM SINH",
  hometown: "QUÊ QUÁN",
  logo: "/assets/image-poster-banner/photo-test.png",
  avatar: "",
  primary: "#1d4ed8",
});

// Khai báo selectedPoster là reactive object
// empty object

// Hàm onView sẽ gán formNewHire vào selectedPoster
function onView(p: Poster) {
  selectedPoster.value = p;     // gán poster đang chọn
  showPreview.value = true;     // mở modal
}
function onEdit(p: Poster) {
  alert(`Sửa ${p.posterId}`);
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
  padding-right: 630px;
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
.table td:nth-child(4) {
  width: 200px;
}

.table th:nth-child(4),
.table td:nth-child(4) {
  width: 400px;
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
.modal {
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

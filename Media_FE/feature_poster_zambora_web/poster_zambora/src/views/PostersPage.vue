<template>
  <div class="card">
    <div class="card-hd">Poster Studio</div>
    <div class="card-bd">
      <div class="toolbar" style="margin-bottom: 12px">
        <button class="btn secondary" @click="saveDraft">Save Draft</button>
        <button class="btn" @click="exportPng">Export PNG</button>
      </div>

      <div class="stage">
        <!-- Panel trái: Form gọn, có group -->
        <div class="card">
          <div class="card-hd">Cấu hình</div>
          <div class="card-bd">
            <div class="field">
              <div class="horizontal">
                <label>Loại Poster</label>
                <select class="select" v-model="activeTemplate" style="width: 50%;">
                  <option value="1">Nhân viên mới</option>
                  <option value="2">Vinh danh</option>
                </select>
              </div>
            </div>
            <div class="field">
              <div class="horizontal">
                <label>Chọn nhân viên</label>
                <div class="select-container">
                  <button @click="showModal = true" class="btn">
                    <i class="pi pi-user"></i> <!-- Icon nhân viên -->
                  </button>
                </div>
              </div>
            </div>
            <div class="field">
              <div class="horizontal">
                <label>Chọn ảnh đại diện</label>
                <div class="select-container">
                  <button @click="triggerFileUpload" class="btn">
                    <i class="pi pi-upload"></i> <!-- Icon upload -->
                  </button>
                </div>
                <input type="file" id="fileInput" @change="handleFileUpload" style="display: none;" />
              </div>
            </div>
            <!-- Checkbox: Chọn ảnh từ nhân viên -->
            <div class="field">
              <div class="horizontal">
                <label>Chọn ảnh từ nhân viên</label>
                <input type="radio" v-model="photoSource" value="default" /> <!-- Chọn ảnh từ nhân viên -->
              </div>
            </div>

            <!-- Checkbox: Chọn ảnh từ máy -->
            <div class="field">
              <div class="horizontal">
                <label>Chọn ảnh từ máy</label>
                <input type="radio" v-model="photoSource" value="uploaded" /> <!-- Chọn ảnh từ máy -->
              </div>
            </div>

            <div class="grid-2">
              <div class="field">
                <label>Tiêu đề</label>
                <input class="input" v-model="poster.title" placeholder="CHÀO MỪNG BẠN ĐẾN VỚI" />
              </div>
              <div class="field">
                <label>Tên công ty</label>
                <input class="input" v-model="poster.companyName" placeholder="REVOTECH" />
              </div>
            </div>
          </div>
        </div>

        <!-- Panel phải: Stage poster -->
        <div class="poster-shell">
          <div class="poster-surface" id="exportTarget">
            <component :is="currentPoster" :form="poster" :key="activeTemplate" />
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal chọn user -->
  <div v-if="showModal" class="modal-backdrop" @click="showModal = false">
    <div class="modal" @click.stop>
      <div class="modal-hd">
        <button @click="showModal = false" class="btn-red">Đóng</button>
      </div>
      <div class="modal-body">
        <!-- Thanh tìm kiếm -->
        <div class="horizontal">
          <input type="text" v-model="searchQuery" placeholder="Tìm kiếm theo tên..." class="search-bar" />
          <div class="sort-buttons">
            <button @click="sortByDateAscending">Ngày vào nhỏ -> lớn</button>
            <button @click="sortByDateDescending">Ngày vào lớn -> nhỏ</button>
          </div>
        </div>
        <!-- Bảng người dùng -->
        <div class="modal-grid">
          <table class="table">
            <thead>
              <tr>
                <th>Tên</th>
                <th>Email</th>
                <th>Ngày sinh</th>
                <th>Quê quán</th>
                <th>Vị trí</th>
                <th>Ngày vào</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in filteredUsers" :key="user.userId">
                <td>{{ user.fullName }}</td>
                <td>{{ user.email }}</td>
                <td>{{ user.dob }}</td>
                <td>{{ user.homeTown }}</td>
                <td>{{ user.role.roleName }}</td>
                <td>{{ formatDate(user.createDate) }}</td>
                <td>
                  <button @click="selectUser(user)">Chọn</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup lang="ts">
import { reactive, ref, computed, watch } from "vue";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import html2canvas from "html2canvas";
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
import { User } from "../model/user";

const photoSource = ref<'default' | 'uploaded'>('default');
const uploadedPhoto = ref<string | null>(null);

// Thay vì computed từ form.user.photo, dùng ref ổn định:
const employeePhoto = ref<string>(defaultLogo);
watch(photoSource, (val, oldVal) => {
  console.log('[photoSource] change:', oldVal, '→', val);
  ensureUser();

  if (val === 'uploaded') {
    if (uploadedPhoto.value) {
      console.log('[apply] use uploadedPhoto =', uploadedPhoto.value.slice(0, 40));
      poster.user.photo = uploadedPhoto.value;
    } else {
      console.log('[apply] chưa có uploadedPhoto, giữ nguyên');
    }
  } else {
    console.log('[apply] use employeePhoto =', employeePhoto.value.slice(0, 40));
    poster.user.photo = employeePhoto.value;
  }
  console.log('[result] form.user.photo =', poster.user.photo?.slice?.(0, 40));
});
function ensureUser() {
  if (!poster.user) {
    poster.user = {} as User;
  }
}

const searchQuery = ref("");
const filteredUsers = computed(() => {
  return usersDemo.filter((user) =>
    user.fullName && user.fullName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});
// Sắp xếp từ ngày bé đến ngày to
function sortByDateAscending() {
  // Sắp xếp trực tiếp trên mảng `usersDemo`
  usersDemo.sort((a, b) => {
    const dateA = new Date(a.createDate).getTime();
    const dateB = new Date(b.createDate).getTime();
    return dateA - dateB;
  });
}

// Sắp xếp từ ngày to đến ngày bé (giảm dần)
function sortByDateDescending() {
  // Sắp xếp trực tiếp trên mảng `usersDemo`
  usersDemo.sort((a, b) => {
    const dateA = new Date(a.createDate).getTime();
    const dateB = new Date(b.createDate).getTime();
    return dateB - dateA;
  });
}

const showModal = ref(false);
function selectUser(user: User) {
  poster.user = user;
  // Lấy ảnh gốc của nhân viên, lưu vào biến riêng
  employeePhoto.value = user.photo || defaultLogo;

  // Đồng bộ ảnh hiển thị theo radio hiện tại
  if (photoSource.value === 'default') {
    poster.user.photo = employeePhoto.value;
  } else {
    poster.user.photo = uploadedPhoto.value ?? employeePhoto.value;
  }
  showModal.value = false;
}

const poster = reactive({
  postStyleId: "1",
  title: "CHÀO MỪNG BẠN ĐẾN VỚI",
  companyName: "TÊN CÔNG TY",
  user: {} as User,
  logo: defaultLogo,
});

const usersDemo = reactive([
  {
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
  },
  {
    userId: 2,
    username: "user2",
    password: "password2",
    phone: "0987654321",
    email: "user2@example.com",
    fullName: "Nguyen Thi B",
    gender: "Female",
    dob: "1992-02-02",
    statusUser: "Active",
    createDate: "2023-12-15T00:00:00Z",
    updateDate: "2023-12-15T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 3,
    username: "user3",
    password: "password3",
    phone: "0345678901",
    email: "user3@example.com",
    fullName: "Nguyen Minh C",
    gender: "Male",
    dob: "1994-03-03",
    statusUser: "Inactive",
    createDate: "2023-11-25T00:00:00Z",
    updateDate: "2023-11-25T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 4,
    username: "user4",
    password: "password4",
    phone: "0512345678",
    email: "user4@example.com",
    fullName: "Le Thi D",
    gender: "Female",
    dob: "1996-04-04",
    statusUser: "Active",
    createDate: "2023-10-10T00:00:00Z",
    updateDate: "2023-10-10T00:00:00Z",
    homeTown: "Hà Nội",
    photo: "/src/assets/image-poster-banner/photo-test.png",
    role: {
      roleId: 1,
      roleName: "HR"
    },
  },
  {
    userId: 5,
    username: "user5",
    password: "password5",
    phone: "0698765432",
    email: "user5@example.com",
    fullName: "Pham Minh E",
    gender: "Male",
    dob: "1998-05-05",
    statusUser: "Active",
    createDate: "2023-09-22T00:00:00Z",
    updateDate: "2023-09-22T00:00:00Z",
    homeTown: "Hà Nội",
    photo: "/src/assets/image-poster-banner/photo-test.png",
    role: {
      roleId: 1,
      roleName: "HR"
    },
  },
  {
    userId: 6,
    username: "user6",
    password: "password6",
    phone: "0701234567",
    email: "user6@example.com",
    fullName: "Tran Thi F",
    gender: "Female",
    dob: "1995-06-12",
    statusUser: "Active",
    createDate: "2024-06-01T00:00:00Z",
    updateDate: "2024-06-01T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 7,
    username: "user7",
    password: "password7",
    phone: "0712345678",
    email: "user7@example.com",
    fullName: "Le Minh G",
    gender: "Male",
    dob: "1993-11-22",
    statusUser: "Inactive",
    createDate: "2024-02-20T00:00:00Z",
    updateDate: "2024-02-20T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 8,
    username: "user8",
    password: "password8",
    phone: "0823456789",
    email: "user8@example.com",
    fullName: "Nguyen Thi H",
    gender: "Female",
    dob: "1997-07-16",
    statusUser: "Active",
    createDate: "2024-04-11T00:00:00Z",
    updateDate: "2024-04-11T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 9,
    username: "user9",
    password: "password9",
    phone: "0934567890",
    email: "user9@example.com",
    fullName: "Pham Minh I",
    gender: "Male",
    dob: "1999-09-09",
    statusUser: "Active",
    createDate: "2024-01-15T00:00:00Z",
    updateDate: "2024-01-15T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
  {
    userId: 10,
    username: "user10",
    password: "password10",
    phone: "0945678901",
    email: "user10@example.com",
    fullName: "Nguyen Thi J",
    gender: "Female",
    dob: "2000-02-22",
    statusUser: "Inactive",
    createDate: "2024-03-25T00:00:00Z",
    updateDate: "2024-03-25T00:00:00Z",
    homeTown: "Hà Nội",
    role: {
      roleId: 1,
      roleName: "HR"
    },
    photo: "/src/assets/image-poster-banner/photo-test.png",
  },
]);
const activeTemplate = ref("1");

const currentPoster = computed(() =>
  activeTemplate.value === "1" ? PosterNewHire : PosterRecognition
);
function triggerFileUpload() {
  document.getElementById('fileInput')?.click(); // Gọi click cho input file ẩn
}
// Phương thức để chọn ảnh từ máy
function handleFileUpload(e: Event) {
  const input = e.currentTarget as HTMLInputElement | null;
  const file = input?.files?.[0];
  if (!file) return;

  if (!file.type.startsWith('image/')) {
    alert('Vui lòng chọn file ảnh hợp lệ.');
    if (input) input.value = '';
    return;
  }

  const reader = new FileReader();
  reader.onload = () => {
    // 1) LƯU RIÊNG ẢNH UPLOAD
    uploadedPhoto.value = reader.result as string;

    // 2) Nếu đang chọn nguồn "uploaded" thì áp ngay vào form để hiển thị
    ensureUser();
    if (photoSource.value === 'uploaded') {
      poster.user.photo = uploadedPhoto.value!;
    }

    if (input) input.value = ''; // cho phép chọn lại cùng file
  };
  reader.onerror = () => {
    console.error('[upload] read error:', reader.error);
    if (input) input.value = '';
  };

  reader.readAsDataURL(file); // đọc thành dataURL (base64)
}

function saveDraft() {
  localStorage.setItem(
    `poster-draft-${activeTemplate.value}`,
    JSON.stringify(poster)
  );
  alert("Đã lưu nháp!");
}
function formatDate(date: string | undefined) {
  if (!date) {
    return ''; // Trả về chuỗi rỗng nếu date là undefined hoặc null
  }

  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();

  return `${day}/${month}/${year}`;
}
async function exportPng() {
  // Kiểm tra xem phần tử có tồn tại hay không
  const el = document.getElementById("exportTarget");
  if (!el) {
    console.error("Element with ID 'exportTarget' not found.");
    return;  // Nếu không có phần tử, thoát khỏi hàm
  }

  // Nếu phần tử tồn tại, tiếp tục với việc vẽ canvas
  const canvas = await html2canvas(el, { backgroundColor: "#fff", scale: 2 });

  const a = document.createElement("a");
  a.download = `${activeTemplate.value}-${poster.user.fullName || "poster"}.png`;
  a.href = canvas.toDataURL("image/png");
  a.click();  // Tạo một click tự động để tải ảnh
}
</script>

<style scoped>
.field table {
  border-collapse: collapse;
}

.field label {
  padding-top: 10px;
}

.field table td {
  padding-right: 12px;
  vertical-align: middle;
}

.upload-btn {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #2563eb;
  color: #fff;
  cursor: pointer;
  text-decoration: none;
}

.upload-btn .pi {
  font-size: 20px;
}

.upload-wrap {
  position: relative;
  display: inline-block;
}

/* Dropdown ẩn mặc định */
.upload-wrap .dropdown {
  list-style: none;
  margin: 0;
  padding: 6px 0;
  position: absolute;
  top: 0;
  /* cùng hàng với nút + */
  left: 105%;
  /* dịch sang phải đúng bằng chiều rộng nút */
  min-width: 180px;
  background: #fff;
  color: #0b1220;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25);
  opacity: 0;
  pointer-events: none;
  transform: translateX(6px);
  /* hiệu ứng đẩy nhẹ sang phải */
  transition: all 0.18s ease;
  z-index: 20;
}

/* Hover vào toàn bộ .upload-wrap mới hiện menu */
.upload-wrap:hover .dropdown {
  opacity: 1;
  pointer-events: auto;
  transform: translateX(0);
}

/* Style từng option */
.upload-wrap .dropdown li {
  padding: 10px 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.upload-wrap .dropdown li:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* Modal album */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(2, 6, 23, 0.55);
  display: grid;
  place-items: center;
  z-index: 40;
}

.modal {
  width: min(90vw, 930px);
  background: #fff;
  color: black;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 20px 60px rgba(2, 6, 23, 0.45);
}

.modal-hd {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 12px;
  margin-left: 800px;
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  max-height: 60vh;
  overflow: auto;
}

.thumb {
  border: 0;
  padding: 0;
  background: #0e1726;
  border-radius: 12px;
  cursor: pointer;
  overflow: hidden;
}

.thumb img {
  display: block;
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.modal-ft {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.btn-red {
  background-color: #dc2626;
  /* đỏ */
  color: #fff;
}

.btn-red:hover {
  background-color: #b91c1c;
  /* đỏ đậm khi hover */
}

.horizontal {
  display: flex;
  flex-direction: row;
  /* Change this to row */
  gap: 6px;
}

/* Cải thiện kiểu bảng */
.table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  /* Đảm bảo các cột có độ rộng cố định */
}

.table th,
.table td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ccc;
}

.table th {
  background-color: #f2f2f2;
}

/* Chỉnh sửa độ rộng các cột */
.table th:nth-child(1),
.table td:nth-child(1) {
  width: 150px;
}

.table th:nth-child(2),
.table td:nth-child(2) {
  width: 200px;
}

.table th:nth-child(3),
.table td:nth-child(3) {
  width: 120px;
}

.table th:nth-child(4),
.table td:nth-child(4) {
  width: 100px;
}

.table th:nth-child(5),
.table td:nth-child(5) {
  width: 80px;
}

.table th:nth-child(6),
.table td:nth-child(6) {
  width: 120px;
}

.table th:nth-child(7),
.table td:nth-child(7) {
  width: 80px;
}

/* Tăng chiều rộng cho button nếu cần */
.table button {
  padding: 5px 10px;
  background-color: #2563eb;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.table button:hover {
  background-color: #1d4ed8;
}

.search-bar {
  width: 20%;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  /* Đảm bảo padding không làm bảng bị lệch */
  font-size: 13px;
}

.search-bar:focus {
  outline: none;
  border-color: #2563eb;
  /* xanh giống màu nút */
  box-shadow: 0 0 4px rgba(37, 99, 235, 0.5);
}

.sort-buttons {
  margin-bottom: 20px;
}

.sort-buttons button {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.sort-buttons button:hover {
  background-color: #1d4ed8;
}

.file-label {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 24px;
  /* Tăng kích thước icon */
  color: #1d4ed8;
  /* Màu của icon */
}
</style>

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
          <div class="field">
            <label>Chọn người dùng</label>
            <div class="select-container">
              <button @click="showModal = true" class="btn">+</button> <!-- Mở Modal -->
            </div>
          </div>
          <div class="card-hd">Cấu hình</div>
          <div class="card-bd">
            <div class="field">
              <label>Loại Poster</label>
              <select class="select" v-model="activeTemplate">
                <option value="new-hire">Nhân viên mới</option>
                <option value="recognition">Vinh danh</option>
              </select>
            </div>

            <div class="grid-2">
              <div class="field">
                <label>Tiêu đề</label>
                <input class="input" v-model="activeForm.title" placeholder="CHÀO MỪNG BẠN ĐẾN VỚI" />
              </div>
              <div class="field">
                <label>Thương hiệu</label>
                <input class="input" v-model="activeForm.companyName" placeholder="REVOTECH" />
              </div>
            </div>

            <div class="grid-2">
              <div class="field">
                <label>Họ tên</label>
                <input class="input" v-model="activeForm.user.fullName" placeholder="Họ tên" />
              </div>
              <div class="field" v-if="activeTemplate === 'new-hire'">
                <label>Chức danh</label>
                <input class="input" v-model="activeForm.user.role" placeholder="Chức danh" />
              </div>
            </div>

            <!-- new-hire -->
            <div class="grid-2" v-if="activeTemplate === 'new-hire'">
              <div class="field">
                <label>Năm sinh</label>
                <input class="input" v-model="activeForm.user.dob" placeholder="1999" />
              </div>
              <div class="field">
                <label>Quê quán</label>
                <input class="input" v-model="activeForm.user.homeTown" placeholder="Hà Nội" />
              </div>
            </div>

            <!-- recognition -->
            <div class="grid-2" v-if="activeTemplate === 'recognition'">
              <div class="field">
                <label>Tháng</label>
                <input class="input" v-model="activeForm.month" placeholder="5" />
              </div>
              <div class="field">
                <label>Năm</label>
                <input class="input" v-model="activeForm.year" placeholder="2025" />
              </div>
            </div>

            <!-- Upload -->
            <div class="grid-2">
              <div class="field">
                <input id="avatarInput" type="file" accept="image/*" style="display: none"
                  @change="onFile('avatar', $event)" />
                <table class="upload-table">
                  <tbody>
                    <tr>
                      <td><label for="avatarInput">Ảnh nhân viên</label></td>
                      <td>
                        <div class="upload-wrap">
                          <a href="javascript:void(0)" class="upload-btn">
                            <i class="pi pi-plus"></i>
                          </a>
                          <ul class="dropdown">
                            <li @click="">Chọn từ album</li>
                            <li @click="triggerDeviceUpload">Chọn từ máy</li>
                          </ul>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <!-- End upload -->
          </div>
        </div>

        <!-- Panel phải: Stage poster -->
        <div class="poster-shell">
          <div class="poster-surface" id="exportTarget">
            <component :is="currentPoster" :form="activeForm" :key="activeTemplate" />
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-if="showModal" class="modal-backdrop" @click="showModal = false">
    <div class="modal" @click.stop>
      <div class="modal-hd">
        <h3>Chọn người dùng</h3>
        <button @click="showModal = false" class="btn-red">Đóng</button>
      </div>
      <div class="modal-grid">
        <table>
          <thead>
            <tr>
              <th>Tên</th>
              <th>Chức danh</th>
              <th>Chọn</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in usersDemo" :key="user.userId">
              <td>{{ user.fullName }}</td>
              <td>{{ user.role.roleName }}</td>
              <td>
                <button @click="selectUser(user)">Chọn</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from "vue";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import html2canvas from "html2canvas";
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
import { User } from "../model/user";
import type { Role } from "../model/role";

const showModal = ref(false);
function selectUser(user: User) {
  activeForm.value.user = user; // Cập nhật user trong form
  showModal.value = false; // Đóng modal sau khi chọn
}

const formNewHire = reactive({
  postStyleId: "1",
  title: "CHÀO MỪNG BẠN ĐẾN VỚI",
  companyName: "TÊN CÔNG TY",
  user: {} as User,
  logo: defaultLogo,
  month: "",
  year: "",
});
const formRecognition = reactive({
  postStyleId: "2",
  title: "CÔNG TY CỔ PHẦN PHẦN MỀM",
  companyName: "TÊN CÔNG TY",
  user: {} as User,
  logo: defaultLogo,
  month: "",
  year: "",
});

const roleDemo: Role = {
  roleId: 1,
  roleName: "HR",
}
const usersDemo: User[] = [
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
    role: roleDemo,
    photo: "@/assets/image-poster-banner/photo-demo.jpg",
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
    createDate: "2024-01-01T00:00:00Z",
    updateDate: "2024-01-01T00:00:00Z",
    homeTown: "Hà Nội",
    role: roleDemo,
    photo: "@/assets/image-poster-banner/photo-demo.jpg",
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
    createDate: "2024-01-01T00:00:00Z",
    updateDate: "2024-01-01T00:00:00Z",
    homeTown: "Hà Nội",
    role: roleDemo,
    photo: "@/assets/image-poster-banner/photo-demo.jpg",

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
    createDate: "2024-01-01T00:00:00Z",
    updateDate: "2024-01-01T00:00:00Z",
    homeTown: "Hà Nội",
    photo: "@/assets/image-poster-banner/photo-demo.jpg",
    role: roleDemo,
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
    createDate: "2024-01-01T00:00:00Z",
    updateDate: "2024-01-01T00:00:00Z",
    homeTown: "Hà Nội",
    photo: "@/assets/image-poster-banner/photo-demo.jpg",
    role: roleDemo,
  },
];

const activeTemplate = ref("1");

const activeForm = computed(() =>
  activeTemplate.value === "1" ? formNewHire : formRecognition
);

const currentPoster = computed(() =>
  activeTemplate.value === "1" ? PosterNewHire : PosterRecognition
);

function triggerDeviceUpload() {
  document.getElementById("avatarInput")?.click();
}
function onFile(key: string, e: Event): void {
  const f = (e.target as HTMLInputElement).files?.[0];
  if (!f) return;
  const reader = new FileReader();
  reader.onload = () => {
    (activeForm.value as any)[key] = reader.result;  // Gán kết quả vào activeForm
  };
  reader.readAsDataURL(f);
}
function saveDraft() {
  localStorage.setItem(
    `poster-draft-${activeTemplate.value}`,
    JSON.stringify(activeForm.value)
  );
  alert("Đã lưu nháp!");
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
  a.download = `${activeTemplate.value}-${activeForm.value.user.fullName || "poster"}.png`;
  a.href = canvas.toDataURL("image/png");
  a.click();  // Tạo một click tự động để tải ảnh
}
</script>

<style scoped>
.field table {
  border-collapse: collapse;
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
  width: min(90vw, 760px);
  background: #0b1220;
  color: #e5e7eb;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 20px 60px rgba(2, 6, 23, 0.45);
}

.modal-hd {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 12px;
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
</style>

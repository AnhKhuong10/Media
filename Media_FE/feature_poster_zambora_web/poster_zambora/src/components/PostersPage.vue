<template>
  <div class="card">
    <div class="card-hd">Poster Studio</div>
    <div class="card-bd">
      <div class="toolbar" style="margin-bottom: 12px">
        <template v-if="mode === 'create'">
          <BaseButton label="Lưu" icon="pi pi-save" color="primary" size="md" @click="save" />
          <BaseButton label="Tải xuống" icon="pi pi-download" color="danger" size="md" @click="exportPng" />
        </template>
        <template v-else>
          <BaseButton label="Cập nhật" icon="pi pi-refresh" color="danger" size="md" @click="update" />
        </template>
      </div>

      <div class="stage">
        <!-- Panel trái: Form gọn, có group -->
        <div class="card">
          <div class="card-hd">Cấu hình</div>
          <div class="card-bd">
            <div class="field">
              <div class="horizontal">
                <label for="poster-type">Loại Poster</label>
                <Dropdown id="poster-type" v-model="activeTemplate" :options="posterTypes" optionLabel="label"
                  optionValue="value" placeholder="Chọn loại poster" appendTo="self" class="w-full md:w-14rem" />
              </div>
            </div>
            <div class="field">
              <div class="horizontal">
                <label>Chọn nhân viên</label>
                <div class="select-container">
                  <!-- Icon nhân viên -->
                  <BaseButton icon="pi pi-user" color="gray" size="md" height="40px" width="40px"
                    @click="showModal = true" />
                </div>
              </div>
            </div>
            <div class="field">
              <div class="horizontal">
                <label>Chọn ảnh đại diện</label>
                <div class="select-container">
                  <BaseButton icon="pi pi-upload" color="gray" size="md" height="40px" width="40px"
                    @click="triggerFileUpload" />
                </div>
                <input type="file" id="fileInput" @change="handleFileUpload" style="display: none" />
              </div>
            </div>
            <!-- Checkbox: Chọn ảnh từ nhân viên -->
            <div class="field">
              <div class="horizontal">
                <label for="radio-default" class="ml-2">Chọn ảnh từ nhân viên</label>
                <RadioButton inputId="radio-default" name="photoSource" value="default" v-model="photoSource" />
              </div>
            </div>

            <!-- Checkbox: Chọn ảnh từ máy -->
            <div class="field">
              <div class="horizontal">
                <label for="radio-uploaded" class="ml-2">Chọn ảnh từ máy</label>
                <RadioButton inputId="radio-uploaded" name="photoSource" value="uploaded" v-model="photoSource" />
              </div>
            </div>

            <div class="grid-2">
              <div class="field">
                <div class="horizontal">
                  <label>Bản nháp</label>
                  <label class="switch">
                    <InputSwitch v-model="isDraft" />
                  </label>
                </div>
              </div>
            </div>

            <div class="grid-2">
              <div class="field">
                <label>Tiêu đề</label>
                <!-- <InputText type="text" v-model="poster.title" placeholder="CHÀO MỪNG BẠN ĐẾN VỚI" /> -->
                <Textarea v-model="poster.title" autoResize  rows="3" cols="30" />
              </div>
            </div>

            <div class="grid-4">
              <div class="field">
                <label>Nội dung</label>
                <Textarea v-model="poster.content" autoResize rows="5" cols="30" />
              </div>
            </div>
          </div>
        </div>

        <!-- Panel phải: Stage poster -->
        <div class="poster-shell">
          <div class="poster-surface" id="exportTarget">
            <component :is="currentPoster" :form="poster" :preview-photo="previewPhoto" :key="activeTemplate" />
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal chọn user -->
  <div v-if="showModal" class="modal-backdrop" @click="showModal = false">
    <div class="modal" @click.stop>
      <div class="modal-hd">
        <BaseButton label="Đóng" icon="pi pi-times" color="danger" @click="showModal = false" />
      </div>
      <div class="modal-body">
        <!-- Thanh tìm kiếm -->
        <div style="padding-bottom: 5px">
          <IconField>
            <InputIcon>
              <i class="pi pi-search" />
            </InputIcon>
            <InputText v-model="searchQuery" placeholder="Tìm kiếm" class="pl-5 w-15rem h-2rem">
            </InputText>
          </IconField>
        </div>
        <!-- Bảng người dùng -->
        <DataTable :value="filteredUsers" dataKey="userId" sortMode="multiple" tableStyle="min-width: 60rem"
          responsiveLayout="scroll">
          <!-- Cột Tên -->
          <Column field="fullName" header="Tên" sortable style="width: 15%">
            <template #body="{ data }">
              {{ data.fullName }}
            </template>
          </Column>

          <!-- Cột Email -->
          <Column field="email" header="Email" sortable style="width: 20%">
            <template #body="{ data }">
              <span class="text-sm text-gray-700">{{ data.email }}</span>
            </template>
          </Column>

          <!-- Cột Ngày sinh -->
          <Column field="dob" header="Ngày sinh" sortable style="width: 15%">
            <template #body="{ data }">
              {{ formatDate(data.dob) }}
            </template>
          </Column>

          <!-- Cột Quê quán -->
          <Column field="homeTown" header="Quê quán" sortable style="width: 15%">
            <template #body="{ data }">
              {{ data.homeTown }}
            </template>
          </Column>

          <!-- Cột Vị trí -->
          <Column field="roleName" header="Vị trí" sortable style="width: 15%">
            <template #body="{ data }">
              {{ data.roleName }}
            </template>
          </Column>

          <!-- Cột Ngày vào -->
          <Column field="createDate" header="Ngày vào" sortable style="width: 15%">
            <template #body="{ data }">
              {{ formatDate(data.createDate) }}
            </template>
          </Column>

          <!-- Cột hành động -->
          <Column header="Thao tác" style="width: 8%; text-align: center">
            <template #body="{ data }">
              <BaseButton label="Chọn" size="sm" color="primary" @click="selectUser(data)" />
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch, onMounted } from "vue";
import BaseButton from "./BaseButton.vue";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import html2canvas from "html2canvas";
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
import { User } from "../model/user";
import { IMAGE_URL } from "../api/configService";

const props = withDefaults(
  defineProps<{
    mode?: "create" | "edit";
    posterData?: any;
  }>(),
  {
    mode: "create",
  }
);
const posterTypes = [
  { label: "Nhân viên mới", value: "new_employee" },
  { label: "Vinh danh", value: "honor" },
];

const isDraft = ref(props.posterData?.isDraft ?? false);
const poster = reactive(
  props.posterData
    ? { ...props.posterData }
    : {
      posterType: "new_employee",
      title: "CHÀO MỪNG BẠN ĐẾN VỚI",
      content: "NỘI DUNG Ở ĐÂY",
      companyName: "TÊN CÔNG TY",
      user: {} as User,
    }
);
const previewPhoto = ref<string>(
  props.posterData?.user?.avatar
    ? `${IMAGE_URL}${props.posterData.user.avatar}`
    : defaultLogo
);
const posterCreateDTO = computed(() => ({
  title: poster.title,
  content: poster.content ?? "",
  posterType: poster.posterType,
  companyName: poster.companyName,
  userId: poster.user.userId,
}));

// Lấy danh sách user cho người dùng chọn trong modal
import { getAllUser } from "../api/graphql/user-service";
const users = ref<User[]>([]);
const searchQuery = ref("");
onMounted(async () => {
  users.value = await getAllUser();
  console.log("Fetched users:", users.value);
});
function sortByDateAscending() {
  users.value.sort(
    (a, b) => new Date(a.createDate!).getTime() - new Date(b.createDate!).getTime()
  );
}
function sortByDateDescending() {
  users.value.sort(
    (a, b) => new Date(b.createDate!).getTime() - new Date(a.createDate!).getTime()
  );
}
const filteredUsers = computed(() => {
  return users.value.filter((user) =>
    user.fullName?.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});
// end Lấy danh sách user cho người dùng chọn trong modal

// Hàm lưu poster (gọi API)
import { createPoster, updatePoster } from "../api/rest/poster-service";
const uploadedFile = ref<File | null>(null);
const photoSource = ref<"default" | "uploaded">("default");
const uploadedPhoto = ref<string | null>(null);

const emit = defineEmits(["create-success", "update-success"]);
async function save() {
  if (!poster.user?.userId) {
    alert("Vui lòng chọn nhân viên trước khi lưu Poster!");
    return;
  }

  let fileToUpload: File | undefined;

  // chỉ khi radio đang chọn "uploaded" và có file mới thì gửi
  if (photoSource.value === "uploaded" && uploadedFile.value) {
    fileToUpload = uploadedFile.value;
  }

  const res = await createPoster(posterCreateDTO.value, fileToUpload, isDraft.value);
  if (res) {
    alert(isDraft ? "Đã lưu bản nháp Poster!" : "Đã lưu thành công Poster!");
    emit("create-success");
  }
}
// end Hàm lưu poster (gọi API)

// Hàm update photo
async function update() {
  let fileToUpdate: File | undefined;
  // phải gán lại không thì sẽ bị mất
  poster.isDraft = isDraft.value;
  if (photoSource.value === "uploaded" && uploadedFile.value) {
    fileToUpdate = uploadedFile.value;
  }
  console.log(poster.isDraft + "before send");
  // Gọi API update thật
  const res = await updatePoster(poster, fileToUpdate);
  if (res) {
    alert("Cập nhật Poster thành công!");
    emit("update-success");
  }
}
// end hàm update photo

// hàm chọn file từ máy
function handleFileUpload(e: Event) {
  const input = e.currentTarget as HTMLInputElement | null;
  const file = input?.files?.[0];
  if (!file) return;

  if (!file.type.startsWith("image/")) {
    alert("Vui lòng chọn file ảnh hợp lệ.");
    if (input) input.value = "";
    return;
  }

  uploadedFile.value = file; // giữ file thật

  const reader = new FileReader();
  reader.onload = () => {
    uploadedPhoto.value = reader.result as string; // để preview
    if (photoSource.value === "uploaded") {
      previewPhoto.value = uploadedPhoto.value;
    }
    if (input) input.value = "";
  };
  reader.readAsDataURL(file);
}
// end hàm chọn file từ máy

const employeePhoto = ref<string>(
  props.posterData?.user?.avatar
    ? `${IMAGE_URL}${props.posterData.user.avatar}`
    : defaultLogo
);
watch(photoSource, (val) => {
  // nếu người dùng chọn option upload ảnh từ máy
  if (val === "uploaded" && uploadedPhoto.value) {
    previewPhoto.value = uploadedPhoto.value;
  } else {
    previewPhoto.value = employeePhoto.value;
  }
});

const showModal = ref(false);
function selectUser(user: User) {
  poster.user = user;
  poster.userId = user.userId;

  // lấy ảnh gốc từ DB (nếu có)
  employeePhoto.value = user.avatar ? `${IMAGE_URL}${user.avatar}` : defaultLogo;

  // preview theo radio
  previewPhoto.value =
    photoSource.value === "uploaded" && uploadedPhoto.value
      ? uploadedPhoto.value
      : employeePhoto.value;

  showModal.value = false;
}

const activeTemplate = ref(poster.posterType || "new_employee");

watch(activeTemplate, (val) => {
  poster.posterType = val; // đồng bộ khi đổi select box
});

const currentPoster = computed(() =>
  activeTemplate.value === "new_employee" ? PosterNewHire : PosterRecognition
);
function triggerFileUpload() {
  document.getElementById("fileInput")?.click(); // Gọi click cho input file ẩn
}
function formatDate(date: string | undefined) {
  if (!date) {
    return ""; // Trả về chuỗi rỗng nếu date là undefined hoặc null
  }

  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, "0");
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const year = d.getFullYear();

  return `${day}/${month}/${year}`;
}
async function exportPng() {
  // Kiểm tra xem phần tử có tồn tại hay không
  const el = document.getElementById("exportTarget");
  if (!el) {
    console.error("Element with ID 'exportTarget' not found.");
    return; // Nếu không có phần tử, thoát khỏi hàm
  }

  // Nếu phần tử tồn tại, tiếp tục với việc vẽ canvas
  const canvas = await html2canvas(el, {
    backgroundColor: "#fff",
    useCORS: true,
    scale: 2
  });

  const a = document.createElement("a");
  a.download = `${activeTemplate.value}-${poster.user.fullName || "poster"}.png`;
  a.href = canvas.toDataURL("image/png");
  a.click(); // Tạo một click tự động để tải ảnh
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
  width: min(90vw, 1000px);
  background: #fff;
  color: black;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 20px 60px rgba(2, 6, 23, 0.45);
}

.modal-hd {
  margin-left: 870px;
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
  width: 250px;
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
  width: 90px;
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

.switch {
  position: relative;
  display: inline-block;
  width: 46px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  inset: 0;
  background-color: #ccc;
  transition: 0.3s;
  border-radius: 34px;
}

.slider::before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
}

input:checked+.slider {
  background-color: #1d4ed8;
}

input:checked+.slider::before {
  transform: translateX(22px);
}
</style>

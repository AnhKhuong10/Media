<template>
  <tr>
    <td class="content">{{ index }}</td>
    <td class="content">{{ poster.posterId }}</td>
    <td class="content">{{ poster.title }}</td>
    <td class="content">{{ poster.content }}</td>
    <td class="content">{{ poster.createDate }}</td>
    <td class="content">{{ poster.createdBy }}</td>
    <td class="content">"No"</td>
    <td class="content">{{ poster.posterType }}</td>
    <td class="content">
      <div class="dropdown">
        <!-- Dấu ba chấm dọc -->
        <button class="dots-btn" @click="toggleMenu(poster.posterId)">
          <i class="pi pi-ellipsis-v"></i>
        </button>
        <!-- Menu tùy chọn (ẩn đi, chỉ hiện khi ấn vào dấu ba chấm) -->
        <div v-if="activeMenu === poster.posterId" class="dropdown-menu">
          <button class="menu-btn" @click="$emit('view', poster)">
            <i class="pi pi-eye" style="padding-right: 2px"></i>View
          </button>
          <button class="menu-btn" @click="$emit('edit', poster)">
            <i class="pi pi-pencil" style="padding-right: 2px"></i>Edit
          </button>
          <button class="menu-btn" @click="$emit('delete', poster)" style="color: red">
            <i class="pi pi-trash" style="color: red; padding-right: 2px"></i>Delete
          </button>
        </div>
      </div>
    </td>
  </tr>
</template>

<script setup lang="ts">
import { defineProps, ref } from "vue";
import type { PosterDTO } from "../model/poster"; // ✅ Dùng lại type đã có

const props = defineProps<{
  poster: PosterDTO;
  index: number;
}>();
console.log("Poster item:", props.poster); // ✅ In dữ liệu mỗi dòng
const activeMenu = ref<number | null>(null);
function toggleMenu(posterId: number) {
  // Nếu menu đang mở thì đóng lại, ngược lại mở menu mới
  activeMenu.value = activeMenu.value === posterId ? null : posterId;
}
</script>

<style scoped>
.content {
  padding: 10px;
}

.dropdown {
  position: relative;
}

.dots-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 18px;
  transition: transform 0.2s ease, opacity 0.2s ease; /* Thêm hiệu ứng khi nhấn */
}

.dots-btn:hover {
  transform: scale(1.2); /* Phóng to dấu ba chấm khi hover */
  opacity: 0.8; /* Làm mờ dấu ba chấm khi hover */
}

.dots-btn:active {
  transform: scale(0.9); /* Thu nhỏ khi nhấn */
}

.dots-btn span {
  height: 3px;
  width: 3px;
  border-radius: 50%;
  background-color: #333;
  display: block;
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.dots-btn:hover span {
  background-color: #555; /* Màu sắc thay đổi khi hover */
}


.dropdown-menu {
  position: absolute;
  top: 30px;
  right: -5px; /* Điều chỉnh để menu gần dấu ba chấm hơn */
  background: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 8px 0;
  width: 85px;
  z-index: 10;
}

.menu-btn {
  background: none;
  border: none;
  padding: 6px 12px;
  text-align: left;
  width: 100%;
  cursor: pointer;
}

.menu-btn:hover {
  background-color: #f0f0f0;
}

.menu-btn:focus {
  outline: none;
}
</style>

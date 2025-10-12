<template>
  <div class="feed">
    <!-- Nếu đang load -->
    <div v-if="loading" class="loading">Đang tải poster...</div>

    <!-- Nếu không có dữ liệu -->
    <div v-else-if="posters.length === 0" class="empty">Chưa có poster nào</div>

    <!-- Danh sách poster -->
    <div v-else>

      <div v-for="poster in posters" :key="poster.posterId" class="feed-item">
        <!-- Header user -->
        <div class="feed-header">
          <img
            :src="poster.user?.avatar ? IMAGE_URL + poster.user.avatar : defaultAvatar"
            alt="avatar"
            class="avatar"
          />
          <div class="feed-info">
            <div class="user-name">{{ poster.user?.fullName || "Không rõ tên" }}</div>
            <div class="post-date">{{ formatDate(poster.createDate) }}</div>
          </div>
        </div>

        <!-- Nội dung poster -->
        <div class="feed-body">
          <component
            :is="getPosterComponent(poster.posterType)"
            :form="poster"
            :preview-photo="
              poster.user?.avatar ? IMAGE_URL + poster.user.avatar : defaultLogo
            "
          />
        </div>
        <div class="reaction-bar">
          <div class="reactions">
            <button
              v-for="r in reactionIcons"
              :key="r.type"
              :class="['reaction-btn']"
              @click="toggleReaction(poster, r.type)"
              :title="r.label"
            >
              <span class="emoji">{{ r.icon }}</span>
            </button>
          </div>
          <div class="reaction-count" >
            55 cảm xúc
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getPostersForUser } from "../api/graphql/poster-service-graphql";
import PosterNewHire from "../components/PosterNewHire.vue";
import PosterRecognition from "../components/PosterRecognition.vue";
import { IMAGE_URL } from "../api/configService";
import defaultAvatar from "@/assets/image-poster-banner/logo_revotech.png";
import defaultLogo from "@/assets/image-poster-banner/logo_revotech.png";
import type { PosterDTO, PosterType } from "../model/poster";
import { ReactionEnum } from "../model/poster-reaction";

// State
const posters = ref<PosterDTO[]>([]);
const loading = ref(true);

// Lifecycle: Load poster khi component mount
onMounted(async () => {
  try {
    posters.value = await getPostersForUser("");
  } catch (e) {
    console.error("Lỗi khi tải poster:", e);
  } finally {
    loading.value = false;
  }
});

// Trả về component tương ứng theo loại poster
function getPosterComponent(type: string | PosterType) {
  const t = type.toString(); // ép về chuỗi
  switch (t) {
    case "new_employee":
      return PosterNewHire;
    case "honor":
      return PosterRecognition;
    default:
      return PosterNewHire;
  }
}

// Format ngày
function formatDate(dateStr: string) {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  return d.toLocaleDateString("vi-VN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
}
// Reaction icons
const reactionIcons = [
  { type: ReactionEnum.like, label: "Thích", icon: "👍" },
  { type: ReactionEnum.love, label: "Yêu thích", icon: "❤️" },
  { type: ReactionEnum.haha, label: "Haha", icon: "😂" },
  { type: ReactionEnum.wow, label: "Wow", icon: "😮" },
  { type: ReactionEnum.sad, label: "Buồn", icon: "😢" },
  { type: ReactionEnum.angry, label: "Phẫn nộ", icon: "😡" },
];
function toggleReaction(poster: any, type: ReactionEnum) {
  if (poster.reaction === type) {
    // bỏ reaction
    poster.reaction = null;
    poster.reactionCount--;
  } else {
    // thay đổi reaction
    if (!poster.reaction) poster.reactionCount++;
    poster.reaction = type;
  }
}

</script>

<style scoped>
.feed {
  display: flex;
  flex-direction: column;
  gap: 60px;
  width: min(95%, 1000px);
  margin: 0 auto;
  padding: 30px 0;
}

.feed-item {
  background: #fff;
  border-radius: 20px;
  box-shadow: 30px 30px 30px rgba(0, 0, 0, 0.1);
  padding: 20px;
  gap: 10px;
  margin-bottom: 30px;
}

.feed-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.avatar {
  width: 62px;
  height: 62px;
  border-radius: 50%;
  object-fit: cover;
}

.user-name {
  font-weight: 700;
  font-size: large;
}

.post-date {
  color: #64748b;
  font-size: 15px;
}

.feed-body {
  border-radius: 10px;
  overflow: hidden;
  height: 1200px;
  border: 1px solid #e5e7eb;
}

.loading {
  text-align: center;
  color: #888;
  padding: 20px;
}

.empty {
  text-align: center;
  color: #999;
  font-style: italic;
  padding: 20px;
}
.reaction-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 10px;
}

.reactions {
  display: flex;
  gap: 8px;
  align-items: center;
}

/* Nút emoji */
.reaction-btn {
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 22px;
  transition: transform 0.15s ease, filter 0.15s ease;
  user-select: none;
  padding: 4px;
  border-radius: 8px;
}

.reaction-btn:hover {
  transform: scale(1.25);
  filter: drop-shadow(0 0 5px rgba(59, 130, 246, 0.4));
  background: rgba(59, 130, 246, 0.05); /* nền mờ khi hover */
}

/* Khi nút đang active (đã chọn) */
.reaction-btn.active {
  transform: scale(1.3);
  filter: drop-shadow(0 0 6px rgba(37, 99, 235, 0.6));
}

/* Emoji bên trong */
.emoji {
  display: inline-block;
  line-height: 1;
  transition: transform 0.15s ease;
}

/* Số lượng cảm xúc */
.reaction-count {
  font-size: 14px;
  color: #475569;
  font-weight: 500;
  user-select: none;
}
</style>

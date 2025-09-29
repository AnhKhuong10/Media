<template>
  <div class="wrap">
    <!-- Hero -->
    <div class="hero">
      <img
        :src="form.logo"
        alt="Zambora"
        style="position: absolute; top: 20px; left: 20px; width: 200px"
      />
      <div class="headline">{{ form.title }}</div>
      <div class="branch">{{ form.companyName }}</div>
    </div>

    <!-- Khung vàng + avatar bên trong -->

    <div class="medal">
      <!-- avatar lọt vòng -->
      <div class="medal-hole">
        <img v-if="form.avatar" :src="form.user.photo" class="medal-photo" />
        <div v-else class="medal-photo ph">Ảnh nhân viên</div>
      </div>

      <!-- ngôi sao ngay phía trên avatar -->
      <img :src="logoNgoiSao" alt="" class="medal-star" />
    </div>
    <div class="title">{{ form.user.fullName }}</div>

    <div class="award">
      <div class="award-headline">
        NHÂN VIÊN XUẤT SẮC<br />
        THÁNG {{ form.month }}.{{ form.year }}
      </div>
      <div class="award-sub">
        Một tháng làm việc tuyệt vời - một dấu ấn đáng tự hào!<br />
        REVOTECH tự hào khi có bạn là một phần của đại gia đình, và chúc bạn tiếp tục gặt
        hái thành công với những nỗ lực trong tương lai
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({ form: Object });
import logoNgoiSao from "@/assets/image-poster-banner/ngoi-sao.png";
</script>

<style scoped>
.headline {
  text-transform: uppercase;
  font-weight: 600;
  font-size: 35px;
  margin-top: 100px;
}
.branch {
  text-transform: uppercase;
  font-weight: 600;
  font-size: 35px;
  margin-top: 5px;
}
.title {
  text-transform: uppercase;
  font-size: 40px;
  font-weight: 700;
  text-align: center;
  color: #fff;
  margin-top: -20px;
}
.wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background: url("@/assets/image-poster-banner/background-vinhdanh.png") no-repeat top
    center;
  background-size: cover;
  border-radius: 16px;
  overflow: hidden;
}

/* Hero text */
.hero {
  padding: 28px 32px 12px;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.badge {
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 999px;
  font-weight: 700;
}

/* ===== Khung vàng ===== */
.medal {
  position: absolute;
  left: 50%;
  top: 40%; /* vị trí khung avatar */
  transform: translate(-50%, -50%);
  width: 370px;
  aspect-ratio: 1/1;
  background: url("@/assets/image-poster-banner/khung-vinh-danh.png") center/contain
    no-repeat;
}

/* Lỗ tròn bên trong vòng để chứa avatar */
.medal-hole {
  position: absolute;
  top: 16%;
  left: 16%;
  width: 68%;
  height: 68%;
  border-radius: 50%;
  overflow: hidden;
  z-index: 1;
}

.medal-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.medal-photo.ph {
  display: grid;
  place-items: center;
  background: #f1f5f9;
  color: #64748b;
  font-size: 14px;
}
.medal-star {
  position: absolute;
  bottom: 97%; /* đẩy ra phía trên khung */
  left: 50%;
  transform: translate(-50%, -20%); /* căn giữa và nhích xuống một chút */
  width: 20%; /* chỉnh size cụm sao */
  z-index: 2;
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.3));
}
.award {
  position: absolute;
  left: 50%;
  bottom: 100px;
  transform: translateX(-50%);
  width: min(90%, 640px);
  text-align: center;
  color: #fff;
}
.award-headline {
  color: #facc15;
  text-transform: uppercase;
  font-weight: 900;
  font-size: clamp(22px, 3.2vw, 34px);
  font-size: 40px;
  line-height: 1.25;
}
.award-sub {
  margin-top: 20px;
  font-size: clamp(12px, 1.6vw, 16px);
  line-height: 1.6;
  font-size: 23px;
  opacity: 0.92;
}
</style>

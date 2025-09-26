<template>
  <div class="wrap">
    <div class="top" :style="{ background: form.primary }">
      <img
        :src="form.logo"
        alt="Zambora"
        style="position: absolute; top: 20px; left: 20px; width: 180px; opacity: 0.8"
      />
      <div class="headline">{{ form.headline }}</div>
      <div class="headline">{{ form.subheadline }}</div>
      <img
        src="@/assets/image-poster-banner/gold-stroke.png"
        alt="gold stroke"
        class="gold-stroke"
      />
    </div>
    <div style="display: flex; justify-content: center; margin-top: -250px">
      <img v-if="form.avatar" :src="form.avatar" class="photo" />
      <div v-else class="photo ph">Ảnh nhân viên</div>
    </div>

    <div class="info">
      <div class="name">{{ form.name }}</div>
      <div class="role">Vị trí: {{ form.role }}</div>
      <div class="meta">
        Năm sinh: {{ form.yob }}
        <br />
        Quê quán: {{ form.hometown }}
      </div>
    </div>
    <!-- <div v-if="form.showFlag" class="flag">🇻🇳</div> -->
  </div>
</template>

<script setup>
defineProps({ form: Object });
</script>

<style scoped>
.wrap {
  width: 100%;
  height: 100%;
  position: relative;
  background: #fff;
}
.top {
  height: 300px;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.headline {
  text-transform: uppercase;
  font-weight: 600;
  font-size: 40px;
}
/* .brand {
  font-size: 35px;
  font-weight: 600;
  letter-spacing: 1.2px;
} */
.photo {
  width: 600px;
  height: 760px;
  object-fit: cover;
  border-radius: 16px;
  border: 10px solid #fff;
  box-shadow: 0 30px 60px rgba(15, 23, 42, 0.18);
}
.photo.ph {
  display: grid;
  place-items: center;
  width: 600px;
  height: 760px;
  border-radius: 16px;
  border: 2px dashed #cbd5e1;
  background: #f3f6fb;
  color: #7a8699;
}
.info {
  position: absolute;
  bottom: 25px;
  left: 50%; /* lấy tâm ngang */
  transform: translateX(-50%); /* dịch ngược lại 1/2 để căn giữa */
  border-radius: 14px;
  min-width: 360px; /* cho đẹp */
  text-align: center; /* căn chữ giữa */
}
.name {
  font-size: 26px;
  font-weight: 700;
}
.role {
  font-size: 20px;
  font-weight: 800;
  color: #334155;
  text-transform: uppercase;
}
.meta {
  margin-top: 4px;
  font-size: 20px;
  color: #4b5563;
  font-weight: 600;
}
.flag {
  position: absolute;
  right: 34px;
  bottom: 34px;
  font-size: 40px;
}
.gold-stroke {
  position: absolute;
  top: 0;
  right: 20px; /* sát bên phải, bạn có thể chỉnh 0 hoặc 20px tùy */ /* chiếm toàn chiều cao khối top */
  width: 90px; /* nhỏ lại so với hiện tại (có thể thử 60px, 70px) */
  object-fit: cover;
}
</style>

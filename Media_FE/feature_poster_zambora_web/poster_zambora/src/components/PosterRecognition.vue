<template>
  <div class="wrap">
    <div class="hero" :style="{ background: form.primary }">
      <div class="badge">VINH DANH</div>
      <div class="title">{{ form.name }} – {{ form.role }}</div>
    </div>
    <div class="body">
      <div class="left">
        <p>🌟 Thành tích nổi bật</p>
        <ul>
          <li>Đạt hiệu suất 120%</li>
          <li>Lan toả văn hoá tích cực</li>
          <li>Đóng góp dự án X</li>
        </ul>
      </div>
      <div class="right">
        <img v-if="form.avatar" :src="form.avatar" class="portrait"/>
        <div v-else class="portrait ph">Ảnh nhân viên</div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({ form: Object })
</script>

<style scoped>
.hero { height:200px; color:white; display:flex; flex-direction:column; align-items:center; justify-content:center; }
.badge { background:rgba(255,255,255,.2); padding:8px 16px; border-radius:999px; font-weight:700; }
.title { font-size:36px; font-weight:900; }
.body { display:grid; grid-template-columns:1fr 1fr; gap:24px; padding:24px; }
.left ul { padding-left:20px; }
.right .portrait { width:100%; height:400px; object-fit:cover; border-radius:16px; }
.portrait.ph { display:grid; place-items:center; background:#f1f5f9; color:#64748b; border:2px dashed #cbd5e1; height:400px; border-radius:16px; }
</style>

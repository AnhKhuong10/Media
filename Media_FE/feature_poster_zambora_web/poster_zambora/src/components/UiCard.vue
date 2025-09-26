<template>
  <div class="card">
    <div class="hd">{{ title }}</div>
    <div class="bd"><slot /></div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, required: true }
})
</script>

<style scoped>
.card { background: #fff; border-radius: 16px; border: 1px solid #e5e7eb; box-shadow: 0 10px 20px rgba(2,6,23,.04); }
.hd { padding: 16px 18px; border-bottom: 1px solid #eef0f3; font-weight: 800; }
.bd { padding: 18px; }
</style>

<template>
  <header class="header">
    <div class="title">{{ title }}</div>
    <div class="actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<script setup>
defineProps({
  title: { type: String, default: 'Dashboard' }
})
</script>

<style scoped>
.header {
  background:#0b1227; color:#e2e8f0;
  display:flex; align-items:center; justify-content:space-between;
  padding:0 24px; height:72px;
  border-bottom:1px solid rgba(255,255,255,.06);
}
.title { font-weight:800; letter-spacing:.2px; }
.actions { display:flex; gap:10px; }
</style>

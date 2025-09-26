<template>
  <div class="field">
    <label>{{ label }}</label>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  label: { type: String, required: true }
})
</script>

<style scoped>
.field { display: flex; flex-direction: column; gap: 6px; margin-bottom: 14px; }
label { font-size: 12px; text-transform: uppercase; color: #64748b; font-weight: 800; letter-spacing: .6px; }
</style>

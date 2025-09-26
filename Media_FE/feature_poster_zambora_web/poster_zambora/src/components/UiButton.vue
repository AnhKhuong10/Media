<template>
  <button :class="['btn', variant]" @click="$emit('click')">
    <slot />
  </button>
</template>

<script setup>
defineProps({
  variant: { type: String, default: 'primary' } // primary, secondary, ghost
})
</script>

<style scoped>
.btn { appearance:none; border:0; padding:10px 14px; border-radius:10px; font-weight:700; cursor:pointer; }
.btn.primary { background:#2563eb; color:white; }
.btn.secondary { background:#1f2937; color:white; }
.btn.ghost { background:transparent; border:1px solid #334155; color:#334155; }
</style>

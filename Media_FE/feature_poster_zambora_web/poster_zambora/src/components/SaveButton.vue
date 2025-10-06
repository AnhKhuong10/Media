<template>
  <button
    class="btn secondary"
    @click="$emit('click')"
  >
    <i :class="icon"></i>
    <span >{{ label }}</span>
  </button>
</template>

<script setup lang="ts">
const props = defineProps<{
  label: string;            // ví dụ: "Save" hoặc "Save Draft"
  icon?: string;            // ví dụ: "pi pi-save"
}>();

defineEmits<{
  (e: "click"): void;
}>();
</script>

<style scoped>
button {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>

<template>
  <button
    class="btn"
    @click="$emit('click')"
    :disabled="loading"
  >
    <i class="pi pi-download"></i>
    <span v-if="!loading">Export PNG</span>
    <span v-else>Exporting...</span>
  </button>
</template>

<script setup lang="ts">
defineProps<{
  loading?: boolean;
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

<template>
  <button class="btn primary" @click="$emit('click')">
    <i class="pi pi-refresh"></i>
    Update
  </button>
</template>

<script setup lang="ts">
defineEmits<{
  (e: "click"): void;
}>();
</script>

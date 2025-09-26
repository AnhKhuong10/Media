<template>
  <UiCard title="Preview">
    <div class="preview-wrap">
      <div id="exportTarget" class="poster-surface">
        <component :is="poster" :form="form" />
      </div>
    </div>
  </UiCard>
</template>

<script setup>
defineProps({
  form: { type: Object, required: true },
  poster: { type: Object, required: true },
});
</script>

<style scoped>
.preview-wrap {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 18px;
}
.poster-surface {
  width: 820px;
  height: 1160px;
  background: white;
  border-radius: 18px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 20px 40px rgba(2, 6, 23, 0.08);
  position: relative;
  overflow: hidden;
}
</style>

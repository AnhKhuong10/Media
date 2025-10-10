<template>
  <button
    class="btn"
    :class="[color, sizeClass]"
    :style="customStyle"
    type="button"
  >
    <i v-if="icon" :class="['btn-icon', icon]" />
    <span class="btn-label">{{ label }}</span>
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  label: { type: String, required: false },
  icon: { type: String, default: '' },
  color: { type: String, default: '' }, // primary, danger, success...
  size: { type: String, default: 'md' }, // sm, md, lg
  width: { type: [String, Number], default: '' },
  height: { type: [String, Number], default: '' },
})

const sizeClass = computed(() => {
  switch (props.size) {
    case 'sm':
      return 'btn-sm'
    case 'lg':
      return 'btn-lg'
    default:
      return 'btn-md'
  }
})

const customStyle = computed(() => {
  const style: Record<string, string> = {}
  if (props.width)
    style.width = typeof props.width === 'number' ? `${props.width}px` : props.width
  if (props.height)
    style.height = typeof props.height === 'number' ? `${props.height}px` : props.height
  return style
})
</script>

<style scoped>
/* ----- Style chung của tất cả nút ----- */
.btn {
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
  font-weight: 750;
  transition: 0.2s ease-in-out;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

/* ----- Trạng thái riêng của từng loại ----- */
.btn.primary {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}
.btn.primary:hover {
  background: #1d4ed8;
}

.btn.danger {
  background: #ef4444;
  color: #fff;
  border-color: #ef4444;
}
.btn.danger:hover {
  background: #dc2626;
}

.btn.success {
  background: #16a34a;
  color: #fff;
  border-color: #16a34a;
}
.btn.success:hover {
  background: #15803d;
}

/* ----- Kích thước ----- */
.btn-sm {
  padding: 4px 8px;
  font-size: 13px;
}
.btn-md {
  padding: 8px 12px;
  font-size: 14px;
}
.btn-lg {
  padding: 12px 20px;
  font-size: 16px;
}

/* ----- Icon & Label ----- */
.btn-icon {
  font-size: 1rem;
  line-height: 0;
}

.btn-label {
  display: inline-block;
  line-height: 1;
  white-space: nowrap;
}
</style>

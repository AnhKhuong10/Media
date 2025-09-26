<template>
  <UiCard title="Cấu hình Poster">
    <UiField label="Loại Poster">
      <select :value="form.template" @change="update('template',$event.target.value)">
        <option value="new-hire">Nhân viên mới</option>
        <option value="recognition">Vinh danh</option>
      </select>
    </UiField>

    <UiField label="Tiêu đề chính">
      <input :value="form.headline" @input="update('headline',$event.target.value)" />
    </UiField>

    <UiField label="Subheadline">
      <input :value="form.subheadline" @input="update('subheadline',$event.target.value)" />
    </UiField>

    <!-- Các field khác như name, role, yob, hometown… -->
  </UiCard>
</template>

<script setup>
const props = defineProps({ form: Object })
const emit = defineEmits(['update:form'])

function update(key, value){
  emit('update:form', { ...props.form, [key]: value })
}
</script>

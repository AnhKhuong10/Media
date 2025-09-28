<template>
  <tr>
    <td class="content">{{ poster.posterId }}</td>
    <td class="content">{{ poster.title }}</td>
    <td class="content">{{ poster.content }}</td>
    <td class="content">{{ poster.createdAt }}</td>
    <td class="content">{{ poster.createdBy }}</td>
    <td class="content">{{ poster.isDraft ? 'Yes' : 'No' }}</td>
    <td class="content">{{ poster.userId }}</td>
    <td class="content">{{ poster.postStyleId }}</td>
  </tr>
</template>
<script setup lang="ts">
import { defineProps } from 'vue'
import type { Poster } from '@/model/poster' // ✅ Dùng lại type đã có

defineProps<{
  poster: Poster
}>()
</script>
<style scoped>
.content {
  padding: 10px;
}
</style>
<template>
    <div class="album-list-header">
        <Button :label="allLabel" :class="['mode-btn', { active: mode === 'all' }]"
            @click="$emit('changeMode', 'all')" />
        <Button :label="sharedLabel" :class="['mode-btn', { active: mode === 'shared' }]"
            @click="$emit('changeMode', 'shared')" />
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
import Button from 'primevue/button';

defineProps({
    mode: {
        type: String,
        default: 'all',
    },
    allLabel: {
        type: String,
        default: 'Tất cả album',
    },
    sharedLabel: {
        type: String,
        default: 'Album đã chia sẻ',
    },
});
</script>

<style scoped>
.album-list-header {
    display: flex;
    gap: 10px;
    margin-bottom: 1rem;
}

.mode-btn {
    padding: 6px 18px;
    border-radius: 6px;
    background: #f3f4f6;
    color: #222;
    border: 1px solid #e5e7eb;
    font-weight: 500;
    cursor: pointer;
}

.mode-btn.active {
    background: #6366f1;
    color: #fff;
    border-color: #6366f1;
}
</style>

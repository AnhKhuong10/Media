<template>
  <div class="app">
    <aside class="sidebar">
      <div class="brandline">
        <div class="badge">HR</div>
        <div>
          <div class="brand">HR Portal</div>
          <div class="sub">Internal Comms</div>
        </div>
      </div>
      <nav class="nav">
        <RouterLink to="/comms/posters/manager" class="link">Manager poster</RouterLink>
        <RouterLink to="/comms/posters/feed" class="link">New feed</RouterLink>
      </nav>
      <div class="sb-foot">v1.0 • Vue 3</div>
    </aside>

    <div class="main">
      <header class="header">
        <div class="h-title">{{ pageTitle }}</div>
        <div class="actions"><slot name="actions" /></div>
      </header>
      <section class="container">
        <div class="wrap">
          <router-view />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { RouterLink, useRoute } from 'vue-router'
import { computed } from 'vue'
const route = useRoute()
const pageTitle = computed(()=> {
  if(route.path.startsWith('/comms/posters')) return 'Poster Studio'
  return 'Dashboard'
})
</script>

<style scoped>
.app {
  display: grid;
  grid-template-columns: 240px 1fr;
  min-height: 100vh;
  background: #f9fafb;
  color: #111827;
  font-family: system-ui, sans-serif;
}
</style>

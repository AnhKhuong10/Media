<template>
  <aside class="sidebar">
    <div class="logo">
      <div class="logo-badge">HR</div>
      <div>
        <div class="brand">HR Portal</div>
        <div class="sub">Internal Comms</div>
      </div>
    </div>

    <nav class="nav">
      <RouterLink to="/comms/posters" class="link" active-class="active">Poster Studio</RouterLink>
    </nav>

    <div class="footer">v1.0 • Vue 3 App</div>
  </aside>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.sidebar {
  background:#0f172a; color:#e2e8f0;
  display:flex; flex-direction:column;
  width:260px; padding:20px 16px; gap:16px; height:100vh;
}
.logo { display:flex; align-items:center; gap:10px; }
.logo-badge { width:36px; height:36px; border-radius:10px;
  background:linear-gradient(135deg,#2563eb,#0ea5e9);
  display:grid; place-items:center; color:white; font-weight:800;
}
.brand { font-weight:800; letter-spacing:.4px; }
.sub { font-size:12px; color:#8aa0bd; font-weight:600; }
.nav { display:flex; flex-direction:column; gap:6px; margin-top:8px; }
.link { padding:10px 12px; border-radius:10px; color:#cbd5e1; text-decoration:none; font-weight:600; }
.link:hover, .active { background:#11193a; color:white; }
.footer { margin-top:auto; font-size:12px; color:#8aa0bd; }
</style>

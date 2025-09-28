<template>
  <section class="card">
    <!-- Header -->
    <header class="card-hd">
      <div class="hd-left">
        <h2>Danh sách Poster</h2>
        <span class="muted">{{ total }} bản ghi</span>
      </div>
      <div class="hd-right">
        <button class="btn primary" @click="onCreate">Tạo mới</button>
        <button class="btn" @click="exportCsv">Export CSV</button>
      </div>
    </header>

    <!-- Toolbar -->
    <div class="toolbar">
      <input v-model="q" type="search" class="input" placeholder="Tìm theo tiêu đề, nội dung, người tạo…" />
      <label class="check"><input type="checkbox" v-model="draftOnly" /><span>Draft</span></label>
      <label class="check"><input type="checkbox" v-model="deletedOnly" /><span>Deleted</span></label>
      <select v-model="styleFilter" class="select">
        <option value="">Tất cả style</option>
        <option v-for="s in styleOptions" :key="s" :value="s">{{ s }}</option>
      </select>
    </div>

    <!-- Table -->
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>Poster ID</th>
            <th>Title</th>
            <th>Content</th>
            <th>Created at</th>
            <th>Created By</th>
            <th>Draft</th>
            <th>User</th>
            <th>Style</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <poster v-for="p in paginated" :key="p.posterId" :poster="p" @view="onView" @edit="onEdit"
            @delete="onDelete" />
          <tr v-if="paginated.length === 0">
            <td colspan="13" class="empty">Không có dữ liệu phù hợp.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <footer class="pager">
      <div class="muted">Hiển thị</div>
      <select v-model.number="pageSize" class="select sm">
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
      </select>
      <div class="spacer"></div>
      <button class="btn sm" :disabled="page === 1" @click="page = 1">«</button>
      <button class="btn sm" :disabled="page === 1" @click="page--">‹</button>
      <span class="muted">Trang {{ page }} / {{ totalPages }}</span>
      <button class="btn sm" :disabled="page === totalPages" @click="page++">›</button>
      <button class="btn sm" :disabled="page === totalPages" @click="page = totalPages">»</button>
    </footer>
  </section>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import poster from '@/components/Poster.vue'
import type { Poster } from '@/model/poster' // nếu chưa có, xem chú thích ở cuối

// seed demo
const posters: Poster[] = [
  {
    posterId: 'p001', title: 'Chào mừng', content: 'Chào mừng bạn đến với công ty!', filePath: '/images/p001.png',
    createdAt: '2025-09-01', updatedAt: '2025-09-02', createdBy: 'Admin', updatedBy: 'Admin',
    isDraft: false, isDeleted: false, userId: 'u001', postStyleId: 'style01', photoId: 'photo001'
  },
  {
    posterId: 'p002', title: 'Vinh danh', content: 'Vinh danh nhân viên xuất sắc tháng 9!', filePath: '/images/p002.png',
    createdAt: '2025-09-05', updatedAt: '2025-09-06', createdBy: 'Manager1', updatedBy: 'Manager1',
    isDraft: false, isDeleted: false, userId: 'u002', postStyleId: 'style02', photoId: 'photo002'
  },
  {
    posterId: 'p003', title: 'Thông báo', content: 'Thông báo nghỉ lễ 2/9', filePath: '/images/p003.png',
    createdAt: '2025-08-28', updatedAt: '2025-08-30', createdBy: 'Hr', updatedBy: 'Hr',
    isDraft: true, isDeleted: false, userId: 'u003', postStyleId: 'style03', photoId: 'photo003'
  },
  {
    posterId: 'p004', title: 'Tuyển dụng', content: 'Tuyển dụng thực tập sinh IT', filePath: '/images/p004.png',
    createdAt: '2025-07-12', updatedAt: '2025-07-13', createdBy: 'Recruiter1', updatedBy: 'Recruiter2',
    isDraft: false, isDeleted: true, userId: 'u004', postStyleId: 'style04', photoId: 'photo004'
  },
  {
    posterId: 'p005', title: 'Chúc mừng sinh nhật', content: 'Chúc mừng sinh nhật các thành viên tháng 9!', filePath: '/images/p005.png',
    createdAt: '2025-09-15', updatedAt: '2025-09-16', createdBy: 'Hr', updatedBy: 'Admin',
    isDraft: false, isDeleted: false, userId: 'u005', postStyleId: 'style05', photoId: 'photo005'
  },
]

// search & filters
const q = ref('')
const draftOnly = ref(false)
const deletedOnly = ref(false)
const styleFilter = ref<string>('')

const styleOptions = Array.from(new Set(posters.map(p => p.postStyleId)))

const filtered = computed(() => {
  const kw = q.value.trim().toLowerCase()
  return posters.filter(p => {
    if (draftOnly.value && !p.isDraft) return false
    if (deletedOnly.value && !p.isDeleted) return false
    if (styleFilter.value && p.postStyleId !== styleFilter.value) return false
    if (!kw) return true
    return [p.posterId, p.title, p.content, p.createdBy, p.updatedBy, p.userId, p.photoId]
      .join(' ').toLowerCase().includes(kw)
  })
})

// pagination
const page = ref(1)
const pageSize = ref(10)
const total = computed(() => filtered.value.length)
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))
const paginated = computed(() => {
  if (page.value > totalPages.value) page.value = totalPages.value
  const start = (page.value - 1) * pageSize.value
  return filtered.value.slice(start, start + pageSize.value)
})

// actions (stub)
function onCreate() { alert('Mở form tạo mới') }
function onView(p: Poster) { alert(`Xem ${p.posterId}`) }
function onEdit(p: Poster) { alert(`Sửa ${p.posterId}`) }
function onDelete(p: Poster) { alert(`Xóa ${p.posterId}`) }

// csv
function exportCsv() {
  const rows = [
    ['posterId', 'title', 'content', 'filePath', 'createdAt', 'updatedAt', 'createdBy', 'updatedBy', 'isDraft', 'isDeleted', 'userId', 'postStyleId', 'photoId'],
    ...filtered.value.map(p => [p.posterId, p.title, p.content, p.filePath, p.createdAt, p.updatedAt, p.createdBy, p.updatedBy, p.isDraft, p.isDeleted, p.userId, p.postStyleId, p.photoId])
  ]
  const csv = rows.map(r => r.map(v => `"${String(v).replaceAll('"', '""')}"`).join(',')).join('\n')
  const url = URL.createObjectURL(new Blob([csv], { type: 'text/csv;charset=utf-8;' }))
  const a = document.createElement('a'); a.href = url; a.download = 'posters.csv'; a.click(); URL.revokeObjectURL(url)
}
</script>

<style scoped>
/* ===== Layout & text (đơn giản, sáng, dễ đọc) ===== */
.card {
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
}

.card-hd {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #eef2f7;
}

.card-hd h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.muted {
  color: #6b7280;
  font-size: 13px;
}

/* Buttons: đơn giản */
.btn {
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.btn:hover {
  background: #f9fafb;
}

.btn.primary {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.btn.primary:hover {
  background: #1d4ed8;
}

.btn.sm {
  padding: 6px 10px;
}

/* Toolbar gọn */
.toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  padding: 12px 0;
  padding-right: 630px;
}

.input {
  min-width: 260px;
}

.check {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #374151;
}

.check input {
  width: 16px;
  height: 16px;
}

/* ===== Table đơn giản ===== */
.table-wrap {
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  overflow: auto;
  /* scroll ngang khi chật */
}

.table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 14px;
}

.table thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f8fafc;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
  text-align: left;
  font-weight: bold;
  padding: 10px 12px;
  color: #111
}

.table tbody td {
  padding: 10px 12px;
  border-bottom: 1px solid #f2f4f7;
  color: #111;
}

/* Zebra + hover */
.table tbody tr:nth-child(odd) {
  background: #fff;
}

.table tbody tr:nth-child(even) {
  background: #fcfdff;
}

.table tbody tr:hover {
  background: #eef6ff;
}

/* Cột rộng hẹp hợp lý */
.table th:nth-child(1),
.table td:nth-child(1) {
  width: 90px;
}

.table th:nth-child(4),
.table td:nth-child(4) {
  width: 130px;
}

.table th:nth-child(5),
.table td:nth-child(5) {
  width: 120px;
}

.table th:nth-child(6),
.table td:nth-child(6) {
  width: 90px;
  text-transform: capitalize;
}

.table th:nth-child(7),
.table td:nth-child(7) {
  width: 90px;
}

.table th:nth-child(8),
.table td:nth-child(8) {
  width: 110px;
}

.table th:last-child,
.table td:last-child {
  width: 100px;
}

/* Rút gọn text dài ở Title/Content */
.table td:nth-child(2),
.table td:nth-child(3) {
  max-width: 420px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Empty state */
.empty {
  text-align: center;
  color: #6b7280;
  padding: 28px 12px;
}

/* ===== Pagination ===== */
.pager {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-top: 12px;
  border-top: 1px solid #eef2f7;
}

.pager .spacer {
  flex: 1;
}

.select.sm {
  height: 32px;
  padding: 0 8px;
  font-size: 13px;
}

/* Responsive nhỏ gọn */
@media (max-width: 900px) {
  .input {
    min-width: unset;
    flex: 1;
  }

  .table td:nth-child(2),
  .table td:nth-child(3) {
    max-width: 260px;
  }
}

/* thu nhỏ ô select */
.select {
  height: 32px;
  /* thấp hơn */
  max-width: 180px;
  /* giới hạn ngang */
  font-size: 14px;
  /* chữ vừa */
  padding: 0 8px;
}

/* menu xổ xuống cũng nhỏ lại */
.select option {
  font-size: 14px;
  padding: 4px 8px;
}

.input,
.select {
  height: 36px;
  background: #fff;
  color: #111;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 0 10px;
  outline: none;
}

.input {
  height: 32px;
  /* thấp hơn */
  max-width: 180px;
  /* giới hạn ngang */
  font-size: 14px;
  /* chữ vừa */
}

</style>

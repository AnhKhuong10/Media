import { createRouter, createWebHistory } from 'vue-router'

import Photos from '../pages/Photos.vue'
import Albums from '../pages/Albums.vue'
import Favorites from '../pages/Favorites.vue'
import Trash from '../pages/Trash.vue'
const routes = [
  {
    path: '/',
    // component: DefaultLayout,
    children: [
      { path: '', redirect: '/photos' },
      { path: 'photos', name: 'Photos', component: Photos },
      { path: 'albums', name: 'Albums', component: Albums },
      { path: 'favorites', name: 'Favorites', component: Favorites },
      { path: 'trash', name: 'Trash', component: Trash }
    ]
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

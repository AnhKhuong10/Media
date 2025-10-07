import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../views/MainLayout.vue'
import PostersPage from '../components/PostersPage.vue'
import ManagerPoster from '../views/ManagerPoster.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        redirect: '/comms/posters/manager'
      },
      {
        path: 'comms/posters/manager',
        name: 'manager-poster',
        component: ManagerPoster
      }

    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../views/MainLayout.vue'
import PostersPage from '../components/PostersPage.vue'
import ManagerPoster from '../views/ManagerPoster.vue'
import PosterFeed from '../views/PosterFeed.vue'

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
      },
      {
        path: 'comms/posters/feed',
        name: 'poster-new-feed',
        component: PosterFeed
      }

    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

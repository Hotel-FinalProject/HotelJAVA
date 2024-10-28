import { createRouter, createWebHistory } from 'vue-router';

import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue'

const routes = [
  {
    path: '/',
    component: () => import('@/components/MainPage.vue'),
  },
  {
    path: '/my_page',
    component: () => import('@/components/myPage.vue')
  },
  {
    path: '/register',
    component: register,
  },
  {
    path: '/login',
    component: login,
  },
  {
    path : '/hotel-details',
    component: () => import('@/components/HotelDetails.vue')
  },
  {
    path : '/rooms',
    component: () => import('@/components/HotelRoom.vue')
  },
  
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

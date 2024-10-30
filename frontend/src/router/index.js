import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue";
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue'
import OAuthRedirectHandler from '@/components/OAuthRedirectHandler.vue';

const routes = [
  {
    path: '/',
    component: MainPage,
  },
  {
    path: '/my_page',
    component: () => import('@/components/UserMypage.vue')
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
  {
    path: '/oauth2/success', // OAuth 리다이렉트 경로 추가
    component: OAuthRedirectHandler,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

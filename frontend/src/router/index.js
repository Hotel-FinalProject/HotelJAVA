import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue";
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue'
import OAuthRedirectHandler from '@/components/OAuthRedirectHandler.vue';
import EmailVerificationPage from '@/components/UserPages/EmailVerification.vue';
import PasswordReset from '@/components/UserPages/PasswordReset.vue'
import HotelAdminPage from '@/components/HotelAdminPage.vue';
import AdminLogin from '@/components/SystemAdminPages/AdminLogin.vue'

const routes = [
  {
    path: '/',
    component: MainPage,
  },
  {
    path: '/my_page',
    component: () => import('@/components/UserPages/UserMypage.vue')
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
    path: '/hotel-details/:id',
    component: () => import('@/components/HotelDetails.vue')
  },
  {
    path: '/room-details/:roomId',
    component: () => import('@/components/HotelRoom.vue'),
    name: 'HotelRoom',
  },
  {
    path: '/oauth2/success', // OAuth 리다이렉트 경로 추가
    component: OAuthRedirectHandler,
  },
  {
    path: '/find-my-id', // OAuth 리다이렉트 경로 추가
    component: () => import ('@/components/UserPages/FindId.vue'),
  },
  {
    path: '/payment', // OAuth 리다이렉트 경로 추가
    component: () => import ('@/components/PaymentPage.vue'),   
    name: 'paymentPage',
  },
  {
    path: '/verify-email', // 이메일 인증 페이지 경로
    component: EmailVerificationPage
  },
  {
    path: '/admin/hotel', // 호텔 관리자 페이지 경로
    component: HotelAdminPage
  },
  {
    path: '/reset-password', // 이메일 인증 페이지 경로
    component: PasswordReset
  },
  {
    path: '/admin',
    component: AdminLogin
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  }
});

export default router;
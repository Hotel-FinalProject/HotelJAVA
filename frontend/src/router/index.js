import { createRouter, createWebHistory } from 'vue-router';
import MainPage from "@/components/MainPage.vue";
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue'
import OAuthRedirectHandler from '@/components/OAuthRedirectHandler.vue';
import EmailVerificationPage from '@/components/UserPages/EmailVerificationPage.vue';
import HotelAdminPage from '@/components/HotelAdminPage.vue'; // 호텔 관리자 페이지
import SearchPage from '@/components/SearchPage.vue'; // 검색 페이지 추가
import MapPage from '@/components/MapPage.vue'; // 지도 페이지 추가

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

    component: () => import('@/components/HotelRoom.vue')
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
    path: '/search-results', // 검색 페이지 경로 추가
    component: SearchPage
  },
  {
    path: '/map-view', // 지도 페이지 경로
    component: MapPage,
    props: route => ({ hotels: route.query.hotels ? JSON.parse(route.query.hotels) : [] }),
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
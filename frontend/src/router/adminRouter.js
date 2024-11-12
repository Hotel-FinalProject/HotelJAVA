import { createRouter, createWebHistory } from 'vue-router';
import AdminLogin from '@/components/SystemAdminPages/AdminLogin.vue';

const adminRoutes = [
  {
    path: '/admin',
    component: AdminLogin,  // 관리자 로그인 페이지
  },

  // 관리자 관련 다른 페이지들
];

const adminRouter = createRouter({
  history: createWebHistory(),
  routes: adminRoutes,
  scrollBehavior() {
    return { top: 0 };
  }
});

export default adminRouter;

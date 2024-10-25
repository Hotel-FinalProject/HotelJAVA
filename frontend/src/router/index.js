import { createRouter, createWebHistory } from 'vue-router';
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue'

const routes = [
  {
    path: '/',
    children: [
      {
        path: '/my_page',
        component: () => import('@/components/myPage.vue')
      },
      {
        path: '/test2',
        component: () => import('@/components/testVue2.vue')
      },
      {
        path: '/test3',
        component: () => import('@/components/testVue3.vue')
      },
    ]
  },
  {
    path: '/register',
    component: register,
  },
  {
    path: '/login',
    component: login,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

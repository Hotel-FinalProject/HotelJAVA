import { createRouter, createWebHistory } from 'vue-router';

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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

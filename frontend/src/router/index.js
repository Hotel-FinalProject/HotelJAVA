import { createRouter, createWebHistory } from 'vue-router';
import DefaultLayout from '@/layout/DefaultLayout.vue';
const routes = [
  {
    path: '/',
    component: DefaultLayout, 
    children: [
      {
        path: '',
        component: () => import('@/components/MainPage.vue'),
      },
      {
        path: 'my_page',
        component: () => import('@/components/myPage.vue')
      },
      {
        path: 'test2',
        component: () => import('@/components/testVue2.vue')
      },
      {
        path: 'test3',
        component: () => import('@/components/testVue3.vue')
      },
      {
        path : 'hotel-details',
        component: () => import('@/components/HotelDetails.vue')
      },
      {
        path : 'rooms',
        component: () => import('@/components/HotelRoom.vue')
      },
    ]
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

import { createRouter, createWebHistory } from 'vue-router';
import DefaultLayout from '@/layout/DefaultLayout.vue';
import register from '@/components/registerPage.vue';
import login from '@/components/loginPage.vue';

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
        path: 'hotel-details/:id',
        component: () => import('@/components/HotelDetails.vue')
      },
      {
        path: 'room-details/:roomId',
        component: () => import('@/components/HotelRoom.vue')
      },
      {
        path: '/register',
        component: register,
      },
      {
        path: '/login',
        component: login,
      }
    ]
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

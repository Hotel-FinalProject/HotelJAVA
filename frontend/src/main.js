import { createApp } from 'vue';
import App from './App.vue';
import { createPinia } from "pinia";

import router from "./router/index";
import { useAuthStore } from './store/register_login';
import VCalendar from 'v-calendar';
import Vue3StarRatings from 'vue3-star-ratings';

const pinia = createPinia();


createApp(App).component('vue3-star-ratings', Vue3StarRatings).use(VCalendar, {}).use(pinia).use(router).mount('#app')

const authStore = useAuthStore();
authStore.checkLoginStatus();
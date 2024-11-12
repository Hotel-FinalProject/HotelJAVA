import { createApp } from 'vue';
import App from './App.vue';
import { createPinia } from "pinia";



import router from "./router/index";
import adminRouter from './router/adminRouter'; 
import { useAuthStore } from './store/register_login';
import VCalendar from 'v-calendar';

const pinia = createPinia();


createApp(App).use(VCalendar, {}).use(pinia).use(router).use(adminRouter).mount('#app')

const authStore = useAuthStore();
authStore.checkLoginStatus();
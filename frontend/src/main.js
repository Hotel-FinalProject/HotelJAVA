import { createApp } from 'vue';
import App from './App.vue';
import { createPinia } from "pinia";

import router from "./router/index";
import { useAuthStore } from './store/regist_login';

const pinia = createPinia();

createApp(App).use(pinia).use(router).mount('#app')

const authStore = useAuthStore();
authStore.checkLoginStatus();
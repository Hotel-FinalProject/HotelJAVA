import { defineStore } from 'pinia';

export const useResetPasswordStore = defineStore('resetPassword', {
  state: () => ({
    email: '',
    token: '',
  }),
  actions: {
    setEmail(email) {
      this.email = email;
    },
    setToken(token) {
      this.token = token;
    },
    clearData() {
      this.email = '';
      this.token = '';
    },
  },
});
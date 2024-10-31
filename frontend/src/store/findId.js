import { defineStore } from 'pinia';
import { findIdUser } from '@/api/api'; // axios로 구현된 findIdUser 메서드 import

export const useFindIdStore = defineStore('findId', {
  state: () => ({
    foundEmail: "",
  }),
  actions: {
    async findId(payload) {
      try {
        const response = await findIdUser(payload);
        this.foundEmail = response.data;
      } catch (error) {
        console.error("아이디 찾기 오류: ", error);
        throw new Error("아이디를 찾을 수 없습니다.");
      }
    },
  },
});

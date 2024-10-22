import config, { fetchTest } from '@/api/api'; // fetchTest 가져오기

export default {
  async fetchUsers({ commit }) {
    try {
      const response = await fetchTest(); // fetchTest 함수 사용
      commit('setUsers', response.data);  // 가져온 데이터를 mutations에 전달
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  },
  
  async createUser({ commit }, userData) {
    try {
      const response = await config.post('/users', userData);  // config 인스턴스 사용
      commit('addUser', response.data);
    } catch (error) {
      console.error('Error creating user:', error);
    }
  },
  
  async deleteUser({ commit }, userId) {
    try {
      await config.delete(`/users/${userId}`);  // config 인스턴스를 사용하여 삭제
      commit('removeUser', userId);
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  },

  async editUser({ commit }, { id, updatedUser }) {
    try {
      const response = await config.put(`/users/${id}`, updatedUser);  // config 인스턴스 사용
      commit('updateUser', response.data);
    } catch (error) {
      console.error('Error updating user:', error);
    }
  }
};

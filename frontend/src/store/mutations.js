export default {
    setUsers(state, users) {
      state.users = users;  // 상태에 유저 리스트를 저장
    },
    addUser(state, user) {
      state.users.push(user);  // 새로운 유저를 리스트에 추가
    },
    removeUser(state, userId) {
      state.users = state.users.filter(user => user.id !== userId);  // 해당 유저를 리스트에서 제거
    },
    updateUser(state, updatedUser) {
      const index = state.users.findIndex(user => user.id === updatedUser.id);  // 업데이트된 유저를 찾고
      if (index !== -1) {
        state.users.splice(index, 1, updatedUser);  // 유저 리스트에서 업데이트
      }
    }
  };
  
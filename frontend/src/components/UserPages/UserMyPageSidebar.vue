<!-- UserMyPageSidebar.vue -->
<template>
    <div class="sidebar">
      <h2>내 정보</h2>
      <router-link to="/my_page" exact-active-class="active-link">내 정보 및 리뷰</router-link>
      <router-link to="/my_page/reservations" exact-active-class="active-link">예약 내역 및 예약 현황</router-link>
      <router-link to="/my_page/edit-info" exact-active-class="active-link">계정 설정</router-link>
    </div>
  </template>

<style scoped>
.sidebar {
  background: #f0f0f0;
  padding: 20px;
  border-radius: 10px;
  width: 200px;
}
.sidebar h2 {
  margin-bottom: 10px;
}
.sidebar a {
  display: block;
  color: #333;
  text-decoration: none;
  margin: 10px 0;
}
.sidebar a:hover {
  text-decoration: underline;
}

.active-link {
  font-weight: bold;
}
</style>

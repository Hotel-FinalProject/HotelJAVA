<template>
  <div>
    <!-- 사이드바 -->
    <div class="sidebar">
      <h2>Hotel Admin</h2>
      <a href="#"><span class="icon">🏨</span>Dashboard</a>
      <a href="#"><span class="icon">🛏️</span>Room Management</a>
      <a href="#"><span class="icon">📅</span>Reservation Management</a>
      <a href="#"><span class="icon">📊</span>Analytics</a>
      <a href="#"><span class="icon">⚙️</span>Settings</a>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="hotel-admin">
        <h1>Hotel Admin Dashboard</h1>
      </div>

      <!-- 카드 섹션 -->
      <div class="card-container">
        <div class="card">
          <h3>Room Management</h3>
          <p>Manage room availability, occupancy, and room details.</p>
        </div>
        <div class="card">
          <h3>Reservation Management</h3>
          <p>View and update guest reservations and statuses.</p>
        </div>
        <div class="card">
          <h3>Analytics</h3>
          <p>Track booking statistics and guest trends.</p>
        </div>
        <div class="card">
          <h3>Settings</h3>
          <p>Update hotel configurations and manage preferences.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HotelAdminPage",
};
</script>

<style scoped>
/* 기본 설정 */
body {
  font-family: 'Arial', sans-serif;
  margin: 0;
  background-color: #f8f9fa;
  display: flex;
}

/* 사이드바 스타일 */
.sidebar {
  width: 250px;
  background-color: #0056b3; /* 호텔 관리자용: 짙은 블루 */
  color: #f8f9fa;
  height: 100vh;
  position: fixed;
  padding-top: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
}

.sidebar h2 {
  text-align: center;
  padding: 20px;
  background-color: #003c82; /* 더 짙은 블루 */
  margin: 0;
  font-size: 24px;
  color: #adb5bd;
}

.sidebar a {
  display: flex;
  align-items: center;
  color: #adb5bd;
  padding: 15px 20px;
  text-decoration: none;
  font-size: 18px;
  border-bottom: 1px solid #003c82;
  transition: background 0.3s, color 0.3s;
}

.sidebar a:hover {
  background-color: #004a99;
  color: #f8f9fa;
}

.sidebar a .icon {
  margin-right: 10px;
  font-size: 20px;
}

/* 메인 콘텐츠 */
.main-content {
  margin-left: 250px;
  padding: 20px;
  width: calc(100% - 250px);
}

.main-content h1 {
  color: #0056b3;
  text-align: center;
  margin-bottom: 20px;
  font-weight: bold;
}

/* 카드 스타일 */
.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.card {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  width: 100%;
  max-width: 300px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.card h3 {
  margin-bottom: 10px;
  color: #0056b3;
  font-size: 20px;
}

.card p {
  color: #6c757d;
}

/* 추가된 호텔 관리자 콘텐츠 */
.hotel-admin {
  padding: 20px;
  font-family: Arial, sans-serif;
  color: #495057;
}
</style>

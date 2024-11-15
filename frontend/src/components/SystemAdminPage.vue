<template>
    <div>
      <!-- 사이드바 -->
      <div class="sidebar">
        <h2>System Admin</h2>
        <a href="#"><span class="icon">📊</span>Dashboard</a>
        <a href="#"><span class="icon">🔍</span>User Management</a>
        <a href="#"><span class="icon">🚨</span>Review Reports</a>
        <a href="#"><span class="icon">🏨</span>Hotel Admin Accounts</a>
        <a href="#"><span class="icon">🛠️</span>System Settings</a>
      </div>
  
      <!-- 메인 콘텐츠 -->
      <div class="main-content">
        <!-- 추가된 콘텐츠 -->
        <div class="system-admin">
          <h1>System Admin Dashboard</h1>
        </div>
  
        <!-- 카드 섹션 -->
        <div class="card-container">
          <div class="card">
            <h3>User Management</h3>
            <p>Manage user accounts, search users, and update account status.</p>
          </div>
          <div class="card">
            <h3>Review Reports</h3>
            <p>View and resolve reported reviews for content moderation.</p>
          </div>
          <div class="card">
            <h3>Hotel Admin Accounts</h3>
            <p>Manage hotel administrator accounts and permissions.</p>
          </div>
          <div class="card">
            <h3>System Settings</h3>
            <p>Configure system settings and preferences.</p>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "SystemAdminPage",
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
    background-color: #343a40;
    color: #f8f9fa;
    height: 100vh;
    position: fixed;
    padding-top: 20px;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  }
  
  .sidebar h2 {
    text-align: center;
    padding: 20px;
    background-color: #212529;
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
    border-bottom: 1px solid #212529;
    transition: background 0.3s, color 0.3s;
  }
  
  .sidebar a:hover {
    background-color: #495057;
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
    color: #343a40;
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
    color: #343a40;
    font-size: 20px;
  }
  
  .card p {
    color: #6c757d;
  }
  
  /* 추가된 시스템 관리자 콘텐츠 */
  .system-admin {
    padding: 20px;
    font-family: Arial, sans-serif;
    color: #495057;
  }
  </style>
  
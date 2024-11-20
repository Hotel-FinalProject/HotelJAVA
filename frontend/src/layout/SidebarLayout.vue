<template>
    <div>
      <!-- 사이드바와 메인 콘텐츠 -->
      <div class="layout-container">
        <!-- 사이드바 -->
        <div v-if="showSidebar" class="sidebar">
          <h2>{{ title }}</h2>
          <!-- 메뉴 슬롯 -->
          <slot name="menu" />
        </div>
  
        <!-- 메인 콘텐츠 -->
        <div class="main-content">
          <!-- 메인 콘텐츠 슬롯 -->
          <slot />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      showSidebar: {
        type: Boolean,
        default: true,
      },
      title: {
        type: String,
        default: "관리자",
      },
    },
  };
  </script>
  
  <style scoped>
  .layout-container {
    display: flex;
  }
  
  /* 사이드바 스타일 */
  .sidebar {
    width: 335px;
    background-color: #00aef0;
    color: #f8f9fa;
    padding-top: 20px;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  }
  
  .sidebar h2 {
    text-align: center;
    padding: 20px;
    margin: 0;
    font-size: 24px;
    color: #000000;
  }
  
  /* 메뉴 스타일 */
  .sidebar a {
    display: flex;
    align-items: center;
    justify-content: start;
    padding: 15px 20px;
    color: #ffffff;
    font-size: 18px;
    text-decoration: none;
    transition: background-color 0.3s ease, color 0.3s ease;
    cursor: pointer;
  }
  
  .sidebar a:hover {
    background-color: #16518c;
    color: #ffffff;
  }
  
  /* 아이콘 스타일 */
  .sidebar a .icon {
    margin-right: 10px;
    font-size: 20px;
  }
  
  /* 활성화된 메뉴 스타일 */
  .sidebar a.active {
    background-color: #004b8d;
    color: #ffffff;
    font-weight: bold;
  }
  
  /* 메인 콘텐츠 스타일 */
  .main-content {
    flex-grow: 1;
    padding: 20px;
    background-color: #f0f0f0;
    min-height: 100vh;
  }
  </style>
  
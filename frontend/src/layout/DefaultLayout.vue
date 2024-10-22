<template>
  <div class="layout">
    <NavBar />
    <div class="layout-content">
      <Sidebar />
      <main class="main-content">
        <router-view />
      </main>
    </div>

  </div>
</template>

<script>
import NavBar from "@/layout/theNavbar.vue";
import Sidebar from "@/layout/theSidebar.vue";


export default {
  components: {
    NavBar,
    Sidebar,

  },
};
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.layout-content {
  display: flex;
  flex: 1;
}

.main-content {
  flex: 1;
  padding: 20px;
}
</style>

<template>
  <div class="layout">
    <div class="layout-content">
      <main class="main-content">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>


export default {
  name: 'DefaultLayout',
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

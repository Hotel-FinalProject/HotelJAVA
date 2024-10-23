<!-- html -->
<template>
  <nav class="navbar">
    <div class="navbar-logo">
      <!-- <img src="" alt="팀 Logo" class="logo" /> -->
      <span class="brand-name" @click="to_home()">2팀</span>
    </div>
    <div class="navbar-icons">
      <button class="navbar-btn" type="submit" aria-label="Notifications">
        <img src="@/assets/bell-48.png" alt="" />
      </button>
      <!-- <button v-if="is_login()" class="navbar-btn" @click="to_my_page()" aria-label="my Page">
        <img src="@/assets/user-48.png" alt="" />
      </button> -->
      <button class="navbar-btn" @click="to_my_page()" aria-label="my Page">
        <img src="@/assets/user-48.png" alt="" />
      </button>
      <!-- <button class="navbar-btn" aria-label="Settings">
        <img src="@/assets/settings-48.png" alt="">
      </button> -->
    </div>
  </nav>
</template>

<!-- javascript -->
<script>
export default {
  name: "theNavbar",
  methods: {
    to_my_page() {
      this.$router.push('/my_page');
    },
    to_home(){
      this.$router.push('/')
    },
  },
};
</script>

<!-- css, scss -->
<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 20px;
}

.navbar-logo {
  display: flex;
  align-items: center;
}

.logo {
  height: 40px;
  margin-right: 10px;
}

.brand-name {
  font-size: 1.5em;
  font-weight: bold;
  color: #333;
}

.navbar-icons {
  display: flex;
  align-items: center;
}

.navbar-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2em;
  margin-left: 20px;
}
</style>

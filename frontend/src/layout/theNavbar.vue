<!-- html -->
<template>
  <nav class="navbar">
    <div class="navbar-logo">
      <!-- <img src="" alt="팀 Logo" class="logo" /> -->
      <span class="brand-name" @click="to_home()">2팀</span>
    </div>
    <div class="navbar-icons">
        <router-link to ="/login">
            <span class="login_btn">로그인</span>
        </router-link>

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
  background-color: #00aef0;
  border-bottom: 1px solid #e0e0e0;
  padding: 10px 20px;
}

.navbar-logo {

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

.login_btn{
  color:white;
}

.navbar-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2em;
  margin-left: 20px;
}
</style>

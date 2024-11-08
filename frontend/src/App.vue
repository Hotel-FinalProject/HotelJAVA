<template>
  <NavBar />
  <DefaultLayout />
</template>

<script>
import DefaultLayout from "@/layout/DefaultLayout.vue";
import NavBar from "@/layout/theNavbar.vue";
import 'v-calendar/dist/style.css';
export default {
  name: "App",
  components: {
    DefaultLayout,
    NavBar,
  }
};
</script>

<style>
body{
  margin: 0;
}
</style>
<template>
  <b-container fluid class="p-5">
        <Navbar v-if="this.$store.getters.isAuthenticated" />
    <router-view/>
  </b-container>
</template>

<script>
  import Vue from 'vue'
  import {BootstrapVue, BootstrapVueIcons} from 'bootstrap-vue'
  import '@fortawesome/fontawesome-free/css/all.css'
  import '@fortawesome/fontawesome-free/js/all.js'
  import 'bootstrap/dist/css/bootstrap.css'
  import 'bootstrap-vue/dist/bootstrap-vue.css'
  import vSelect from 'vue-select'
  import Navbar from "./components/Navbar";
  import 'vue-select/dist/vue-select.css';

  Vue.use(BootstrapVue)
  Vue.use(BootstrapVueIcons)
  Vue.component('v-select', vSelect);



  export default {

    name: 'App',
    components: {
      Navbar
    }
  }
</script>

<style>
  body {
    background-color: #f5f5f5;
  }

</style>

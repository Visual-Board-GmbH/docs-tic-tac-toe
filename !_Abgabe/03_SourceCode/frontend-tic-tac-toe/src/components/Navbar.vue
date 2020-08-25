<template>
       <b-container class="d-flex justify-content-center">
        <b-navbar toggleable="lg">
          <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
          <b-collapse id="nav-collapse" is-nav  align="center">
            <b-navbar-nav>
              <b-nav-item to="/play"><i class="fas fa-play fa-3x"></i></b-nav-item>
              <b-nav-item to="/history"><i class="fas fa-history fa-3x"></i></b-nav-item>
              <b-nav-item to="/user"><b-row><b-col><i class="fas fa-user fa-3x"></i></b-col></b-row><b-row><b-col><span class="mr-2 userName">{{this.$store.getters.authenticatedUser.nickname}}</span></b-col></b-row></b-nav-item>
              <b-nav-item to="/logout"><i class="fas fa-sign-out-alt fa-3x"></i></b-nav-item>
            </b-navbar-nav>
          </b-collapse>
        </b-navbar>
      </b-container>
</template>

<script>
    import { AUTH_LOGOUT } from "@/store/actions/auth";
    export default {
        name: "Navbar",
        methods: {
            logout: function () {
                this.$store.dispatch(AUTH_LOGOUT)
                    .then(() => {
                        this.$router.push('/login')
                    })
            }
        }
    }
</script>

<style scoped>
    .nav {
        margin-bottom: 20px;
    }

    .nav>a {
        margin: 0 30px;
        color: black;
    }
    .userName {
      font-size: 1.25em;
    }
</style>
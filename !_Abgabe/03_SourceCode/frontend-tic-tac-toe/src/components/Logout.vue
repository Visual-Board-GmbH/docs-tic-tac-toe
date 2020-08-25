<template>
  <div>Logout Successfull!</div>
</template>

<script>
import {AUTH_LOGOUT} from "@/store/actions/auth";

export default {
name: "Logout",
  created: function () {
    this.$store.dispatch(AUTH_LOGOUT)
        .then(() => {
          this.$router.push('/login')
        })
  }
}
</script>

<style scoped>

</style>
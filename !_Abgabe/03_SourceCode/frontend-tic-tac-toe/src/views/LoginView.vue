<template>
    <b-container class="shadow p-3 mb-5 mt-5 bg-white rounded">
        <h3>Login</h3>
        <b-form validated="true" @submit.prevent="login">
            <b-form-group
                    id="input-group-1"
                    label="Benutzername:"
                    label-for="input-1"
                    label-align="left"
            >
                <b-form-input
                        id="input-1"
                        v-model="form.username"
                        type="text"
                        required
                ></b-form-input>
            </b-form-group>

            <b-form-group
                    id="input-group-2"
                    label="Passwort:"
                    label-for="input-2"
                    label-align="left"
            >
                <b-form-input
                        id="input-2"
                        v-model="form.password"
                        required
                        type="password"
                ></b-form-input>
            </b-form-group>
            <b-button block type="submit" variant="primary">Anmelden</b-button>
            <small>Besitzen Sie noch keinen Account. Erstellen Sie einen
                <router-link :to="{name: 'Register'}">hier</router-link>
            </small>
        </b-form>
    </b-container>
</template>

<script>
    import {AUTH_REQUEST} from "@/store/actions/auth";

    export default {
        name: "LoginVue",
        components: {},
        data() {
            return {
                form: {
                    username: '',
                    password: ''
                },
                show: true
            }
        },
        methods: {
            login: function () {
                this.$store.dispatch(AUTH_REQUEST, this.form).then(() => {
                    this.$router.push("/play");
                }).catch(() => {
                  this.$bvToast.toast("Ups! Da ist etwas schiefgelaufen. Bitte versuchen Sie es nochmals.", {
                    title: "Login Fehlgeschlagen.",
                    variant: "danger",
                    solid: true,
                    appendToast: true
                  });
                });
            }
        }
    }
</script>

<style scoped>
</style>

<template>
    <b-container class="shadow p-3 mb-5 mt-5 bg-white rounded">
        <h3>Registration</h3>
        <b-form @submit="onSubmit" validated="true" @reset="onReset" v-if="show">
            <b-form-group
                    id="input-group-1"
                    label="Name:"
                    label-for="input-1"
                    label-align="left"
            >
                <b-form-input
                        id="input-1"
                        v-model="form.name"
                        type="text"
                        required
                ></b-form-input>
            </b-form-group>

            <b-form-group
                    id="input-group-2"
                    label="Benutzername:"
                    label-for="input-2"
                    label-align="left"
                    description="Name für das Benutzerlogin"
            >
                <b-form-input
                        id="input-2"
                        v-model="form.username"
                        required
                        type="text"
                ></b-form-input>
            </b-form-group>

            <b-form-group
                    id="input-group-3"
                    label="Nickname:"
                    label-for="input-3"
                    label-align="left"
                    description="Spielername, wird anderen Spielern angezeigt"
            >
                <b-form-input
                        id="input-3"
                        v-model="form.nickname"
                        required
                        type="text"
                ></b-form-input>
            </b-form-group>

            <b-form-group
                    id="input-group-4"
                    label="Passwort:"
                    label-for="input-4"
                    label-align="left"
            >
                <b-form-input
                        id="input-4"
                        v-model="form.password"
                        required
                        type="password"
                ></b-form-input>
            </b-form-group>
            <b-button block type="submit" variant="primary">Registrieren</b-button>
            <small>Bereits registriert?
                <router-link :to="{name: 'Login'}">Anmelden</router-link>
            </small>
        </b-form>
    </b-container>
</template>

<script>
    import ticTacToeApi from "@/mixins/ticTacToeAPI";
    import router from "@/router";

    export default {
        name: "RegisterView",
        components: {},
        data() {
            return {
                form: {
                    name: '',
                    username: '',
                    nickname: '',
                    password: ''
                },
                show: true
            }
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault()
                ticTacToeApi({
                    method: 'post',
                    url: '/v1/player',
                    data: this.form,
                    headers: {'Content-Type': 'application/json'}
                })
                    .then(() => {
                        //handle success
                        router.push('/login')
                    })
                    .catch( (err) => {
                        console.log("Registration failed with the following error: " + err);
                        this.$bvToast.toast("Ups! Da ist etwas schiefgelaufen. Bitte versuchen Sie es nochmals.", {
                          title: "Registration Fehlgeschlagen.",
                          variant: "danger",
                          solid: true,
                          appendToast: true
                        });
                    });
            },
            onReset(evt) {
                evt.preventDefault()
                // Reset our form values
                this.form.email = ""
                this.form.name = ""
                this.form.food = null
                this.form.checked = []
                // Trick to reset/clear native browser form validation state
                this.show = false
                this.$nextTick(() => {
                    this.show = true
                })
            }
        }
    }
</script>

<style scoped>
    .registerView {
        margin-left: 20%;
        margin-right: 20%;
    }
</style>

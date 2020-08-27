<template>
  <b-container class="shadow p-3 mb-5 mt-5 bg-white rounded">
    <b-tabs user_profile v-if="dataLoaded">
      <b-tab title="Benutzerinformation">
        <b-form class="mt-3" @submit.prevent="updateUser">
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
              label-for="input-1"
              label-align="left"
          >
            <b-form-input
                id="input-2"
                v-model="form.userName"
                type="text"
                required
            ></b-form-input>
          </b-form-group>

          <b-form-group
              id="input-group-3"
              label="Nickname:"
              label-for="input-1"
              label-align="left"
          >
            <b-form-input
                id="input-3"
                v-model="form.nickname"
                type="text"
                required
            ></b-form-input>
          </b-form-group>

          <b-form-group
              id="input-group-4"
              label="Passwort:"
              label-for="input-2"
              label-align="left"
          >
            <b-form-input
                id="input-4"
                v-model="form.password"
                required
                type="password"
            ></b-form-input>
          </b-form-group>
          <b-button block type="submit" variant="primary">Speichern</b-button>
        </b-form>
      </b-tab>
      <b-tab title="Spielzeichen">
        <b-row class="mt-3">
          <b-col>
            <b-form-group
                id="input-group-6"
                label="Spiel-Zeichen:"
                label-for="input-6"
                label-align="left"
            >
              <b-file
                  id="input-6"
                  v-model="form.tileImage"
                  placeholder="Datei hinzufüpgen um Zeichen anzupassen"
                  drop-placeholder="Ziehen Sie eine Datei hier hin."
                  @change="onTileImageChange"
              ></b-file>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
                id="input-group-7"
                label="Aktuelles-Zeichen:"
                label-for="input-7"
                label-align="left"
            >
              <img id="input-7" class="mt-2" v-if="tileImageSrc" :src="tileImageSrc">
            </b-form-group>
          </b-col>
        </b-row>
      </b-tab>
    </b-tabs>
  </b-container>

</template>

<script>
  import ticTacToeApi from "@/mixins/ticTacToeAPI";
  import {SET_PLAYER_IMAGE} from "@/store/actions/game";

  export default {
    name: "UserView",
    data: () => {
      return {
        dataLoaded: false,
        form: {
          name: "",
          userName: "",
          nickname: "",
          password: "",
          tileImage: null
        }
      }
    },
    methods: {
      updateUser: function () {
        let userData = {
              name: this.form.name,
              username: this.form.userName,
              nickname: this.form.nickname,
              password: this.form.password
            },
            authenticatedUser = this.$store.getters.authenticatedUser;

        ticTacToeApi({
          url: "/v1/player/" + authenticatedUser.id,
          data: userData,
          method: "PUT",
          headers: {'Content-Type': 'application/json'}
        }).catch(err => {
          console.log(err);
        });
      },
      onTileImageChange: function (e) {
        let file = e.target.files[0],
            formData = new FormData();

        formData.append("image", file);

        ticTacToeApi({
          url: "/v1/player/" + this.$store.getters.authenticatedUser.id + "/image",
          data: formData,
          method: "POST",
          headers: {'Content-Type': 'multipart/form-data'}
        }).then((resp) => {
          this.$store.dispatch(SET_PLAYER_IMAGE, [this.$store.getters.authenticatedUser.id])
          console.log(resp);
        }).catch(err => {
          console.log(err);
        })
      }
    },
    created: function () {
      this.$store.dispatch(SET_PLAYER_IMAGE, [this.$store.getters.authenticatedUser.id]);
      let authenticatedUser = this.$store.getters.authenticatedUser;
      this.form.name = authenticatedUser.name;
      this.form.userName = authenticatedUser.username;
      this.form.nickname = authenticatedUser.nickname;
      this.dataLoaded = true;
    },
    computed: {
      tileImageSrc: function () {
        return this.$store.getters.getPlayerImages[this.$store.getters.authenticatedUser.id];
      }
    },
    components: {}
  }
</script>

<style scoped>

</style>

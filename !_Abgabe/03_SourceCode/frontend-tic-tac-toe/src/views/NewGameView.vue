<template>
  <b-container class="shadow p-3 mb-5 mt-5 bg-white rounded">
    <b-form @submit.prevent="newGame">
      <b-form-group
          id="input-group-1"
          label="Spielname:"
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
      <b-button block type="submit" variant="primary">Erstellen</b-button>
    </b-form>
  </b-container>
</template>

<script>
export default {
  name: "NewGameView",
  data() {
    return {
      form: {
        name: '',
      },
      show: true
    }
  },
  methods: {
    newGame: function () {
      const newGame = {
        name: this.form.name,
        gameData: {
          host: "2"
        },
        playerData: [],
        requestId: Math.random().toString(16).substr(2, 8)
      }
      this.$mqtt.publish("ttt/new_game", JSON.stringify(newGame))
      //ToDo set values dynamically
      const testGame = [{
        gameId: "1",
        name: "Test1",
        state: "OPEN",
        lastModified: "28.07.2020",
        matrixId: "",
        host: "2",
        guest: "",
        gameData: {
          "host": "2",
          "guest": "",
          "moves": [],
          "winner": ""
        }
      }];
      this.$mqtt.publish("ttt/all_games", JSON.stringify(testGame))

      this.$router.push({name: "Play"});
    }
  }
}
</script>

<style scoped>

</style>
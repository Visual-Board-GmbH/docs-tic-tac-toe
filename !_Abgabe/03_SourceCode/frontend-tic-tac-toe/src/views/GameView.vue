<template>
  <b-container fluid>
    <b-row class="mt-3" v-if="this.game">
      <b-col>
        <b-row>
          <b-col cols="5">
            <router-link tag="button" to="\play" @click.native="goBack()" class="btn"><i class="fas fa-arrow-left"></i>
            </router-link>
          </b-col>

          <b-col cols="7">
            <PlayerBoard
                :host="game.gameData.host"
                :guest="game.gameData.guest"
                :activePlayer="playerOnTheMove"
                :winner="game.gameData.winner"
                :playerData="game.playerData"
            ></PlayerBoard>
          </b-col>
        </b-row>
        <b-row class="mt-5">
          <b-col xs="12" sm="5" class="mb-2">
            <b-row>
              <b-col lg="12" xl="6">
                <v-select
                    placeholder="Bitte wählen Sie eine Matrix"
                    v-model="selectedMatrixIds"
                    v-if="isHost && matrixIds.length > 0"
                    :options="matrixIds"
                    multiple=""
                    :reduce="matrixId => matrixId.value"
                    @input="sendMatrixIds"
                    @created="getMatrixIds"
                ></v-select>
              </b-col>
              <b-col cols="auto"><h3>{{ this.game.name }}</h3></b-col>
            </b-row>
            <b-row>
              <GameHistory
                v-if="game.gameData.moves.length > 0"
                :moves="game.gameData.moves"
                :playerData="game.playerData"
                :isHistory="isHistory"
                :selectedMove="selectedMove"
                @goBackInHistory="revertMove">

              </GameHistory>
            </b-row>

          </b-col>
          <b-col  xs="12" sm="7">
            <GameBoard
                :moves="tempMoves"
                :host="game.gameData.host"
                :hostImg="hostImg"
                :guest="game.gameData.guest"
                :guestImg="guestImg"
                :isHistory="isHistory"
                :serverError="isServerError"
                :playerOnTheMove="playerOnTheMove"
                :waitingForPlayer="waitingForPlayer"
                :winner="game.gameData.winner"
                @updateGame="updateGame"
            ></GameBoard>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row v-if="!this.game">
      Kein Spiel gefunden
    </b-row>
  </b-container>
</template>

<script>
import GameBoard from "@/components/GameBoard";
import GameHistory from "@/components/GameHistory";
import PlayerBoard from "@/components/PlayerBoard";
import {BUILD_ACTIVE_GAME_LIST, BUILD_GAME_HISTORY} from "@/store/actions/game";
import router from "@/router";
import ticTacToeApi from "@/mixins/ticTacToeAPI";

export default {
  name: "GameView",
  components: {PlayerBoard, GameHistory, GameBoard},
  data() {
    return {
      game: this.$store.getters.allGames.find((g) => {
        return ((g.gameId === parseInt(this.$route.params.id, 10)) || (g.id === parseInt(this.$route.params.id, 10))) && ((
            g.gameData.host === this.$store.getters.authenticatedUser.id ||
            g.gameData.guest === this.$store.getters.authenticatedUser.id
        ) || (
            this.$store.getters.authenticatedUser.id !== g.gameData.host &&
            g.gameData.guest === 0
        ));
      }),
      tempMoves: [],
      isHistory: false,
      selectedMatrixIds: [],
      matrixIds: []
    }
  },
  methods: {
    sendMatrixIds: function () {
      let game = this.game;
      game.gameData.moves = [];
      game.matrixIds = this.selectedMatrixIds;
      game.statusCode = 0;
      game.serverResponse = false;
      this.$mqtt.publish("ttt/game", JSON.stringify(game));
    },
    revertMove: function (moves) {
      this.tempMoves = moves;
      this.isHistory = this.game.gameData.moves.length > this.tempMoves.length;
    },
    updateGame: function (move) {
      let game = this.game;

      if (this.selectedMove === 1) {
        game.state = "ACTIVE";
      }
      game.matrixIds = this.selectedMatrixIds;
      game.gameData.moves.push(move);
      game.statusCode = 0;
      game.serverResponse = false;
      delete game._cellVariants;
      this.$mqtt.publish("ttt/game", JSON.stringify(game));
    },
    goBack: function () {
      router.go(-1);
    },
    getMatrixIds: function () {
      ticTacToeApi({
        url: "/v1/matrix",
        method: "GET"
      }).then(resp => {
        let matrixIds = [];

        resp.data.forEach((matrix) => {
          if (matrix.available) {
            matrixIds.push({
              value: matrix.id,
              label: "Matrix " + matrix.id
            });
          }
        });
        this.matrixIds = matrixIds;
      }).catch(err => {
        console.log(err);
      })
    }
  },
  created: function () {
    this.getMatrixIds();

    this.$mqtt.on('message', (topic, message) => {
      // message is Buffer
      if (topic === "ttt/game") {
        let resp = JSON.parse(message.toString());

        //Player Joins Game
        if (resp.serverResponse == true && resp.statusCode === 200 && resp.state === "PENDING") {
          this.game = resp;
        }

        //Player Updated Game
        if (resp.serverResponse == true && resp.statusCode === 200 && resp.state === "ACTIVE") {
          this.game = resp;
          this.tempMoves = resp.gameData.moves;
        }

        this.selectedMatrixIds = this.game.matrixIds;
      }

      if (!this.game) {
        if (topic === "ttt/all_games") {
          let resp = JSON.parse(message);
          if (this.game === undefined) {
            this.game = resp.find((g) => {
              return (g.gameId === parseInt(this.$route.params.id, 10)) && ((
                  g.gameData.host === this.$store.getters.authenticatedUser.id ||
                  g.gameData.guest === this.$store.getters.authenticatedUser.id
              ) || (
                  this.$store.getters.authenticatedUser.id !== g.gameData.host &&
                  g.gameData.guest === 0
              ));
            });
          }
          this.$store.dispatch(BUILD_ACTIVE_GAME_LIST, resp);
        }

        if (topic === "ttt/all_game_histories") {
          let resp = JSON.parse(message);
          if (this.game === undefined) {
            this.game = resp.find((g) => {
              return (g.gameId === parseInt(this.$route.params.id, 10)) && ((
                  g.gameData.host === this.$store.getters.authenticatedUser.id ||
                  g.gameData.guest === this.$store.getters.authenticatedUser.id
              ) || (
                  this.$store.getters.authenticatedUser.id !== g.gameData.host &&
                  g.gameData.guest === 0
              ));
            });
          }

          this.$store.dispatch(BUILD_GAME_HISTORY, resp);
        }

        if (this.game) {
          this.tempMoves = this.game.gameData.moves;
          this.selectedMatrixIds = this.game.matrixIds;
        }
      }


    });

    if (this.game) {
      this.tempMoves = this.game.gameData.moves;
      this.selectedMatrixIds = this.game.matrixIds;


      if (this.waitingForPlayer && this.$store.getters.authenticatedUser.id !== this.game.gameData.host) {
        let game = this.game;
        game.state = "PENDING";
        game.gameData.guest = this.$store.getters.authenticatedUser.id;
        game.statusCode = 0;
        game.serverResponse = false;

        this.$mqtt.publish("ttt/game", JSON.stringify(game));
      }

    }
  },
  computed: {
    playerOnTheMove: function () {
      let moves = this.game.gameData.moves;

      if (moves.length > 0 && moves[moves.length - 1].player === "HOST") {
        return "GUEST";
      }
      return "HOST";
    },
    isHost: function () {
      return this.game.gameData.host === this.$store.getters.authenticatedUser.id;
    },
    waitingForPlayer: function () {
      return this.game.gameData.guest === 0 && this.game.state === "OPEN";
    },
    selectedMove: function () {
      return this.tempMoves.length === 0 ? 1 : this.tempMoves.length;
    },
    hostImg: function () {
      return this.$store.getters.getPlayerImages[this.game.gameData.host];
    },
    guestImg: function () {
      return this.$store.getters.getPlayerImages[this.game.gameData.guest];
    },
    getGame: function () {
      return this.$store.getters.allGames.find((g) => {
        return ((g.gameId === parseInt(this.$route.params.id, 10)) || (g.id === parseInt(this.$route.params.id, 10))) && ((
            g.gameData.host === this.$store.getters.authenticatedUser.id ||
            g.gameData.guest === this.$store.getters.authenticatedUser.id
        ) || (
            this.$store.getters.authenticatedUser.id !== g.gameData.host &&
            g.gameData.guest === 0
        ));
      })
    },
    isServerError: function () {
      return this.game.statusCode === 5000 & this.game.serverResponse === true;
    }
  }
}
</script>

<style scoped>

</style>
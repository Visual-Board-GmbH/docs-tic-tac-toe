<template>
  <b-container>
    <h3>Vergangene Spiele</h3>
    <GameList :games="gameHistory" :items="myClosedGame"></GameList>
  </b-container>
</template>

<script>
  import GameList from "@/components/GameList";
  import {BUILD_GAME_HISTORY} from "@/store/actions/game";

  export default {
    name: "HistoryView",
    data: () => {
      return {
        gameHistory: {
          fields: [
            {
              key: "gameId",
              class: "d-none"
            },
            {
              key: "name",
              sortable:true
            },
            {
              label: "Gastgeber",
              key: "gameData.host",
              formatter: (value, key, item) => {
                return item.playerData.find(p => p.player === "HOST").displayName;
              }
            },
            {
              label: "Gast",
              key: "gameData.guest",
              formatter: (value, key, item) => {
                return item.playerData.find(p => p.player === "GUEST") ? item.playerData.find(p => p.player === "GUEST").displayName : "Warte auf Spieler";
              }
            },
            {
              label: "Spieldatum",
              key: "datePlayed",
              formatter: value => {
                let date = new Date(parseInt(value, 10)),
                    options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
                return date.toLocaleDateString('de-CH', options);
              },
              sortable:true
            },
            {
              key: "playerData",
              class: "d-none"
            }
          ],
          items: []
        }
      }
    },
    created: function () {
      this.$mqtt.on('message', (topic, message) => {
        // message is Buffer
        if (topic === "ttt/all_game_histories") {
          let resp = JSON.parse(message);
          this.$store.dispatch(BUILD_GAME_HISTORY, resp);
        }
      });
    },
    computed: {
      myClosedGame: function () {

        return this.$store.getters.myClosedGames.map((closedGame) => {
          let winner = closedGame.gameData.winner;
            if (winner !== "NONE") {
              let winnerId = winner === "HOST" ? closedGame.gameData.host : closedGame.gameData.guest;
              if (winnerId === this.$store.getters.authenticatedUser.id) {
                closedGame._rowVariant = "success";
              } else {
                closedGame._rowVariant = "danger";
              }
            } else {
              closedGame._rowVariant = "warning";
            }
            return closedGame;
        });
      }
    },
    components: {
      GameList
    }
  }
</script>

<style scoped>

</style>

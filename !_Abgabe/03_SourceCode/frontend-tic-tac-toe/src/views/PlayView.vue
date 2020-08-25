<template>
  <b-container>
    <!-- <router-link to="/new_game" class="new-game"><i class="fas fa-plus"></i><span class="ml-2">Neues Spiel</router-link></span>-->
    <button class="btn" v-b-modal.modal-new-game><i class="fas fa-plus"></i><span class="ml-2">Neues Spiel</span>
    </button>
    <b-modal
        id="modal-new-game"
        title="Neues Spiel"
        hide-footer
    >
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
    </b-modal>
    <h3>Meine Spiele</h3>
    <GameList :games="myGames" :items="myGameItems" ></GameList>
    <h3>Offene Spiele</h3>
    <GameList :games="openGames" :items="openGameItems"></GameList>
  </b-container>
</template>

<script>
import GameList from "@/components/GameList";
import {BUILD_ACTIVE_GAME_LIST, NEW_GAME} from "@/store/actions/game";

export default {
  name: "PlayView",
  data() {
    return {
      form: {
        name: ""
      },
      myGames: {
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
            label: "Status",
            key: "state",
            sortable:true,
            formatter: (value) => {
              switch(value) {
                case "OPEN":
                  return "Warte auf Spieler"
                case "PENDING":
                    return "Warte auf Spielstart"
                case "ACTIVE":
                  return "Spiel gestartet"
                  default:
                    return value;
              }
            }
          },
          {
            label: "Gastgeber",
            key: "gameData.host",
            formatter: (value, key, item) => {
              return item.playerData.find(p => p.player === "HOST").displayName;
            },
          },
          {
            label: "Gast",
            key: "gameData.guest",
            formatter: (value, key, item) => {
              return item.playerData.find(p => p.player === "GUEST") ? item.playerData.find(p => p.player === "GUEST").displayName : "Warte auf Spieler";
            }
          },
          {
            label: "Zuletzt geändert",
            key: "lastModified",
            formatter: value => {
              let date = new Date(parseInt(value, 10)),
                  options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
              return date.toLocaleDateString('de-DE', options);
            },
            sortable:true
          },
          {
            key: "playerData",
            class: "d-none"
          }
        ],
        items: []
      },
      openGames: {
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
            label: "Status",
            key: "state",
            sortable:true,
            formatter: (value) => {
              switch(value) {
                case "OPEN":
                  return "Warte auf Spieler"
                case "PENDING":
                  return "Warte auf Spielstart"
                case "ACTIVE":
                  return "Spiel gestartet"
                default:
                  return value;
              }
            }
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
            label: "Zuletzt geändert",
            key: "lastModified",
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
  computed: {
    myGameItems: function () {
      return this.$store.getters.myGames.map((myGame) => {
        let moves = myGame.gameData.moves;
        if (moves.length > 0 && moves[moves.length - 1].player === "HOST") {
          myGame._cellVariants = {"gameData.guest": "success"};
        } else if (moves.length > 0 && moves[moves.length - 1].player === "GUEST") {
          myGame._cellVariants = {"gameData.host": "success"};
        }
        return myGame;
      });

    },
    openGameItems: function () {
      return this.$store.getters.openGames;
    }
  },
  created: function () {
    this.$mqtt.on('message', (topic, message) => {

      // message is Buffer
      if (topic === "ttt/new_game") {
        // let activeRequest = sessionStorage.getItem("activeRequest") ? JSON.parse(sessionStorage.getItem("activeRequest")) : [],
        let resp = JSON.parse(message);
        // activeRequest = activeRequest.splice(activeRequest.indexOf(resp.requestId), 1);


        // sessionStorage.setItem("activeRequest", JSON.stringify(activeRequest));

        if (resp.serverResponse == true && resp.statusCode === 200) {
          console.log("message: " + resp);

          //ToDo Fix bug with Toaster
          // this.$bvToast.toast("Neues Spiel mit dem Namen " + resp.name + " wurde erstellt.", {
          //   title: "Neues Spiel erstellt",
          //   variant: "success",
          //   solid: true,
          //   appendToast: true
          // });
        }
      }

      if (topic === "ttt/all_games") {
        let resp = JSON.parse(message);

        this.$store.dispatch(BUILD_ACTIVE_GAME_LIST, resp);
      }
    });
  },
  components: {
    GameList
  },
  methods: {
    newGame: function () {
            // if (Array.isArray(activeRequest)) {
      //   activeRequest.push(requestId)
      //   sessionStorage.setItem("activeRequest", JSON.stringify(activeRequest));
      // }
      this.$store.dispatch(NEW_GAME, this.form.name);

      //this.$mqtt.publish("ttt/new_game", JSON.stringify(newGame))
      this.$root.$emit("bv::hide::modal", "modal-new-game");

    }
  }
}
</script>

<style scoped>
.new-game {
  color: black;
  text-decoration: none;
}

</style>

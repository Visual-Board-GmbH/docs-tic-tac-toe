<template>
  <b-container>
    <GameBoardGrid :moves="moves" :grid="gridLayout" :isReadOnly="(isHistory || isPlayerOnTheMove!==true || waitingForPlayer === true || winner !== null || serverError)" @updateGame="updateGame"></GameBoardGrid>
  </b-container>
</template>

<script>
import GameBoardGrid from "@/components/GameBoardGrid";

export default {
  name: "GameBoard",
  props: ["moves", "host", "guest", "isHistory", "playerOnTheMove", "waitingForPlayer", "winner", "hostImg", "guestImg", "serverError"],
  methods: {
    updateGame: function (position) {

      const move = {
        count: this.moves.length + 1,
        player: this.playerOnTheMove,
        gridPosition: position,
      }

      this.$emit("updateGame", move);
    },
  },
  computed: {
    gridLayout: function () {
      let gridLayout = [
            {
              id: 1,
              position: "TOP_LEFT",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 2,
              position: "TOP_MID",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 3,
              position: "TOP_RIGHT",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 4,
              position: "MID_LEFT",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 5,
              position: "MID_MID",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 6,
              position: "MID_RIGHT",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 7,
              position: "BOTTOM_LEFT",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 8,
              position: "BOTTOM_MID",
              isSet: false,
              tile: "",
              image: ""
            },
            {
              id: 9,
              position: "BOTTOM_RIGHT",
              isSet: false,
              tile: "",
              image: ""
            }
          ],
          moves = this.moves,
          index;

      for (let i = 0, movesLength = moves.length; i < movesLength; i++) {
        index = gridLayout.findIndex(element => element.position === moves[i].gridPosition);
        gridLayout[index].isSet = true;




        gridLayout[index].tile = moves[i].player === "HOST" ? "X" : "O";
        gridLayout[index].image = moves[i].player === "HOST" ? this.hostImg : this.guestImg;
      }

      return gridLayout;
    },
    isPlayerOnTheMove: function () {
      let moves = this.moves,
          currentPlayer = this.host,
          authenticatedUser = this.$store.getters.authenticatedUser ? this.$store.getters.authenticatedUser : {};


      if (moves.length > 0 && moves[moves.length -1].player === "HOST") {
        currentPlayer = this.guest;
      }

      // eslint-disable-next-line no-prototype-builtins
      if (authenticatedUser.hasOwnProperty("id") && authenticatedUser.id === currentPlayer) {
        return true;
      }
      return false;
    },

  },
  components: {GameBoardGrid}
}
</script>

<style scoped>

</style>
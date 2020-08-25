<template>
  <b-container class="shadow bg-white rounded">
    <b-row>
      <b-col><h5>History</h5></b-col>
    </b-row>

      <b-row
          v-for="move in this.moves"
          :key="move.count"
          :class="{'bg-info' : selectedMove == move.count}"
          @click="changeMove(move.count)"
      >
        <b-col v-if="move.player === 'HOST'">Zug Nr. {{move.count}} von Spieler {{playerData.find(p => p.player === "HOST").displayName}}</b-col>
        <b-col v-else-if="move.player === 'GUEST'">Zug Nr. {{move.count}} von Spieler {{playerData.find(p => p.player === "GUEST").displayName}}</b-col>
        <b-col v-else>Zug Nr. {{move.count}} von Spieler {{move.player}}</b-col>
        <b-col cols="3"><i class="fas fa-undo-alt ml-1"></i></b-col>
      </b-row>
  </b-container>
</template>

<script>
export default {
  name: "GameHistory",
  props: ["moves", "selectedMove", "playerData"],
  methods: {
    changeMove: function (count) {
      let moveHistory = this.moves.filter(move => move.count <= count)
      this.$emit("goBackInHistory", moveHistory);
    },
  }
}
</script>

<style scoped>

</style>
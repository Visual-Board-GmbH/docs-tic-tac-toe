<template>
  <b-container class="shadow bg-white rounded">
    <b-row>
      <b-col class="p-2 rounded text-center" :class="{active: activePlayer === 'HOST', 'bg-success': winner === 'HOST', 'bg-warning': winner === 'NONE'}">{{ getDisplayName("HOST") }}</b-col>
      <b-col class="p-2 rounded text-center" :class="{active: activePlayer === 'GUEST', 'bg-success': winner === 'GUEST', 'bg-warning': winner === 'NONE'}">{{ this.guest === 0 ? "Warten auf Spieler" : getDisplayName("GUEST") }}</b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "PlayerBoard",
  props: ["host", "guest", "activePlayer", "winner", "playerData"],
  methods: {
    getDisplayName: function (playerType) {
      return this.playerData.find(p => p.player === playerType) ? this.playerData.find(p => p.player === playerType).displayName : playerType;
    }
  }
}
</script>
<style scoped>
.active {
  background-color: darkgray;
}
</style>
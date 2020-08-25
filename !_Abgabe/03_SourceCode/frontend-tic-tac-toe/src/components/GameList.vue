<template>
  <b-table
      :items="this.items || games.items"
      :fields="games.fields"
      @row-clicked="openGame"
      show-empty
      empty-text="Keine Spiele vorhanden"
      sticky-header
      :hover="true"
  ></b-table>
</template>

<script>
import router from "@/router";
import {SET_PLAYER_IMAGE} from "@/store/actions/game";

export default {
  name: "GameList",
  props: ["games", "items"],
  methods: {
    openGame: function (item) {
      this.$store.dispatch(SET_PLAYER_IMAGE, [item.gameData.host, item.gameData.guest]).then(() => {
            router.push({name: "Game", params: {id: item.gameId || item.id}});
          });

    }
  }
}
</script>

<style scoped>

</style>
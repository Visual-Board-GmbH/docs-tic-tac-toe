<template>
    <b-col @click="setValue(colId)" :class="{disabled: isSet===true}" class="game-board-cell align-items-center">
      <div class="game-board-cell-content" v-if="isSet && getImage !== null"><b-img :src="getImage" center class="p-2" fluid-grow :alt="getTile"></b-img></div>
      <div class="game-board-cell-content text-center" v-else><span v-html="getTile"></span></div>
    </b-col>
</template>

<script>

    export default {
        name: "GameBoardCell",
        props: ["colId", "isSet", "position", "tile", "image", "isActivePlayer"],
        methods: {
            setValue: function (colId) {
                this.$emit("setValueInCell", colId);
            }
        },
      computed : {
          getImage: function () {
            return this.image !== '' && this.image != null ? this.image : null;
          },
          getTile: function () {
            return this.tile === "X" ? '<i class="fas fa-times"></i>' : this.tile === "O" ? '<i class="far fa-circle"></i>' : "";
          }
      }
    }
</script>

<style scoped>
    .game-board-cell {
        border: 1px solid black;
        position: relative;
        padding-right: 0px;
        padding-left: 0px;
        cursor: pointer;
    }
    .disabled {
        pointer-events: none
    }

    .game-board-cell:after {
      content: "";
      display: block;
      padding-bottom: 100%;
    }

    .game-board-cell-content {
      position: absolute;
      width: 100%;
      height: 100%;
      font-size:6vw;
    }

</style>
<template>
    <b-container class="game-board-grid" :class="{disabled: isReadOnly }" fluid>
        <b-row cols="3">
            <GameBoardCell
                    v-for="col in grid"
                    :key="col.id"
                    :colId="col.id"
                    :tile="col.tile"
                    :image="col.image"
                    :position="col.position"
                    :isSet="col.isSet"
                    @setValueInCell="updateCellValue(col.position)"
            >
            </GameBoardCell>
        </b-row>
    </b-container>
</template>

<script>
    import GameBoardCell from "@/components/GameBoardCell";

    export default {
        name: "GameBoardGrid",
        props: ["grid", "isReadOnly"],
        methods: {
            updateCellValue: function (position) {
              console.log(position)
                this.$emit("updateGame", position);
            }
        },
        components: {
            GameBoardCell
        }
    }
</script>

<style scoped>
    .game-board-grid {
      max-width: 75%;
    }

    .disabled {
      pointer-events: none
    }
</style>
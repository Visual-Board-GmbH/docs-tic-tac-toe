import Vue from "vue";
import Vuex from "vuex";
import auth from "./modules/auth";
import game from "./modules/game";

Vue.use(Vuex);

const debug = false;//process.env.NODE_ENV !== "production";

export default new Vuex.Store({
    modules: {
        auth,
        game
    },
    strict: debug
});
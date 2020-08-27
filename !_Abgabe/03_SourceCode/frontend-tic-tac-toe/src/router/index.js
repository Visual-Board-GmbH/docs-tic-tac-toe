import Vue from 'vue';
import VueRouter from 'vue-router'
import LoginView from "../views/LoginView";
import RegisterView from "../views/RegisterView";
import PlayView from "../views/PlayView";
import UserView from "../views/UserView";
import HistoryView from "../views/HistoryView";
import GameView from "@/views/GameView";
import store from "../store";
import {AUTH_CHECK} from "../store/actions/auth";
import Logout from "@/components/Logout";

Vue.use(VueRouter);

const routes = [
    {
        path: "*",
        name: "App",
        redirect: "/play",
        meta: {
            requiresAuth: true
        }
    },
    {
        path: "/login",
        name: "Login",
        component: LoginView,
        meta: {
            requiresAuth: false
        }
    },
    {
        path: "/logout",
        name: "Logout",
        component: Logout,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: "/register",
        name: "Register",
        component: RegisterView,
        meta: {
            requiresAuth: false
        }
    },
    {
        path: '/play',
        name: "Play",
        component: PlayView,
        meta: {
            requiresAuth: true
        },
    },
    {
        path: '/history',
        name: "History",
        component: HistoryView,
        meta: {requiresAuth: true}
    },
    {
        path: '/user',
        name: "User",
        component: UserView,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: "/game/:id",
        name: "Game",
        component: GameView,
        props: true
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: routes
});

//router.addRoutes(routes)


//prevent user from accessing views if they are not authenticated
router.beforeEach(async (to, from, next) => {
        //Check if the user is authenticated --> async
        await store.dispatch(AUTH_CHECK);
        if (to.matched.some((route) => route.meta.requiresAuth)) {
            if (!store.getters.isAuthenticated) {
                next({name: "Login"});
            } else {
                next();
            }
        } else {
           next();
        }
});

export default router;

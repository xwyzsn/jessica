import Vue from 'vue'
import VueRouter from 'vue-router'
import qs from 'qs'
import routes from './routes'
import {VueJsonp} from 'vue-jsonp'
import axios from "axios";
Vue.use(VueJsonp)
Vue.use(VueRouter)
Vue.prototype.$qs = qs
axios.interceptors.request.use(config=>{
  if (localStorage.getItem('auth')){
    config.headers.Authorization=localStorage.getItem('auth')
  }
  return config
})
Vue.prototype.$axios = axios
/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default function (/* { store, ssrContext } */) {
  const Router = new VueRouter({
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes,

    // Leave these as they are and change in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    mode: process.env.VUE_ROUTER_MODE,
    base: process.env.VUE_ROUTER_BASE
  })

  Router.beforeEach((to,from,next)=>{
    let auth = localStorage.getItem('auth')
    if(to.name !== 'login' && !auth ){

      next({name:'login'})
    }
    else if (to.name ==='login' && auth){
          next()
    }
    else if(from.name ==='login' && auth){
      axios.get(process.env.API_URL+"/api/study/user",{
        headers:{
          Authorization:localStorage.getItem('auth')
        }
      })
        .then(res=>{
          if(res.data.code!==400){
            next()
          }
        }).catch(err=>{
        localStorage.clear()
      })
    }
    else {
      next()
    }

  })

  return Router
}


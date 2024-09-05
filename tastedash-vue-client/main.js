import App from './App'
import Vue from 'vue'
import GoEasy from 'goeasy'

import './style/animat.css'

// let moment = require('moment')
// moment.locale('zh-cn')
// Vue.prototype.$Time = moment

Vue.prototype.goeasy = GoEasy.getInstance({
    host: "hangzhou.goeasy.io",
    appkey: "改为你自己的",
    modules: ['pubsub']
});


Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()

import Vue from 'vue'
import Vuex from 'vuex'
import { getRequest } from '../utils/api'
import SocketJS from 'sockjs-client'
import Stomp from 'stompjs'
import { Notification } from 'element-ui';

Vue.use(Vuex)
const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [],
        sessions: {},
        admins: [],
        currentAdmin: {},
        currentSession: null,
        filterKey: '',
        stomp: null,
        isDot: {}
    },
    mutations: {
        INIT_ADMIN(state,admin){
            state.currentAdmin = admin;
        },

        initRoutes(state, data) {
            state.routes = data;
        },
        changecurrentSession(state, currentSession) {
            state.currentSession = currentSession;
            Vue.set(state.isDot,state.currentAdmin.username+'#'+state.currentSession.username,false);
        },
        addMessage(state, msg) {
            console.log(state.currentAdmin.username)
            console.log(msg.to)
            let mss = state.sessions[state.currentAdmin.username + '#' + msg.to]
            if (!mss) {
                Vue.set(state.sessions, state.currentAdmin.username + '#' + msg.to, []);
            }
            state.sessions[state.currentAdmin.username + '#' + msg.to].push({
                content: msg.content,
                date: new Date(),
                self: !msg.notSelf
            });
        },
        INIT_DATA(state) {
            // 浏览器本地的历史聊天记录
            let data = localStorage.getItem('vue-chat-session');
            //console.log(data)
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        INIT_ADMINS(state, data) {
            state.admins = data;
        }
    },
    actions: {
        connect(context) {
            //连接端点
            context.state.stomp = Stomp.over(new SocketJS('/ws/ep'));
            //获取token 创建连接 
            let token = window.sessionStorage.getItem('tokenStr');
            context.state.stomp.connect({ 'Auth-Token': token }, success => {
                //订阅消息   默认前置有个/user
                context.state.stomp.subscribe('/user/queue/chat', msg => {
                    let receiveMsg = JSON.parse(msg.body);
                    //如果当前未选中 被发送人   或者 发送人和被发送人 不是同一个
                    if (!context.state.currentSession || receiveMsg.from != context.state.currentSession.username) {
                        Notification.info({
                            title: '【'+receiveMsg.formNickName+'】发来一条消息',
                            message: receiveMsg.content.length >10 ? receiveMsg.content.substr(0,10):receiveMsg.content,
                            position: 'bottom-right'
                        });
                        Vue.set(context.state.isDot,context.state.currentAdmin.username+'#'+receiveMsg.from,true);
                    }
                    receiveMsg.notSelf = true;
                    receiveMsg.to = receiveMsg.from;
                    console.log("消息体" + JSON.stringify(receiveMsg));
                    context.commit('addMessage', receiveMsg);
                })
            }, error => {

            })
        },

        // initCurrentAdmin(context) {
        //     context.state.currentAdmin = JSON.parse(window.sessionStorage.getItem("user"));
        // },

        initData(context) {
            context.commit('INIT_DATA');
            //发送后端请求获取其他用户信息 
            getRequest('/admin/chat/').then(resp => {
                context.commit('INIT_ADMINS', resp)
            }, err => {

            })
        }
    }

})


store.watch(function (state) {
    return state.sessions
}, function (val) {
    console.log('CHANGE: ', val);
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})


export default store;
//该文件用于创建整个文件的路由器 
import VueRouter from "vue-router";

//引入组件 
const Login = () => import('../views/Login.vue')
const Home = () => import('../views/Home.vue')
const FriendChat = () => import('../views/chat/FriendChat.vue')
const AdminInfo = () => import('../views/AdminInfo.vue')


export const constantRouterMap = [
    {
        path: '/',
        name: 'Login',
        component: Login,
        hidden: true
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        children:[
            {
                path: '/chat',
                name: '在线聊天',
                component: FriendChat,
            },
            {
                path: '/admininfo',
                name: '个人中心',
                component: AdminInfo
            }
        ]
    }
]




const createRouter  = () => new VueRouter({
    routes: constantRouterMap,
    mode: 'hash'  //取出url#
})


const router = createRouter();
export function resetRouter() {
    const newRouter = createRouter();
    router.matcher = newRouter.matcher;
}



export default router
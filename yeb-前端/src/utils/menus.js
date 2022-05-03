import { getRequest } from "./api";
import {resetRouter} from '../router/index'


//定义获取菜单的方法 
export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }

    //否则 给后端发送请求获取 
    getRequest('/system/config/menu').then(data => {
        
        // console.log('返回当前用户的菜单信息为',JSON.stringify(data))
        if (data) {
            //格式化号的router   
            let fmtRoutes = formatRoutes(data);
            // console.log("格式化后的数据为"+JSON.stringify(fmtRoutes))
            //添加到router里面    https://www.jianshu.com/p/3d17f8549fad
            // router.matcher = new VueRouter().matcher
            resetRouter()
            router.addRoutes(fmtRoutes);
            //将数据存入vuex
            store.commit('initRoutes', fmtRoutes);
            //连接websocket 
            store.dispatch('connect');
        }
    })
}


//格式化后端返回的数据
export const formatRoutes = (routes) => {
    let fmtRoutes = [];
    routes.forEach(router => {
        //解构表达式 
        let {
            path,
            component,
            name,
            iconCls,
            children
        } = router;
        //如果 children存在 并且 属于一个数组 
        if (children && children instanceof Array) {
            //递归
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                if (component.startsWith('Home')) {
                    require(['../views/' + component + '.vue'], resolve)
                } else if (component.startsWith('Emp')) {
                    require(['../views/emp/' + component + '.vue'], resolve)
                } else if (component.startsWith('Per')) {
                    require(['../views/per/' + component + '.vue'], resolve)
                } else if (component.startsWith('Sal')) {
                    require(['../views/sal/' + component + '.vue'], resolve)
                } else if (component.startsWith('Sta')) {
                    require(['../views/sta/' + component + '.vue'], resolve)
                } else if (component.startsWith('Sys')) {
                    require(['../views/sys/' + component + '.vue'], resolve)
                }
            }
        }
        fmtRoutes.push(fmRouter)
    });
    return fmtRoutes;
}
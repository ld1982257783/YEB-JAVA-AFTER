import axios from 'axios';
//引入message 错误提示消息
import { Message } from 'element-ui';
import router from '../router/index';



//定义请求拦截器     config相当于request 请求 
axios.interceptors.request.use(config => {
    //如果存在token 获取tokenStr 放入请求头 
    if(window.sessionStorage.getItem('tokenStr')){
        config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    }
    return config;
},error=>{
    console.log(error)
})



//响应拦截器
axios.interceptors.response.use(success => {
    //业务逻辑错误 

    if (success.status && success.status == 200) {
        //查看返回数据 
        if (success.data.code == 500 || success.data.code == 401 || success.data.code == 403) {
            Message.error({ message: success.data.message });
            return;
        }
        if (success.data.message) {
            Message.success({ message: success.data.message })
        }
    }

    return success.data;

}, error => {
    //
    if (error.response.code == 504 || error.response.code == 404) {
        Message.error({ message: '服务器被吃了' });
    } else if (error.response.code == 403) {
        Message.error({ message: '权限不足，请联系系统管理员' });
    } else if (error.response.code == 401) {
        Message.error({ message: '尚未登录,请登录' });
        //跳转到登录页面
        router.replace('/');
    } else {
        if (error.response.data.message) {
            Message.error({ message: error.response.data.message });
        } else {
            Message.error({ message: '未知错误' })
        }
    }
    return;
})




//post请求 
let base = ''
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        //为请求添加前置路径
        url: `${base}${url}`,
        data: params
    })
}



//传送json的 put请求 
export const putRequest = (url,params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}


//传送json的 get请求 
export const getRequest = (url,params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}


//传送json的 delete请求 
export const deleteRequest = (url,params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}


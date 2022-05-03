import axios from 'axios'

// 设置响应拦截器 
const service = axios.create({
    responseType: 'arraybuffer'
})

service.interceptors.request.use(config=>{
    config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    return config;
},error=>{
    console.log(error);
})  



// 设置相应拦截器 
service.interceptors.response.use(resp=>{
    //获取请求头 
    const headers = resp.headers;
    let reg = RegExp(/application\/json/);
    if(headers['content-type'].match(reg)){
        resp.data = unitToString(resp.data);
    }else{
        //引入js-file-download
        let fileDownload = require('js-file-download');
        //获取文件名
        let fileName = headers['content-disposition'].split(';')[1].split('filename=')[1];
        //获取类型
        let contentType = headers['content-type'];
        fileName = decodeURIComponent(fileName);
        //通过js-file-load的方式去下载文件
        fileDownload(resp.data,fileName,contentType);
    }
},(err)=>{
    console.log(error);
})



function unitToString(unitArray){
    let encodeString = String.fromCharCode.apply(null,new Uint8Array(unitArray));
    //进行转化 防止中文乱码
    let decodeString = decodeURIComponent(escape(encodeString));
    return JSON.parse(decodeString);
}


let base = '';
export const downloadRequest=(url,params)=>{
    return service({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}


export default service;

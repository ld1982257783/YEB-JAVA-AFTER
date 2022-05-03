let proxyObj={}
//跨域请求转发 
proxyObj['/']={
    //webScoket
    ws:false,
    target: 'http://localhost:8081',
    //发送请求头host会被设置为targer
    changeOrigin: true,
    //不重写请求地址 
    pathReWrite: {
        '^/': '/'
    }
}


//websocket 跨域 
proxyObj['/ws'] = {
    ws: true,
    target: 'ws://localhost:8081'
};

module.exports = {
    //跨域
    devServer: {
        host: 'localhost',
        port: 8080,
        proxy: proxyObj
    },
    lintOnSave: false
}

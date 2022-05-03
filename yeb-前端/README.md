# 安装element-ui 
  1. npm i element-ui -S
  2. npm install --save vue-router@3.5.3  安装路由 

## 1 登录页面创建 
```
<!--  -->
<template>
  <div>
    <el-form
      :rules="rules"              //负责与data里面的rules进行校验功能
      ref="loginForm"             //相当于给form起了一个名字
      :model="loginForm"
      class="loginContainer"
    >
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">        //prop 只有写了prop的标签 才会和rules里面的属性进行校验
        <el-input
          type="test"
          auto-complete="false"             //自动补全
          v-model="loginForm.username"          
          placeholder="请输入用户名"        
        ></el-input>
      </el-form-item>       
      <el-form-item prop="password">
        <el-input
          type="pasword"
          auto-complete="false"
          v-model="loginForm.password"
          placeholder="请输入密码"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          type="text"
          auto-complete="false"
          v-model="loginForm.code"
          placeholder="点击图片更换验证码"
          style="width: 250px; margin-right: 5px"
        >
        </el-input>
        <img :src="captchaUrl" alt="" />
      </el-form-item>
      <el-checkbox v-model="loginForm.checked" class="loginRember"
        >记住我</el-checkbox
      >
      <el-button type="primary" style="width: 100%" @click="submitLogin"
        >登录</el-button
      >
    </el-form>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  name: "Login",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      captchaUrl: "",
      loginForm: {
        username: "admin",
        password: "123",
        code: "",
        checked: true,
      },

      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
      },
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
      //提交登录校验
    submitLogin() {
      //根据表单 el-form 的ref进行绑定校验
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          alert("submit!");
        } else {
          this.$message.error("请输入所有字段");
          return false;
        }
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
};
</script>
<style scoped>
.loginContainer {
  /* 边框圆润 */
  border-radius: 15px;
  background-clip: padding-box;
  /* 外边距 */
  margin: 180px auto;
  /* 宽度 */
  width: 350px;
  /* 内边距 */
  padding: 15px 35px 15px 35px;
  /* 背景颜色 */
  background-color: #fff;
  /* 边框 */
  border: 1px solid #eaeaea;
  /* 阴影 */
  box-shadow: 0 0 25px #cacaca;
}

.loginTitle {
  margin: 0px auto 40px auto;
  text-align: center;
}

.loginRember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}
</style>
    
```



## 2. 安装 axios 
  1. npm install axios 
  2. /utils/api.js 封装请求拦截 与请求方式 




## 3. 安装 vuex
  1. npm install vuex@3.4.0 --save 
  2. 在main.js里面进行导入 css样式     import 'font-awesome/css/font-awesome.css'





## 安装 font-awesome
  1. npm install font-awesome@4.7.0 



## 封装文件下载请求 
  1. 安装js-file-download
  2. npm install js-file-download@0.4.12


## 安装sass-loader  node-sass
  1. npm install sass-loader@8.0.2 --save-dev
  2. npm install node-sass@4.14.1 --save-dev


## 实现websocket 
  1. npm install sockjs-client@1.4.0
  2. npm install stompjs@2.3.3





    <el-dialog title="编辑用户信息" :visible.sync="dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>用户名称:</td>
            <td><el-input v-model="admin.name"></el-input></td>
          </tr>
          <tr>
            <td>电话号码:</td>
            <td><el-input v-model="admin.telephone"></el-input></td>
          </tr>
          <tr>
            <td>手机号码:</td>
            <td><el-input v-model="admin.phone"></el-input></td>
          </tr>
          <tr>
            <td>用户地址:</td>
            <td><el-input v-model="admin.address"></el-input></td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAdminInfo">确 定</el-button>
      </span>
    </el-dialog>
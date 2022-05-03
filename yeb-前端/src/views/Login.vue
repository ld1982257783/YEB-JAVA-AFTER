<!--  -->
<template>
  <div>
    <el-form
      v-loading="loading"
      element-loading-text="正在登陆"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      :rules="rules"
      ref="loginForm"
      :model="loginForm"
      class="loginContainer"
    >
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input
          type="test"
          auto-complete="false"
          v-model="loginForm.username"
          placeholder="请输入用户名"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
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
        <img :src="captchaUrl" @click="updateCaptcha" />
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
      //调用后端接口 返回图片
      captchaUrl: "/captcha?time=" + new Date(),
      loginForm: {
        username: "admin",
        password: "123",
        code: "",
        checked: true,
      },
      loading: "",

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
    submitLogin() {
      //根据表单 el-form 的ref进行绑定校验
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.postRequest("/login", this.loginForm)
            .then((resp) => {
              this.loading = true;
              //alert(JSON.stringify(resp))
              if (resp) {
                this.loading = false;
                //获取token
                const tokenStr = resp.obj.tokenHead + resp.obj.token;
                //本地存储到浏览器 存储用户token
                window.sessionStorage.setItem("tokenStr", tokenStr);
                //获取要跳转页面的路径
                // this.$router.replace('/home')
                let path = this.$route.query.redirect;
                console.log("要跳转的请求路径为" + path);
                this.$router.replace(
                  path == "/" || path == undefined ? "/home" : path
                );
              } else {
                this.loading = false;
              }
            })
            .catch((err) => {
              this.loading = false;``
            });
        } else {
          this.loading = false;
          this.$message.error("请输入所有字段");
          return false;
        }
      });
    },

    updateCaptcha() {
      this.captchaUrl = "/captcha?time=" + new Date();
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


<style>
/*  此处吧 scpoed 去掉 否则   验证码 和输入框无法一行 */
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

.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>
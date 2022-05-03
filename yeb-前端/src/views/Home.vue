<!--  -->
<template>
  <div>
    <el-container>
      <el-header class="HomeHeader">
        <div class="title">云E办系统</div>
        <div>
          <el-button
            icon="el-icon-bell"
            type="text"
            size="normal"
            style="margin-right: 8px;color: black;"
            @click="goChat()"
          ></el-button>
          <el-dropdown class="userInfo" @command="handleCommand">
            <span class="el-dropdown-link">
              {{ user.name }}<i><img :src="user.userFace" /></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <!-- 菜单点击事件@select="menuClick   router启用路由模式  -->
          <el-menu router unique-opened>
            <template v-for="(item, index) in routes">
              <el-submenu :index="index + ''" :key="index" v-if="!item.hidden">
                <template slot="title"
                  ><i
                    :class="item.iconCls"
                    style="color: #1accff; margin-right: 5px"
                  ></i
                  >{{ item.name }}</template
                >
                <el-menu-item
                  :index="children.path"
                  v-for="(children, indexj) in item.children"
                  :key="indexj"
                  >{{ children.name }}</el-menu-item
                >
              </el-submenu>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb
            separator-class="el-icon-arrow-right"
            v-if="this.$router.currentRoute.path != '/home'"
          >
            <el-breadcrumb-item :to="{ path: '/home' }"
              >首页</el-breadcrumb-item
            >
            <el-breadcrumb-item>{{
              this.$router.currentRoute.name
            }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div
            class="homeWelcome"
            v-if="this.$router.currentRoute.path == '/home'"
          >
            欢迎来到云E办系统
          </div>
          <router-view class="homeRouterView" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  name: "Home",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    return {
      // user: JSON.parse(window.sessionStorage.getItem("user"))
      //   ? JSON.parse(window.sessionStorage.getItem("user"))
      //   : "",
      num: 123,
    };
  },
  //监听属性 类似于data概念
  computed: {
    routes() {
      console.log(this);
      return this.$store.state.routes;
    },
    user(){
      return this.$store.state.currentAdmin;
    }
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    //跳转聊天界面
    goChat(){
      this.$router.push('/chat')
    },

    handleCommand(command) {
      if (command == "logout") {
        this.$confirm("此操作将注销登录, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.postRequest("/logout");
            //移除 tokenStr 移除 user 等相关信息
            window.sessionStorage.removeItem("tokenStr");
            window.sessionStorage.removeItem("user");
            //退出登录之前   把vuex里面的所有菜单数据进行清除
            this.$store.commit("initRoutes", []);
            //跳转到登录页面
            this.$router.replace("/");
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      }
      if(command == "userInfo"){
          this.$router.replace("/admininfo");
      }
    },
  },
  mounted() {
    console.log("==============================");
  },
};
</script>
<style scoped>
.HomeHeader {
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
}

.HomeHeader .title {
  font-size: 30px;
  font-family: 华文楷体;
  color: white;
}

.HomeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link img {
  widows: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文楷体;
  color: #409eff;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}
</style>
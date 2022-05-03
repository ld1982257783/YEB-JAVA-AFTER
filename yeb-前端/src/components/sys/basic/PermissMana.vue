<!--  -->
<template>
  <div>
    <div class="permissionManaTool">
      <el-input placeholder="请输入角色英文名" v-model="role.name" size="small">
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input
        v-model="role.nameZh"
        placeholder="请输入角色中文名"
        size="small"
        @keydown.enter.native="doAddRole"
      ></el-input>
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        style="margin-left: 6px"
        @click="doAddRole"
        >添加角色</el-button
      >
    </div>
    <div class="permissionManaMain">
      <el-collapse v-model="activeName" accordion @change="change">
        <el-collapse-item
          :title="r.nameZh"
          :name="r.id"
          v-for="(r, index) in roles"
          :key="index"
        >
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>可访问资源</span>
              <el-button
                style="float: right; padding: 3px 0; color: #ff0000"
                type="text"
                icon="el-icon-delete"
                @click="doDeleteRole(r)"
              ></el-button>
            </div>
            <div>
              <!-- 属性空间展示菜单权限 -->
              <el-tree
                :data="allMenus"
                :props="defaultProps"
                show-checkbox
                :default-checked-keys="selectedMenus"
                node-key="id"
                :key="index"
                ref="tree"
              ></el-tree>
              <div style="display: flex; justify-content: flex-end">
                <el-button size="mini">取消修改</el-button>
                <el-button
                  type="primary"
                  size="mini"
                  @click="doUpdate(r.id, index)"
                  >确认修改</el-button
                >
              </div>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {deleteRequest} from '../../../utils/api'

export default {
  name: "PermissMana",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      role: {
        name: "",
        nameZh: "",
      },
      roles: [],
      allMenus: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      selectedMenus: [],
      activeName: -1,
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    //删除角色 
    doDeleteRole(role){
      console.log(deleteRequest)
        this.$confirm(
        "此操作将永久删除[" + role.nameZh + "]角色, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
            deleteRequest('/system/basic/permiss/role/'+role.id).then((resp)=>{
              if(resp){
                this.initRoles();
              }
            },(err)=>{

            })
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },


    //添加角色
    doAddRole() {
      if (this.role.name && this.role.nameZh) {
        this.postRequest("/system/basic/permiss/role", this.role).then(
          (resp) => {
            if (resp) {
              //修改完毕 初始化 角色  
              this.initRoles();
              this.role.name = "";
              this.role.nameZh = "";
            }
          },
          (err) => {}
        );
      } else {
        this.$message.error("所有字段不能为空");
      }
    },

    doUpdate(rid, index) {
      let tree = this.$refs.tree[index];
      //getCheckedKeys tree的方法 属性 获取所有选中节点的id 组成一个数组  加上true 只会打印最低层叶子节点的id
      let SelectedKeys = tree.getCheckedKeys(true);
      //     console.log(SelectedKeys)
      let url = "/system/basic/permiss/?rid=" + rid;
      SelectedKeys.forEach((key) => {
        url += "&mids=" + key;
      });
      //   //拼接完毕 发送请求
      this.putRequest(url).then(
        (resp) => {
          if (resp) {
            //关闭 折叠框
            this.activeName = -1;
          }
        },
        (err) => {}
      );
    },

    change(rid) {
      if (rid) {
        this.initAllMenus();
        this.initSelectedMenus(rid);
      }
    },

    //初始化 用户 所拥有的菜单权限
    initSelectedMenus(rid) {
      this.getRequest("/system/basic/permiss/mid/" + rid).then(
        (resp) => {
          if (resp) {
            this.selectedMenus = resp;
          }
        },
        (err) => {}
      );
    },

    //初始化所有菜单
    initAllMenus() {
      this.getRequest("/system/basic/permiss/menus").then(
        (resp) => {
          if (resp) {
            this.allMenus = resp;
          }
        },
        (err) => {}
      );
    },

    //初始化 所有角色
    initRoles() {
      this.getRequest("/system/basic/permiss/").then(
        (resp) => {
          if (resp) {
            this.roles = resp;
          }
        },
        (err) => {}
      );
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  mounted() {
    this.initRoles();
  },
};
</script>
<style scoped>
.permissionManaTool {
  display: flex;
  justify-content: flex-start;
}

.permissionManaTool .el-input {
  width: 300px;
  margin-left: 6px;
}

.permissionManaMain {
  margin-top: 10px;
  margin-left: 10px;
  width: 710px;
}
</style>
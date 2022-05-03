<!--  -->
<template>
  <div>
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <el-input
        placeholder="通过用户名搜索用户..."
        prefix-icon="el-icon-search"
        style="width: 400px; margin-right: 10px"
        v-model="keywords"
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="doSearch"
        >搜索</el-button
      >
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin, index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
          <el-button
            style="float: right; padding: 3px 0; color: red"
            type="text"
            icon="el-icon-delete"
            @click="deleteAdmin(admin)"
          ></el-button>
        </div>
        <div>
          <div class="img-container">
            <img
              :src="admin.userFace"
              :alt="admin.name"
              :title="admin.name"
              class="userFace-img"
            />
          </div>
          <div class="userInfo-container">
            <div>用户名:{{ admin.name }}</div>
            <div>手机号码:{{ admin.phone }}</div>
            <div>电话号码:{{ admin.telephone }}</div>
            <div>地址:{{ admin.address }}</div>
            <div>
              用户状态:
              <el-switch
                v-model="admin.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="启用"
                inactive-text="禁用"
                @change="enabledChange(admin)"
              >
              </el-switch>
            </div>
          </div>
          <div>
            用户角色
            <el-tag
              style="margin-right: 4px"
              type="success"
              v-for="(role, indexj) in admin.roles"
              :key="indexj"
              >{{ role.nameZh }}</el-tag
            >
            <el-popover
              placement="right"
              title="角色列表"
              width="200"
              @show="showPop(admin)"
              @hide="hidePop(admin)"
              trigger="click"
            >
              <el-select v-model="selectedRoles" multiple placeholder="请选择">
                <el-option
                  v-for="(r, index) in allRoles"
                  :key="index"
                  :label="r.nameZh"
                  :value="r.id"
                >
                </el-option>
              </el-select>
              <el-button
                slot="reference"
                type="text"
                icon="el-icon-more"
              ></el-button>
            </el-popover>
          </div>
          <div>备注:{{ admin.remark }}</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { deleteRequest } from "../../utils/api";

export default {
  name: "SysAdmin",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      admins: [],
      keywords: "",
      allRoles: [],
      selectedRoles: [],
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    hidePop(admin) {
      let roles = [];
      Object.assign(roles,admin.roles);
      let flag = false;
      if (roles.length != this.selectedRoles.length) {
        flag = true;
      } else {
        for (let i = 0; i < roles.length; i++) {
          let role = roles[i];
          for (let j = 0; j < this.selectedRoles.length; j++) {
            let sr = this.selectedRoles[j];
            if (role.id == sr) {
              roles.splice(i, 1);
              i--;
              break;
            }
          }
        }
        if (roles.length != 0) {
          flag = true;
        }
      }

      if (flag) {
        let url = "/system/admin/role?adminId=" + admin.id;
        this.selectedRoles.forEach((sr) => {
          url += "&rids=" + sr;
        });

        this.putRequest(url).then(
          (resp) => {
            if (resp) {
              //重新初始化
              this.initAdmins();
            }
          },
          (err) => {}
        );
      }
    },

    showPop(admin) {
      this.initAllRoles();
      let roles = admin.roles;
      this.selectedRoles = [];
      roles.forEach((r) => {
        this.selectedRoles.push(r.id);
      });
    },

    initAllRoles() {
      this.getRequest("/system/admin/roles").then(
        (resp) => {
          if (resp) {
            this.allRoles = resp;
          }
        },
        (err) => {}
      );
    },

    enabledChange(admin) {
      console.log(admin);
      this.putRequest("/system/admin/", admin).then(
        (resp) => {
          if (resp) {
            this.initAdmins();
          }
        },
        (err) => {}
      );
    },

    deleteAdmin(admin) {
      this.$confirm(
        "此操作将永久删除[" + admin.name + "]操作员, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          deleteRequest("/system/admin/" + admin.id).then(
            (resp) => {
              if (resp) {
                this.initAdmins();
              }
            },
            (err) => {}
          );
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    doSearch() {
      console.log(this.keywords);
      this.initAdmins();
    },

    initAdmins() {
      //获取所有操作员
      this.getRequest("/system/admin/?keywords=" + this.keywords).then(
        (resp) => {
          if (resp) {
            this.admins = resp;
          }
        },
        (err) => {}
      );
    },
  },
  mounted() {
    this.initAdmins();
  },
};
</script>
<style scoped>
.admin-container {
  display: flex;
  margin-top: 10px;
  justify-content: space-around;
  flex-wrap: wrap;
}

.admin-card {
  width: 350px;
  margin-bottom: 20px;
}

.userFace-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userInfo-container {
  font-size: 12px;
  color: #409eff;
}
</style>
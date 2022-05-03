<!--  -->
<template>
  <div>
    <div>
      <el-input
        size="small"
        v-model="jl.name"
        placeholder="添加职称等级..."
        prefix-icon="el-icon-plus"
        style="width: 300px"
      ></el-input>

      <el-select
        size="small"
        v-model="jl.titleLevel"
        placeholder="职称等级"
        style="margin-left: 6px; width: 100px"
      >
        <el-option
          v-for="item in titleLevels"
          :key="item"
          :label="item"
          :value="item"
        >
        </el-option>
      </el-select>
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        style="margin-left: 8px"
        @click="addJobLevel"
        >添加</el-button
      >
    </div>
    <div style="margin-top: 10px">
      <el-table
        :data="jls"
        stripe
        border
        style="width: 70%"
        size="small"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="编号" width="55"> </el-table-column>
        <el-table-column prop="name" label="职称名称" width="150">
        </el-table-column>
        <el-table-column prop="titleLevel" label="职称等级" width="150">
        </el-table-column>
        <el-table-column prop="createDate" label="初级日期" width="150">
        </el-table-column>
        <el-table-column prop="enabled" label="是否启用" width="150">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-button size="small" @click="showEditView(scope.row)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
              @click="deleteHandler(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-button
        type="danger"
        size="small"
        style="margin-top: 10px"
        :disabled="this.multipleSelection.length == 0"
        @click="deleteMany"
        >批量删除</el-button
      >

      <el-dialog title="编辑职称" :visible.sync="dialogVisible" width="30%">
        <table>
          <tr>
            <td><el-tag>职称名称</el-tag></td>
            <td>
              <el-input
                v-model="updateJl.name"
                size="small"
                style="margin-left: 6px; width: 200px"
              ></el-input>
            </td>
          </tr>
          <tr>
            <td><el-tag>职称等级</el-tag></td>
            <td>
              <el-select
                size="small"
                v-model="updateJl.titleLevel"
                placeholder="职称等级"
                style="margin-left: 6px; width: 200px"
              >
                <el-option
                  v-for="item in titleLevels"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>是否启用</el-tag>
            </td>
            <td>
              <el-switch
                style="margin-left: 6px"
                v-model="updateJl.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="已启用"
                inactive-text="未启用"
              >
              </el-switch>
            </td>
          </tr>
        </table>

        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" size="small"
            >取 消</el-button
          >
          <el-button size="small" type="primary" @click="doUpdate"
            >确 定</el-button
          >
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  name: "JobLevelMana",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      jl: {
        name: "",
        titleLevel: "",
      },
      titleLevels: ["正高级", "副高级", "中级", "初级", "员级"],
      jls: [],
      dialogVisible: false,
      updateJl: {
        name: "",
        titleLevel: "",
        enabled: "",
      },
      multipleSelection: [],
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    deleteMany() {
         this.$confirm(
        "此操作将永久删除[" + this.multipleSelection.length + "]条数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          let ids = '?';
          this.multipleSelection.forEach((item)=>{
            ids += 'ids='+item.id+'&';
          })
          this.deletetRequest("/system/basic/joblevel/" + ids).then(
            (resp) => {
              if (resp) {
                this.initJls();
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

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    doUpdate() {
      //发送请求给后盾 进行 修改
      this.putRequest("/system/basic/joblevel/", this.updateJl).then(
        (resp) => {
          if (resp) {
            //刷新数据
            this.initJls();
            //关闭对话框
            this.dialogVisible = false;
          }
        },
        (err) => {}
      );
    },

    //弹出框 弹出事件
    showEditView(data) {
      Object.assign(this.updateJl, data);
      this.updateJl.createDate = "";
      this.dialogVisible = true;
    },

    initJls() {
      this.getRequest("/system/basic/joblevel/").then(
        (resp) => {
          if (resp) {
            this.jls = resp;
            this.jl.name = "";
            this.jl.titleLevel = "";
          }
        },
        (err) => {}
      );
    },

    addJobLevel() {
      if (this.jl.name && this.jl.titleLevel) {
        //发送后端请求添加数据
        this.postRequest("/system/basic/joblevel/", this.jl).then(
          (resp) => {
            if (resp) {
              //如果有返回值  就重新刷新页面
              this.initJls();
            }
          },
          (err) => {}
        );
      } else {
        this.$$message.error("字段不能为空!");
      }
    },

    deleteHandler(data) {
      this.$confirm(
        "此操作将永久删除[" + data.name + "]职称, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.deletetRequest("/system/basic/joblevel/" + data.id).then(
            (resp) => {
              if (resp) {
                this.initJls();
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
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  mounted() {
    this.initJls();
  },
};
</script>
<style scoped>
</style>
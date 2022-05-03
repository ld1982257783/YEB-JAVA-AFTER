<!--  -->
<template>
  <div style="width: 500px">
    <el-input
      placeholder="请输入部门名称进行搜索..."
      v-model="filterText"
      prefix-icon="el-icon-search"
    >
    </el-input>

    <el-tree
      :data="deps"
      :props="defaultProps"
      :filter-node-method="filterNode"
      :expand-on-click-node="false"
      ref="tree"
    >
      <span
        class="custom-tree-node"
        slot-scope="{ node, data }"
        style="display: flex; justify-content: space-between; width: 100%"
      >
        <span>{{ data.name }}</span>
        <span>
          <el-button
            type="primary"
            size="mini"
            class="depBtn"
            @click="() => showAddDep(data)"
          >
            添加部门
          </el-button>
          <el-button
            type="danger"
            size="mini"
            class="depBtn"
            @click="() => deleteDep(node, data)"
          >
            删除部门
          </el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog title="添加部门" :visible.sync="dialogVisible" width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>上级部门</el-tag>
            </td>
            <td>
              <el-input v-model="pname" disabled type="text"></el-input>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>部门名称</el-tag>
            </td>
            <td>
              <el-input
                v-model="dep.name"
                placeholder="请输入部门名称"
              ></el-input>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddDep">添加</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { deleteRequest } from "../../../utils/api";

export default {
  name: "DepMana",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      filterText: "",
      deps: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      dialogVisible: false,
      dep: {
        name: "",
        parentId: -1,
      },
      pname: "",
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  //方法集合
  methods: {
    //删除部门

    initDep() {
      (this.dep = {
        name: "",
        parentId: -1,
      }),
        (this.name = "");
    },

    addDep2Deps(deps, dep) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == dep.parentId) {
          d.children = d.children.concat(dep);
          if(d.children.length > 0){
            d.isParent = true;
          }
          return; 
        } else {
          this.addDep2Deps(d.children, dep);
        }
      }
    },

    doAddDep() {
      this.postRequest("/system/basic/department/", this.dep).then((resp) => {
        if (resp) {
          this.addDep2Deps(this.deps, resp.obj);
          this.dialogVisible = false;
          //给当前deps内部 添加一个dep
        }
      });
    },

    showAddDep(data) {
      //   console.log(data);
      this.dep.parentId = data.id;
      this.pname = data.name;
      this.dialogVisible = true;
    },


  removeDepFromDeps(p,deps,id){
      for(let i=0;i<deps.length;i++){
        let d = deps[i];
        if(d.id == id){
          deps.splice(i,1);
          if(deps.length == 0){
            p.isParent = false;
          }
          return;
        }else{
          this.removeDepFromDeps(d,d.children,id)
        }
      }
  },


    deleteDep(node, data) {
      if (data.isParent) {
       this.$message.error("父部门删除失败")
      } else {
        this.$confirm(
          "此操作将永久删除[" + data.name + "]部门, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            deleteRequest("/system/basic/department/" + data.id).then(
              (resp) => {
                if (resp) {
                  //手动删除某一个
                  this.removeDepFromDeps(null,this.deps, data.id);
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
      }
    },

    //初始化所有部门
    initDeps() {
      this.getRequest("/system/basic/department/").then((resp) => {
        if (resp) {
          this.deps = resp;
        }
      });
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  mounted() {
    this.initDeps();
  },
};
</script>
<style scoped>
.depBtn {
  padding: 2px;
}
</style>
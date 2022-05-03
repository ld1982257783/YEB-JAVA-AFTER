<!--  -->
<template>
  <div>
    <el-input
      size="small"
      class="addPosInput"
      placeholder="添加职位..."
      suffix-icon="el-icon-plus"
      v-model="pos.name"
      @keydown.enter.native="addPostion"
    >
    </el-input>
    <el-button
      size="small"
      icon="el-icon-plus"
      type="primary"
      @click="addPostion"
      >添加</el-button
    >
    <div class="posManaMain">
      <el-table
        :data="positions"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        border
        stripe
        size="small"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="编号" width="55"> </el-table-column>
        <el-table-column prop="name" label="职位" width="120">
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="200">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="showEditView(scope.$index, scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button
      type="danger"
      size="small"
      style="margin-top: 8px"
      :disabled="this.multipleSelection.length == 0"
      @click="deleteMany"
      >批量删除</el-button
    >

    <el-dialog title="编辑职位" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-tag>职位名称 </el-tag>
        <el-input
          v-model="updatePos.name"
          class="updatePosInput"
          size="small"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="doUpdate"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { deleteRequest } from "../../../utils/api";

export default {
  name: "PosMana",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      pos: {
        name: "",
      },
      positions: [],
      dialogVisible: false,
      updatePos: {
        name: "",
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
    //批量删除
    deleteMany() {
      this.$confirm(
        "此操作将永久删除[" + this.multipleSelection.length + "]条职位, 是否继续?",
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
            ids+='ids='+ item.id+"&";
          })
          this.deletetRequest("/system/basic/pos/" + ids).then(
            (resp) => {
              if (resp) {
                this.initPositions();
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

    initPositions() {
      this.getRequest("/system/basic/pos/")
        .then((resp) => {
          if (resp) {
            this.positions = resp;
          }
        })
        .catch((err) => {});
    },

    showEditView(index, data) {
      Object.assign(this.updatePos, data);
      this.updatePos.createDate = "";
      this.dialogVisible = true;
    },

    handleDelete(index, data) {
      this.$confirm(
        "此操作将永久删除[" + data.name + "]职位, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.deletetRequest("/system/basic/pos/" + data.id).then(
            (resp) => {
              if (resp) {
                this.initPositions();
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

    addPostion() {
      if (this.pos.name) {
        this.postRequest("/system/basic/pos/", this.pos).then(
          (resp) => {
            if (resp) {
              this.initPositions();
              this.pos.name = "";
            }
          },
          (err) => {}
        );
      } else {
        this.$message.error("职称名称不能为空!");
      }
    },

    doUpdate() {
      this.putRequest("/system/basic/pos/", this.updatePos).then((resp) => {
        if (resp) {
          this.initPositions();
          this.dialogVisible = false;
        }
      });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val);
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  mounted() {
    this.initPositions();
  },
};
</script>
<style scoped>
.addPosInput {
  width: 300px;
  margin-right: 8px;
}

.posManaMain {
  margin-top: 10px;
}

.updatePosInput {
  width: 200px;
  margin-left: 10px;
}
</style>
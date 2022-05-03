<!--  -->
<template>
  <div>
    <div>
      <el-table :data="emps" border stripe>
        <el-table-column
          type="selection"
          width="55"
          align="left"
        ></el-table-column>

        <el-table-column
          prop="name"
          label="姓名"
          align="left"
          fixed
          width="120"
        ></el-table-column>
        <el-table-column
          prop="workID"
          label="工号"
          align="left"
          fixed
          width="120"
        ></el-table-column>

        <el-table-column
          prop="email"
          label="邮箱地址"
          align="left"
          width="200"
        ></el-table-column>

        <el-table-column
          prop="phone"
          label="电话号码"
          align="left"
          width="120"
        ></el-table-column>

        <el-table-column
          prop="department.name"
          label="所属部门"
          align="left"
          width="120"
        ></el-table-column>

        <el-table-column label="工资账套" align="center">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-tooltip placement="right" v-if="scope.row.salary">
              <div slot="content">
                <table>
                  <tr>
                    <td>基本工资</td>
                    <td>{{ scope.row.salary.basicSalary }}</td>
                  </tr>
                  <tr>
                    <td>交通补助</td>
                    <td>{{ scope.row.salary.trafficSalary }}</td>
                  </tr>
                  <tr>
                    <td>午餐补助</td>
                    <td>{{ scope.row.salary.lunchSalary }}</td>
                  </tr>
                  <tr>
                    <td>奖金</td>
                    <td>{{ scope.row.salary.bonus }}</td>
                  </tr>
                  <tr>
                    <td>养老金比率</td>
                    <td>{{ scope.row.salary.pensionPer }}</td>
                  </tr>
                  <tr>
                    <td>养老金基数</td>
                    <td>{{ scope.row.salary.pensionBase }}</td>
                  </tr>
                  <tr>
                    <td>医疗保险比率</td>
                    <td>{{ scope.row.salary.medicalPer }}</td>
                  </tr>
                  <tr>
                    <td>医疗保险基数</td>
                    <td>{{ scope.row.salary.medicalBase }}</td>
                  </tr>
                  <tr>
                    <td>公积金比例</td>
                    <td>{{ scope.row.salary.accumulationFundPer }}</td>
                  </tr>
                  <tr>
                    <td>公积金基数</td>
                    <td>{{ scope.row.salary.accumulationFundBase }}</td>
                  </tr>
                </table>
              </div>
              <el-tag>{{ scope.row.salary.name }}</el-tag>
            </el-tooltip>
            <el-tag v-else>暂未设置</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-popover
              placement="left"
              title="编辑工资账套"
              width="200"
              @show="showPop(scope.row.salary)"
              @hide="hidePop(scope.row)"
              trigger="click"
            >
              <div>
                <el-select v-model="currentSalary" placeholder="请选择">
                  <el-option
                    v-for="item in salayies"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </div>
              <el-button type="danger" slot="reference">修改工资账套</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 5px">
        <el-pagination
          background
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          :total="total"
          @current-change="currentChange"
          @size-change="sizeChange"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  name: "SalSobCfg",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      emps: [],
      salayies: [],
      currentPage: 1,
      currentSalary: null,
      size: 10,
      total: 0,
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    hidePop(data) {
      console.log(data)
      if (this.currentSalary && this.currentSalary != data.salaryId) {
        this.putRequest(
          "/salary/sobcfg/?eid=" + data.id + "&sid=" + this.currentSalary
        ).then(
          (resp) => {
            if (resp) {
              this.initEmps();
            }
          },
          (err) => {}
        );
      }
    },

    showPop(data) {
      if (data) {
        this.currentSalary = data.id;
      } else {
        this.currentSalary = null;
      }
    },

    //获取所有工资账套
    initSalayies() {
      this.getRequest("/salary/sobcfg/salaries").then(
        (resp) => {
          if (resp) {
            this.salayies = resp;
          }
        },
        (err) => {}
      );
    },

    currentChange(page) {
      this.currentPage = page;
      this.initEmps();
    },

    sizeChange(size) {
      this.size = size;
      this.initEmps();
    },

    initEmps() {
      this.getRequest(
        "/salary/sobcfg/?currentPage=" + this.currentPage + "&size=" + this.size
      ).then(
        (resp) => {
          if (resp) {
            this.emps = resp.data;
            this.total = resp.total;
          }
        },
        (err) => {}
      );
    },
  },
  mounted() {
    this.initEmps();
    this.initSalayies();
  },
};
</script>
<style scoped>
</style>
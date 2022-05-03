<!--  -->
<template>
  <div>
    <div>
      <div style="display: flex; justify-content: space-between">
        <div>
          <el-input
            style="width: 300px; margin-right: 10px"
            prefix-icon="el-icon-search"
            placeholder="请输入员工名进行搜索..."
            v-model="empName"
            clearable
            @clear="initEmps('normal')"
            @keydown.enter.native="initEmps('normal')"
            :disabled="showAdvanceSearchVisable"
          ></el-input>
          <el-button
            type="primary"
            @click="initEmps('normal')"
            :disabled="showAdvanceSearchVisable"
            >搜索</el-button
          >
          <el-button
            type="primary"
            @click="showAdvanceSearchVisable = !showAdvanceSearchVisable"
          >
            <i
              :class="
                showAdvanceSearchVisable
                  ? 'fa fa-angle-double-up'
                  : 'fa fa-angle-double-down'
              "
              aria-hidden="true"
            ></i>
            高级搜索</el-button
          >
        </div>
        <div>
          <el-upload
            style="display: inline-flex; margin-right: 10px"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpdate"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="importDataDisabled"
            action="/employee/basic/import"
          >
            <el-button
              type="success"
              :icon="importDataBtnIcon"
              :disabled="importDataDisabled"
            >
              <!-- <i class="fa fa-level-up" aria-hidden="true"></i> -->
              {{ importDataBtnText }}</el-button
            >
          </el-upload>
          <el-button type="success" @click="exportData" icon="el-icon-download">
            <!-- <i class="fa fa-level-down" aria-hidden="true"></i> -->
            导出数据</el-button
          >
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加员工</el-button
          >
        </div>
      </div>
    </div>
    <transition name="fade">
      <div
        v-show="showAdvanceSearchVisable"
        style="
          border: 1px solid #409eff;
          border-radius: 5px;
          box-sizing: border-box;
          padding: 5px;
          margin: 10px 0px;
        "
      >
        <el-form :model="emp" label-width="80px">
          <el-row>
            <el-col :span="5">
              <el-form-item
                label="政治面貌:"
                prop="politicId"
                label-width="100px"
              >
                <el-select
                  v-model="searchValue.politicId"
                  placeholder="政治面貌"
                >
                  <el-option
                    v-for="item in politicsStatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="民族:" prop="nationId" label-width="124px">
                <el-select
                  v-model="searchValue.nationId"
                  placeholder="民族"
                  style="width: 220px"
                >
                  <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="职位:" prop="jobLevelId" label-width="130px">
                <el-select v-model="searchValue.jobLevelId" placeholder="职位">
                  <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="职称:" prop="posId" label-width="100px">
                <el-select
                  v-model="searchValue.posId"
                  placeholder="职称"
                  style="width: 200px"
                >
                  <el-option
                    v-for="item in joblebels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item
                label="所属部门:"
                prop="departmentId"
                label-width="100px"
              >
                <el-popover
                  placement="bottom"
                  title="请选择部门"
                  width="200"
                  trigger="manual"
                  v-model="visible2"
                >
                  <el-tree
                    :data="allDeps"
                    :props="defaultProps"
                    :default-expand-all="true"
                    @node-click="SearchHandleNodeClick"
                  ></el-tree>

                  <div @click="showDepView2" slot="reference">
                    <el-input
                      v-model="inputDepName"
                      placeholder="请输入所属部门"
                      prefix-icon="el-icon-edit"
                      size="mini"
                      style="width: 165px"
                    ></el-input>
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="入职日期:"
                prop="beginDate"
                label-width="72px"
              >
                <el-date-picker
                  v-model="searchValue.beginDateScope"
                  type="daterange"
                  unlink-panels
                  range-separator="至"
                  value-format="yyyy-MM-dd"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 220px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item
                label="聘用形式:"
                prop="engageForm"
                label-width="80px"
              >
                <el-radio-group
                  v-model="searchValue.engageForm"
                  style="width: 200px"
                >
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="5" style="margin-left: 52px">
              <el-button
                size="mini"
                icon="el-icon-search"
                type="primary"
                @click="initEmps('advanced')"
                >搜索</el-button
              >
              <el-button size="mini" icon="el-icon-circle-close"
                >取消</el-button
              >
            </el-col>
          </el-row>
        </el-form>
      </div>
    </transition>
    <div style="margin-top: 12px">
      <el-table
        :data="emps"
        stripe
        border
        style="width: 100%"
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="name" label="姓名" width="90" align="left" fixed>
        </el-table-column>
        <el-table-column prop="workID" label="工号" width="85" align="left">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="50">
        </el-table-column>
        <el-table-column
          prop="birthday"
          label="出生日期"
          width="95"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="idCard"
          label="身份证号码"
          width="150"
          align="left"
        >
        </el-table-column>
        <el-table-column prop="wedlock" label="婚姻状况" width="70">
        </el-table-column>
        <el-table-column prop="nation.name" label="民族" width="50">
        </el-table-column>
        <el-table-column prop="nativePlace" label="籍贯" width="100">
        </el-table-column>
        <el-table-column
          prop="politicsStatus.name"
          label="政治面貌"
          width="120"
        >
        </el-table-column>
        <el-table-column prop="email" label="电子邮件" width="180" align="left">
        </el-table-column>
        <el-table-column prop="phone" label="电话号码" width="100" align="left">
        </el-table-column>
        <el-table-column
          prop="address"
          label="联系地址"
          width="270"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="department.name"
          label="所属部门"
          width="100"
          align="left"
        >
        </el-table-column>
        <el-table-column prop="joblevel.name" label="职称" width="100">
        </el-table-column>
        <el-table-column prop="position.name" label="职位" width="100">
        </el-table-column>
        <el-table-column
          prop="engageForm"
          label="聘用形式"
          width="100"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="tiptopDegree"
          label="最高学历"
          width="80"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="school"
          label="毕业院校"
          width="150"
          align="left"
        >
        </el-table-column>
        <el-table-column prop="specialty" label="专业" width="150" align="left">
        </el-table-column>
        <el-table-column
          prop="workState"
          label="在职状态"
          width="70"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="beginDate"
          label="入职日期"
          width="95"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="conversionTime"
          label="转正日期"
          width="95"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="beginContract"
          label="合同起始日期"
          width="95"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="endContract"
          label="合同截止日期"
          width="95"
          align="left"
        >
        </el-table-column>
        <el-table-column
          prop="contractTerm"
          label="合同期限"
          width="100"
          align="left"
        >
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-tag type>{{ scope.row.contractTerm }}</el-tag
            >年
          </template>
        </el-table-column>
        <el-table-column
          prop="操作"
          label="合同起始日期"
          width="200"
          align="left"
          fixed="right"
        >
          <!-- eslint-disable-next-line -->
          <template slot-scope="scope">
            <el-button
              style="padding: 3px"
              size="mini"
              @click="showEditEmpView(scope.row)"
              >编辑</el-button
            >
            <el-button style="padding: 3px" type="primary"
              >查看高级资料</el-button
            >
            <el-button
              @click="deleteEmp(scope.row)"
              style="padding: 3px"
              type="danger"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: flex-end; margin-top: 12px">
        <el-pagination
          background
          layout="sizes,prev, pager, next, jumper, ->, total"
          :total="total"
          @current-change="currentChange"
          @size-change="sizeChange"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="55%"
      :lock-scroll="true"
      :before-close="handleClose"
    >
      <div>
        <el-form ref="empForm" :model="emp" label-width="80px" :rules="rules">
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名:" prop="name" label-width="120px">
                <el-input
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                  v-model="emp.name"
                  placeholder="请输入员工姓名"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别:" prop="gender" label-width="120px">
                <el-radio-group v-model="emp.gender" style="width: 200px">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item
                label="出生日期:"
                prop="birthday"
                label-width="120px"
              >
                <el-date-picker
                  v-model="emp.birthday"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="出生日期"
                  style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="政治面貌:"
                prop="politicId"
                label-width="120px"
              >
                <el-select v-model="emp.politicId" placeholder="政治面貌">
                  <el-option
                    v-for="item in politicsStatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="民族:" prop="nationId" label-width="120px">
                <el-select
                  v-model="emp.nationId"
                  placeholder="民族"
                  style="width: 200px"
                >
                  <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="籍贯:"
                prop="nativePlace"
                label-width="120px"
              >
                <el-input
                  v-model="emp.nativePlace"
                  placeholder="请输入籍贯"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 120px"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="电子邮箱:" prop="email" label-width="120px">
                <el-input
                  v-model="emp.email"
                  placeholder="请输入电子邮箱"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="联系地址:"
                prop="address"
                label-width="120px"
              >
                <el-input
                  v-model="emp.address"
                  placeholder="请输入联系地址"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 270px"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="职位:" prop="jobLevelId" label-width="120px">
                <el-select
                  v-model="emp.jobLevelId"
                  placeholder="职位"
                  style="width: 200px"
                >
                  <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职称:" prop="posId" label-width="120px">
                <el-select
                  v-model="emp.posId"
                  placeholder="职称"
                  style="width: 200px"
                >
                  <el-option
                    v-for="item in joblebels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item
                label="所属部门:"
                prop="departmentId"
                label-width="120px"
              >
                <el-popover
                  placement="bottom"
                  title="请选择部门"
                  width="200"
                  trigger="manual"
                  v-model="visible"
                >
                  <el-tree
                    :data="allDeps"
                    :props="defaultProps"
                    :default-expand-all="true"
                    @node-click="handleNodeClick"
                  ></el-tree>

                  <div @click="showDepView" slot="reference">
                    <el-input
                      v-model="inputDepName"
                      placeholder="请输入所属部门"
                      prefix-icon="el-icon-edit"
                      size="mini"
                      style="width: 200px"
                    ></el-input>
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电话号码:" prop="phone" label-width="120px">
                <el-input
                  v-model="emp.phone"
                  placeholder="请输入电话号码"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="工号:" prop="workID" label-width="120px">
                <el-input
                  v-model="emp.workID"
                  placeholder="请输入工号"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                  disabled
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="学历:"
                prop="tiptopDegree"
                label-width="120px"
              >
                <el-select
                  v-model="emp.tiptopDegree"
                  placeholder="学历"
                  style="width: 200px"
                >
                  <el-option
                    v-for="tiptopDegree in tiptopDegrees"
                    :key="tiptopDegree.value"
                    :label="tiptopDegree.label"
                    :value="tiptopDegree.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="毕业院校:" prop="school" label-width="120px">
                <el-input
                  v-model="emp.school"
                  placeholder="请输入毕业院校"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="专业名称:"
                prop="specialty"
                label-width="120px"
              >
                <el-input
                  v-model="emp.specialty"
                  placeholder="请输入专业名称"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item
                label="入职日期:"
                prop="beginDate"
                label-width="120px"
              >
                <el-date-picker
                  v-model="emp.beginDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="入职日期"
                  style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="转正日期:"
                prop="conversionTime"
                label-width="120px"
              >
                <el-date-picker
                  v-model="emp.conversionTime"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="转正日期"
                  style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item
                label="合同起始日期:"
                prop="beginContract"
                label-width="120px"
              >
                <el-date-picker
                  v-model="emp.beginContract"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="合同起始日期"
                  style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="合同终止日期:"
                prop="endContract"
                label-width="120px"
              >
                <el-date-picker
                  v-model="emp.endContract"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="合同终止日期"
                  style="width: 200px"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item
                label="身份证号码:"
                prop="idCard"
                label-width="120px"
              >
                <el-input
                  v-model="emp.idCard"
                  placeholder="请输入身份证号码"
                  prefix-icon="el-icon-edit"
                  size="mini"
                  style="width: 200px"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="聘用形式:"
                prop="engageForm"
                label-width="120px"
              >
                <el-radio-group v-model="emp.engageForm" style="width: 200px">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item
                label="婚姻状况:"
                prop="wedlock"
                label-width="120px"
              >
                <el-radio-group v-model="emp.wedlock" style="width: 300px">
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { deleteRequest } from "../../utils/api";

export default {
  name: "EmpBasic",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      //决定是高级搜索下一页 还是 普通搜索的下一页
      currentSearch: "mormal",
      searchValue: {
        politicId: null,
        nationId: null,
        posId: null,
        jobLevelId: null,
        engageForm: "",
        departmentId: null,
        beginDateScope: null,
      },
      showAdvanceSearchVisable: false,
      refresh: 0,
      headers: {
        Authorization: window.sessionStorage.getItem("tokenStr"),
      },
      importDataDisabled: false,
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      title: "",
      defaultProps: {
        children: "children",
        label: "name",
      },
      allDeps: [],
      visible: false,
      visible2: false,
      emps: [],
      loading: false,
      total: null,
      currentPage: 1,
      size: 10,
      empName: "",
      dialogVisible: false,
      inputDepName: "",
      emp: {
        id: null,
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: null,
        nativePlace: "",
        politicId: null,
        email: "",
        phone: "",
        address: "",
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "在职",
        workID: "",
        contractTerm: null,
        conversionTime: "",
        notWorkDate: null,
        beginContract: "",
        endContract: "",
        workAge: null,
        salaryId: null,
      },
      nations: [],
      joblebels: [],
      politicsStatus: [],
      positions: [],
      tiptopDegrees: [
        {
          value: "博士",
          label: "博士",
        },
        {
          value: "硕士",
          label: "硕士",
        },
        {
          value: "本科",
          label: "本科",
        },
        {
          value: "大专",
          label: "大专",
        },
        {
          value: "高中",
          label: "高中",
        },
        {
          value: "初中",
          label: "初中",
        },
        {
          value: "小学",
          label: "小学",
        },
        {
          value: "其他",
          label: "其他",
        },
      ],
      value: "",
      rules: {
        name: [{ required: true, message: "请输入员工姓名", trigger: "blur" }],
        gender: [
          { required: true, message: "请输入员工性别", trigger: "blur" },
        ],
        birthday: [
          { required: true, message: "请输入员工出生日期", trigger: "blur" },
        ],
        idCard: [
          { required: true, message: "请输入身份证号码", trigger: "blur" },
          {
            pattern:
              /^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: "身份证号码不正确",
            trigger: "blur",
          },
        ],
        wedlock: [
          { required: true, message: "请输入婚姻状况", trigger: "blur" },
        ],
        nationId: [{ required: true, message: "请输入民族", trigger: "blur" }],
        nativePlace: [
          { required: true, message: "请输入籍贯", trigger: "blur" },
        ],
        politicId: [
          { required: true, message: "请输入政治面貌", trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          { type: "email", message: "邮箱地址格式不正确", trigger: "blur" },
        ],
        phone: [{ required: true, message: "请输入手机号码", trigger: "blur" }],
        address: [
          { required: true, message: "请输入员工地址", trigger: "blur" },
        ],
        departmentId: [
          { required: true, message: "请输入部门", trigger: "blur" },
        ],
        jobLevelId: [
          { required: true, message: "请输入职称", trigger: "blur" },
        ],
        posId: [{ required: true, message: "请输入职位", trigger: "blur" }],
        engageForm: [
          { required: true, message: "请输入聘用形式", trigger: "blur" },
        ],
        tiptopDegree: [
          { required: true, message: "请输入学历", trigger: "blur" },
        ],
        specialty: [{ required: true, message: "请输入专业", trigger: "blur" }],
        school: [
          { required: true, message: "请输入毕业院校", trigger: "blur" },
        ],
        beginDate: [
          { required: true, message: "请输入入职日期", trigger: "blur" },
        ],
        workState: [
          { required: true, message: "请输入工作状态", trigger: "blur" },
        ],
        workID: [{ required: true, message: "请输入工号", trigger: "blur" }],
        contractTerm: [
          { required: true, message: "请输入合同期限", trigger: "blur" },
        ],
        conversionTime: [
          { required: true, message: "请输入转正日期", trigger: "blur" },
        ],
        notWorkDate: [
          { required: true, message: "请输入离职日期", trigger: "blur" },
        ],
        beginContract: [
          { required: true, message: "请输入合同起始期限", trigger: "blur" },
        ],
        endContract: [
          { required: true, message: "请输入合同结束期限", trigger: "blur" },
        ],
        workAge: [{ required: true, message: "请输入工龄", trigger: "blur" }],
      },
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    onSuccess() {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      //导入数据完毕 回复导入数据按钮功能
      this.importDataDisabled = false;
      //重新展示数据
      if (this.currentSearch == "normal") {
        this.initEmps("normal");
      } else {
        this.initEmps("advanced");
      }
    },

    onError() {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      //导入数据完毕 回复导入数据按钮功能
      this.importDataDisabled = false;
    },

    beforeUpdate() {
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataBtnText = "正在导入";
      //正在导入过程中 禁用导入数据按钮
      this.importDataDisabled = true;
    },

    //导出数据
    exportData() {
      this.downloadRequest("/employee/basic/export");
    },

    //显示编辑员工
    showEditEmpView(data) {
      this.title = "编辑员工信息";
      this.inputDepName = data.department.name;
      this.emp = data;
      this.initPositions();
      this.dialogVisible = true;
    },

    //删除员工
    deleteEmp(data) {
      this.$confirm(
        "此操作将永久删除[" + data.name + "]员工, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          deleteRequest("/employee/basic/" + data.id).then(
            (resp) => {
              if (resp) {
                if (this.currentSearch == "normal") {
                  this.initEmps("normal");
                } else {
                  this.initEmps("advanced");
                }
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

    //添加员工
    doAddEmp() {
      if (this.emp.id) {
        //存在更新
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.putRequest("/employee/basic/", this.emp).then(
              (resp) => {
                if (resp) {
                  //更新成功
                  this.dialogVisible = false;
                  if (this.currentSearch == "normal") {
                    this.initEmps("normal");
                  } else {
                    this.initEmps("advanced");
                  }
                }
              },
              (err) => {}
            );
          }
        });
      } else {
        //添加
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.postRequest("/employee/basic/", this.emp).then(
              (resp) => {
                if (resp) {
                  //添加成功
                  this.dialogVisible = false;
                  if (this.currentSearch == "normal") {
                    this.initEmps("normal");
                  } else {
                    this.initEmps("advanced");
                  }
                }
              },
              (err) => {}
            );
          }
        });
      }
    },

    handleNodeClick(data) {
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.visible = !this.visible;
    },

    SearchHandleNodeClick(data) {
      this.inputDepName = data.name;
      this.searchValue.departmentId = data.id;
      this.visible2 = !this.visible2;
    },

    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          this.visible = false;
          done();
        })
        .catch((_) => {});
    },

    //获取最大工号
    getMaxWrokId() {
      this.getRequest("/employee/basic/maxworkID").then(
        (resp) => {
          if (resp) {
            this.emp.workID = resp.obj;
          }
        },
        (err) => {}
      );
    },

    //获取所有职位
    initPositions() {
      this.getRequest("/employee/basic/positions").then(
        (resp) => {
          if (resp) {
            this.positions = resp;
          }
        },
        (err) => {}
      );
    },

    showDepView() {
      this.visible = !this.visible;
    },

    showDepView2() {
      this.visible2 = !this.visible2;
    },

    initDate() {
      //获取所有民族
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest("/employee/basic/nations").then(
          (resp) => {
            if (resp) {
              this.nations = resp;
              window.sessionStorage.setItem("nations", JSON.stringify(resp));
            }
          },
          (err) => {}
        );
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      //获取所有职称
      if (!window.sessionStorage.getItem("joblevels")) {
        this.getRequest("/employee/basic/joblevels").then(
          (resp) => {
            if (resp) {
              this.joblebels = resp;
              window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
            }
          },
          (err) => {}
        );
      } else {
        this.joblebels = JSON.parse(window.sessionStorage.getItem("joblevels"));
      }

      //获取所有政治面貌
      if (!window.sessionStorage.getItem("politicsStatus")) {
        this.getRequest("/employee/basic/politicsstatus").then(
          (resp) => {
            if (resp) {
              this.politicsStatus = resp;
              window.sessionStorage.setItem(
                "politicsStatus",
                JSON.stringify(resp)
              );
            }
          },
          (err) => {}
        );
      } else {
        this.politicsStatus = JSON.parse(
          window.sessionStorage.getItem("politicsStatus")
        );
      }

      //获取所有部门
      if (!window.sessionStorage.getItem("allDeps")) {
        this.getRequest("/employee/basic/deps").then(
          (resp) => {
            if (resp) {
              this.allDeps = resp;
              window.sessionStorage.setItem("allDeps", JSON.stringify(resp));
            }
          },
          (err) => {}
        );
      } else {
        this.allDeps = JSON.parse(window.sessionStorage.getItem("allDeps"));
      }
    },

    showAddEmpView() {
      this.title = "添加员工";
      this.emp = {
        id: null,
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: null,
        nativePlace: "",
        politicId: null,
        email: "",
        phone: "",
        address: "",
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "在职",
        workID: "",
        contractTerm: null,
        conversionTime: "",
        notWorkDate: null,
        beginContract: "",
        endContract: "",
        workAge: null,
        salaryId: null,
      };
      this.inputDepName = "";
      this.initPositions();
      this.getMaxWrokId();
      this.dialogVisible = true;
    },

    sizeChange(size) {
      this.size = size;
      if (this.currentSearch == "normal") {
        this.initEmps("normal");
      } else {
        this.initEmps("advanced");
      }
    },

    currentChange(currentPage) {
      this.currentPage = currentPage;
      if (this.currentSearch == "normal") {
        this.initEmps("normal");
      } else {
        this.initEmps("advanced");
      }
    },

    initEmps(type) {
      this.loading = true;
      let url =
        "/employee/basic/?currentPage=" +
        this.currentPage +
        "&size=" +
        this.size;

      if (type && type == "advanced") {
        this.currentSearch = "advanced";
        if (this.searchValue.politicId) {
          url += "&politicId=" + this.searchValue.politicId;
        }
        if (this.searchValue.nationId) {
          url += "&nationId=" + this.searchValue.nationId;
        }
        if (this.searchValue.posId) {
          url += "&posId=" + this.searchValue.posId;
        }
        if (this.searchValue.jobLevelId) {
          url += "&jobLevelId=" + this.searchValue.jobLevelId;
        }
        if (this.searchValue.engageForm) {
          url += "&engageForm=" + this.searchValue.engageForm;
        }
        if (this.searchValue.departmentId) {
          url += "&departmentId=" + this.searchValue.departmentId;
        }
        if (this.searchValue.beginDateScope) {
          url += "&beginDateScope=" + this.searchValue.beginDateScope;
        }
      } else {
        this.currentSearch = "normal";
        url += "&name=" + this.empName;
      }

      this.getRequest(url).then(
        (resp) => {
          this.loading = false;
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
    this.initEmps("normal");
    this.initDate();
    this.initPositions();
  },
  created() {},
};
</script>
<style scoped>
</style>
<!--  -->
<template>
  <div>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="部门管理" name="DepMana"><DepMana></DepMana></el-tab-pane>
      <el-tab-pane label="职位管理" name="PosMana"><PosMana></PosMana></el-tab-pane>
      <el-tab-pane label="职称管理" name="JobLevelMana"><JobLevelMana></JobLevelMana></el-tab-pane>
      <el-tab-pane label="奖惩规则" name="EcMana"><EcMana></EcMana></el-tab-pane>
      <el-tab-pane label="权限组" name="PermissMana"><PermissMana></PermissMana></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

import DepMana from '../../components/sys/basic/DepMana.vue'
import EcMana from '../../components/sys/basic/EcMana.vue'
import JobLevelMana from '../../components/sys/basic/JobLevelMana.vue'
import PermissMana from '../../components/sys/basic/PermissMana.vue'
import PosMana from '../../components/sys/basic/PosMana.vue'


export default {
  name: "SysBasic",
  //import引入的组件需要注入到对象中才能使用
  components: {
    DepMana,
    EcMana,
    JobLevelMana,
    PermissMana,
    PosMana
  },
  data() {
    //这里存放数据
    return {
      activeName: "DepMana",
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    initPositions(){
      
    }
  },
};
</script>
<style scoped>
</style>
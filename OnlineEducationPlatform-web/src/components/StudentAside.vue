<!--学生首页边栏组件-->
<script>
export default {
  name:'TeacherAsider',
  data() {
    return {
      activeMenu: '/student',
    };
  },
  methods:{
    handleMenuSelect(index) {
      this.activeMenu = index;
    }
  }
};
</script>

<template>
  <el-menu router :default-active="activeMenu" @select="handleMenuSelect" class="el-menu-vertical">
    <el-menu-item index="/student" class="aside-bar">课程</el-menu-item>
    <el-menu-item index="/inbox" class="aside-bar">收件箱</el-menu-item>
    <el-menu-item index="3" class="aside-bar">笔记</el-menu-item>
  </el-menu>

</template>

<style scoped>

.aside-bar{
  font-size: 20px;
}
</style>

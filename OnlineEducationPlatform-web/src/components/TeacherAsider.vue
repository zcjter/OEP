<!--老师首页信息侧边栏-->
<script>
export default {
  name:'TeacherAsider',
  data() {
    return {
      activeMenu: '/teacher',
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
      <el-menu-item class="aside-bar">账号管理</el-menu-item>
      <el-menu-item index="/teacher" class="aside-bar">课程</el-menu-item>
      <el-menu-item index="2" class="aside-bar">收件箱</el-menu-item>
    </el-menu>

</template>

<style scoped>

.aside-bar{
  font-size: 20px;
}
</style>

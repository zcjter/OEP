<!--管理员侧边栏组件-->
<script>
export default {
  name:'AdminAside',
  data() {
    return {
      activeMenu:''
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
    <el-menu-item index="/admin-manage-view" class="aside-bar">用户管理</el-menu-item>
    <el-menu-item index="/admin-course-manage-view" class="aside-bar">课程管理</el-menu-item>
    <el-menu-item index="/admin-system-log"  class="aside-bar">日志管理</el-menu-item>
  </el-menu>
</template>

<style scoped>
.aside-bar{
  font-size: 20px;
}
</style>

<!--管理员侧边栏组件-->
<script>
export default {
  name:'AdminAside',
  data() {
    return {
      activeMenu: '/admin-manage-view'
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
  <el-menu 
    router 
    :default-active="activeMenu" 
    @select="handleMenuSelect" 
    class="el-menu-vertical aside-menu"
  >
    <div class="menu-header">
      <div class="menu-header-icon">
        <el-icon><Menu /></el-icon>
      </div>
      <span>管理菜单</span>
    </div>
    <el-menu-item index="/admin-manage-view" class="aside-bar">
      <el-icon><User /></el-icon>
      <span>用户管理</span>
    </el-menu-item>
    <el-menu-item index="/admin-course-manage-view" class="aside-bar">
      <el-icon><Reading /></el-icon>
      <span>课程管理</span>
    </el-menu-item>
    <el-menu-item index="/admin-system-log" class="aside-bar">
      <el-icon><Document /></el-icon>
      <span>日志管理</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { Menu, User, Reading, Document } from '@element-plus/icons-vue'
</script>

<style scoped>
.aside-menu {
  height: 100vh;
  background: linear-gradient(180deg, #1f2937 0%, #111827 100%);
  padding-top: 16px;
}

.menu-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  margin: 0 12px 24px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.menu-header-icon {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.aside-bar {
  margin: 4px 12px;
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 15px;
  transition: all 0.3s ease;
}

.aside-bar:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  color: white !important;
}

.aside-bar.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.aside-bar .el-icon {
  font-size: 18px;
  margin-right: 12px;
}
</style>

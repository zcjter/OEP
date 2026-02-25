<!--管理员之系统管理界面入口-->
<script>
import AdminHeader from "../components/AdminHeader.vue";
import AdminAside from "../components/AdminAside.vue";
import Footer from "../components/Footer.vue";
import AdminSystemLog from  "../components/AdminSystemLog.vue"
export default {
  name:'AdminSystemLogView',
  components:{
    Footer, AdminHeader,AdminAside,AdminSystemLog
  }
};
</script>

<template>
  <div id="admin-dashboard">
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <AdminHeader></AdminHeader>
      </el-header>

      <el-container>
        <!-- 侧边栏 -->
        <el-aside class="sidebar">
          <AdminAside></AdminAside>
        </el-aside>
        <!-- 中间部分 -->
        <el-main class="main">
          <AdminSystemLog></AdminSystemLog>
        </el-main>
      </el-container>
      <!-- 尾部 -->
      <el-footer class="footer" >
        <Footer></Footer>
      </el-footer>

    </el-container>
  </div>
</template>

<style scoped>
#admin-dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  background-color: transparent;
  padding: 0;
}

.sidebar {
  width: 220px;
}

.main {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  min-width: 1000px;
  min-height: calc(100vh - 140px);
}

.footer {
  background-color: #ffffff;
  padding: 16px;
  text-align: center;
  border-top: 1px solid #e4e7ed;
}
</style>

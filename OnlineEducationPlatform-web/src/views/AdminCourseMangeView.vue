<!--管理员之课程管理界面入口-->
<script>
import AdminHeader from "../components/AdminHeader.vue";
import AdminAside from "../components/AdminAside.vue";
import Footer from "../components/Footer.vue";
import AdminCourseManage from "../components/AdminCourseManage.vue";
export default {
  name:'AdminCourseManageView',
  components:{
    Footer, AdminHeader,AdminAside,AdminCourseManage
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
          <AdminCourseManage></AdminCourseManage>
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
.header {
  background-color: #409EFF;
  color: #FFFFFF;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}
.sidebar {
  width: 200px;
}
.main{
  min-width: 1000px;
  min-height: 1000px;
}
.footer {
  background-color: #F0F2F5;
  padding: 10px;
  text-align: center;
  margin-top: 100px
}
</style>

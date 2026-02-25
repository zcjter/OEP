<!--教师端课程首页-->
<template>
  <div class="teacher-dashboard">
    <!-- 头部 -->
    <el-header class="header">
      <TeacherHeader></TeacherHeader>
    </el-header>

    <div class="main-container">
      <!-- 侧边栏 -->
      <el-aside class="sidebar">
        <TeacherAsider></TeacherAsider>
      </el-aside>
      <!-- 中间部分 -->
      <el-main class="main-content">
        <TeacherCourseList></TeacherCourseList>
      </el-main>
    </div>
    <!-- 尾部 -->
    <el-footer class="footer">
      <Footer></Footer>
    </el-footer>
  </div>
</template>
<script>
import TeacherHeader from "../components/TeacherHeader.vue";
import TeacherAsider from "../components/TeacherAsider.vue";
import Footer from "../components/Footer.vue";
import TeacherCourseList from "../components/TeacherCourseList.vue";

  export default {
    name:'TeacherCourseView',
    components:{
      TeacherAsider,TeacherHeader,Footer,TeacherCourseList
    },
  };

</script>

<style scoped>
.teacher-dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  background-color: transparent;
  padding: 0;
}

.main-container {
  display: flex;
  flex: 1;
}

.sidebar {
  width: 220px;
}

.main-content {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  min-width: 1000px;
  min-height: calc(100vh - 140px);
  overflow-y: auto;
}

.footer {
  background-color: #ffffff;
  padding: 16px;
  text-align: center;
  border-top: 1px solid #e4e7ed;
}
</style>

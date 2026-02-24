<!--教师端课程首页-->
<template>
  <div id="teacher-dashboard">
  <el-container>
    <!-- 头部 -->
    <el-header class="header">
      <TeacherHeader></TeacherHeader>
    </el-header>

    <el-container>
    <!-- 侧边栏 -->
    <el-aside class="sidebar">
      <TeacherAsider></TeacherAsider>
    </el-aside>
    <!-- 中间部分 -->
    <el-main class="main">
      <TeacherCourseList></TeacherCourseList>
    </el-main>
    </el-container>
    <!-- 尾部 -->
    <el-footer class="footer" >
      <Footer></Footer>
    </el-footer>

  </el-container>
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

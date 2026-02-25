<!--学生端课程首页-->
<template>
  <div class="student-dashboard">
    <!-- 头部 -->
    <el-header class="header">
      <StudentHeader></StudentHeader>
    </el-header>
    
    <div class="main-container">
      <el-aside class="sidebar">
        <StudentAside></StudentAside>
      </el-aside>

      <el-main class="main-content">
        <StudentCourseList></StudentCourseList>
      </el-main>
    </div>

    <!-- 尾部 -->
    <el-footer class="footer">
      <Footer></Footer>
    </el-footer>
  </div>
</template>

<script>
import StudentCourseList from '../components/StudentCourseList.vue'
import StudentHeader from "../components/StudentHeader.vue";
import StudentAside from "../components/StudentAside.vue";
import Footer from "../components/Footer.vue";

export default {
  name: "StudentDashboard",
  data(){
    return{
      activeTab: '',
    }
  },
  components:{
    StudentCourseList,StudentHeader,StudentAside,Footer
  },
  methods: {
  }
};
</script>

<style scoped>
.student-dashboard {
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

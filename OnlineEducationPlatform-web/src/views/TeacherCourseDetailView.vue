<!--教师端课程详情页面-->
<template>
  <div class="course-detail">
    <!-- Header -->
    <el-header class="header" style="background-color: #409EFF">
      <div class="header-top">
        <div class="header-left">
          <span class="course-portal" >{{ courseName }}</span>
        </div>
      </div>
      <div class="header-bottom">
        <div class="header-center">
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'HomePage' }"
              @click="switchTab('HomePage')"
          >
          <el-link type="info" href="#" class="navbar">首页</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'statistics' }"
              @click="switchTab('statistics')">
            <el-link type="info" href="#"   class="navbar">统计</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'Resource' }"
              @click="switchTab('Resource')"
          >
          <el-link type="info" href="#" class="navbar">资料</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'Notice' }"
              @click="switchTab('Notice')">
            <el-link type="info" href="#"  class="navbar">通知</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'HomeWork' }"
              @click="switchTab('HomeWork')"
          >
          <el-link type="info" href="#"  class="navbar">作业</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'ExamHomePage' }"
              @click="switchTab('ExamHomePage')"
          >
          <el-link type="info" href="#"  class="navbar">考试</el-link>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'manageView' }"
              @click="switchTab('manageView')"
          >
            <el-link type="info" href="#"  class="navbar">管理</el-link>
          </div>
          <el-link type="info" href="#"  @click="returnToTeacherCourseView" class="navbar">个人中心</el-link>
          <span  class="el-user-name">{{user.name}}</span>
        </div>
      </div>
    </el-header>

    <el-main>
      <div v-if="activeTab === 'HomePage'">
        <HomePage></HomePage>
      </div>
      <div v-if="activeTab === 'Resource'">
        <CourseResourceView></CourseResourceView>
      </div>
      <div v-if="activeTab === 'manageView'">
        <CourseManageView></CourseManageView>
      </div>
      <div v-if="activeTab === 'ExamHomePage'">
        <ExamHomePage></ExamHomePage>
      </div>
      <div v-if="activeTab === 'Notice'">
        <NoticeHomePage></NoticeHomePage>
      </div>
      <div v-if="activeTab === 'HomeWork'">
        <TeacherHomeworkHomePage></TeacherHomeworkHomePage>
      </div>
      <div v-if="activeTab === 'statistics'">
        <StaticHomePage></StaticHomePage>
      </div>
    </el-main>

    <!-- Footer Area (Bottom) -->
    <el-footer class="footer"></el-footer>
  </div>
</template>

<script>
import CourseManageView from "./CourseManageView.vue";
import HomePage from "../components/CourseHomePage.vue"
import CourseResourceView from "./CourseResourceView.vue";
import ExamHomePage from "../components/TeacherExamHomePage.vue";
import NoticeHomePage from "../components/NoticeHomePage.vue";
import TeacherHomeworkHomePage from "../components/TeacherTaskHomePage.vue";
import StaticHomePage from "../components/StatisticsHomePage.vue"
export default {
  name: 'CourseDetailView',
  data() {
    return {
      courseName:'',
      activeTab: '', // 当前选中的栏目
      user: {},
    };
  },
  components:{
    CourseManageView,HomePage,CourseResourceView,ExamHomePage,NoticeHomePage,TeacherHomeworkHomePage,
    StaticHomePage
  },
  mounted() {
    this.courseName = localStorage.getItem("courseName");
    this.activeTab = localStorage.getItem("courseActiveTab");
    this.user.name = localStorage.getItem("username");
  },
  methods:{
    switchTab(tabName) {
      this.activeTab = tabName;
      if(tabName==='Resource'){
        localStorage.setItem('resourceActiveTab','questionBank');
      }
    },
    returnToTeacherCourseView(){//点击个人中心页面跳转
      localStorage.removeItem("folderItem");//清除缓存
      localStorage.removeItem("courseActiveTab");
      this.$router.push('/teacher')
    }
  },
};
</script>


<style scoped>
.course-detail {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

.header-left {
  font-size: 18px;
}

.course-portal {
  font-size: 18px;
}

.header-center {
  display: flex;
  gap: 20px;
  justify-content: flex-end; /* Move links to the right */
}

header-top {
  flex: 1;
}

.header-bottom {
  flex: 1;
}
.navbar{
  color: #FFFFFF;
  margin-right: 5px;
  font-size: 20px;
}
.course-portal{
  color: #FFFFFF;
  font-size: 25px;
}
.el-user-name{
  color: #FFFFFF;
  margin-right: 5px;
  font-size: 18px;
}
</style>

<!--老师细节信息展示-->
<script>
export default {
  name: 'DetailHeader',
  data() {
    return {
      courseName:'',
    };
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
  },
  methods: {
    returnToTeacherCourseView(){//单击个人中心页面跳转
      this.$router.push('/teacher');
    },
    returnTeacherCourseDetailView(activeTab){//单击首页、资料、管理页面跳转
      localStorage.setItem("courseActiveTab",activeTab);
      if(activeTab==='Resource'){
        localStorage.setItem('resourceActiveTab','questionBank');
      }
      this.$router.push('/teacher-course-detail');
    },
    returnExamHomePageDetailView(activeTab){//点击返回考试信息界面
      localStorage.setItem("courseActiveTab",activeTab);
      this.$router.push('/teacher-course-detail');
    },
    returnNoticeHomePage(activeTab){//点击返回通知信息界面
      localStorage.setItem("courseActiveTab",activeTab);
      this.$router.push('/teacher-course-detail');
    },
    returnHomeWorkDetailView(activeTab){//点击返回作业信息界面
      localStorage.setItem("courseActiveTab",activeTab);
      this.$router.push('/teacher-course-detail');
    },
    returnStaticsCourseDetailView(activeTab){//点击返回课程统计信息界面
      localStorage.setItem("courseActiveTab",activeTab);
      this.$router.push('/teacher-course-detail');
    }

  },

};
</script>

<template>
  <div class="header-top">
    <span class="course-portal" style="color: #FFFFFF;">{{ courseName }}</span>
  </div>
  <div class="header-bottom">
    <div class="header-center">
      <el-link type="info" href="#"  class="navbar" @click="returnTeacherCourseDetailView('HomePage')">首页</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnStaticsCourseDetailView('statistics')">统计</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnTeacherCourseDetailView('Resource')">资料</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnNoticeHomePage('Notice')">通知</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnHomeWorkDetailView('HomeWork')">作业</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnExamHomePageDetailView('ExamHomePage')">考试</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnTeacherCourseDetailView('manageView')">管理</el-link>
      <el-link type="info" href="#"  class="navbar" @click="returnToTeacherCourseView">个人中心</el-link>
    </div>
  </div>
</template>

<style scoped>
.course-portal {
  font-size: 18px;
}

.header-center {
  display: flex;
  gap: 20px;
  justify-content: flex-end; /* Move links to the right */
}
.header-bottom {
  flex: 1;
}
.navbar{
  color: #FFFFFF;
  margin-right: 5px;
  font-size: 20px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.course-portal{
  color: #FFFFFF;
  font-size: 25px;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>

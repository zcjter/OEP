<!--学生端课程详情页面-->
<template>
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
            :class="{ 'active': activeTab === 'taskManagement' }"
            @click="switchTab('taskManagement')"
        >
          <el-link type="info" href="#" class="navbar">任务</el-link>
        </div>
        <div
            class="sidebar-item"
            :class="{ 'active': activeTab === 'chapterManagement' }"
            @click="switchTab('chapterManagement')"
        >
          <el-link type="info" href="#"   class="navbar">章节</el-link>
        </div>
        <div
            class="sidebar-item"
            :class="{ 'active': activeTab === 'homeworkManagement' }"
            @click="switchTab('homeworkManagement')"
        >
          <el-link type="info" href="#"  class="navbar">作业</el-link>
        </div>
        <div
            class="sidebar-item"
            :class="{ 'active': activeTab === 'examManagement' }"
            @click="switchTab('examManagement')"
        >
          <el-link type="info" href="#"  class="navbar">考试</el-link>
        </div>
        <el-link type="primary" class="navbar" @click="handleBack" >个人中心</el-link >
        <span  class="el-user-name">{{user.name}}</span>
      </div>
    </div>
  </el-header>
  <el-main>
    <el-row>
      <el-col :span="20">
        <div v-if="activeTab === 'chapterManagement'" style="margin-top: 10px">
          <StudentContent></StudentContent>
        </div>
        <div v-if="activeTab === 'examManagement'"  style="margin-top: 10px">
          <StudentExamHomePage></StudentExamHomePage>
        </div>
        <div v-if="activeTab === 'homeworkManagement'" style="margin-top: 10px">
          <StudentTaskHomePage></StudentTaskHomePage>
        </div>
        <!-- 其他内容区域根据需要添加 -->
      </el-col>
    </el-row>
  </el-main>
</template>

<script>
import StudentContent from "../components/StudentContent.vue";
import StudentExamHomePage from "../components/StudentExamHomePage.vue";
import StudentTaskHomePage from "../components/StudentTaskHomePage.vue";

export default {
  name: 'manageView',
  components: {
    StudentContent,StudentExamHomePage,StudentTaskHomePage
  },
  created() {
    this.activeTab = localStorage.getItem("courseActiveTab");
  },
  data() {
    return {
      user: {},
      activeTab: '', // 当前选中的栏目
      courseName: '',
    };
  },
  mounted() {
    this.user.name = localStorage.getItem("username");
    this.courseName = localStorage.getItem("courseName");
  },
  methods: {
    switchTab(tabName) {
      this.activeTab = tabName;
    },
    handleBack(){
      this.$router.push('/student');
    }
  }
};
</script>

<style scoped>
course-detail {
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

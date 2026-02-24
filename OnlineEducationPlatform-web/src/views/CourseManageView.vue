<!--课程信息管理首页-->
<template>
  <div>
    <el-container>
      <el-aside width="200px">
        <el-menu default-active="1" class="el-menu-vertical">
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'classManagement' }"
              @click="toggleCollapse('classManagement')"
          >
            <el-menu-item index="1" class="sidebar-el-menu">
              <i class="el-icon-user"></i>
              <span>班级管理</span>
            </el-menu-item>
            <i class="el-icon-arrow-down" v-if="isCollapsed('teamManagement')"></i>
            <i class="el-icon-arrow-up" v-else></i>
          </div>
          <div v-show="!isCollapsed('classManagement')">
           <PopClassCreate @messageToParent="updateClassList"></PopClassCreate>
            <el-row>
              <el-col :span="24" v-for="classItems in classList" :key="classItems.id">
                <el-menu-item @click="switchTab('classDetails', classItems)" class="courseView">
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ classItems.name }}</el-menu-item>
              </el-col>
            </el-row>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'teamManagement' }"
              @click="switchTab('teamManagement')"
          >
            <el-menu-item index="2" class="sidebar-el-menu">
              <i class="el-icon-user"></i>
              <span>教师团队管理</span>
            </el-menu-item>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'courseManagement' }"
              @click="switchTab('courseManagement')"
          >
            <el-menu-item index="3" class="sidebar-el-menu">
              <i class="el-icon-s-order"></i>
              <span>课程管理</span>
            </el-menu-item>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'downloadManagement' }"
              @click="switchTab('downloadManagement')"
          >
            <el-menu-item index="5" class="sidebar-el-menu">
              <i class="el-icon-download"></i>
              <span>下载中心</span>
            </el-menu-item>
          </div>
          <div
              class="sidebar-item"
              :class="{ 'active': activeTab === 'logManagement' }"
              @click="switchTab('logManagement')"
          >
            <el-menu-item index="6" class="sidebar-el-menu">
              <i class="el-icon-s-promotion"></i>
              <span>操作日志</span>
            </el-menu-item>
          </div>
        </el-menu>
      </el-aside>
      <el-main>
        <div v-if="activeTab === 'teamManagement'">
          <TeacherManagement></TeacherManagement>
        </div>
        <div v-if="activeTab === 'classDetails'">
          <ClassDetail :classItems="selectedClass" ></ClassDetail>
        </div>
        <div v-if="activeTab === 'logManagement'">
          <TeacherOperationLog></TeacherOperationLog>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import TeacherManagement from "../components/TeacherManagement.vue";
import PopClassCreate from "../components/PopClassCreate.vue";
import ClassDetail from "../components/ClassDatail.vue";
import TeacherOperationLog from "../components/TeacherOperationLog.vue";

export default {
  name:'manageView',
  components:{
    ClassDetail,
    PopClassCreate,TeacherManagement,TeacherOperationLog
  },
  data() {
    return {
      activeTab: '', // 当前选中的栏目
      collapsedTabs: [], // 存储折叠的栏目
      classList: [], // 班级列表数据
      className: '',
      courseId: 0,
      dialogVisible: false,
      selectedClass: null, // 当前选中的班级
      // 其他管理模块的数据
    };
  },
  mounted() {
    this.courseId = localStorage.getItem("courseID");
    localStorage.setItem("activeTab",this.activeTab);
    this.loadClassList();
  },
  methods: {
    switchTab(tabName,classItems=null) {//课程管理栏目切换
      this.collapsedTabs.push('classManagement');
      this.activeTab = tabName;
      // 点击班级时，保持侧边栏班级管理展开状态
      if (tabName === 'classDetails') {//进入具体班级的细节
        console.log("classItem",classItems);
        this.selectedClass = classItems;
        this.collapsedTabs = this.collapsedTabs.filter(tab => tab !== 'classManagement');
      }
    },

    loadClassList() {//装载某门课程下的班级列表
      // Implement loading class list logic here
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      this.$axios.post('/class/list',formData).then(response=>{
        if (response.data.code === 20000){
          this.classList = response.data.data
        }
      }).catch(error => {
        console.error('获取课程班级列表错误', error);
      });

    },
    toggleCollapse(tabName) {//处理折叠显示效果相关
      const index = this.collapsedTabs.indexOf(tabName);
      if (index === -1) {
        this.collapsedTabs.push(tabName);
      } else {
        this.collapsedTabs.splice(index, 1);
      }
    },
    isCollapsed(tabName) {//同上
      return this.collapsedTabs.includes(tabName);
    },
    updateClassList(message){//处理CreateClass子组件的消息
      this.loadClassList();
      console.log(message);
    }
  },

};
</script>

<style scoped>
  .sidebar-el-menu{
    font-size: 20px;
  }
  .courseView{
    font-size: 16px;
  }

</style>

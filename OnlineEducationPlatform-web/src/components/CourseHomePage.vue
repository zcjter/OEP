<!--课程首页显示组件,仅显示章节目录信息,用户点击具体章节跳转至课程ChapterContent组件-->
<template>
  <div>
    <!-- 标题和编辑按钮 -->
    <el-row class="title-row">
      <h2 class="title">目录</h2>
      <div class="flex-spacer"></div>
      <el-button type="primary" @click="handleCourseEdit" class="edit-button">编辑</el-button>
    </el-row>

    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane v-for="(classItem, index) in classData" :key="classItem.id" :label="classItem.name" :name="`tab${index + 1}`">
        <!--课程章节目录组件-->
        <CourseChapter :content="classItem"></CourseChapter>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import CourseChapter from './CourseChapter.vue';

export default {
  data() {
    return {
      activeTab: 'tab1', // 默认激活的选项卡
      classData: [], // 存储从后端获取的班级数据
      courseId: '',
      user:{},
    };
  },
  components: {
    CourseChapter
  },
  mounted() {
    this.courseId = localStorage.getItem("courseID");
    this.user.id = localStorage.getItem("userID");
    this.fetchClassList();
  },
  methods: {
    fetchClassList() {//获取该门课程下所有活动班级
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      formData.append("userId",this.user.id);
      this.$axios.post('/class/list', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.classData = response.data.data;
            }
          })
          .catch(error => {
            console.error('获取班级列表错误', error);
          });
    },
    handleCourseEdit(){//跳转到课程编辑页面
      this.$router.push('/course-edit');
    }
  }
};
</script>

<style scoped>
/* 样式 */
.title-row {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
.title {
  font-size: 28px;
  font-weight: bold;
}
.flex-spacer {
  flex-grow: 1;
}
.edit-button{
  font-size: 20px;
}
</style>

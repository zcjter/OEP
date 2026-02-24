<!--资料首页-->
<template>
  <div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="课程资料" name="courseInformation" >
        <CourseSource></CourseSource>
      </el-tab-pane>
      <el-tab-pane label="题库" name="questionBank" >
        <QuestionBank></QuestionBank>
      </el-tab-pane>
      <el-tab-pane label="作业库" name="taskBank">
        <TaskBank></TaskBank>
      </el-tab-pane>
      <el-tab-pane label="试卷库" name="examPaperBank">
        <ExamPaperLibrary></ExamPaperLibrary>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import QuestionBank from '../components/QuestionBank.vue'
import TaskBank from '../components/TaskBank.vue'
import ExamPaperLibrary from '../components/ExamPaperLibrary.vue'
import CourseSource from '../components/CourseSource.vue'
export default {
  data() {
    return {
      activeTab: 'questionBank', // 默认激活的选项卡
      folderItem:{
        id:0,
        name:'选择目录',
      }
    };
  },
  components: {
    QuestionBank,TaskBank,ExamPaperLibrary,CourseSource
  },
  created() {
    const data = JSON.parse(localStorage.getItem("folderItem"));
    console.log("Information created classItems:",data);
    this.activeTab =  localStorage.getItem("resourceActiveTab");
    if(data===null)//防止folderItem.id始终设置为0
      localStorage.setItem("folderItem",JSON.stringify(this.folderItem));
  },
  methods: {

  }
};
</script>

<style scoped>
</style>

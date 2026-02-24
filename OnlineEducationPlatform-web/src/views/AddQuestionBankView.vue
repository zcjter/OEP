<!--添加题库的入口页面-->
<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main class="main">
    <div class="header-links">
        <div class="left-links">
          <!-- 链接题库/创建试题向左移动的内容 -->
          <h2 type="primary" class="title">题库/创建试题</h2>
        </div>

        <div class="right-links">
          <!-- 返回链接向右移动的内容 -->
          <el-link type="primary" @click="handleGoBack" class="right-part">返回</el-link>
        </div>
    </div>
    <div class="additional-links">
      <QuestionBankFolder></QuestionBankFolder>
    </div>
    <div class="question-type">
      <span>题型:</span>
      <!-- 题型按钮 -->
      <el-button type="primary" @click="handleQuestionTypeClick(1)" class="question-button">单选题</el-button>
      <el-button type="primary" @click="handleQuestionTypeClick(2)"  class="question-button">多选题</el-button>
      <el-button type="primary" @click="handleQuestionTypeClick(4)" class="question-button">填空题</el-button>
      <el-button type="primary" @click="handleQuestionTypeClick(8)" class="question-button">判断题</el-button>
      <el-button type="primary" @click="handleQuestionTypeClick(16)" class="question-button">简答题</el-button>
    </div>
    <div v-if="mask&option===1">
      <SingleChoice></SingleChoice>
    </div>
    <div v-if="mask&option===2">
      <EditMultipleChoice></EditMultipleChoice>
    </div>
    <div v-if="mask&option===4">
      <FillinBlack></FillinBlack>
    </div>
    <div v-if="mask&option===8">
      <TrueOrFalse></TrueOrFalse>
    </div>
    <div v-if="mask&option===16">
      <ShortAnswer></ShortAnswer>
    </div>
  </el-main>
</template>

<script>

import SingleChoice from "../components/EditSingleChoice.vue";
import EditMultipleChoice from "../components/EditMultipleChoice.vue";
import TeacherCourseDetailView from "../views/TeacherCourseDetailView.vue";
import FillinBlack from "../components/EditFillinBlack.vue";
import TrueOrFalse from "../components/EditTrueOrFalse.vue";
import ShortAnswer from "../components/EditShortAnswer.vue";
import QuestionBankFolder from "../components/PopQuestionBankFolderSelect.vue"
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";

export default {
  name: 'AddQuestionBank',
  components: {
    TeacherDetailHeader,
    SingleChoice, EditMultipleChoice, TeacherCourseDetailView, FillinBlack, TrueOrFalse, ShortAnswer, QuestionBankFolder
  },
  data() {
    return {
      mask:31,//用做位运算控制题型选择
      option:1,//当前选中题型的值
      courseId: 0,
      courseName:'',
      folderItem: null
    };
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));//获取当前文件夹数据
    this.handleQuestionTypeClick(1);//默认选择单选题
  },
  methods: {
    handleQuestionTypeClick(option) {//选择不同题型
      this.option = option;
    },
    handleGoBack(){//返回按键处理
      this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
      localStorage.setItem("courseActiveTab","Information");
      this.$router.push('/teacher-course-detail');
    },
  },
};
</script>

<style scoped>
.header-links {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
}

.additional-links {
  margin-top: 10px; /* 可根据需要调整间距 */
  padding-bottom: 20px; /* 添加底部填充 */
}

.left-links {
  display: flex;
}

.right-links {
  display: flex;
}


.right-links el-link {
  margin-left: 20px; /* 可根据需要调整间距 */
}



.action-buttons button {
  margin-right: 10px;
  padding: 10px 20px;
  cursor: pointer;
}


header-top {
  flex: 1;

}

.header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #409EFF;
}
.title{
  font-size: 24px;
}
.question-type{
  display: flex;
  margin-top: 40px;
  font-size: 20px;
  margin-bottom: 10px;
  font-weight: bold;
  margin-left: 30px;
}
.question-button{
  background-color: #409EFF;
  font-size: 20px;
  margin-left: 30px;
  margin-bottom: 10px;
}
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
.right-part{
  font-size: 20px;
}

</style>


<!--添加题库的入口页面-->
<template>
  <el-header class="header">
   <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main>
    <div class="header-links">
      <div class="left-links">
        <!-- 链接题库/创建试题向左移动的内容 -->
        <h2 type="primary" class="title">编辑题目</h2>
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
      <span>题型:{{switchQuestionType(this.question.questiontype)}}</span>
    </div>
    <div v-if="question.questiontype===0">
      <SingleChoice :question="question"></SingleChoice>
    </div>
    <div v-if="question.questiontype===1">
      <EditMultipleChoice :question="question"></EditMultipleChoice>
    </div>
    <div v-if="question.questiontype===3">
      <FillinBlack :question="question"></FillinBlack>
    </div>
    <div v-if="question.questiontype===2">
      <TrueOrFalse :question="question"></TrueOrFalse>
    </div>
    <div v-if="question.questiontype===4">
      <ShortAnswer :question="question"></ShortAnswer>
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
    SingleChoice, EditMultipleChoice, TeacherCourseDetailView, FillinBlack, TrueOrFalse, ShortAnswer, QuestionBankFolder,TeacherDetailHeader
  },
  data() {
    return {
      courseId: 0,
      courseName:'',
      folderItem: null,
      question:null
    };
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));//获取当前文件夹数据
    this.handleQuestionTypeClick(1);//默认选择单选题
    this.question = JSON.parse(localStorage.getItem("questionItem"));
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
    switchQuestionType(questionType) {
      switch(questionType) {
        case 0:
          return "单选题";
        case 1:
          return "多选题";
        case 2:
          return "判断题";
        case 3:
          return "填空题";
        case 4:
          return "简答题";
        default:
          return "未知题型";
      }
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

.header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #409EFF;
}
</style>


<!--作业预览组件-->
<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
export default {
  name:'TaskPreView',
  data(){
    return{
      taskItem:null,
      taskData:null,
      isChecked:false,
    }
  },
  components:{
    TeacherDetailHeader
  },
  created() {
    this.taskItem = JSON.parse(localStorage.getItem("taskItem"));
    this.getTaskById();
  },
  methods:{
    getTaskById(){
      let formData = new FormData();
      formData.append("taskId",this.taskItem.id);
      this.$axios.post('/task/selectQuestionByTaskId', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.taskData = response.data.data;
              console.log("查询作业信息成功")
            }
          }).catch(error => {
        console.error('查询作业信息失败', error);
      });
    },
    switchQuestionType(questionType) {
      switch(questionType) {
        case 0:return "单选题";
        case 1:return "多选题";
        case 2:return "判断题";
        case 3:return "填空题";
        case 4:return "简答题";
        default:return "未知题型";
      }
    },
    goBack(){
      localStorage.setItem("courseActiveTab","Resource");
      localStorage.setItem('resourceActiveTab','taskBank');
      this.$router.push('/teacher-course-detail');
    }
  }
};
</script>

<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main class="main">
    <div class="header-links">
      <div class="left-links">
        <!-- 链接题库/创建试题向左移动的内容 -->
        <h4 type="primary" class="title">{{ taskItem.name}}</h4>
      </div>
      <div class="right-links">
        <el-link type="primary" class="right-part" @click="goBack">返回</el-link>
      </div>
    </div>
    <hr class="underline" />
    <div style="float: right">
      <el-checkbox v-model="isChecked">显示答案</el-checkbox>
    </div>
    <div v-for="(item, index) in taskData">
      <div class="question-preview">
        {{index+1}}&nbsp;&nbsp;&nbsp;&nbsp;({{switchQuestionType(item.questiontype)}},{{item.score}}分)
       <span v-html="item.content" class="question-stem"></span>
        <div class="options" v-if="item.questiontype===0||item.questiontype===1">
          <div  class="option">
            <span class="option-label">A:</span> <span v-html="item.option1" class="option-content"></span>
          </div>
          <div class="option">
            <span class="option-label">B:</span> <span v-html="item.option2" class="option-content"></span>
          </div>
          <div class="option">
            <span class="option-label">C:</span> <span v-html="item.option3" class="option-content"></span>
          </div>
          <div class="option">
            <span class="option-label">D:</span> <span v-html="item.option4" class="option-content"></span>
          </div>
        </div>
        <div class="answer" v-if="isChecked">
          <span class="answer-label">正确答案:</span>
          <span class="answer-content" v-html="item.answer"></span>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
.header {
  background-color: #409EFF;
  color: #FFFFFF;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}
.header-links {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
.left-links {
  display: flex;
}
.right-links {
  display: flex;
  justify-content: flex-end;
  margin-right: 20px; /* Adjust the margin as needed */
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 0; /* 如果需要将下划线与上方内容贴近，则去除默认的上边距 */
}
.question-preview {
  text-align: left; /* 将内容左对齐 */
}
.question-stem{
  margin-top: 15px;
}
.option {
  display: flex; /* 使用 Flex 布局 */
  align-items: center; /* 垂直居中选项内容 */
  margin-bottom: 10px; /* 添加选项之间的垂直间距 */
  margin-top: 10px;
}

.option-label {
  flex: 0 0 auto; /* 禁止选项标签自动扩展 */
  margin-right: 5px; /* 右侧间距 */
  line-height: 1; /* 将行高设置为1，以解决可能的对齐问题 */
}

.option-content {
  flex: 1; /* 选项内容自动填充剩余空间 */
  margin-top: 15px;
}

.options {
  display: flex; /* 使用 Flex 布局 */
  flex-direction: column; /* 垂直排列选项 */
}
.answer {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-top: 10px;
  text-align: left; /* 将内容左对齐 */
}

.answer-label {
  flex: 0 0 auto; /* 禁止自动扩展 */
  margin-right: 5px; /* 右侧间距 */
  margin-top: 16px;
}

.answer-content{
  margin-top: 16px;
  margin-left: 10px;
}
.right-part{
  font-size:20px ;
}
</style>

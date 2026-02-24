<!--学生查看批改后作业信息组件-->
<script>
export default {
  name:'TaskPreView',
  data(){
    return{
      taskRecordItem:null,
      taskData:null,
      scoreList:[],
      currentTask:null,
      className:'',
      subjectQuestion:[],
      user:{},
      userItem:null,
      courseId:0,
      count:0
    }
  },
  created() {
    this.taskRecordItem = JSON.parse(localStorage.getItem("taskItem"));
    //修改当前试卷时间字符串格式
    this.taskRecordItem.starttime = this.taskRecordItem.starttime.slice(0, -10); // 去除末尾的 10 个字符
    this.taskRecordItem.endtime = this.taskRecordItem.endtime.slice(0, -10); // 去除末尾的 10 个字符
    this.taskRecordItem.starttime = this.taskRecordItem.starttime.replace("T", " ");// 替换 "T" 为空格
    this.taskRecordItem.endtime = this.taskRecordItem.endtime.replace("T", " ");// 替换 "T" 为空格

    this.user.name = localStorage.getItem("username");
    this.user.id = localStorage.getItem("userID")
    this.courseId = parseInt(localStorage.getItem("courseId"));
    this.getTaskById();
  },
  methods:{
    getTaskById(){//得到学生批改后的试卷信息
      let formData = new FormData();
      formData.append("taskId",this.taskRecordItem.taskid);
      formData.append("studentId",this.user.id);
      this.$axios.post('/task/selectTaskByTaskIdAndStudentId', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.taskData = response.data.data;
              this.count = this.taskData.length;
              this.getQuestionScoreList();
              console.log("查询学生作业信息成功")
            }
          }).catch(error => {
        console.error('查询学生作业信息失败', error);
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
      localStorage.setItem("courseActiveTab","homeworkManagement");
      this.$router.push('/student-course-detailView');
    },
    updateScore(index,questionId) {//在输入框失去焦点时被调用
      const pos = index;
      const newValue = parseInt(this.scoreList[pos]);
      // 更新 scoreList 中对应位置的 score 和 questionId
      const existingItem = this.subjectQuestion.find(item=>item.questionId==questionId);
      if(existingItem){
        // 如果存在相同 id 的记录，则更新该记录的值
        Object.assign(existingItem,{ score: newValue, questionId: questionId })
      }else{
        // 如果不存在相同 id 的记录，则插入新数据
        this.subjectQuestion.push({ score: newValue, questionId: questionId });
      }
    },
    getQuestionScoreList(){
      for(let i=0;i<this.taskData.length;i++){
        this.scoreList[i] = this.taskData[i].sscore
      }
    },
  }
};
</script>

<template>
  <el-main class="main">
    <div class="header-links">
      <div class="left-links">
        <!-- 链接题库/创建试题向左移动的内容 -->
        <h4 type="primary" class="title">{{ taskRecordItem.name}}</h4>
      </div>
      <div class="right-links">
        <el-link type="primary" class="right-part" @click="goBack">返回</el-link>
      </div>
    </div>
    <hr class="underline" />
    <div class="title">
      <span class="title-content">题量:{{count}}</span>
      <span class="title-content">作答时间:{{taskRecordItem.starttime}}至{{taskRecordItem.endtime}}</span>
      <span class="title-content">作答人:{{user.name}}</span>
    </div>
    <div v-for="(item, index) in taskData">
      <div class="question-preview">
        <div class="input-wrapper1">
          <span>打分:</span>
          <!--updateScore()在输入框失去焦点时被调用-->
          <el-input type="text" class="custom-input3" style="color: red;" v-model="scoreList[index]" @blur="updateScore(index,item.id)"></el-input>
        </div>
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
        <div class="answer" >
          <span class="answer-label">正确答案:</span>
          <span class="answer-content" v-html="item.answer"></span>
          <span class="answer-label">{{user.name}}答案:</span>
          <span class="answer-content" v-html="item.sanswer"></span>
        </div>
        <div>
          <div v-if="item.sscore===0">
            <img src="/error~4.ico" style="width: 60px;height: 60px;"/>
          </div>
          <div v-else>
            <img src="/right~4.ico" style="width: 60px;height: 60px;" />
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
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
  margin-left: 40px;
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
  font-weight: bold;
}

.answer-content{
  margin-top: 16px;
  margin-left: 10px;
  font-weight: bold;
}
.right-part{
  font-size:20px ;
}
.custom-input3{
  width:100px;
  margin-left: 20px; /* 调整输入框与前面 span 元素之间的间距 */
  margin-right: 30px;
}
.title{
  display: flex;
}
.title-content{
  font-size: 18px;
  margin-left: 40px;
  margin-bottom: 20px;
}
</style>

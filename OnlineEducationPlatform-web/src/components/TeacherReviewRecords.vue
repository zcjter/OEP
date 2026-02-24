<!--教师批改试卷界面组件-->
<script>

import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
export default {
  data() {
    return {
      examRecordItem:null,
      studentExamQuestion:null,
      currentExamPaper:null,
      className:'',
      examQuestionList:null,
      examQuestionTypes:null,
      subjectQuestion:[],
      currentIndex:0,
      scoreList:[],
      actualScore:'',
      showType:1,       //用来筛选显示的题目类型,0表示所有题目，1表示只显示主观题
      showTypeOptions:[//题目显示类型选择框数据
        { value: 0, label: '显示全部题目' },
        { value: 1, label: '仅显示主观题' }
      ],
      user:{},
      courseId:0
    };
  },
  components:{
    TeacherDetailHeader
  },
  created() {
    this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
    this.className = localStorage.getItem("className");
    this.examRecordItem =  JSON.parse(localStorage.getItem("examRecordItem"));
    this.user.name = localStorage.getItem("username");
    this.user.id = localStorage.getItem("userID");
    this.courseId = localStorage.getItem("courseID");
    this.getAllQuestion();
    this.countAllQuestionType();
  },
  methods:{
    updateScore(index,questionIndex, questionId) {//在输入框失去焦点时被调用
      const pos = this.getTotalQuestionIndex(index,questionIndex)+1;
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
    getAllQuestion(){//查询学生某张试卷的题目信息
      let formData = new FormData();
      formData.append("examPaperId",this.currentExamPaper.paperid);
      formData.append("number",this.examRecordItem.count);
      formData.append("studentId",this.examRecordItem.sid);
      this.$axios.post('/exam/selectByStudentIdAndQuestionId', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestionList = response.data.data;
              this.getQuestionScoreList();//获取主观题分数
              console.log("查询题目信息成功 ");
            }
          }).catch(error => {
        console.error('查询题目信息失败', error);
      });
    },
    getQuestionScoreList(){
      for(let i=0;i<this.examQuestionList.length;i++){
        this.scoreList[i+1] = this.examQuestionList[i].sscore;
      }
    },
    countAllQuestionType(){//查询某种题目类型题目数量
      let formData = new FormData();
      formData.append("examPaperId",this.currentExamPaper.paperid);
      formData.append("number",this.examRecordItem.count);
      this.$axios.post('/exam/countAllByQuestionType', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestionTypes = response.data.data;
              console.log("统计某种题目信息成功")
            }
          }).catch(error => {
        console.error('统计某种信息失败', error);
      });
    },
    getQuestionTypeName(type,index) {
      const orderNo = [
        '一、','二、','三、','四、','五、'
      ];
      switch (type) {
        case 0:return orderNo[index]+'单选题';
        case 1:return orderNo[index]+'多选题';
        case 2:return orderNo[index]+'判断题';
        case 3:return orderNo[index]+'填空题';
        case 4:return orderNo[index]+'简答题';
        default:return orderNo[index]+'未知题型';
      }
    },
    // 根据题型筛选题目
    getQuestionsByType(questionType) {
      // 假设 questionType 是你想要筛选的题目类型
      /*filter 是 JavaScript 中数组的一个方法，用于筛选满足指定条件的元素并返回一个新的数组。*/
      return this.examQuestionList.filter(question => question.questiontype === questionType);
    },
    submitRecordView(){
      let currentTime = this.getCurrentDataTime();
      const data = {//提交数据
        subjectQuestion: this.subjectQuestion,              //答题记录列表
        reviewer: this.user.name, 				                  //批阅者
        studentId: this.examRecordItem.sid,                //学生id
        paperId:this.currentExamPaper.paperid,            //试卷id
        reviewTime:currentTime,                          //批阅时间
        teacherId:this.user.id,                          //用户id
        courseId:this.courseId                           //课程id
      };
      this.$axios.post('/exam/updateReviewRecord', data)
          .then(response => {
            if (response.data.code === 20000) {
              console.log("更新批阅信息成功");
              this.$router.push("/teacher-exam-record-list");
            }
          }).catch(error => {
        console.error('更新批阅信息失败', error);
      });
    },
    getCurrentDataTime(){//获取当前时间
      const currentDate = new Date();
      // 获取当前日期
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始计数，需要加1
      const day = String(currentDate.getDate()).padStart(2, '0');

      // 获取当前时间
      const hour = String(currentDate.getHours()).padStart(2, '0');
      const minute = String(currentDate.getMinutes()).padStart(2, '0');
      const second = String(currentDate.getSeconds()).padStart(2, '0');

      // 格式化日期和时间
      let currentDateTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
      return currentDateTime;
    },
    getTotalQuestionIndex(typeIndex, questionIndex) {// 计算题目的总序号,用于右边题目序号显示
      let totalIndex = 0;
      for (let i = 0; i < typeIndex; i++) {
        totalIndex += this.examQuestionTypes[i].count;
      }
      this.currentIndex = totalIndex + questionIndex;
      return totalIndex + questionIndex;
    },
    goBank(){
      this.$router.push('/teacher-exam-record-list');
    }
  }
};
</script>

<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <div class="header-content">
    <!-- 左侧文本 -->
    <span>{{currentExamPaper.name}} ({{examRecordItem.count}})</span>
    <!-- 右侧返回链接 -->
    <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="goBank" >返回</el-link>
  </div>
  <hr class="underline" />
  <div class="bordered-section">
    <div class="bordered-right">
      <span class="bordered-right-content">姓名:{{examRecordItem.sname}}</span>
      <span class="bordered-right-content">班级:{{className}}</span>
      <span class="bordered-right-content">成绩:{{examRecordItem.score}}</span>
      <el-select v-model="showType" class="bordered-right-content" style="width: 150px;">
        <el-option
            v-for="item in showTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        ></el-option>
      </el-select>
    </div>
  </div>
  <div class="el-exam">
    <!-- 按题型依次显示题目信息 -->
    <div v-for="(item, index) in examQuestionTypes" :key="index"  >
      <!--根据筛选条件显示全部/主观题-->
      <div v-if="showType===0||(item.questiontype===3||item.questiontype===4)">
      <h3 class="title-type">{{ getQuestionTypeName(item.questiontype,index) }}(共{{item.count}}道,{{(item.score)*(item.count)}}分)</h3>
      <div v-for="(question, questionIndex) in getQuestionsByType(item.questiontype)" :key="questionIndex" class="question-content">
        <div class="question-header">
          <span>{{ getTotalQuestionIndex(index, questionIndex) + 1}}. </span><span class="content" v-html="question.content"></span>
          <span>({{question.score}}分)</span>
        </div>
        <!-- 如果是选择题，则显示选项 -->
        <div class="options" v-if="item.questiontype === 0 || item.questiontype === 1">
          <div class="option" >
            <span class="option-label">A:</span> <span v-html="question.option1" class="option-content"></span>
          </div>
          <div class="option" >
            <span class="option-label">B:</span> <span v-html="question.option2" class="option-content"></span>
          </div>
          <div class="option">
            <span class="option-label">C:</span> <span v-html="question.option3" class="option-content"></span>
          </div>
          <div class="option">
            <span class="option-label">D:</span> <span v-html="question.option4" class="option-content"></span>
          </div>
        </div>
        <div class="answer">
          <span class="answer-label">正确答案:</span>
          <span class="answer-content" v-html="question.answer"></span>
          <span class="answer-label">{{examRecordItem.sname}}的答案:</span>
          <span class="answer-content" v-html="question.sanswer"></span>
        </div>
        <div v-if="item.questiontype==0||item.questiontype===1||item.questiontype==2">
            <div v-if="question.sscore===0">
              <img src="/error~4.ico" style="width: 60px;height: 60px;"/>
            </div>
            <div v-else>
              <img src="/right~4.ico" style="width: 60px;height: 60px;" />
            </div>
        </div>
        <div v-if="item.questiontype === 3 || item.questiontype === 4" class="input-wrapper1">
            <span>打分:</span>
            <!--updateScore()在输入框失去焦点时被调用-->
            <el-input v-model="scoreList[getTotalQuestionIndex(index, questionIndex) + 1]" @blur="updateScore(index,questionIndex, question.id)" type="text" class="custom-input3" style="color: red;"></el-input>
        </div>
      </div>
      </div>
      </div>
      <div>
        <el-button type="primary" @click="submitRecordView">保存并进入下一份</el-button>
      </div>
  </div>
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
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 20px;
}

.header-right{
  font-size: 20px;
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 10px; /* 调整下划线与按钮组之间的间距 */
}
.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  margin-left: 80px;
  width:1200px;
  height: 50px;
}
.bordered-right{
  display: flex;
  margin-top: 10px;
}
.bordered-right-content{
  margin-left: 100px;
}
.el-exam{
  float: left;
  margin-top: 20px;
}

.question-header {
  font-weight: bold; /* 加粗题目头部 */
  display: flex;
  text-align: left; /* 将内容左对齐 */
  margin-left: 80px;
}

.options {
  display: flex; /* 设置选项为弹性布局 */
  flex-direction: column; /* 垂直排列选项 */
}
.option {
  display: flex; /* 设置选项为弹性布局 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 10px; /* 控制选项之间的间距 */
  margin-left: 80px;
}

.option-label {
  font-weight: bold; /* 加粗选项标签 */
  margin-right: 5px; /* 控制选项标签与内容之间的间距 */
  margin-bottom: 20px;
}
.title-type{
  display: flex; /* 设置选项为弹性布局 */
  margin-bottom: 20px;
  margin-left: 80px;
}
.answer {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-top: 10px;
  font-weight: bold; /* 加粗 */
  margin-left: 80px;
  text-align: left; /* 将内容左对齐 */
}

.answer-label {
  flex: 0 0 auto; /* 禁止自动扩展 */
  margin-right: 5px; /* 右侧间距 */
  margin-top: 16px;
  margin-left: 10px;
}

.answer-content{
  margin-top: 16px;
  margin-left: 10px;
}
.input-wrapper1{
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-left: 80px;
}
.custom-input3{
  width:100px;
  margin-left: 20px; /* 调整输入框与前面 span 元素之间的间距 */
  margin-right: 30px;
}
.input-wrapper1{
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-left: 80px;
}

</style>

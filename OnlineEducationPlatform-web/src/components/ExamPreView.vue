<!--某份试卷预览-->
<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main class="main">
    <div class="header-links">
      <div class="left-links">
        <!-- 链接题库/创建试题向左移动的内容 -->
        <h2 type="primary" class="title">查看试卷</h2>
      </div>

      <div class="right-links">
        <el-button type="primary" class="right-part1">导出试卷</el-button>
        <el-checkbox class="right-part2">包含答案</el-checkbox>
        <el-link type="primary" class="right-part" @click="goBack">返回</el-link>
      </div>
    </div>
    <hr class="underline" />
    <div class="bordered-section">
      <div class="bordered-right">
        <h2>{{currentExam.name}} ({{currentExamNo}})</h2>
      </div>
      <div>
        <a style="float: left;margin-left: 20px">创建人：{{ currentExam.creator }} | 题量：{{ totalCount }} | 满分：{{ currentExam.score }} 分</a>
        <el-checkbox v-model="isChecked" style="float: right;margin-right: 10px">显示答案</el-checkbox>
      </div>
    </div>
    <div class="el-exam">
      <!-- 按题型依次显示题目信息 -->
      <div v-for="(item, index) in examQuestionTypes" :key="index" >
        <h3 class="title-type">{{ getQuestionTypeName(item.questiontype,index) }}(共{{item.count}}道,{{(item.score)*(item.count)}}分)</h3>
        <div v-for="(question, questionIndex) in getQuestionsByType(item.questiontype)" :key="questionIndex" class="question-content">
          <div class="question-header">
            <span>{{ getTotalQuestionIndex(index, questionIndex) + 1 }}. </span><span class="content" v-html="question.content"></span>
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
          <div class="answer" v-if="isChecked">
            <span class="answer-label">正确答案:</span>
            <span class="answer-content" v-html="question.answer"></span>
          </div>
        </div>
      </div>

    </div>
  </el-main>
</template>

<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";

export default {
  name: 'TestPreView',
  data() {
    return {
      courseId: 0,
      courseName:'',
      currentExamNo:null,
      currentExam:null,
      examQuestionList:null,
      examQuestionTypes:null,
      countExamNum:0,
      isChecked:false,
      examQuestion:null,
      totalCount:0,
      currentIndex:0
    };
  },
  components:{
    TeacherDetailHeader
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
    this.currentExam = localStorage.getItem("currentExam");
    this.currentExamNo =localStorage.getItem("currentExamNo");
    this.currentExam =  JSON.parse(localStorage.getItem("currentExam"))
    this.getAllQuestion();
    this.countAllQuestionType();
    this.selectExamPaperNum();
  },
  methods: {
    getAllQuestion(){//得到所有题目信息
      let formData = new FormData();
      formData.append("examPaperId",this.currentExam.id);
      formData.append("number",this.currentExamNo);
      this.$axios.post('/exam/selectAllQuestion', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestionList = response.data.data;
              this.totalCount = this.examQuestionList.length;
              console.log("查询题目信息成功")
            }
          }).catch(error => {
        console.error('查询题目信息失败', error);
      });
    },
    countAllQuestionType(){//统计每种题型题目信息
      let formData = new FormData();
      formData.append("examPaperId",this.currentExam.id);
      formData.append("number",this.currentExamNo);
      this.$axios.post('/exam/countAllByQuestionType', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestionTypes = response.data.data;
              console.log("countAllQuestionType examQuestionTypes",this.examQuestionTypes);
              console.log("统计某种题目信息成功")
            }
          }).catch(error => {
        console.error('统计某种信息失败', error);
      });
    },
    selectExamPaperNum(){//统计题目试卷信息关联表
      let formData = new FormData();
      formData.append("examPaperId",this.currentExam.id);
      formData.append("number",this.currentExamNo);
      this.$axios.post('/exam/selectAllNumExamPaper', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestion = response.data.data;
              console.log("统计题目关联表信息成功")
            }
          }).catch(error => {
        console.error('统计题目关联表信息失败', error);
      });
    },
    // 根据题型编号获取题型名称
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
    goBack(){//返回上一页
      this.$router.push('/exam-paper-bank');
    },
    getTotalQuestionIndex(typeIndex, questionIndex) {// 计算题目的总序号,用于右边题目序号显示
      let totalIndex = 0;
      for (let i = 0; i < typeIndex; i++) {
        totalIndex += this.examQuestionTypes[i].count;
      }
      this.currentIndex = totalIndex + questionIndex;
      return totalIndex + questionIndex;
    },
  },


};
</script>

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

.right-part {
  font-size: 20px;
  margin-left: 40px;
}
.right-part1{
  margin-right: 10px;
  font-size: 20px;
}
.right-part2 {
  margin-right: 30px; /* Adjust the right margin for the specific element */
  font-size: 16px; /* Adjust the font size for the specific element */
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
.header {
  background-color: #409EFF;
  color: #FFFFFF;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}
.title{
  font-size: 24px;
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 0; /* 如果需要将下划线与上方内容贴近，则去除默认的上边距 */
}
.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  margin-left: 80px;
  display: flex;
  width:1200px;
  height: 100px;
}
.bordered-right{
  display: flex;
}
.el-exam{
  float: left;
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
}

.answer-content{
  margin-top: 16px;
  margin-left: 10px;
}
</style>


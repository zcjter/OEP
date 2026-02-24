<!--学生答题界面-->
<template>
  <div class="notice-container" style="display: flex;">
    <!-- 左侧显示试卷的具体信息 -->
    <div class="left-panel">
      <div class="left-panel-content">
        <span class="left-panel-title">{{currentExamPaper.name}}</span>
      </div>
      <div class="left-panel-content">
        <!--倒计时组件-->
        <ExamCountDown  @messageToParent="autoSubmitExamPaper"></ExamCountDown>
      </div>
      <div class="left-panel-content1">
        <span class="left-panel-contents">姓名:{{user.name}}</span>
      </div>
      <div class="left-panel-content1">
        <span class="left-panel-contents">题量:{{totalCount}}</span>
      </div>
      <div class="left-panel-content1">
        <span class="left-panel-contents">满分:{{currentExamPaper.score}}</span>
      </div>
      <div class="left-panel-content2">
        <span class="left-panel-contents">考试时间:{{currentExamPaper.starttime}}至{{currentExamPaper.endtime}}</span>
      </div>
    </div>

    <!-- 中间显示试卷内容 -->
    <div class="middle-panel">
      <div class="el-exam"><!-- 只显示当前题目 -->
        <div v-if="currentQuestion != null">
          <div class="question-content">
            <div class="question-header">
              <span>{{ currentQuestionIndex + 1 }}. </span><span class="content" v-html="currentQuestion.content"></span>
              <span>({{currentQuestion.score}}分)</span>
            </div>
            <!-- 如果是选择题，则显示选项 -->
            <div class="options" v-if="currentQuestion.questiontype === 0 ">
              <div class="option" >
                <el-radio  v-model="selectedOption" :label="'A'" class="option-label">A:</el-radio > <span v-html="currentQuestion.option1" class="option-content"></span>
              </div>
              <div class="option" >
                <el-radio  v-model="selectedOption" :label="'B'" class="option-label">B:</el-radio > <span v-html="currentQuestion.option2" class="option-content"></span>
              </div>
              <div class="option">
                <el-radio v-model="selectedOption" :label="'C'" class="option-label">C:</el-radio> <span v-html="currentQuestion.option3" class="option-content"></span>
              </div>
              <div class="option">
                <el-radio v-model="selectedOption"  :label="'D'" class="option-label">D:</el-radio > <span v-html="currentQuestion.option4" class="option-content"></span>
              </div>
            </div>
            <div class="options" v-if="currentQuestion.questiontype ===1 ">
              <div class="option" >
                <el-checkbox :label="'A'" v-model="selectOptions" class="option-label">A:</el-checkbox > <span v-html="currentQuestion.option1" class="option-content"></span>
              </div>
              <div class="option" >
                <el-checkbox :label="'B'" v-model="selectOptions" class="option-label">B:</el-checkbox> <span v-html="currentQuestion.option2" class="option-content"></span>
              </div>
              <div class="option">
                <el-checkbox :label="'C'" v-model="selectOptions" class="option-label">C:</el-checkbox> <span v-html="currentQuestion.option3" class="option-content"></span>
              </div>
              <div class="option">
                <el-checkbox :label="'D'" v-model="selectOptions" class="option-label">D:</el-checkbox  > <span v-html="currentQuestion.option4" class="option-content"></span>
              </div>
            </div>
            <div class="option" v-if="currentQuestion.questiontype === 2">
              <el-select v-model="FacilityValue" placeholder="请选择答案" style="width: 150px;">
                <el-option
                    v-for="item in FacilityOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                ></el-option>
              </el-select>
            </div>
            <div class="option" v-if="currentQuestion.questiontype===3">
              <StudentAnswerTinyMCEEditor :initialContent="fillBackContent" @editorContentChange="updateEditorContent('fillBank',$event)"></StudentAnswerTinyMCEEditor>
            </div>
            <div class="option" v-if="currentQuestion.questiontype===4">
              <StudentAnswerTinyMCEEditor :initialContent="shortAnswerContent" @editorContentChange="updateEditorContent('shortAnswer',$event)"></StudentAnswerTinyMCEEditor>
            </div>
          </div>
        </div>
      </div>
      <div style="margin-top: 400px;margin-right: 10px">
        <el-button @click="nextQuestion()" type="primary">下一题</el-button>
      </div>
      <div style="margin-top: 600px;margin-right: 10px">
        <el-button type="primary" @click="submitExamPaper()">提交试卷</el-button>
      </div>
    </div>

    <!-- 右侧显示题目列表 -->
    <div class="right-panel">
      <div v-if="examQuestionTypes!=null" v-for="(item, index) in examQuestionTypes" :key="index" >
        <p class="title-type">{{ getQuestionTypeName(item,index) }}(共{{item.count}}道,{{(item.score)*(item.count)}}分)</p>
        <div class="question-list" style="display: flex; flex-wrap: wrap;">
        <div v-for="(question, questionIndex) in getQuestionsByType(item)" :key="questionIndex">
            <el-button style="font-size: 18px"
                       @click="locateToQuestion(question,getTotalQuestionIndex(index, questionIndex) )"
                       :class="{ 'answered': question.answered, 'unanswered': !question.answered, 'selected': question.selected }">
              {{getTotalQuestionIndex(index, questionIndex) + 1 }}
            </el-button>
        </div>
      </div>
      </div>
    </div>
  </div>
</template>

<script>
import StudentAnswerTinyMCEEditor from '../components/StudentAnswerTinyMCEEditor.vue';
import ExamCountDown from "../components/ExamCountDown.vue";
export default {
  data() {
    return {
      questionList: null,
      examQuestionTypes: null,
      selectedOption: null,
      currentQuestionIndex: 0, // 当前题目的索引
      currentQuestion: null, // 当前显示的题目
      currentTotal: 0,
      selectOptions: [],
      FacilityValue: '',
      FacilityOptions: [
        {value: '正确', label: '正确'},
        {value: '错误', label: '错误'}
      ],
      answerList: [],//答题列表
      answerInfo: {
        questionId: 0,//题目id
        questionType: 0,//题目类型
        answerText: '',//答题内容
        flag: 0,//答题标记，用来标记当前题目有没有答过
      },
      fillBackContent: '',
      shortAnswerContent: '',
      currentIndex: 0,
      totalCount: 0,
      user: {},
      currentExamPaper: null,
      courseId:0,
      classId:0
    };
  },
  components: {
    StudentAnswerTinyMCEEditor, ExamCountDown
  },
  created() {
    //获取本地缓存信息
    this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
    this.user.name = localStorage.getItem("username");
    this.user.id = localStorage.getItem("userID");
    this.courseId  = parseInt(localStorage.getItem("courseID"));
    this.classId = parseInt(localStorage.getItem("classId"));

    //修改当前试卷时间字符串格式
    this.currentExamPaper.starttime = this.currentExamPaper.starttime.slice(0, -10); // 去除末尾的 10 个字符
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.slice(0, -10); // 去除末尾的 10 个字符
    this.currentExamPaper.starttime = this.currentExamPaper.starttime.replace("T", " ");// 替换 "T" 为空格
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.replace("T", " ");// 替换 "T" 为空格


    this.getExamPaper(); // 在组件创建时获取试卷题目列表
    this.countAllByQuestionType();//统计当前试卷各种题目类型题数
  },
  methods: {
    nextQuestion() {//点击下一道按钮动作
      //step1 检查当前题目有没有答案，有则保存
      let answerText = this.getQuestionAnswerByType();
      if (answerText != '') {
        this.saveQuestionAnswer(this.currentQuestion, answerText);
      }

      this.currentQuestionIndex++;
      this.currentQuestionIndex %= this.questionList.length
      this.currentQuestion = this.questionList[this.currentQuestionIndex];
      //step2 更新当前题目的答案信息
      this.updateAnswerInformation(this.currentQuestion);
    },
    getExamPaper() {//学生进入考试随机获取试卷
      let formData = new FormData();
      let currentTime = this.getCurrentDataTime();
      formData.append("paperId", this.currentExamPaper.paperid);
      formData.append("studentId",this.user.id);
      formData.append("studentName",this.user.name);
      formData.append("enterTime",currentTime);
      formData.append("classId",this.currentExamPaper.classid);
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);

      this.$axios.post('/exam/getExamPaper', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.questionList = response.data.data;
              console.log("查询某份试卷信息成功")
              // 初始化显示第一题
              this.currentQuestionIndex = 0;
              this.currentQuestion = this.questionList[0];
            }
          }).catch(error => {
        console.error('查询某份试卷信息失败', error);
      });
    },
    countAllByQuestionType() {//统计当前试卷各种题目类型题数
      let formData = new FormData();
      formData.append("examPaperId", this.currentExamPaper.paperid);
      formData.append("number",1);
      this.$axios.post('/exam/countAllByQuestionType', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.examQuestionTypes = response.data.data;
              this.totalCount = 0;
              for (let i = 0; i < this.examQuestionTypes.length; i++) {
                this.totalCount += this.examQuestionTypes[i].count;
              }
              console.log("获取题目类型数量信息成功")
            }
          }).catch(error => {
        console.error('获取题目类型数量信息失败', error);
      });
    },
    submitExamPaper() {//提交试卷请求
      // 提交前，检查当前题目有没有答案，有则保存
      let answerText = this.getQuestionAnswerByType();
      let currentTime = this.getCurrentDataTime();
      if (answerText != '') {//保存当前页面显示题目答案,避免漏掉
        this.saveQuestionAnswer(this.currentQuestion, answerText);
      }

      const data = {//提交数据
        answerList: this.answerList,            //答题记录列表
        paperId: this.currentExamPaper.paperid, //试卷id
        studentId: this.user.id,                //用户id
        currentTime:currentTime,                //提交时间
        courseId:this.courseId,                 //课程id
        classId:this.classId                    //班级id
      };
      this.$axios.post('/exam/submitExamPaper', data)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('创建考试记录信息成功');
              this.$router.push("/submission-sucessful");
            }
          }).catch(error => {
              console.error('创建考试记录信息失败', error);
      });
    },
    autoSubmitExamPaper(message){//答题时间结束，定时器触发自动交卷
      console.log(message);
      this.submitExamPaper();//提交试卷
    },
    getTotalQuestionIndex(typeIndex, questionIndex) {// 计算题目的总序号,用于右边题目序号显示
      let totalIndex = 0;
      for (let i = 0; i < typeIndex; i++) {
        totalIndex += this.examQuestionTypes[i].count;
      }
      this.currentIndex = totalIndex + questionIndex;
      return totalIndex + questionIndex;
    },
    locateToQuestion(question, questionIndex) {//点击右边题目序号切换到指定题目
      let answer = this.getQuestionAnswerByType();
      if (answer != null) {
        this.saveQuestionAnswer(this.currentQuestion, answer);
      }
      // 点击题目列表按钮时，切换到对应题目
      this.currentQuestionIndex = questionIndex;
      this.currentQuestion = question;
      this.updateAnswerInformation(this.currentQuestion);
    },
    saveQuestionAnswer(question, answerText) {//保存当前题目的答案信息
      let answerArray = this.answerList.filter(answerInfo => answerInfo.questionId === question.id);//根据题目id查找已答题记录
      //此处filter获得的answerInfo是一个数组
      if (answerArray.length === 0) {//如果没有答题记录则新增
        let answerInfo = {
          questionId: question.id,
          questionType: question.questiontype,
          answerText: answerText,
          flag: 1
        };
        this.answerList.push(answerInfo);
      } else {//存在答题记录则更新答案
        answerArray[0].answerText = answerText;
        this.answerList.push(answerArray[0]);
      }
    },
    getQuestionsByType(item) {//根据item的题型信息获取题目列表
      /*filter 是 JavaScript 中数组的一个方法，用于筛选满足指定条件的元素并返回一个新的数组。*/
      return this.questionList.filter(question => question.questiontype === item.questiontype);
    },
    getQuestionAnswerByType() {//根据题目类型获取答题信息
      switch (this.currentQuestion.questiontype) {
        case 0: return this.selectedOption;
        case 1: return this.selectOptions;
        case 2: return this.FacilityValue;
        case 3: return this.fillBackContent;
        case 4: return this.shortAnswerContent;
      }
    },
    setQuestionAnswerByType(answerInfo) {//根据题目类型刷新答题信息,用于以答题的切换
      switch (answerInfo.questionType) {
        case 0: this.selectedOption = answerInfo.answerText;      break;
        case 1: this.selectOptions = answerInfo.answerText;       break;
        case 2: this.FacilityValue = answerInfo.answerText;       break;
        case 3: this.fillBackContent = answerInfo.answerText;     break;
        case 4: this.shortAnswerContent = answerInfo.answerText;  break;
      }
    },
    updateAnswerInformation(question) {//更新当前题目的答题信息,用于不同题目切换
      //step1 清空答案信息
      this.selectedOption = '';
      this.selectOptions = [];
      this.FacilityValue = '';
      this.fillBackContent = '';
      this.shortAnswerContent = '';

      //step2 检查当前题目是否有答题信息
      let answerArray = this.answerList.filter(answerInfo => answerInfo.questionId === question.id);
      if (answerArray.length != 0) {
        this.setQuestionAnswerByType(answerArray[0]);
      }
    },
    getQuestionTypeName(item, index) {//获取试题类型名称
      const orderNo = [
        '一、', '二、', '三、', '四、', '五、'
      ];
      switch (item.questiontype) {
        case 0:   return orderNo[index] + '单选题';
        case 1:   return orderNo[index] + '多选题';
        case 2:   return orderNo[index] + '判断题';
        case 3:   return orderNo[index] + '填空题';
        case 4:   return orderNo[index] + '简答题';
        default:  return '未知题型';
      }
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
    updateEditorContent(type, content) {//处理编辑器内容更新事件消息，用于主观题答案更新
      if (type === 'fillBank') {
        this.fillBackContent = content;
      } else if (type === 'shortAnswer') {
        this.shortAnswerContent = content;
      }
    },

  },
};
</script>

<style scoped>
.notice-container {
  display: flex;
  gap: 20px;
}

.left-panel, .right-panel {
  flex: 0.31;
}

.middle-panel {
  flex: 2;
  flex-grow: 1;
  display: flex;
}

.left-panel, .right-panel {
  overflow: auto;
}

.left-panel, .right-panel {
  border: 1px solid #ccc;
  padding: 10px;
}

.question-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 每行显示3个按钮 */
  gap: 10px; /* 控制按钮之间的间隔 */
}

.question-list el-button {
  width: 100%;
  align-self: flex-start; /* 强制按钮垂直对齐到顶部 */
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
  text-align: left; /* 将内容左对齐 */
}
.title-type{
  display: flex; /* 设置选项为弹性布局 */
  margin-bottom: 20px;
}
.el-exam{
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}
.left-panel-content{
  margin-top: 20px;
}
.left-panel-content1{
  display: flex;
  margin-left: 10px;
  margin-top: 10px;
}
.left-panel-content1{
  display: flex;
}
.left-panel-title{
  font-size: 20px;
  font-weight: bold;
}
.left-panel-contents{
  font-size: 20px;
}
</style>

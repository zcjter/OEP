<!--学生作业答题界面-->
<script>
import StudentAnswerTinyMCEEditor from '../components/StudentAnswerTinyMCEEditor.vue';

export default {
  name:'TaskPreView',
  data(){
    return{
      taskItem:null,
      taskData:null,
      selectedOption: null,
      currentQuestionIndex: 0, // 当前题目的索引
      selectOptions: [],
      FacilityValue: '',
      FacilityOptions: [
        {value: '正确', label: '正确'},
        {value: '错误', label: '错误'}
      ],
      fillBackContent: '',
      shortAnswerContent: '',
      count:0,
      answerList: [],//答题列表
      answerInfo: {
        questionId: 0,//题目id
        questionType: 0,//题目类型
        answerText: '',//答题内容
      },
      user: {},
      courseId:0,
      classId:0
    }
  },
  components:{
    StudentAnswerTinyMCEEditor
  },
  created() {
    this.taskItem = JSON.parse(localStorage.getItem("taskItem"));
    this.user.id = localStorage.getItem("userID");
    this.taskItem.starttime = this.taskItem.starttime.slice(0, -10).replace("T", " ");
    this.taskItem.endtime = this.taskItem.endtime.slice(0, -10).replace("T", " ");
    this.courseId = parseInt(localStorage.getItem("courseID"));
    this.classId = parseInt(localStorage.getItem("classId"));
    this.getTaskById();
  },
  methods:{
    getTaskById(){//获取作业信息
      let formData = new FormData();
      formData.append("taskId",this.taskItem.taskid);
      this.$axios.post('/task/selectQuestionByTaskId', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.taskData = response.data.data;
              this.count = this.taskData.length;
              for(let i=0;i<this.taskData.length;i++){
                let item = {
                  id:this.taskData[i].id,
                  type:this.taskData[i].questiontype,
                  selectOptions:[],
                  answerText:''
                }
                this.answerList.push(item);
              }
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
    submitTask() {//提交作业请求
      // 获取当前时间
      let currentTime = this.getCurrentDataTime();
      const data = {//提交数据
        answerList: this.answerList,            //答题记录列表
        taskId: this.taskItem.taskid,           //作业id
        studentId: this.user.id,                //用户id
        currentTime:currentTime,                //提交时间
        courseId:this.courseId,                 //课程id
        classId:this.classId                   //班级id
      };
      this.$axios.post('/task/submitTask', data)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('保存作业记录信息成功');
              localStorage.setItem("courseActiveTab","homeworkManagement");
              this.$router.push('/student-course-detailView');
            }
          }).catch(error => {
        console.error('保存作业记录信息失败', error);
      });
    },
    updateEditorContent(type, index,content) {//处理编辑器内容更新事件消息，用于主观题答案更新
      if (type === 'fillBank') {
        this.answerList[index].answerText = content;
      } else if (type === 'shortAnswer') {
        this.answerList[index].answerText  = content;
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
  }
};
</script>

<template>
  <el-main class="main">
    <div>
      <h4 class="title">{{taskItem.name}}</h4>
    </div>
    <div class="title-content">
      <span class="content">题量:{{count}}</span>
      <span class="content">满分:{{taskItem.score}}</span>
      <span class="content">做答时间:{{taskItem.starttime}}至{{taskItem.endtime}}</span>
    </div>
    <div v-for="(item, index) in taskData">
      <div class="question-preview">
        {{index+1}}&nbsp;&nbsp;&nbsp;&nbsp;({{switchQuestionType(item.questiontype)}},{{item.score}}分)
        <span v-html="item.content" class="question-stem"></span>
        <!-- 如果是选择题，则显示选项 -->
        <div class="options" v-if="item.questiontype === 0 ">
          <div class="option" >
            <el-radio  v-model="answerList[index].answerText" :label="'A'" class="option-label">A:</el-radio > <span v-html="item.option1" class="option-content"></span>
          </div>
          <div class="option" >
            <el-radio  v-model="answerList[index].answerText" :label="'B'" class="option-label">B:</el-radio > <span v-html="item.option2" class="option-content"></span>
          </div>
          <div class="option">
            <el-radio v-model="answerList[index].answerText" :label="'C'" class="option-label">C:</el-radio> <span v-html="item.option3" class="option-content"></span>
          </div>
          <div class="option">
            <el-radio v-model="answerList[index].answerText"  :label="'D'" class="option-label">D:</el-radio > <span v-html="item.option4" class="option-content"></span>
          </div>
        </div>
        <div class="options" v-if="item.questiontype ===1 ">
          <div class="option" >
            <el-checkbox :label="'A'" v-model="answerList[index].selectOptions" class="option-label">A:</el-checkbox > <span v-html="item.option1" class="option-content"></span>
          </div>
          <div class="option" >
            <el-checkbox :label="'B'" v-model="answerList[index].selectOptions" class="option-label">B:</el-checkbox> <span v-html="item.option2" class="option-content"></span>
          </div>
          <div class="option">
            <el-checkbox :label="'C'" v-model="answerList[index].selectOptions" class="option-label">C:</el-checkbox> <span v-html="item.option3" class="option-content"></span>
          </div>
          <div class="option">
            <el-checkbox :label="'D'" v-model="answerList[index].selectOptions" class="option-label">D:</el-checkbox  > <span v-html="item.option4" class="option-content"></span>
          </div>
        </div>
        <div class="option" v-if="item.questiontype === 2">
          <el-select v-model="answerList[index].answerText" placeholder="请选择答案" style="width: 150px;">
            <el-option
                v-for="item in FacilityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            ></el-option>
          </el-select>
        </div>
        <div class="option" v-if="item.questiontype===3">
          <StudentAnswerTinyMCEEditor :initialContent="fillBackContent" @editorContentChange="updateEditorContent('fillBank',index,$event)"></StudentAnswerTinyMCEEditor>
        </div>
        <div class="option" v-if="item.questiontype===4">
          <StudentAnswerTinyMCEEditor :initialContent="shortAnswerContent" @editorContentChange="updateEditorContent('shortAnswer',index,$event)"></StudentAnswerTinyMCEEditor>
        </div>
      </div>
    </div>
    <div style="margin-top: 100px;margin-right: 10px">
      <el-button type="primary" @click="submitTask">提交作业</el-button>
    </div>
  </el-main>
</template>

<style scoped>
.title{
  font-weight: bold;
  display: flex;
  margin-left: 40px;
}
.title-content{
  display: flex;
  margin-top: 40px;
}
.content{
  margin-left: 40px;
  font-size: 18px;
}
.question-preview {
  text-align: left; /* 将内容左对齐 */
  margin-left: 40px;
  margin-top: 40px;
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

</style>

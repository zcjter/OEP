<!--学生考试说明界面-->
<script>
import StudentEnterExamPrompt from "./PopStudentExamPrompt.vue";
export default {
  data() {
    return {
      examInfoLines: [
        "考试说明：",
        "1、离开或退出考试界面答题计时不停止，请不要中途离开考试界面",
        "2、保持座位前的桌面干净，不要有与考试无关的内容。",
        "3、考试时间截止或答题时间结束，如果处于答题页面，将自动提交试卷。",
        "4、考试过程中如果出现页面卡死、题目空白情况，请尝试切换网络或退出重新进入考试。"
      ],
      currentExamPaper:null,
      examDuration:'',
      isChecked:false
    };
  },
  components:{
    StudentEnterExamPrompt
  },
  created() {
    this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
    // 截取字符串，去除末尾的 .000+00:00 部分
    this.currentExamPaper.starttime =this.currentExamPaper.starttime.slice(0, -10); // 去除末尾的 10 个字符
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.slice(0, -10); // 去除末尾的 10 个字符
    this.currentExamPaper.starttime = this.currentExamPaper.starttime.replace("T"," ");// 替换 "T" 为空格
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.replace("T"," ");// 替换 "T" 为空格
    this.getTime();
  },
  methods:{
    getTime(){
      let startTime = new Date(this.currentExamPaper.starttime);
      let endTime = new Date(this.currentExamPaper.endtime);
      // 计算时间间隔（毫秒）
      var timeDiffInMilliseconds = Math.abs(endTime-startTime);
      var timeDiffInMinute = Math.floor(timeDiffInMilliseconds / (1000*60));
      var timeDiffInHours = Math.floor(timeDiffInMinute / 60);
      timeDiffInMinute = Math.floor(timeDiffInMinute % 60);
      this.examDuration = timeDiffInHours+'小时'+timeDiffInMinute+'分钟';
    },
  },
};
</script>

<template>
  <div class="bordered-section" >
    <div  class="info-line">
      <span>考试名称:{{currentExamPaper.name}}</span>
    </div>
    <div class="info-line">
      <span>考试时长:{{examDuration}}</span>
    </div>
    <div class="info-line">
      <span>考试时间:{{currentExamPaper.starttime}}至{{currentExamPaper.endtime}}</span>
    </div>

  </div>
  <div  class="exam-info">
    <p v-for="(line, index) in examInfoLines" :key="index">{{ line }}</p>
  </div>
  <div class="info">
  <el-checkbox class="info-checkbox" v-model="isChecked" ><span style="font-size: 20px">我已阅读并同意</span></el-checkbox>
  <StudentEnterExamPrompt :examDuration="examDuration" v-if="isChecked"></StudentEnterExamPrompt>
</div>
</template>

<style>
.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  width:800px;
  height: 140px;
  margin-left: 80px;
}
.info-line {
  display: flex;
  margin-top: 10px;
  font-size: 18px;
}
.info{
  margin-right: 10px;
}
.info-checkbox{
  float: left;
  margin-left: 80px;
  margin-top: 20px;
}

.exam-info {
  display: flex;
  flex-direction: column;
  text-align: justify;
  margin-top: 10px;
  margin-left: 80px;
}
.exam-info p {
  margin: 0; /* Remove default margin */
  font-size: 18px;
}
</style>

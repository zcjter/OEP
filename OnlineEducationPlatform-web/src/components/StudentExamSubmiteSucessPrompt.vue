<!--学生成功提交考试界面-->
<script>
  export default {
    data(){
      return{
        examRecord:null,
        currentExamPaper:null,
        user:{},
        examDuration:'',
      }
    },
    created() {
      this.user.id = localStorage.getItem("userID");
      this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
      this.getExamPaperRecord();
    },
    methods:{
      getExamPaperRecord(){//计算学生提交时间
        let formData = new FormData();
        formData.append("studentId",this.user.id);
        formData.append("paperId",this.currentExamPaper.paperid);
        this.$axios.post('/exam/submitSucess', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.examRecord = response.data.data;
                console.log("查询考试信息成功");
                // 对获取的时间进行处理
                if (this.examRecord && this.examRecord.starttime && this.examRecord.endtime) {
                  this.examRecord.starttime = this.examRecord.starttime.slice(0, -10).replace("T", " ");
                  this.examRecord.endtime = this.examRecord.endtime.slice(0, -10).replace("T", " ");

                  let startTime = new Date(this.examRecord.starttime);
                  let endTime = new Date(this.examRecord.endtime);

                  // 计算时间间隔（毫秒）
                  var timeDiffInMilliseconds = Math.abs(endTime - startTime);
                  var timeDiffInMinute = Math.floor(timeDiffInMilliseconds / (1000 * 60));
                  var timeDiffInHours = Math.floor(timeDiffInMinute / 60);
                  timeDiffInMinute = Math.floor(timeDiffInMinute % 60);

                  this.examDuration = timeDiffInHours + '小时' + timeDiffInMinute + '分钟';
                }
              }
            }).catch(error => {
          console.error('查询考试信息失败失败', error);
        });
      },
      handleBack(){//返回学生考试首页
        localStorage.setItem("courseActiveTab","examManagement");
        this.$router.push('/student-course-detailView');
      }
    }
  };
</script>

<template>
  <div class="exam-summary">
    <!-- 第一行 -->
    <h2 class="submit-success">交卷成功</h2>
    <el-link style="float: right;font-size: 18px" @click="handleBack" type="primary">返回</el-link>

    <!-- 第二到第五行 -->
    <el-card class="exam-info" v-if="examRecord">
      <div class="info-item">
        <span class="label">考生姓名:</span>
        <span>{{examRecord.sname}}</span>
      </div>
      <div class="info-item">
        <span class="label">试卷名称:</span>
        <span>{{currentExamPaper.name}}</span>
      </div>
      <div class="info-item">
        <span class="label">领取时间:</span>
        <span>{{examRecord.starttime}}</span>
      </div>
      <div class="info-item">
        <span class="label">提交时间:</span>
        <span>{{examRecord.endtime}}</span>
      </div>
      <div class="info-item">
        <span class="label">考试用时:</span>
        <span>{{examDuration}}</span>
      </div>
      <div class="info-item">
        <span class="label">成绩:</span>
        <span>{{examRecord.score}}</span>
      </div>
    </el-card>

    <!-- 第六行 -->
    <p class="warning-message">本试卷考后不允许查看试卷内容</p>
  </div>

</template>

<style scoped>
.exam-summary {
  text-align: center;
  margin-top: 20px;
}

.submit-success {
  color: #409EFF;
  font-size: 30px;
}

.exam-info {
  margin-top: 20px;
  width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.info-item {
  margin-bottom: 10px;
}

.label {
  font-weight: bold;
}

.warning-message {
  margin-top: 20px;
  color: red;
}
</style>

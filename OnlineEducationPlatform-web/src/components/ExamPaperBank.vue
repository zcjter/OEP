<!--某张试卷每份试卷统计组件-->
<script>
  import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
  export default {
    name:'ExamPaperBank',
    data(){
      return{
        courseName:'',
        courseId:0,
        exams:null,
        examItem:null
      }
    },
    components:{
      TeacherDetailHeader
    },
    mounted() {
      this.courseName = localStorage.getItem("courseName");
      this.courseId = localStorage.getItem("courseID");
      this.examItem = JSON.parse(localStorage.getItem("examPaperItem"));
      this.getExamInformation();
    },
    methods:{
      getExamInformation(){//获得试卷信息
        let formData = new FormData();
        formData.append("examPaperId",this.examItem.id);
        console.log("getExamPaper ",this.examItem.id);
        this.$axios.post('/exam/selectByexamId', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.exams = response.data.data;
                localStorage.setItem("currentExam",JSON.stringify(this.exams));
              }
            }).catch(error => {
          console.error('获得试卷列表失败', error);
        });
      },
      previewExamPaper(index){//查看某一份试卷
        localStorage.setItem("currentExamNo",index);//当前第几份试卷
        this.$router.push('/exam-pre-view');
      },
      handleBack(){//返回上一页
        localStorage.setItem("courseActiveTab","Resource");
        localStorage.setItem('resourceActiveTab','examPaperBank');
        this.$router.push('/teacher-course-detail');
      }
    }
  }
</script>

<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main>
    <div class="header-content">
      <!-- 左侧文本 -->
      <span>随机试卷 </span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="handleBack" style="font-size: 20px">返回</el-link>
    </div>
    <hr class="underline" />
    <div class="container">
      <!-- Tab view -->
      <div class="course-info-container" v-if="exams!=null">
        <div class="row"  v-for="(row, index) in Math.ceil(exams.count / 3)" :key="index">
          <div class="col-md-4" v-for="( idx) in 3" :key="idx">
            <div class="card">
            <div class="card-body">
              <h5 class="card-title">{{ exams.name}} ({{index*3+idx}})</h5>
              <el-icon  class="el-button1"><Edit /></el-icon>
              <el-icon class="el-button2" @click="previewExamPaper(index*3+idx)"><Reading /></el-icon>
            </div>
          </div>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
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
.container{
  margin-top: 40px;
}
.card{
  width: 400px;
  height: 200px;
  margin-top: 10px;
  margin-right: 10px;
  margin-left: 20px;
}
.el-button1{
  margin-top: 80px;
  font-size:20px;
  float: left;
  color: blue;
}
.el-button2{
  margin-top: 80px;
  float: right;
  font-size:20px;
  margin-right: 20px;
  color:blue;
}

</style>

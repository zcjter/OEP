<!--试卷发布组件-->
<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
export default {
  name:'ExamOverallPriview',
  components:{
    TeacherDetailHeader
  },
  data(){
    return{
      courseName:'',
      courseId:0,
      examItem:null,
      classItems: [],
      checkedItems: {}, // 用于保存选中状态的对象
      selectedStartTime:'',
      selectedEndTime:'',
      user:null
    }
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
    this.courseId = parseInt(localStorage.getItem("courseID"));
    this.examItem = JSON.parse(localStorage.getItem("examPaperItem"));
    this.user = JSON.parse(localStorage.getItem("user"));
    this.getClass();
  },
  methods:{
    handleBack(){//回到上一页
      localStorage.setItem("courseActiveTab","Resource");
      localStorage.setItem('resourceActiveTab','examPaperBank');
      this.$router.push('/teacher-course-detail');
    },
    getClass(){//得到班级信息
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      this.$axios.post('/class/list', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.classItems = response.data.data;
            }
          })
          .catch(error => {
            console.error('获取班级列表错误', error);
          });
    },
    createReleaseInformation(){//发布试卷
      const selectedIds = [];
      for(let index=0;index<this.classItems.length;index++){
        const item = this.classItems[index];
        if(this.checkedItems[index]){//当前班级已经选中
          selectedIds.push(item.id);
        }
      }
      //step2 组装请求数据
      const data={
        paperId:this.examItem.id,
        startTime:this.genDateTime(this.selectedStartTime.toString()),
        endTime:this.genDateTime(this.selectedEndTime.toString()),
        classIds:selectedIds,
        userId:this.user.id,
        courseId:this.courseId
      }
      this.$axios.post('/exam/release', data)
          .then(response => {
            if (response.data.code === 20000) {
              this.classItems = response.data.data;
              localStorage.setItem("courseActiveTab","ExamHomePage");
              this.$router.push('/teacher-course-detail');
            }
          })
          .catch(error => {
            console.error('发布考试信息失败', error);
          });
    },
    genDateTime(timestr){//得到当前时间
      const monthMap = {
        "Jan":'01',"Feb":'02',"Mar":'03',"Apr":'04',"May":'05',"Jun":'06',
        "Jul":'07',"Aug":'08',"Set":"09","Oct":"10","Nov":"11","Dec":"12"
      };
      let items = timestr.split(" ");
      let newTimeStr = items[3]+'-'+monthMap[items[1]]+'-'+items[2]+" "+items[4];
      return newTimeStr;
    },
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
      <span>发布设置 </span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="handleBack">返回</el-link>
    </div>
    <hr class="underline" />
    <div class="container">
      <span>试卷标题:{{examItem.name}}</span>
      <span>发放对象:按班级发放</span>
      <div class="bordered-section">
        <el-checkbox class="class-list">全选</el-checkbox>
        <div class="class-list">
          <el-checkbox v-for="(item,index) in classItems" :key="item.id" v-model="checkedItems[index]">{{ item.name }}</el-checkbox>
        </div>
      </div>
      <div class="row">
        <span>发放时间:</span>
        <el-date-picker
            v-model="selectedStartTime"
            type="datetime"
            placeholder="选择日期和时间"
            style="width: 300px"
        ></el-date-picker>
      </div>
      <div class="row">
        <span>截止时间:</span>
        <el-date-picker
            v-model="selectedEndTime"
            type="datetime"
            placeholder="选择日期和时间"
            style="width: 300px"
        ></el-date-picker>
      </div>
      <div class="button-container">
        <el-button type="primary" @click="createReleaseInformation">发布考试</el-button>
        <el-button type="primary">取消</el-button>
      </div>
    </div>

  </el-main>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 20px;
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
.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  width:1200px;
  height: 100px;
}
.container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.bordered-section {
  border: 1px solid #ccc;
  padding: 10px;
  margin-top: 10px;
}

.class-list {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}
.row {
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 10px;
}
.button-container {
  margin-top: 10px;
  margin-left: 700px;
}
.header-right{
  font-size: 20px;
}
</style>

<!--学生作业首页组件-->
<script>
export default {
  data(){
    return{
      selectedOption: '', // 存储用户选择的选项标签
      user:{},
      task:null,
      courseId:0,
      status: {},
      currentDateTime: '', // 存储当前日期和时间
    }
  },
  created() {
    this.user.id = localStorage.getItem("userID");
    this.courseId = localStorage.getItem("courseID");
    this.getTask();
  },
  methods:{
    getTask() {//获取学生作业信息
      let formData = new FormData();
      formData.append("studentId", this.user.id);
      formData.append("courseId", this.courseId);
      this.$axios.post('/task/selectTask', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.task = response.data.data;
              console.log("查询作业信息成功");
            }
          }).catch(error => {
            console.error('查询作业信息失败', error);
      });
    },
    getCurrentDataTime(){//查询当前时间
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
      let currentDateTime = `${year}-${month}-${day}T${hour}:${minute}:${second}.000+00:00`;
      return currentDateTime;
    },
    getStatus(item,index) {//按段试卷状态
      let currentDateTime = this.getCurrentDataTime();
      if(currentDateTime<item.starttime){
        this.status[index] = '未开始';
      }else if(item.status=='完成'){
        this.status[index]  = '已完成';
      }else{
        this.status[index] = item.status;
      }
      return this.status[index];
    },
    showExamReminder(index,item){//通过状态判断该进入哪一个界面
      let currentDateTime = this.getCurrentDataTime();
      localStorage.setItem("taskItem",JSON.stringify(item));
      if(this.status[index]==='未交'&&currentDateTime<item.endtime){
        this.$router.push('/student-task-answering')
      }else if(this.status[index]==='已完成'){
        console.log(item)
        this.$router.push('/student-task-check')
      }

    },
  }
}
</script>

<template>
  <div class="main-head">
    <span>筛选:</span>
    <el-radio v-model="selectedOption" label="all" style="margin-left: 10px">全部</el-radio>
    <el-radio v-model="selectedOption" label="uncompleted">未完成</el-radio>
    <el-radio v-model="selectedOption"  label="completed" >已完成</el-radio>
  </div>
  <div class="notice-container">
    <div class="bordered-section" v-for="(item, index) in task" :key="index">
      <div  class="info-line">
        <span>作业标题标题:{{item.name}}</span>
      </div>
      <div class="info-line">
        <span>试卷状态:{{ getStatus(item, index) }}</span>
      </div>
      <div class="info">
        <el-icon style="font-size: 20px;color: blue" @click="showExamReminder(index,item)"><reading /></el-icon>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-head{
  display: flex;
}
.notice-container {
  display: flex;
  flex-wrap: wrap;
}

.bordered-section {
  width: calc(33.33% - 20px); /* Adjust width as needed */
  margin: 10px;
  height: 160px;
}
.info-line {
  margin-bottom: 5px;
}
.info{
  float: right;
  margin-right: 10px;
  font-size: 18px;
}
</style>

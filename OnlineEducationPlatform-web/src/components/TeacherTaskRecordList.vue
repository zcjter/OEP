<!--批阅作业组件-->
<script >
import TeacherDetailHeader from "./TeacherDetailHeader.vue";
export default {
  name:'MarkExamPapers',
  data() {
    return {
      tableData:'',
      currentTask:null,
      user:{},
      currentPage: 1, // 当前页码
      pageSize:10, // 每页显示条数,
      total:0,//考试记录人数
      searchText:'',
      pagePaperTask:null,
      taskRecord:null,
      taskStatus: {}
    };
  },
  components:{
    TeacherDetailHeader
  },
  created() {
    this.user.name = localStorage.getItem("username");
    this.currentTask = JSON.parse(localStorage.getItem("currentTask"));
    this.getTaskByTaskId();
    this.countTaskStatus();
  },
  methods:{
    getTaskByTaskId(){//得到这张试卷信息
      let formData = new FormData();
      formData.append("taskId", this.currentTask.taskid);
      formData.append("classId",this.currentTask.classid);
      this.$axios.post('/task/selectTaskRecord', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.TaskRecords = response.data.data;
              this.total = this.TaskRecords.length;
              this.updateTask();
              console.log("查询学生作业记录信息成功")
            }
          }).catch(error => {
            console.error('查询学生作业记录信息失败', error);
      });
    },
    countTaskStatus(){//统计发送给，已交，待批阅人数
      let formData = new FormData();
      formData.append("taskId", this.currentTask.taskid);
      formData.append("classId",this.currentTask.classid);
      this.$axios.post('/task/countTaskStatus', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.taskStatus = response.data.data;
              console.log("查询学生作业状态记录信息成功")
            }
          }).catch(error => {
            console.error('查询学生作业状态记录信息失败', error);
          });
    },
    formatDateTime(dateTimeString) {//把T换成空格，把后面的10个字符去掉
      return dateTimeString.slice(0, -10).replace("T", " ");
    },
    handleSizeChange(val) {//分页相关事件
      this.pageSize = val;
      this.updateTask();
    },
    handleCurrentChange(val) {//分页相关事件
      this.currentPage = val;
      this.updateTask();
    },
    updateTask() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pagePaperTask = this.TaskRecords.slice(startIndex, endIndex);
    },
    handleBack(){
      localStorage.setItem("courseActiveTab","HomeWork");
      this.$router.push('/teacher-course-detail');
    },
    studentNotInClass(){
      localStorage.setItem("classId",this.currentTask.classid);
      this.$router.push('/un-submitted-homework');
    },
    selectTaskByStudent(taskRecord){
      localStorage.setItem("taskRecordItem",JSON.stringify(taskRecord));
      this.$router.push('/teacher-task-review-record');
    },
    checkStudentTask(taskRecord){
      localStorage.setItem("taskRecordItem",JSON.stringify(taskRecord));
      this.$router.push('/teacher-check-task')
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
      <span>{{currentTask.name}}</span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="handleBack">返回</el-link>
    </div>
    <hr class="underline" />
    <div class="search-bar" >
      <div class="search-input">
        <el-input v-model="searchText" placeholder="请输入搜索内容"  class="el-input-text"></el-input>
        <el-icon  color="blue" size="20"><Search /></el-icon>
      </div>
    </div>
    <div class="bordered-right">
        <span class="header-right-content">创建时间:{{currentTask.starttime}}</span>
        <span class="header-right-content">发送给:{{taskStatus.unpaid}}人</span>
        <span class="header-right-content">已交:{{taskStatus.complete}}人</span>
        <span class="header-right-content">待批阅:{{taskStatus.reviewed}}人</span>
        <el-link type="primary" style="font-size:20px;float: right" @click="studentNotInClass">未提交作业人员</el-link>
    </div>
    <div style="margin-top: 100px;margin-left: 80px">
      <el-table :data="pagePaperTask" style="width: 100%;font-size: 18px" ref="studentTable">
        <el-table-column prop="sname" label="姓名"  width="100"></el-table-column>
        <el-table-column prop="sid" label="学号"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
        <el-table-column prop="starttime" label="领取时间">
          <template v-slot="scope">
            {{ formatDateTime(scope.row.starttime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endtime" label="提交时间" >
          <template v-slot="scope">
            {{ formatDateTime(scope.row.endtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="ipadress" label="IP"></el-table-column>
        <el-table-column prop="score" label="成绩"></el-table-column>
        <el-table-column prop="reviewer" label="批阅人"></el-table-column>
        <el-table-column prop="reviewtime" label="批阅时间">
          <template v-slot="scope" >
            {{  scope.row.reviewtime ? formatDateTime(scope.row.reviewtime) : '' }}<!--判断该字符数据是否为空,不为空再转化-->
          </template>
        </el-table-column>
        <el-table-column label="操作" >
          <template v-slot="scope" >
            <el-icon style="font-size: 18px;color:blue" v-if="scope.row.status==='待批阅'" @click="selectTaskByStudent(scope.row)"><CircleCheck /></el-icon>
            <el-icon style="font-size: 18px;color:blue" v-if="scope.row.status==='完成'" @click="checkStudentTask(scope.row)"><reading /></el-icon>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          :total="total">
      </el-pagination>
    </div>
  </el-main>
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
.header-right-content{
  float: left;
  margin-left: 40px;
  display: block;
  font-size: 18px
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 20px; /* 调整下划线与按钮组之间的间距 */
}
.bordered-right{
  margin-left: 50px;
  margin-bottom: 40px;
}
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.search-input {
  margin-top: 20px;
  margin-left: 80px;
  display: flex;
  align-items: center;
}

.el-input-text{
  font-size: 20px;
}
.el-input-text{
  font-size: 20px;
}
</style>

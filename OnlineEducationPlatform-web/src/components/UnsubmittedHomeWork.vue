<!--未提交的学生作业信息记录-->
<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";

export default {
  name:'MarkExamPapers',
  components: {
    TeacherDetailHeader
  },
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
  created() {
    this.user.name = localStorage.getItem("username");
    this.currentTask = JSON.parse(localStorage.getItem("currentTask"));
    this.getTaskByTaskId();
  },
  methods:{
    getTaskByTaskId(){//得到未提交的学生记录
      let formData = new FormData();
      formData.append("taskId", this.currentTask.taskid);
      formData.append("classId",this.currentTask.classid);
      this.$axios.post('/task/selectStudentNotInTask', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.TaskRecords = response.data.data;
              this.total = this.TaskRecords.length;
              this.updateTask();
              console.log("查询未提交学生作业记录信息成功")
            }
          }).catch(error => {
        console.error('查询未提交学生作业记录信息失败', error);
      });
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
      this.$router.push('/teacher-task-record-list');
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
    </div>
    <div style="margin-top: 100px;margin-left: 80px">
      <el-table :data="pagePaperTask" style="width: 100%;font-size: 18px" ref="studentTable">
        <el-table-column prop="sname" label="姓名"  width="100"></el-table-column>
        <el-table-column prop="sid" label="学号"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
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

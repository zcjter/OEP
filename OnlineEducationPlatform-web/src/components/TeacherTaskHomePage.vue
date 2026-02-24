<!--教师作业首页组件-->
<script >
import {Reading} from "@element-plus/icons-vue";

export default {
  name:"TeacherHomeworkHomePage",
  components: {Reading},
  data(){
    return{
      courseId:0,
      selectClass:'',
      classOptions:[],
      taskItem:null,
      taskStatus:{}
    }
  },
  created(){
    this.courseId = localStorage.getItem("courseID");
    this.getClassList();
  },
  methods: {
    createHomeWork(){
      this.$router.push('/add-homework')
    },
    ShowTaskMarkList(item,index){//获取班级作业记录
      localStorage.setItem("currentTask",JSON.stringify(item));
      this.$router.push('/teacher-task-record-list');
    },
    getAllClassTaskPaper(){//获取某个班的作业信息
      let formData = new FormData();
      const selectedOption = this.classOptions.find(option => option.id === this.selectClass);
      const selectedLabel = selectedOption ? selectedOption.name : null;
      localStorage.setItem("className",selectedLabel);
      formData.append("classId", this.selectClass);
      this.$axios.post('/task/selectClassTask', formData)
          .then(response => {
            if (response.data.code === 20000) {
              // 处理时间格式
              response.data.data.forEach(item => {
                item.starttime = item.starttime.slice(0, -10).replace("T", " ");
                item.endtime = item.endtime.slice(0, -10).replace("T", " ");

              });

              this.taskItem = response.data.data;
              console.log(this.taskItem);
              console.log("获取班级作业信息成功");
            }
          }).catch(error => {
        console.error('获取班级作业信息失败', error);
      });
    },
    getClassList(){//获得班级列表信息
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      this.$axios.post('/class/list', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.classOptions = response.data.data;
              console.log("查询课程班级信息成功");

              //默认显示下拉框的第一个班级数据
              if (this.classOptions.length > 0) {
                this.selectClass = this.classOptions[0].id;
                this.getAllClassTaskPaper(); // 获取作业列表数据
              }
            }
          }).catch(error => {
            console.error('查询课程班级信息失败', error);
      });
    },
    returnTaskBank() {
      localStorage.setItem("courseActiveTab", 'Resource');
      localStorage.setItem('resourceActiveTab', 'taskBank');
      // 如果当前路由是 /teacher-course-detail
      this.$router.go({ path: '/teacher-course-detail' });

    },
  }
}
</script>

<template>
  <el-main>
    <div class="el-main-header">
      <el-button type="primary" class="el-main-header-button" @click="createHomeWork">新建作业</el-button>
      <el-button type="primary" class="el-main-header-button">新建文件夹</el-button>
      <el-button type="primary" class="el-main-header-button" @click="returnTaskBank">作业库</el-button>
    </div>
    <hr class="underline" />
    <div class="el-main-center">
      <el-select v-model="selectClass" placeholder="请选择班级" style="width: 150px;margin-left: 10px;font-size: 20px;float: left"  @change="getAllClassTaskPaper">
        <el-option
            v-for="item in classOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        ></el-option>
      </el-select>
    </div>
    <div class="notice-container">
      <div class="bordered-section" v-for="(item, index) in taskItem" :key="index">
        <div  class="info-line">
          <span>{{item.name}}</span>
        </div>
        <div class="info-line">
          <span>开始时间:{{item.starttime}}</span>
        </div>
        <div class="info-line">
          <span>截至时间:{{item.endtime}}</span>
        </div>
        <div class="info">
          <el-icon style="font-size: 20px;color: blue" @click="ShowTaskMarkList(item,index)"><reading /></el-icon>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
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
.el-main-header{
  float: right;
}
.el-main-header-button{
  margin-left: 10px;
  font-size:20px;
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
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 40px; /* 调整下划线与按钮组之间的间距 */
}
.el-main-center{
  margin-top: 20px;
  display: flex;
  font-size: 18px;
}
.bordered-section {
  flex: 0 0 calc(33.33% - 20px); /* 每行显示三个试卷信息，计算每个元素的宽度 */
  margin: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  height: 200px;
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

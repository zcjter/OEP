<!--统计信息首页组件-->
<script >
export default {
  name:"TeacherHomeworkHomePage",
  data(){
    return{
      courseId:0,
      selectClass:'',
      classOptions:[],
      courseCount:0
    }
  },
  created(){
    this.courseId = localStorage.getItem("courseID");
    this.getClassList();
  },
  methods: {
    getCountCourseContent(){
      let formData = new FormData();
      const selectedOption = this.classOptions.find(option => option.id === this.selectClass);
      const selectedLabel = selectedOption ? selectedOption.name : null;
      localStorage.setItem("className",selectedLabel);
      formData.append("classId", this.selectClass);
      this.$axios.post('/course/countCourseContent', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.courseCount = response.data.data;
              console.log("统计一个班级学生访问该课程的章节内容数信息成功");
            }
          }).catch(error => {
              console.error('统计一个班级学生访问该课程的章节内容数信息失败', error);
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
              if (this.classOptions.length > 0) {
                this.selectClass = this.classOptions[0].id;
                this.getCountCourseContent(); // 获取作业列表数据
              }
            }
          }).catch(error => {
        console.error('查询课程班级信息失败', error);
      });
    },
    scoreStatics(selectClass){
      localStorage.setItem("classId",selectClass);
      this.$router.push('/score-statics');
    },
    chapterStatics(selectClass) {
      localStorage.setItem("classId", selectClass);
      this.$router.push('/chapter-statics');
    }
  }
};
</script>

<template>
  <el-main>
    <div class="el-main-center">
      <el-select v-model="selectClass" placeholder="请选择班级" style="width: 150px;margin-left: 10px;font-size: 20px;float: left"  @change="getCountCourseContent">
        <el-option
            v-for="item in classOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        ></el-option>
      </el-select>
    </div>
    <hr class="underline" />
    <div class="notice-container">
      <div class="bordered-section">
        <div style="display: flex; align-items: center;margin-top: 20px">
          <el-icon size="20" style="margin-left: 20px;" @click="scoreStatics(selectClass)"><Notebook /></el-icon>
          <span style="margin-left: 5px;">成绩管理</span>
        </div>
      </div>
      <div class="bordered-section">
        <div style="display: flex; align-items: center;margin-top: 20px">
          <el-icon size="20" style="margin-left: 20px;" @click="chapterStatics(selectClass)"><Reading /></el-icon>
          <span style="margin-left: 5px;">章节管理({{courseCount}})</span>
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
  height: 90px;
  width: 200px;
}
</style>

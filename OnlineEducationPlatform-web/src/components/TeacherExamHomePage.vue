<!--教师考试信息首页组件-->
<script >
  export default {
    name:"ExamHomePage",
    data(){
      return{
        selectClass:'',
        classOptions:[],
        courseId:0,
        examItem:[],
        status: {},
      }
    },
    created(){
      this.courseId = localStorage.getItem("courseID");
      this.getClassList();
      //默认显示下拉框的第一个班级数据
      if (this.classOptions.length > 0) {
        this.selectClass = this.classOptions[0].id;
      }
    },
    methods: {
      createExamPaper(){//新建试卷
        this.$router.push('/exam-paper-create');
      },
      returnExamPaperBank() {
        localStorage.setItem("courseActiveTab", 'Resource');
        localStorage.setItem('resourceActiveTab', 'examPaperBank');
        // 如果当前路由是 /teacher-course-detail
        this.$router.go({ path: '/teacher-course-detail' });

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
                  this.getAllClassExamPaper(); // 获取考试列表数据
                }
              }
            }).catch(error => {
          console.error('查询课程班级信息失败', error);
        });
      },
      getAllClassExamPaper() {//获取指定班级考试列表
        let formData = new FormData();
        const selectedOption = this.classOptions.find(option => option.id === this.selectClass);
        const selectedLabel = selectedOption ? selectedOption.name : null;
        localStorage.setItem("className",selectedLabel);
        formData.append("classId", this.selectClass);
        this.$axios.post('/exam/selectAllByClassId', formData)
            .then(response => {
              if (response.data.code === 20000) {
                // 处理时间格式
                response.data.data.forEach(item => {
                  item.starttime = item.starttime.slice(0, -10).replace("T", " ");
                  item.endtime = item.endtime.slice(0, -10).replace("T", " ");

                });

                this.examItem = response.data.data;
                console.log("获取班级考试信息成功");
              }
            }).catch(error => {
          console.error('获取班级考试信息失败', error);
        });
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
      getStatus(item,index) {//根据时间判断试卷状态
        let currentDateTime = this.getCurrentDataTime();
        if(currentDateTime<item.starttime){
          this.status[index] = '未开始';
        }else if(currentDateTime<=item.endtime){
          this.status[index] = '进行中';
        }else{
          this.status[index] = '过期';
        }
        return this.status[index];
      },
      ShowMarkExamPapersList(index,item){//查看试卷答题记录
        localStorage.setItem("currentExamPaper",JSON.stringify(item));
        this.$router.push('/mark-exam-papers');
      },

    }
  }
</script>

<template>
  <el-main>
    <div class="el-main-header">
      <el-button type="primary" class="el-main-header-button">在线监考</el-button>
      <el-button type="primary" class="el-main-header-button" @click="createExamPaper">新建</el-button>
      <el-button type="primary" class="el-main-header-button" @click="returnExamPaperBank()">试卷库</el-button>
    </div>
    <hr class="underline" />
    <div class="el-main-center">
      <el-select v-model="selectClass" placeholder="请选择班级" style="width: 150px;margin-left: 10px;font-size: 20px"  @change="getAllClassExamPaper">
        <el-option
            v-for="item in classOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        ></el-option>
      </el-select>
    </div>
    <div class="notice-container">
      <div class="bordered-section" v-for="(item, index) in examItem" :key="index">
        <div  class="info-line">
          <span>试卷标题:{{item.name}}</span>
        </div>
        <div class="info-line">
          <span>考试时间:{{item.starttime}}至{{item.endtime}}</span>
        </div>
        <div class="info-line">
          <span>试卷份数:{{item.count}}</span>
        </div>
        <div class="info-line">
          <span>试卷状态:{{getStatus(item, index) }}</span>
        </div>
        <div class="info">
          <el-icon style="font-size: 20px;color: blue"  @click="ShowMarkExamPapersList(index,item)"><reading /></el-icon>
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
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 40px; /* 调整下划线与按钮组之间的间距 */
}
.el-main-center{
  margin-top: 20px;
  float: left;
  font-size: 18px;
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
.notice-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: 100px;
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

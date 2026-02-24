<!--批阅试卷组件-->
<script >
  import TeacherDetailHeader from "./TeacherDetailHeader.vue";
  export default {
    name:'MarkExamPapers',
    data() {
      return {
        tableData:'',
        currentExamPaper:null,
        user:{},
        countStudent:0,
        totalCount:0,
        className:'',
        currentPage: 1, // 当前页码
        pageSize:10, // 每页显示条数,
        total:0,//考试记录人数
        examRecords:[],
        pagePaperExam:[],
        searchText:''

      };
    },
    components:{
      TeacherDetailHeader
    },
    created() {
      this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
      this.user.name = localStorage.getItem("username");
      this.className = localStorage.getItem("className");
      this.examRecords = [];
      this.pagePaperExam = [];
      this.getExamRecordByExamPaperId();
      this.getStudentCount();
    },
    methods:{
      getExamRecordByExamPaperId(){//得到这张试卷信息
        let formData = new FormData();
        formData.append("paperId", this.currentExamPaper.paperid);
        formData.append("classId",this.currentExamPaper.classid);
        this.$axios.post('/exam/selectByExamId', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.examRecords = response.data.data;
                this.total = this.examRecords.length;
                this.updateExamRecord();
                console.log("查询学生考试记录信息成功")
              }
            }).catch(error => {
          console.error('查询学生考试记录信息失败', error);
        });
      },
      formatDateTime(dateTimeString) {//把T换成空格，把后面的10个字符去掉
        return dateTimeString.slice(0, -10).replace("T", " ");
      },
      // 格式化百分比
      formatPercentage(row, column) {
        const value = row[column.property];
        if (typeof value !== 'number') return value; // 如果值不是数字，则保持原样
        return `${value.toFixed(2)}%`; // 将数字格式化为两位小数并添加百分号
      },
      //统计班级总人数
      getStudentCount(){
        let formData = new FormData();
        formData.append("classId",this.currentExamPaper.classid);
        this.$axios.post('/exam/countStudentsByClassId', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.countStudent = response.data.data;
                console.log("统计班级人数成功")
              }
            }).catch(error => {
          console.error('统计班级人数失败', error);
        });
      },
      showExamPaper(examRecord){
        localStorage.setItem("examRecordItem",JSON.stringify(examRecord));
        this.$router.push('/teacher-review-record');
        } ,
      handleSizeChange(val) {//分页相关事件
        this.pageSize = val;
        this.updateExamRecord();
      },
      handleCurrentChange(val) {//分页相关事件
        this.currentPage = val;
        this.updateExamRecord();
      },
      updateExamRecord() {//分页相关事件
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.pagePaperExam = this.examRecords.slice(startIndex, endIndex);
      },
      goBack(){
        localStorage.setItem("courseActiveTab","ExamHomePage");
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
      <span>批阅列表 </span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="goBack">返回</el-link>
    </div>
    <hr class="underline" />
    <div class="bordered-section">
      <div class="bordered-right">
        <h2 style="margin-right: 20px">{{currentExamPaper.name}}</h2><el-link type="primary">试卷详情</el-link>
      </div>
      <div class="bordered-right">
        <a style="float: left;margin-left: 20px; display: block;">涉及人数:{{countStudent}}| 已交:{{total}}</a>
      </div>
      <div class="bordered-right">
        <a style="float: left;margin-left: 20px; display: block;">创建人:{{user.name}}| 发送对象:{{noticeName}}|有效时间段:{{currentExamPaper.starttime}}至{{currentExamPaper.endtime}}</a>
      </div>
    </div>
    <div class="search-bar" >
      <div class="search-input">
        <el-input v-model="searchText" placeholder="请输入搜索内容"  class="el-input-text"></el-input>
        <el-button type="primary"  class="c">搜索</el-button>
      </div>
      <div class="action-buttons">
        <el-link type="primary" style="font-size: 20px">未提交考试人员</el-link>
      </div>
    </div>
    <div style="margin-top: 40px;margin-left: 80px">
      <el-table :data="pagePaperExam" style="width: 100%;font-size: 18px" ref="studentTable">
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
        <el-table-column prop="objectiveaccuracy" label="客观题正确率" :formatter="formatPercentage"></el-table-column>
        <el-table-column prop="score" label="分数"></el-table-column>
        <el-table-column prop="reviewer" label="批阅人"></el-table-column>
        <el-table-column prop="reviewtime" label="批阅时间">
          <template v-slot="scope" >
            {{  scope.row.reviewtime ? formatDateTime(scope.row.reviewtime) : '' }}<!--判断该字符数据是否为空,不为空再转化-->
          </template>
        </el-table-column>
        <el-table-column label="操作" >
          <template v-slot="scope" >
            <el-icon style="font-size: 18px;color:blue" @click="showExamPaper(scope.row)"><reading /></el-icon>
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
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 20px; /* 调整下划线与按钮组之间的间距 */
}
.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  margin-left: 80px;
  width:1200px;
  height: 120px;
}
.bordered-right{
  display: flex;
  margin-left: 50px;
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

.action-buttons {
  display: flex;
  align-items: center;
  margin-right: 100px;
}
.el-input-text{
  font-size: 20px;
}
.el-input-text{
  font-size: 20px;
}
</style>

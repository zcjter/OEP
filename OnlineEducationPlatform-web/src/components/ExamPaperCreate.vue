<!--试卷创建组件-->
<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
export default {
  data() {
    return {
      radio: '选项一', // 单选按钮组模型,
      courseName:'',
    };
  },
  components:{
    TeacherDetailHeader
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
  },
  methods:{
    goToNextStep() {
      // 根据选择的单选按钮跳转到不同的页面
      if (this.radio === '选项一') {
        // 跳转到手动创建主体页面
        this.$router.push('/manual-create');
      } else if (this.radio === '选项二') {
        // 跳转到自动抽取创建页面
        this.$router.push('/exam-paper-auto-extract');
      }
    },
    handleBack(){//返回上一页
      localStorage.setItem("courseActiveTab","ExamHomePage");
      this.$router.push('/teacher-course-detail');
    }
  }
};
</script>

<template>
  <el-header class="header">
   <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main>
    <div class="header-content">
      <!-- 左侧文本 -->
      <span>选择创建方式</span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="handleBack">返回</el-link>
    </div>
    <hr class="underline" />
    <el-form>
      <!-- 单选按钮组 -->
      <el-radio-group v-model="radio" class="el-radio-type">
        <el-radio label="选项一" size="large"><span class="radio-span">手动创建主体（手动填写相应创建主体）</span></el-radio>
        <el-radio label="选项二" size="large" style="margin-top: 40px"><span class="radio-span">自动抽取创建（系统从已有资料中抽取相应创建）</span></el-radio>
      </el-radio-group>
    </el-form>
    <!-- 操作按钮 -->
    <el-button-group class="el-button-type">
      <el-button type="primary" class="button-type" @click="goToNextStep">下一步</el-button>
      <el-button  class="button-type" style="margin-left: 40px">取消</el-button>
    </el-button-group>
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

.header-right{
  font-size: 20px;
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 20px; /* 调整下划线与按钮组之间的间距 */
}
.el-radio-type{
  margin-top: 40px;
  text-align: center;
}
.el-button-type{
  margin-top: 40px;
  text-align: center;
  margin-left: 150px;
}
.button-type{
  font-size: 20px;
}
.radio-span{
  font-size: 20px;
}

header-links {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
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
.course-portal {
  font-size: 18px;
}

.header-center {
  display: flex;
  gap: 20px;
  justify-content: flex-end; /* Move links to the right */
}
.header-bottom {
  flex: 1;
}
.navbar{
  color: #FFFFFF;
  margin-right: 5px;
  font-size: 20px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.course-portal{
  color: #FFFFFF;
  font-size: 25px;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>

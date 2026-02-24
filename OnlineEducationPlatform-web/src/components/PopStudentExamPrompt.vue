<!--学生进入考试信息提示-->
<template>
  <el-button type="primary" plain @click="dialogVisible = true" class="info-button">进入考试</el-button>
  <el-dialog v-model="dialogVisible" title="提示" width="500" class="custom-dialog">
    <el-form
        label-width="100px"
        ref="classForm"
        class="form"
    >
      <el-form-item class="dialog-content">
        <!-- 使用插值表达式，通过replace方法将 "T" 替换为空格 -->
        <p>本次考试时长为{{examDuration}}，进入考试后开始倒计时，中途退出或离开考试界面会继续计时，考试截止时间后系统将会自动提交试卷，确认进入考试?</p>
      </el-form-item>

      <el-form-item>
        <el-button @click="dialogVisible = false" style="font-size: 20px">取消</el-button>
        <el-button type="primary"  style="font-size: 20px" @click="enterExam">进入考试</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>
.dialog-content {
  display: flex;
  justify-content: flex-end;
}

.dialog-content p {
  text-align: left;
  display: flex;
  font-size: 18px;
}
.info-button{
  float: right;
  margin-right: 760px;
  margin-top: 20px;
  font-size: 20px;
}
.custom-dialog {
  margin-left: 0; /* 设置左边间距为0 */
}
</style>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      examDuration:'',
    };
  },
  props:{
    examDuration:{
      type:Object,
      required: true // 必须要传递
    }
  },
  methods: {
    enterExam() {
      // 处理进入考试操作,跳转到学生答题界面
      this.$router.push('/student-answering-view');
    },
  }
};
</script>

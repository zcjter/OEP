<!--创建班级弹框组件,用于班级管理创建班级操作-->
<script>
export default {
  name: 'CreateClass',
  data() {
    return {
      dialogVisible: false,
      className: '',
      courseId:0,
      user:{},
    };
  },
  mounted() {
    this.courseId = localStorage.getItem("courseID");
    this.user.id = localStorage.getItem("userID");
  },
  methods: {
    // 其他管理模块的方法
    handleSubmit() {
      this.dialogVisible = false;
      // 处理提交逻辑
      let formData = new FormData();
      formData.append("className", this.className);
      formData.append("courseId", this.courseId);
      formData.append("userId",this.user.id);
      this.$axios.post("/class/add", formData)
          .then(response => {
            if (response.data.code === 20000) {
              // 处理成功上传的响应
              console.log('班级创建成功');
              this.$emit('messageToParent', '请更新班级');
              this.className = '';
            } else {
              // 处理上传失败的情况
              console.error('班级创建失败');

            }
          }).catch(error => {
      });

    }
  }
};


</script>

<template>

  <el-button plain @click="dialogVisible = true" >+新建班级</el-button>

  <el-dialog v-model="dialogVisible" title="班级名称" width="500">
    <el-form
        label-width="100px"
        ref="classForm"
        class="form"
    >
      <el-form-item label="班级名称">
        <el-input v-model="className" placeholder="请输入班级名称"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit">完成</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>


</template>

<style scoped>
header {
  background-color: #7abbff;
  color: #020101;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

</style>

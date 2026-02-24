<!--班级重命名弹框组件,用于班级管理中班级重命名-->
<script>

export default {
  name: 'RenameClass',
  props:{
    classItem:{
      type: Object, // 类型为对象
      required: true // 必须要传递
    },
  },
  data() {
    return {
      dialogVisible: false,
      className: '',
      user:{},
      courseId:0
    };
  },
  created() {
    this.user.id = localStorage.getItem("userID")
    this.courseId = localStorage.getItem("courseId");
  },
  methods: {
    handleSubmit() {//班级重命名
      this.dialogVisible = false;
      // 处理提交逻辑
      let formData = new FormData();
      formData.append("className", this.classItem.name);
      formData.append("classId", this.classItem.id);
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post("/class/update", formData)
          .then(response => {
            if (response.data.code === 20000) {
              // 处理成功上传的响应
              console.log('班级重命名成功');
              this.$emit('messageToParent', '请更新班级名称');
            } else {
              // 处理上传失败的情况
              console.error('班级重命名失败');

            }
          }).catch(error => {
      });

    }
  }
};


</script>

<template>

  <el-link plain @click="dialogVisible = true" style="font-size: 20px">重命名</el-link>

  <el-dialog v-model="dialogVisible" title="班级名称" width="500">
    <el-form
        label-width="100px"
        ref="classForm"
        class="form"
    >
      <el-form-item label="班级名称">
        <el-input v-model="classItem.name" placeholder="请输入班级名称"></el-input>
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

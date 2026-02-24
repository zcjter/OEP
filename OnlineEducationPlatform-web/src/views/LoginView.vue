<!--登录页面-->
<template>
  <div class="login-container">
    <div class="left-section">
      <!-- 左侧标题和封面图片 -->
      <h1 class="title">欢迎使用南京理工大学紫金学院网络教学平台</h1>
      <img src="/cover-image.jpg" alt="封面图片" class="cover-image">
      <p class="description">OEP（Open Education Platform）是为学校师生设计的网络教学平台，提供高效便捷的在线教育服务。教师可创建课程、布置作业、进行评估并与学生互动；学生可访问资料、参与讨论、完成作业并接收反馈。</p>
    </div>
    <!-- 竖线分割 -->
    <div class="separator"></div>
    <div class="right-section">
      <!-- 右侧登录表单 -->
      <h2 class="login-title">用户登录</h2>
      <el-form ref="loginForm" :model="form" :rules="rules" label width=" 80px" class="login-box">
        <el-form-item label="账号" prop="id">
          <el-input yi placeholder=" 请输入账号" v-model=" form.id"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" placeholder=" 请输入密码" v-model=" form.password"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="onSubmit('loginForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {

      form: {
        id: '',
        password: '',
      },
// 表单验证， 需要在el-form-item- 元素中增加prop属性
      rules: {
        id: [{required: true, message: '账号不可为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不可为空', trigger: 'blur'}]
      },
//对话框显示和隐藏
      dialogVisible: false
    }
  },
  created() {
    localStorage.clear();
  },
  methods: {
    handleClose: function () {
      console.log("Handle Close，空函数");
    },
    onSubmit(formName) {
      //为表单绑定验证功能
      this.$refs [formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/user/login', {
            id: this.form.id,
            password: this.form.password
          })
              .then(response => {
                if (response.data.code === 20000) {
                  localStorage.setItem("username", response.data.data.name);
                  localStorage.setItem("userID", response.data.data.id);
                  localStorage.setItem("user",JSON.stringify(response.data.data));
                  //使用vue-router 路由到指定页面，该方式称之为编程式导航
                  if (response.data.data.type === 0) {
                    this.$router.push({name: 'Student'});
                  } else if (response.data.data.type === 1) {
                    this.$router.push({name: 'TeacherCourseView'});
                  }else{
                    this.$router.push({name:'AdminManageView'});
                  }
                  console.log(response.data);
                  this.$message.success(response.data.msg)
                } else {
                  console.log(response.data);
                  this.$message.error(this.form.id + response.data.msg)
                }
              });
        } else {
          this.dialogVisible = true;
          return false;
        }
      });
    },

  }
};
</script>

<style scoped>
/* 整体容器样式 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

/* 左侧样式 */
.left-section {
  flex: 1;
  max-width: 400px;
}

.title {
  font-size: 32px;
  margin-bottom: 20px;
}

.cover-image {
  width: 80%;
  max-width: 400px;
}

.description {
  margin-top: 20px;
  font-size: 16px;
}

/* 竖线分割样式 */
.separator {
  width: 2px;
  background-color: #ccc;
  margin: 0 20px;
}

/* 右侧样式 */
.right-section {
  flex: 1;
  width: 80%;
  max-width: 400px;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */

}

.login-title {
  font-size: 22px;
  margin-bottom: 20px;
}

.login-form-label {
  font-size: 22px;
}
</style>

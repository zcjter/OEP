<!--登录页面-->
<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>
    <div class="login-card">
      <div class="left-panel">
        <div class="brand">
          <div class="logo">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h1 class="title">OEP</h1>
          <p class="subtitle">在线教育平台</p>
        </div>
        <div class="features">
          <div class="feature-item">
            <el-icon><Reading /></el-icon>
            <span>精品课程</span>
          </div>
          <div class="feature-item">
            <el-icon><Edit /></el-icon>
            <span>在线作业</span>
          </div>
          <div class="feature-item">
            <el-icon><ChatDotRound /></el-icon>
            <span>师生互动</span>
          </div>
        </div>
        <p class="description">为学校师生设计的网络教学平台，提供高效便捷的在线教育服务。</p>
      </div>
      <div class="divider"></div>
      <div class="right-panel">
        <div class="login-header">
          <h2>欢迎回来</h2>
          <p>请登录您的账号</p>
        </div>
        <el-form ref="loginForm" :model="form" :rules="rules" class="login-form">
          <el-form-item prop="id">
            <el-input 
              v-model="form.id" 
              placeholder="请输入账号"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              type="password" 
              v-model="form.password" 
              placeholder="请输入密码"
              size="large"
              :prefix-icon="Lock"
              show-password
              @keyup.enter="onSubmit('loginForm')"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              class="login-btn"
              :loading="loading"
              @click="onSubmit('loginForm')"
            >
              登 录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="login-footer">
          <span>南京理工大学紫金学院</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { User, Lock, Reading, Edit, ChatDotRound } from '@element-plus/icons-vue'

export default {
  name: "Login",
  data() {
    return {
      User,
      Lock,
      Reading,
      Edit,
      ChatDotRound,
      loading: false,
      form: {
        id: '',
        password: '',
      },
      rules: {
        id: [{required: true, message: '账号不可为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不可为空', trigger: 'blur'}]
      },
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
      this.loading = true;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/user/login', {
            id: this.form.id,
            password: this.form.password
          })
          .then(response => {
            this.loading = false;
            if (response.data.code === 20000) {
              localStorage.setItem("username", response.data.data.name);
              localStorage.setItem("userID", response.data.data.id);
              localStorage.setItem("user",JSON.stringify(response.data.data));
              if (response.data.data.type === 0) {
                this.$router.push({name: 'Student'});
              } else if (response.data.data.type === 1) {
                this.$router.push({name: 'TeacherCourseView'});
              }else{
                this.$router.push({name:'AdminManageView'});
              }
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(this.form.id + response.data.msg)
            }
          })
          .catch(() => {
            this.loading = false;
          });
        } else {
          this.loading = false;
          return false;
        }
      });
    },
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
  background: white;
}

.bg-shape-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.bg-shape-2 {
  width: 400px;
  height: 400px;
  bottom: -100px;
  left: -100px;
  animation: float 15s ease-in-out infinite reverse;
}

.bg-shape-3 {
  width: 200px;
  height: 200px;
  top: 50%;
  left: 30%;
  animation: float 18s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30px, 30px); }
}

.login-card {
  display: flex;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  width: 900px;
  max-width: 95vw;
  min-height: 560px;
  position: relative;
  z-index: 1;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.left-panel {
  flex: 1;
  padding: 48px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.brand {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.logo svg {
  width: 40px;
  height: 40px;
  color: white;
}

.title {
  font-size: 36px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 4px;
}

.subtitle {
  font-size: 14px;
  opacity: 0.8;
  margin-top: 8px;
}

.features {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 32px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  opacity: 0.9;
}

.feature-item .el-icon {
  font-size: 28px;
}

.feature-item span {
  font-size: 12px;
}

.description {
  text-align: center;
  font-size: 13px;
  line-height: 1.8;
  opacity: 0.8;
  padding: 0 20px;
}

.divider {
  width: 1px;
  background: linear-gradient(to bottom, transparent, rgba(255,255,255,0.5), transparent);
}

.right-panel {
  flex: 1;
  padding: 48px 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px;
}

.login-header p {
  color: #6b7280;
  font-size: 14px;
  margin: 0;
}

.login-form {
  margin-top: 8px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 8px 12px;
  border-radius: 12px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.login-footer {
  text-align: center;
  margin-top: 32px;
  color: #9ca3af;
  font-size: 12px;
}

@media (max-width: 768px) {
  .login-card {
    flex-direction: column;
    width: 100%;
    min-height: auto;
    margin: 20px;
    border-radius: 20px;
  }

  .left-panel {
    padding: 32px 24px;
  }

  .right-panel {
    padding: 32px 24px;
  }

  .features {
    gap: 20px;
  }

  .divider {
    width: 100%;
    height: 1px;
  }
}
</style>

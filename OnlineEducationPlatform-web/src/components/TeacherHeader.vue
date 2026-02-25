<!--教师课程信息头部-->
<template>
  <header class="app-header">
    <div class="header-left">
      <div class="logo-icon">
        <el-icon><Reading /></el-icon>
      </div>
      <span class="university">南京理工大学紫金学院</span>
    </div>
    <div class="header-right">
      <div class="user-info">
        <el-avatar :size="36" class="user-avatar">
          {{ user.name ? user.name.charAt(0) : 'T' }}
        </el-avatar>
        <div class="user-detail">
          <span class="user-name">{{ user.name }}</span>
          <span class="user-role">教师</span>
        </div>
      </div>
      <el-button type="text" @click="logout" class="logout-btn">
        <el-icon><SwitchButton /></el-icon>
        退出登录
      </el-button>
    </div>
  </header>
</template>

<script>
import { Reading, SwitchButton } from '@element-plus/icons-vue'

export default {
  name:'TeacherHeader',
  components: {
    Reading,
    SwitchButton
  },
  data() {
    return {
      user: {},
    };
  },
  mounted() {
    this.user.id = localStorage.getItem("userID");
    this.user.name = localStorage.getItem("username");
    if(this.user.name==null)
      this.$router.push('/login');
  },
  methods:{
    logout() {
      localStorage.clear();
      this.$router.push('/login');
    },
  }
};
</script>

<style scoped>
.app-header {
  height: 64px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.university {
  color: white;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 16px 6px 6px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 30px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.25);
}

.user-avatar {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  font-weight: 600;
}

.user-detail {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
}

.user-name {
  color: white;
  font-size: 14px;
  font-weight: 600;
}

.user-role {
  color: rgba(255, 255, 255, 0.7);
  font-size: 11px;
}

.logout-btn {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}
</style>

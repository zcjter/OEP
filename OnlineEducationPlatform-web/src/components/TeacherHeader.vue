<!--教师课程信息头部-->
<template>
  <div class="header-left">
    <span class="university">南京理工大学紫金学院</span>
  </div>
  <div class="header-right">
    <span class="navbar">欢迎 {{ user.name }} 老师登录&nbsp;&nbsp;&nbsp;&nbsp;</span>
    <el-button type="text" @click="logout" class="logout-btn">退出登录</el-button>
  </div>
</template>
<script>
export default {
  name:'TeacherHeader',
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
      localStorage.clear();//清除全部的缓存数据
      this.$router.push('/login');
    },
  }
};
</script>

<style scoped>
.header-left {
  font-size: 18px;
}

.header-right {
  font-size: 16px;
  display: flex;
  align-items: center;
}

.logout-btn {
  color: #FFFFFF; /* 将退出按钮颜色设置为白色 */
}

.navbar,.logout-btn{
  color: #FFFFFF;
  margin-right: 5px;
  font-size: 20px;
}
.university{
  color: #FFFFFF;
  font-size: 25px;
}

</style>

<!--学生首页头部信息组件-->
<template>
  <div class="header-left">
    <span class="university">南京理工大学紫金学院</span>
  </div>
  <div class="header-right">
    <span  class="navbar">欢迎 {{ user.name }} 学生登录&nbsp;&nbsp;&nbsp;&nbsp;</span>
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
      localStorage.removeItem("username");
      localStorage.removeItem("userID");
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

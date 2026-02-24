<!--学生端收件箱组件-->
<template>
  <div class="teacher-dashboard">
    <!-- 头部 -->
    <el-header class="header">
      <StudentHeader></StudentHeader>
    </el-header>
    <div class="main-and-sidebar">
      <el-aside class="sidebar">
        <StudentAside></StudentAside>
      </el-aside>

      <el-main class="main">
        <InboxContent></InboxContent>
      </el-main>
    </div>
    <!-- 尾部 -->
    <el-footer class="footer">
      <Footer></Footer>
    </el-footer>
  </div>
</template>

<script>

import StudentAside from "../components/StudentAside.vue";
import StudentHeader from "../components/StudentHeader.vue";
import Footer from "../components/Footer.vue";
import InboxContent from  "../components/InboxContent.vue"
export default {
  name:'Inbox',
  components:{
    StudentHeader,StudentAside,Footer,InboxContent
  },
};


</script>

<style scoped>
.teacher-dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  background-color: #409EFF;
  color: #FFFFFF;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  margin-bottom: 0;
}

.sidebar {
  width: 200px;
  margin-bottom: 0; /* 调整侧边栏底部间距为0 */
}


.main {
  flex: 1;
  padding: 0 20px; /* 调整中间部分左右padding为20px */
  margin-top: 10px; /* 调整顶部间距为0 */
  min-width: 1000px;
  min-height: 1000px;
}

.footer {
  background-color: #F0F2F5;
  padding: 10px;
  text-align: center;
}
.main-and-sidebar {
  display: flex;
  flex: 1; /* 使用Flexbox布局，并将其拉伸以占据剩余的空间 */
}

</style>

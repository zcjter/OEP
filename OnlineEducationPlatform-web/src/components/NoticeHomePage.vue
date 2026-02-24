<!--发送通知信息首页-->
<template>
  <div class="course-detail">
    <!-- Content Area (Middle) -->
    <el-main class="main">
      <div class="header-content">
        <!-- 左侧文本 -->
        <span>我的通知 </span>
        <!-- 右侧返回链接 -->
        <div class="header-right"> <PopAddNotice @messageToParent="updateNoticeList"></PopAddNotice></div>
      </div>
      <hr class="underline" />
      <div class="notice-container">
        <div class="bordered-section" v-for="(item, index) in sortedNoticeItems" :key="index">
          <div  class="info-line">
            <span>发布时间:{{item.releasetime}}</span>
          </div>
          <div class="info-line">
            <span>发送人:{{item.sender}}</span>
          </div>
          <div class="info-line">
            <span>班级:{{item.classname}}</span>
          </div>
          <div class="info-line">
            <span>已读:</span>
          </div>
          <div class="info">
            <el-icon  style="font-size: 20px;color: blue" @click="deleteNotice(item)"><Delete /></el-icon>
          </div>
        </div>
      </div>
    </el-main>

    <!-- Footer Area (Bottom) -->
    <el-footer class="footer"></el-footer>
  </div>
</template>

<script>
import PopAddNotice from "../components/PopAddNotice.vue";
import {ElMessage, ElMessageBox} from "element-plus";
export default {
  name: 'Notice',
  data(){
    return{
      noticeItem:[],
      courseId:0,
      user:{}
    }
  },
  created() {
    this.courseId = localStorage.getItem("courseID");
    this.user.id = localStorage.getItem("userID");
    this.selectByCourseIdAndUserId();
  },
  components:{
    PopAddNotice,
  },
  computed: {
    // 计算属性，用于对通知数据按时间字段降序排序
    sortedNoticeItems() {
      return this.noticeItem.slice().sort((a, b) => {
        return new Date(b.releasetime) - new Date(a.releasetime);
      });
    },
  },
  methods:{
    selectByCourseIdAndUserId(){//查询教师发的通知信息
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      formData.append("userId",this.user.id);
      this.$axios.post('/notice/selectByCourseIdAndUserId', formData)
          .then(response => {
            if (response.data.code === 20000) {
              response.data.data.forEach(item => {
                item.releasetime = item.releasetime.slice(0, -10).replace("T", " ");
              });
              this.noticeItem = response.data.data;
              console.log("获取学生通知信息成功");
            }
          }).catch(error => {
            console.error('获取学生通知信息失败', error);
      });
    },
    deleteNotice(item){//删除老师发送的通知
      ElMessageBox.confirm(
          '请确认是否移除通知',
          '移除通知',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        let formData = new FormData();
        formData.append("noticeId", item.id);
        formData.append("userId",this.user.id);
        formData.append("courseId",this.courseId);
        this.$axios.post('/notice/deleteNotice', formData)
            .then(response => {
              if (response.data.code === 20000) {
                console.log("删除通知信息成功");
                this.selectByCourseIdAndUserId();
              }
            }).catch(error => {
          console.error('删除通知信息失败', error);
        });

        ElMessage({
          type: 'success',
          message: '移除通知成功',
        })
      })
          .catch(() => {
          })
    },
    updateNoticeList(message){
      console.log(message);
      this.selectByCourseIdAndUserId();
    }
  }
};
</script>

<style scoped>
.course-detail {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.main {
  flex: 1;
}

.footer {
  padding: 10px;
  text-align: center;
}
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
.notice-container {
  display: flex;
  flex-wrap: wrap;
  margin-top: 100px;
}

.bordered-section {
  flex: 0 0 calc(33.33% - 20px); /* 每行显示三个试卷信息，计算每个元素的宽度 */
  margin: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  height: 200px;
}

.info-line {
  margin-bottom: 5px;
}
.info{
  float: right;
  margin-right: 10px;
  font-size: 18px;
}

</style>

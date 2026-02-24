<!--学生端收件箱内容组件-->
<script>
  export default {
    name:'InboxContent',
    data(){
      return{
        user:{},
        noticeItem:[],
      }
    },
    computed: {
      // 计算属性，用于对通知数据按时间字段降序排序
      sortedNoticeItems() {
        return this.noticeItem.slice().sort((a, b) => {
          return new Date(b.releasetime) - new Date(a.releasetime);
        });
      },
    },
    created() {
      this.user.id = localStorage.getItem("userID");
      this.fetchNotices();
    },
    methods:{
      fetchNotices(){//获取学生通知内容
        let formData = new FormData();
        formData.append("userId",this.user.id)
        this.$axios.post('/notice/fetchNotices', formData)
            .then(response => {
              if (response.data.code === 20000) {
                response.data.data.forEach(item => {
                  item.releasetime = item.releasetime.slice(0, -10).replace("T", " ");
                });
                this.noticeItem = response.data.data;
                console.log("获取通知信息成功");
              }
            }).catch(error => {
          console.error('获取通知信息失败', error);
        });
      },
      ShowNotice(item){//展示通知信息
        localStorage.setItem("noticeItem",JSON.stringify(item));
        this.$router.push("/notice-content");
      }
    },
  }
</script>

<template>
  <div class="bordered-section"  v-for="(item, index) in sortedNoticeItems" :key="index">
    <div class="notification">
      <!-- 左边背景为橘黄色的通知儿子 -->
      <div class="notification-background">
        <p class="notification-text">通知</p>
      </div>
      <!-- 右边的通知内容 -->
      <div class="notification-details" >
        <h3>{{item.title}}</h3>
        <div class="notification-details-inner">
          <div v-if="item.type===1">
            <p>收件人: {{item.receiver}}</p>
          </div>
          <div v-if="item.type===0">
            <p>收件人: {{item.classname}}</p>
          </div>
        </div>
      </div>
      <div style="float:right;margin-left: 380px">
        <p>收件时间: {{ item.releasetime}}</p>
        <el-icon style="font-size: 20px;color: blue" @click="ShowNotice(item)"><reading /></el-icon>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bordered-section {
  border: 1px solid #ccc; /* 边框 */
  padding: 10px; /* 内边距 */
  display: inline-block; /* 行内块级元素 */
  height: 170px;
  width: 850px;
  display: flex;
}

.notification {
  display: flex;
}

.notification-background {
  width: 50px; /* 左边背景的宽度 */
  height: 50px; /* 左边背景的高度充满整个通知 */
  background-color:#3498db ;
  display: flex;
  align-items: center; /* 垂直居中 */
}

.notification-text {
  font-size: 16px; /* 字体大小 */
  color: white; /* 字体颜色 */
}

.notification-details {
  display: flex;
  flex-direction: column; /* 垂直布局 */
}

.notification-details-inner {
  padding-left: 10px; /* 内容与边框之间的空隙 */
}
</style>

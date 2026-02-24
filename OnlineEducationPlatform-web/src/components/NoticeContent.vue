<!--收件箱某个通知内容展示-->
<script>
export default {
  name:'NoticeContent',
  data(){
    return{
      noticeItem:null
    }
  },
  created() {
    this.noticeItem = JSON.parse(localStorage.getItem("noticeItem"));
  }
}
</script>

<template>
  <div class="noticeTitle">
    <h1>通知</h1>
  </div>
    <div class="noticeContent">
      <h3>{{noticeItem.title}}</h3>
    </div>
    <div class="noticeContent">
      <div >
        <span>{{noticeItem.sender}}</span>
        <span style="margin-left: 20px">{{noticeItem.releasetime}}</span>
      </div>
  </div>
  <div class="noticeContent">
    <div v-if="noticeItem.type===0">
      <span>收件人:{{noticeItem.classname}}</span>
    </div>
    <div v-if="noticeItem.type===1">
      <span>收件人:{{noticeItem.receiver}}</span>
    </div>
  </div>
  <div class="noticeContent">
    <span>{{noticeItem.content}}</span>
  </div>
</template>

<style scoped>
.noticeContent{
  display: flex;
  margin-left: 20px;
  margin-bottom: 30px;
}
.noticeTitle{
  text-align: center;
  background: #3498db;
}
</style>

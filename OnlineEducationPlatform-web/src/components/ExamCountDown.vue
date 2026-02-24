<!--倒计时组件，用于学生考试答题倒计时-->
<script>
export default {
  data() {
    return {
      remainingMinutes: '',
      remainingSeconds: '',
      currentExamPaper:null,
    };
  },
  created() {
    //获取当前试卷对象
    this.currentExamPaper = JSON.parse(localStorage.getItem("currentExamPaper"));
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.slice(0, -10); // 去除末尾的 10 个字符
    this.currentExamPaper.endtime = this.currentExamPaper.endtime.replace("T"," ");// 替换 "T" 为空格
    this.startTimer();//启动定时器
  },
  computed: {
    formattedTime() {//格式化显示时间
      const minutes = this.remainingMinutes < 10 ? '0' + this.remainingMinutes : this.remainingMinutes;
      const seconds = this.remainingSeconds < 10 ? '0' + this.remainingSeconds : this.remainingSeconds;
      return `${minutes}'${seconds}`;
    }
  },
  beforeDestroy() {
    // 组件销毁时清除定时器，避免内存泄漏
    clearInterval(this.timer);
  },
  methods: {
      startTimer(){
        //获取当前时间
        let currentDateTime = this.getCurrentDataTime();
        let currentTime = new Date(currentDateTime);
        let endTime = new Date(this.currentExamPaper.endtime);

        // 计算时间间隔（毫秒）
        let timeDiffInMilliseconds = Math.abs(endTime - currentTime);
        let timeDiffInSeconds = Math.floor(timeDiffInMilliseconds / 1000);

        // 将时间间隔转换为分钟和秒
        this.remainingMinutes = Math.floor(timeDiffInSeconds / 60);
        this.remainingSeconds = timeDiffInSeconds % 60;

      // 模拟考试倒计时，每秒更新剩余时间
      this.timer = setInterval(() => {
        if (this.remainingMinutes > 0 || this.remainingSeconds > 0) {
          if (this.remainingSeconds === 0) {
            // 如果秒数为 0，减少一分钟，重置秒数为 59
            this.remainingMinutes--;
            this.remainingSeconds = 59;
          } else {
            // 秒数减少 1
            this.remainingSeconds--;
          }
        } else {
          // 考试时间到，往父窗口发送消息,自动提交答案
          this.$emit('messageToParent', '答题时间已结束');
          clearInterval(this.timer); // 清除定时器
        }
      }, 1000);
    },
    getCurrentDataTime(){//获取当前时间
      const currentDate = new Date();

      // 获取当前日期
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始计数，需要加1
      const day = String(currentDate.getDate()).padStart(2, '0');

      // 获取当前时间
      const hour = String(currentDate.getHours()).padStart(2, '0');
      const minute = String(currentDate.getMinutes()).padStart(2, '0');
      const second = String(currentDate.getSeconds()).padStart(2, '0');

      // 格式化日期和时间
      let currentDateTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
      return currentDateTime;
    },
  }
};
</script>

<template>
  <span class="time">{{ formattedTime}}''</span>
</template>

<style scoped>
.time{
  font-size: 30px;
  color: orange;
}
</style>

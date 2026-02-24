<!--教师端分数统计组件-->
<template>
  <div style="display: flex">
    <el-link type="primary" style="font-size: 20px" @click="handleBank">返回</el-link>
  </div>
  <div id="main" class="echart-style">
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { onMounted, ref } from 'vue';
import axios from "axios";
import { useRouter } from 'vue-router';

const router = useRouter();


onMounted(() => {
  updateChartWithClassId()
})
// 假设您已经设置了 option 模板
const updateChartWithClassId = ()=>{//获取图表x轴和y轴坐标
  let formData = new FormData();
  formData.append("classId", localStorage.getItem("classId"));

  axios.post("/course/selectScoreByClassId", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          const data = response.data.data;
          const myChart = echarts.init(document.getElementById('main'));

          // 定义分数区间和对应的数据
          const xAxisData = ['<60', '60-69', '70-79', '80-89', '90-99'];
          const yAxisData = [0, 0, 0, 0, 0]; // 初始化各个区间的数据为0

          // 将数据分类到各个分数区间
          data.forEach(item => {
            if (item.score < 60) {
              yAxisData[0]++;
            } else if (item.score >= 60 && item.score < 70) {
              yAxisData[1]++;
            } else if (item.score >= 70 && item.score < 80) {
              yAxisData[2]++;
            } else if (item.score >= 80 && item.score < 90) {
              yAxisData[3]++;
            } else if (item.score >= 90 && item.score < 100) {
              yAxisData[4]++;
            }
          });

          const option = {
            title:{
              text:'学生试卷成绩统计',
              left: 'center', // 设置标题居中
              textStyle: {
                color: '#333', // 标题颜色
                fontSize: 18, // 标题字体大小
                fontWeight: 'bold', // 标题字体粗细
              }
            },
            xAxis: {
              data: xAxisData
            },
            yAxis: {},
            series: [
              {
                type: 'bar',
                data: yAxisData
              }
            ]
          };
          myChart.setOption(option);
        } else {
          // 处理上传失败的情况
          console.error('学生考试记录查询正确');
        }
      })
      .catch(error => {
        console.error('学生考试记录查询错误', error);
      });

}
const handleBank = () =>{
  localStorage.setItem("courseActiveTab","statistics");
  router.push('/teacher-course-detail');
}
</script>
<style scoped>

.echart-style {
  width: 1000px;
  height: 900px;
}
</style>

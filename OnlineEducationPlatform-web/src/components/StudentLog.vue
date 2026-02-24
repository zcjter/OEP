<!--某个学生的日志信息-->
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from "axios";
import * as echarts from "echarts";

const router = useRouter();
// 存储选中的月份
const selectMouth = ref('1');
const selectYear = ref('2024')
// 月份选项
const selectMouthOptions = [
  { value: '1', label: '1' },
  { value: '2', label: '2' },
  { value: '3', label: '3' },
  { value: '4', label: '4' },
  { value: '5', label: '5' },
  { value: '6', label: '6' },
  { value: '7', label: '7' },
  { value: '8', label: '8' },
  { value: '9', label: '9' },
  { value: '10', label: '10' },
  { value: '11', label: '11' },
  { value: '12', label: '12' }
];
const selectYearOptions = [
  { value: '2010', label: '2010' },
  { value: '2011', label: '2011' },
  { value: '2012', label: '2012' },
  { value: '2013', label: '2013' },
  { value: '2014', label: '2014' },
  { value: '2015', label: '2015' },
  { value: '2016', label: '2016' },
  { value: '2017', label: '2017' },
  { value: '2018', label: '2018' },
  { value: '2019', label: '2019' },
  { value: '2020', label: '2020' },
  { value: '2021', label: '2021' },
  { value: '2022', label: '2022' },
  { value: '2023', label: '2023' },
  { value: '2024', label: '2024' },
  { value: '2025', label: '2025' },
];

const searchChapter = () =>{//获取学生日志信息
  let formData = new FormData();
  formData.append("userId", localStorage.getItem("userId"));
  formData.append("year",selectYear.value);
  formData.append("mouth",selectMouth.value);
  axios.post("/class/getLogStatisticsByStudent", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          const data = response.data.data;
          const dataSize = data.length;
          console.log(data);
          // 提取数据
          const xAxisData = [];
          const yAxisData = [];

          const myChart = echarts.init(document.getElementById('main'));

          data.forEach(item => {
            xAxisData.push(item.formatted);
            yAxisData.push(item.countStudentLog);
          });

          const option = {
            title:{
              text:'学生访问日志内容',
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
            yAxis: {
              type: 'value'
            },
            series: [
              {
                type: 'line',
                data: yAxisData
              }
            ]
          };
          myChart.setOption(option);
        } else {
          // 处理上传失败的情况
          console.error('学生日志记录查询正确');
        }
      })
      .catch(error => {
        console.error('学生日志记录查询错误', error);
      });
}
const handleBank = () =>{
  localStorage.setItem("courseActiveTab","manageView");
  localStorage.setItem("activeTab","classDetails");
  router.push('/teacher-course-detail');
}
</script>

<template>
  <div style="display: flex">
    <el-link type="primary" style="font-size: 20px;" @click="handleBank">返回</el-link>
  </div>
  <div style="float: right;margin-top: 20px">
    <el-select v-model="selectYear" placeholder="请选择月份" style="width: 150px;margin-left: 20px">
      <el-option
          v-for="item in selectYearOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      ></el-option>
    </el-select>
    <el-select v-model="selectMouth" placeholder="请选择月份" style="width: 150px;margin-left: 20px">
      <el-option
          v-for="item in selectMouthOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      ></el-option>
    </el-select>
    <el-icon size="20" style="margin-left: 20px;" @click="searchChapter"><CircleCheck /></el-icon>
  </div>
  <div id="main" class="echart-style">
  </div>
</template>

<style scoped>
.echart-style {
  width: 1000px;
  height: 900px;
}
</style>

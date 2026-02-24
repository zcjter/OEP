<template>
  <div id="main" class="echart-style">
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import { onMounted, ref } from 'vue';
let myChart = ref()
let option = ref({})
onMounted(() => {
  init()
})
const init = () => {
  // 基于准备好的dom，初始化echarts实例
  myChart.value = echarts.init(document.getElementById('main'));
  // 绘制图表
  option.value = {
    xAxis: {
      type: 'category',
      data: ['A', 'B', 'C']
    },
    yAxis: {},
    series: [
      {
        data: [120, 200, 150],
        type: 'bar'
      }
    ]
  };
  myChart.value.setOption(option.value)
};
</script>
<style scoped>
.echart-style {
  width: 1000px;
  height: 900px;
  background: skyblue;
}
</style>

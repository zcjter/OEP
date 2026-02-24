<!--课程章节目录及内容显示组件,用于教师课程预览-->

<template>
  <div class="container">
      <!--课程章节目录树形结构-->
      <el-tree class="leftdiv" ref="leftPanel"
          :data="treeData"
          :props="defaultProps"
          :highlight-current="true"
          :default-expand-all="true"
          @node-click="handleNodeClick"
          style="font-size: 20px;"/>
    <div class="rightdiv">
      <!--课程章节内容文本显示组件-->
      <CourseContent :currentTreeNode="currentTreeNode"></CourseContent>
    </div>
  </div>
 <div style="float:right;">
   <el-link type="primary" @click="handleBack" class="exit-link">退出</el-link>
 </div>
</template>

<script lang="ts" setup>
import {onMounted, ref,provide} from 'vue'
import axios from 'axios'
import { defineComponent } from 'vue';
import CourseContent from "./CourseContent.vue";
import { useRouter } from 'vue-router';

const router = useRouter();
interface TreeNode  {
  id:number
  label:string
  pid:number
  courseid:number
  type:number
  children?: TreeNode []
}
const ParentComponent = defineComponent({
  components: {
    CourseContent,
  },
});
const treeData = ref<TreeNode[]>([])
const currentTreeNode = ref<TreeNode | null>(null);
currentTreeNode.value = JSON.parse(localStorage.getItem("chapterItem"));

const defaultProps = {
  children: 'children',
  label: 'label',
}
const handleBack = () => {//跳转到教师课程详情页面
  localStorage.setItem("courseActiveTab","HomePage");
  router.push('/teacher-course-detail');
}
const handleNodeClick = (classItem: TreeNode ) => {
  if(classItem.type === 1){
    currentTreeNode.value = classItem;
  }
}

//从后端获取树形结构
const fetchCourseChapter = async ()=>{//获取课程章节树形目录
  let formData = new FormData();
  const courseId = localStorage.getItem("courseID");
  formData.append("courseId", courseId);
  axios.post("/course/chapter", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          treeData.value = response.data.data;
        } else {
          // 处理上传失败的情况
          console.error('获取课程章节树形目录失败');

        }
      }).catch(error => {
    console.error('获取课程章节树形目录错误:', error);
  });
}

// 在组件挂载后调用数据获取函数
onMounted(() => {
  fetchCourseChapter()
});
provide('updateContent', (node) => {
  currentTreeNode.value = node;
});

</script>

<style scoped>
.container {
  display: grid;
  grid-template-columns: minmax(450px, auto) 900px; /* 设置右侧容器宽度为 400px */
  grid-template-areas: "left right"; /* 定义网格布局，左侧和右侧区域 */
  height: 100vh;

}

.leftdiv {
  grid-area: left; /* 将左侧容器放置在左侧区域 */
  position: sticky;
  top: 0;
  max-height: 100vh;
  overflow: auto;
}

.rightdiv {
  grid-area: right; /* 将右侧容器放置在右侧区域 */
  text-align: left;
  position: sticky;
  top: 0;
  max-height: 100vh;
  margin-left: 30px;
  overflow-x: auto;
  width: 900px; /* 设置右侧容器的宽度为 1000px */
}

.exit-link {
  font-size: 20px;
  margin-right: 30px;
}

</style>

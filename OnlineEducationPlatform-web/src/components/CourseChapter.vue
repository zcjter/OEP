<!--课程章节树形目录组件(只读),用于显示课程章节目录信息,作为CourseHomePage的子组件-->
<template>
  <el-tree
      :data="treeData"
      :props="defaultProps"
      :highlight-current="true"
      :default-expand-all="true"
      @node-click="handleNodeClick"
      style="font-size: 20px"/>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import { useRouter } from 'vue-router';
import axios from 'axios'

const router = useRouter();
interface TreeNode  {
  id:number
  label:string
  pid:number
  courseid:number
  type:number
  children?: TreeNode []
}
const treeData = ref<TreeNode[]>([])

const defaultProps = {
  children: 'children',
  label: 'label',
}

const handleNodeClick = (classItem: TreeNode) => {
  localStorage.setItem("chapterItem",JSON.stringify(classItem));//将选定章节节点写入缓存
  router.push('/course-preview');
};

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
            console.log("获取课程章节树形目录成功");
          } else {
            // 处理上传失败的情况
            console.error('获取课程章节树形目录失败');

          }
        }).catch(error => {
         console.error('获取课程章节树形目录错误', error);
    });
}

// 在组件挂载后调用数据获取函数
onMounted(() => {
  fetchCourseChapter()
});

</script>
<style scoped>

</style>

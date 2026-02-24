<!--课程章节详细内容显示组件,作为ChapterContent组件的子组件-->
<script lang="ts" setup>
import { defineProps, onMounted, ref, watch } from 'vue';
import axios from "axios";

interface TreeNode {
  id: number;
  label: string;
  pid: number;
  courseid: number;
  type: number;
  children?: TreeNode[];
}
interface Content{
  id:number,
  cid:number,
  courseid:number,
  content:string
}
const currentContent = ref<Content | null>(null);

const props = defineProps<{
  currentTreeNode: TreeNode;
}>();
const fetchContent = async () =>{//获取章节内容信息
  let formData = new FormData();
  const user = JSON.parse(localStorage.getItem("user"));


  formData.append("chapterId", currentTreeNode.value.id.toString());
  formData.append("courseId", localStorage.getItem("courseID"));
  if(user.id===0)
    formData.append("classId", localStorage.getItem("classId"));
  else
    formData.append("classId", "0")
  formData.append("userId", user.id);
  formData.append("userRole", user.type);
  console.log(user.type)
  axios.post("/course/content", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          currentContent.value = response.data.data;
          console.log(currentContent);
        } else {
          // 处理上传失败的情况
          console.error('课程内容没有查到');

        }
      }).catch(error => {
        console.error('查询课程内容错误', error);
  });
}
const currentTreeNode = ref(props.currentTreeNode);

const updateContent = (node) => {
  currentTreeNode.value = node;
};

watch(() => props.currentTreeNode, (newValue, oldValue) => {
  currentTreeNode.value = newValue;
//  updateContent(newValue);
  if (newValue !== oldValue) {
    fetchContent();
  }
});

// 在组件挂载后调用数据获取函数
onMounted(() => {
  fetchContent();
});
</script>

<template>
  <div v-if="currentTreeNode" class="div1">
    <h2 style="font-weight: bold">{{currentTreeNode.label}}</h2>
  </div>
  <div v-if="currentContent" class="div2">
    <!--用v-html就可以显示数据库中text字段的网页片段-->
    <div v-html="currentContent.content" ></div>
  </div>
</template>

<style scoped>

</style>

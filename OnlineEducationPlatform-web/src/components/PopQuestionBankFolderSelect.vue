<!--选择题库文件夹目录弹框组件-->
<template>
  <el-link class="select-folder" type="primary" @click="dialogVisible = true;">{{folderItem.name}}</el-link>
  <el-dialog v-model="dialogVisible" title="选择目录" width="500">
    <el-tree
        :data="treeData"
        :props="defaultProps"
        :highlight-current="true"
        :default-expand-all="true"
        @node-click="handleNodeClick"
        style="font-size: 20px"/>
  </el-dialog>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import { useRouter } from 'vue-router';
import { ElDialog } from 'element-plus';

const dialogVisible = ref(false); // 控制对话框可见性的变量
const selectedNodeName = ref('选择目录'); // 用于显示在 el-link 上的文本
const selectedNodeId = ref(''); // 用于记录选中节点的 id

import axios from 'axios'
const router = useRouter();
interface TreeNode1  {
  id:number
  name:string
  folderType:number
  pid:number
  courseid:number
  creator:string
  createtime:string
  children?: TreeNode1 []
}
const treeData = ref<TreeNode1[]>([])

const folderItem = ref<Object>(null);
folderItem.value = JSON.parse(localStorage.getItem("folderItem"));

const defaultProps = {
  children: 'children',
  label: 'name',
}

const handleNodeClick = (classItem: TreeNode1) => {
  // 单击节点时切换对话框的可见性
  dialogVisible.value = false;
  // 更新选择目录的文本
  selectedNodeName.value = classItem.name;
  //console.log("classItems:",classItems);

  folderItem.value.id = classItem.id;
  folderItem.value.name = classItem.name;
  //发消息通知父窗口文件夹发生变化
  localStorage.setItem("folderItem",JSON.stringify(folderItem.value));//更新缓存

  // 更新选中节点的 id，并保存到 localStorage
  selectedNodeId.value = classItem.id.toString();
  localStorage.setItem('selectedNodeId', classItem.id.toString());
};
//从后端获取树形结构
const fetchQuestionBankFolder = async ()=>{//查询题库文件夹
  let formData = new FormData();
  const courseId = localStorage.getItem("courseID");
  formData.append("courseId", courseId);
  axios.post("/folder/activeFolderTree", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          treeData.value = response.data.data;
          console.log(treeData.value);
        } else {
          // 处理上传失败的情况
          console.error('查询题库文件夹失败');

        }
      }).catch(error => {
    console.error('查询题库文件夹错误:', error);
  });
}


// 在组件挂载后调用数据获取函数
onMounted(() => {
  folderItem.value = JSON.parse(localStorage.getItem("folderItem"));
  console.log("QuestionBankFolder onMounted folderItem: ",folderItem.value);
//  const storedNodeId = localStorage.getItem('selectedNodeId');
  selectedNodeName.value = folderItem.value.name;
  fetchQuestionBankFolder();

  const storedNodeId = folderItem.value.id;
  if (storedNodeId) {
    selectedNodeId.value = storedNodeId;
  }
});

</script>
<style scoped>
.select-folder{
  margin-right: 20px;
  float: left;
  font-size: 20px;
}
</style>

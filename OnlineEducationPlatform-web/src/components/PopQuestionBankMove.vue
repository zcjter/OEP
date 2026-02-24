<!--选择题库文件夹目录弹框组件-->
<template>
<!--  <el-link  type="primary" @click="dialogVisible = true;" class="que-op-link">移动</el-link>-->
  <el-icon type="primary" @click="dialogVisible = true;" class="que-op-link"><Top /></el-icon>
  <el-dialog v-model="dialogVisible" title="选择目录" >
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
import {onMounted, ref,defineProps,defineEmits } from 'vue'
import { useRouter } from 'vue-router';
import { ElDialog,ElMessage  } from 'element-plus';
// 定义 emit
const emits = defineEmits(['messageToParent'])

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
interface Props {
  item: object; // 声明一个名为 type 的 prop
}

const props = defineProps<Props>()

const defaultProps = {
  children: 'children',
  label: 'name',
}

const handleNodeClick = (item) => {
  const classItem = {//组装对象数据
    id:props.item.id,
    type:props.item.type,
    folderId:item.id
  }
  //向父窗口发送消息
  emits('messageToParent', JSON.stringify(classItem));//将对象数据转换成JSON格式字符串传递给父窗口
  dialogVisible.value = false; //关闭窗口
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
.que-op-link{
  margin-left: 10px;
  font-size: 16px;
  color: #007bff;
}

</style>

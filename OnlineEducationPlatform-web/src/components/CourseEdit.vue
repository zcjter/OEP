<template>
  <el-header>
    <div class="editor-operator" >
      <h2 style="color: #409EFF;display: flex">编辑课程</h2>
    </div>
  </el-header>
  <el-main>
    <div class="button-row">
      <el-button style="float: left" type="primary" @click="HandleAddChapter">添加章节</el-button>
      <el-button class="header-button" type="primary" @click="HandleCourseSave">保存</el-button>
      <el-button class="header-button" type="primary" @click="HandleCoursePreView">预览</el-button>
      <el-button class="header-button" type="primary" @click="HandleCourseGoBy">退出</el-button>
    </div>
    <div class="tree-and-editor">
      <el-tree
          class="tree"
          style="max-width: 600px"
          :allow-drop="allowDrop"
          :allow-drag="allowDrag"
          :data="dataSource"
          draggable
          default-expand-all
          node-key="id"
          :expand-on-click-node="false"
          :render-content="renderContent"
          @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          @node-click="handleNodeClick">
        <template #default="{ node, data }">
            <span class="custom-tree-node">
                <span>{{ node.label }}</span>
                <span class="tree-node-operator">
                  <el-icon style="margin-left: 8px;color: blue" @click="append(node,data)"><Plus /></el-icon>
                  <el-icon style="margin-left: 8px;color: blue" @click="remove(node, data)"><Minus /></el-icon>
                  <el-icon style="margin-left: 8px;color: blue"  @click="rename(node,data)"><Edit /></el-icon>
                </span>
            </span>
        </template>

      </el-tree>
      <div class="editor">
        <EditTinyMCEEditor :currentTreeNode="currentTreeNode" @editorContentChange="updateEditorContent($event)"></EditTinyMCEEditor>
      </div>
    </div>

  </el-main>
</template>

<script lang="ts" setup>
import {onMounted, ref, watch} from 'vue'
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import { defineComponent,defineProps } from 'vue';
import EditTinyMCEEditor from '../components/EditTinyMCEEditor.vue';
import { useRouter } from 'vue-router';
import axios from "axios";
import {ElMessageBox} from "element-plus";

import type {
  AllowDropType,
  NodeDropType,
} from 'element-plus/es/components/tree/src/tree.type'

interface TreeNode  {
  id: number;
  label: string;
  pid: number;
  courseid: number;
  type: number;
  children?: TreeNode[];
}

const currentTreeNode = ref<TreeNode | null>(null);
// 获取本地存储的 courseId 和当前节点的 id
const courseId = localStorage.getItem("courseID");

const router = useRouter();

//let id = 1000
const currentContentText = ref('');//存放编辑器组件EditTinyMCEEditor中的内容
let previousTreeNode = null;//用来记录前一选中结点对象
const handleNodeClick = (classItem: TreeNode) => {//把章节信息存到文本框上
  if(previousTreeNode != null)
  {
    //处理上一结点的保存更新
    if(currentContentText.value != ''){
      if(previousTreeNode.type === 0){//原来结点是目录
        addContent(previousTreeNode,currentContentText.value);
      }else{//原结点是内容
        updateContent(previousTreeNode,currentContentText.value);
      }
    }
  }
  previousTreeNode = classItem;//更新前一结点
  currentTreeNode.value = classItem;//更新当前选择结点
};

const addContent = (classItem:TreeNode,content:String) => {//新增章节内容
  let formData = new FormData();
  formData.append("chapterId",String(classItem.id));
  formData.append("courseId",String(classItem.courseid));
  formData.append("content",content);
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  axios.post("/course/insertContent", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 如果查询到了内容，则更新文本框内容
          console.log("新增章节内容成功")
          fetchCourseChapter();
        } else {
          console.error('新增章节内容失败');
        }
      }).catch(error => {
    console.error('新增章节内容错误', error);
  });
}
const updateContent = (classItem:TreeNode,content:String) =>{//更新章节内容
  let formData = new FormData();
  formData.append("chapterId",String(classItem.id));
  formData.append("content",content);
  formData.append("courseId",String(classItem.courseid));
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  axios.post("/course/updateContent", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 如果查询到了内容，则更新文本框内容
          console.log("更新章节内容成功")
          fetchCourseChapter();
        } else {
          console.error('更新章节内容失败');
        }
      }).catch(error => {
    console.error('更新章节内容错误', error);
  });
}

const ParentComponent = defineComponent({
  components: {
    EditTinyMCEEditor
  },
});
///////////////////////////////////////////////////////////////////////////////////////////////////
//拖拽功能相关函数
const handleDragStart = (node: TreeNode, ev: DragEvents) => {
  console.log('drag start', node)
}
const handleDragEnter = (
    draggingNode: TreeNode,
    dropNode: TreeNode,
    ev: DragEvents
) => {
  console.log("handleDragEnter draggingNode" ,draggingNode);
  console.log("handleDragEnter dropNode" ,dropNode);
}
const handleDragLeave = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
  console.log('tree drag leave:', dropNode.label)
}
const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {
  console.log('tree drag over:', dropNode.label)
}
const handleDragEnd = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
  console.log("handleDragEnd draggingNode" ,draggingNode);
  console.log("handleDragEnd dropNode" ,dropNode);

}
const handleDrop = (//上下拖动章节信息
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  ev: DragEvents
) => {
  let formData = new FormData();
  formData.append("chapterId", String(draggingNode.data.id));
  formData.append("courseId",courseId);
  // 通过 dropNode 获取父节点ID
  formData.append("newParentId", String(dropNode.data.id));
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  // 发送请求将 FormData 发送到后端保存
  axios.post('/course/adjustChapter', formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功存入数据库的响应
          console.log('结点位置改变成功');
          fetchCourseChapter();
        } else {
          // 处理存入数据库失败的情况
          console.error('结点位置改变失败');
        }
      }).catch(error => {
    // 处理存入数据库过程中的错误
    console.error('结点位置改变错误:', error);
  });

}

const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  if (dropNode.label === 'Level two 3-1') {
    return type !== 'inner'
  } else {
    return true
  }
}
const allowDrag = (draggingNode: Node) => {
  return !draggingNode.label.includes('Level three 3-1-1')
}
///////////////////////////////////////////////////////////////////////////////////////////////////

const append = (node: Node,classItem: TreeNode) => {//添加章节内容按钮
  // 创建 FormData 对象
  let formData = new FormData();
  // 向 FormData 中添加界面上的信息
  formData.append('label', '新建目录'); // 根据界面上的信息确定目录名称
  formData.append('parentId', String(classItem.id)); // 设置父节点的ID作为parentId
  formData.append("courseId", String(classItem.courseid));
  formData.append('type', String(0)); // 设置类型为0
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  // 发送请求将 FormData 发送到后端保存
  axios.post('/course/insertChapter', formData, )
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功存入数据库的响应
          console.log('新建目录信息存入数据库成功');
          fetchCourseChapter();
        } else {
          // 处理存入数据库失败的情况
          console.error('新建目录信息存入数据库失败');
        }
      }).catch(error => {
    // 处理存入数据库过程中的错误
    console.error('新建目录信息存入数据库错误:', error);
  });
};

const rename = async (node: Node,classItem:TreeNode) => {//重命名章节内容按钮
  const newName = await new Promise((resolve, reject) => {
    ElMessageBox.prompt('请输入新名称', '重命名', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /\S+/,
      inputValue:classItem.label,
      inputErrorMessage: '结点名称不能为空'
    }).then(({ value }) => {
      resolve(value);
    }).catch(() => {
      reject();
    });
  });

  if (newName) {
    classItem.label = String(newName); // Update node label
    HandRename(classItem);//处理重命名请求
  }
}
const HandRename =(classItem:TreeNode) =>{//处理重命名请求
  let formData = new FormData();
  formData.append("chapterName",classItem.label);
  formData.append("chapterId",String(classItem.id));
  formData.append("type",String(classItem.type));
  formData.append("courseId",String(classItem.courseid));
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  axios.post('/course/rename', formData, )
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功存入数据库的响应
          fetchCourseChapter();
          console.log('章节重命名成功');
        } else {
          // 处理存入数据库失败的情况
          console.error('章节重命名失败');
        }
      }).catch(error => {
    // 处理存入数据库过程中的错误
    console.error('章节重命名错误:', error);
});
}
const HandleAddChapter = () =>{//处理添加章节请求
  // 创建 FormData 对象
  let formData = new FormData();
  // 向 FormData 中添加界面上的信息
  formData.append('label', '新章节'); // 根据界面上的信息确定目录名称
  formData.append('parentId', String(0)); // 设置父节点的ID作为parentId
  formData.append("courseId", courseId);
  formData.append('type', String(0)); // 设置类型为0
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);

  // 发送请求将 FormData 发送到后端保存
  axios.post('/course/insertChapter', formData, )
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功存入数据库的响应
          console.log('新建章节信息存入数据库成功');
          fetchCourseChapter();
        } else {
          // 处理存入数据库失败的情况
          console.error('新建章节信息存入数据库失败');
        }
      }).catch(error => {
    // 处理存入数据库过程中的错误
    console.error('新建章节信息存入数据库错误:', error);
});
}
const HandleCourseSave = () => {//保存按钮
  if(currentContentText.value != ''){
    if(currentTreeNode.value.type === 0){//原来结点是目录
      addContent(currentTreeNode.value,currentContentText.value);
    }else{//原结点是内容
      updateContent(currentTreeNode.value,currentContentText.value);
    }
}
  localStorage.setItem("courseActiveTab","HomePage");
  router.push('/teacher-course-detail');
}
const HandleCoursePreView = () => {//课程预览按钮
  router.push('/chapter-content');
}
const HandleCourseGoBy = () => {//退出编辑页按钮
  localStorage.setItem("courseActiveTab","HomePage");
  router.push('/teacher-course-detail');
}
const remove = (node: Node, classItem: TreeNode) => {//删除按钮
  let formData = new FormData();
  formData.append("chapterId",String(classItem.id));
  formData.append("type",String(classItem.type));
  formData.append("courseId",String(classItem.courseid))
  const userId = localStorage.getItem("userID");
  formData.append("userId",userId);
  // 发送请求将 FormData 发送到后端保存
  axios.post('/course/delete', formData, )
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功存入数据库的响应
          console.log('删除目录成功');
          fetchCourseChapter();
          currentTreeNode.value = null;
        } else {
          // 处理存入数据库失败的情况
          console.error('删除目录失败');
        }
      }).catch(error => {
    // 处理存入数据库过程中的错误
    console.error('删除目录错误:', error);
  });
}


const dataSource = ref<TreeNode[]>([])//树形控件数据源对象
const fetchCourseChapter = async ()=>{//获取课程章节树形目录
let formData = new FormData();
formData.append("courseId", courseId);
axios.post("/course/chapter", formData)
    .then(response => {
      if (response.data.code === 20000) {
        // 处理成功上传的响应
        dataSource.value = response.data.data;
        //console.log(dataSource.value);
        console.log('获取课程章节树形目录成功');
      } else {
        // 处理上传失败的情况
        console.error('获取课程章节树形目录失败');

      }
    }).catch(error => {
  console.error('获取课程章节树形目录错误', error);
});
}
const updateEditorContent = (content)  =>{//处理从EditTinyMCEEditor子组件传来的内容更新消息
currentContentText.value = content;
}
// 在组件挂载后调用数据获取函数
onMounted(() => {
fetchCourseChapter()
});
</script>

<style>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 20px;
  padding-right: 2px;
  float: left;
}

.tree-and-editor {
  display: flex;
  align-items: flex-start;
}

.tree {
  margin-top: 40px;
  flex: 1;
  max-width: 600px;
}

.editor {
  margin-top: 40px;
  margin-left: 20px; /* 或者适当的距离 */
}
.header-button{
  float: right;
  margin-left: 20px;
}
.button-row {
  display: flex;
  margin-bottom: 10px; /* adjust as needed */
}

</style>

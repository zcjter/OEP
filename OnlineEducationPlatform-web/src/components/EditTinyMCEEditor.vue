<!--课程编辑组件-->
<template>
  <div>
    <div>
      <Editor v-model="editorContent"
              api-key="bqydn06pdsz57iqekdms2xiejk2eyh459kpmt9y2kav25gdh"
              :init="tinyMceConfig" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, onMounted, watch, defineProps,defineEmits } from 'vue';
import Editor from '@tinymce/tinymce-vue';
import axios from "axios";

const useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

const editorContent = ref('');
const tinyMceConfig = {
  menubar: false,
  language: 'zh_CN',
  plugins: 'preview importcss searchreplace autolink autosave HandleSave directionality code visualblocks visualchars fullscreen image codesample table  pagebreak nonbreaking  insertdatetime advlist lists wordcount help  quickbars',
  editimage_cors_hosts: ['picsum.photos'],
  toolbar: "undo redo blocks fontfamily fontsize | \ " +
      "bold italic underline align outdent indent lineheight numlist bullist |\ " +
      "quickimage table code codesample forecolor backcolor removeformat | pagebreak | fullscreen preview",
  importcss_append: true,
  quickbars_selection_toolbar: '',
  height: 800,
  width: 800,
  image_caption: true,
  noneditable_class: 'mceNonEditable',
  toolbar_mode: 'sliding',
  skin: useDarkMode ? 'oxide-dark' : 'oxide',
  content_css: useDarkMode ? 'dark' : 'default',
  content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:20px }'
};

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
 /* const courseId = localStorage.getItem("courseID");
  console.log(courseId);
  formData.append("courseId", courseId);*/
  formData.append("courseId", currentTreeNode.value.courseid.toString());
  formData.append("chapterId", currentTreeNode.value.id.toString());
  axios.post("/course/teacherContent", formData)
      .then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          currentContent.value = response.data.data;
          editorContent.value = currentContent.value.content;
        } else {
          // 处理上传失败的情况
          console.error('课程内容没有查到');

        }
      }).catch(error => {
    console.error('查询课程内容错误', error);
  });
}
const currentTreeNode = ref(props.currentTreeNode);

watch(() => props.currentTreeNode, (newValue, oldValue) => {
  if(newValue!=null){
    currentTreeNode.value = newValue;
    if (newValue !== oldValue) {
      if(newValue.type === 1)
        fetchContent();
      else{//如果类型是目录则清空编辑框的内容
        editorContent.value = '';
      }
    }
  }
  else
    editorContent.value = '';
});

// 在组件挂载后调用数据获取函数
onMounted(() => {
 /* if(currentTreeNode!=null)
    fetchContent();*/
});

//给CourseEdit父组件发通知消息
const emit = defineEmits(['editorContentChange']);
// Watch for changes in editor content and emit event
watch(editorContent, (newValue, oldValue) => {
  emit('editorContentChange', newValue);//通知父窗口内容发生改变
});

</script>

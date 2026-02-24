<!--TinyMCE文本编辑器组件-->
<template>
  <div>
    <div>
      <Editor v-model="editorContent"
              api-key="bqydn06pdsz57iqekdms2xiejk2eyh459kpmt9y2kav25gdh"
              :init="tinyMceConfig" />
    </div>
  </div>
</template>

<script>
import Editor from '@tinymce/tinymce-vue';
const useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

export default {
  props: {
    initialContent: {
      type: String,
      default: ''
    }
  },
  components: {
    Editor
  },
  data() {
    return {
      editorContent: '',//编辑器中的内容
      tinyMceConfig: {
        menubar: false,
        language:'zh_CN',
        plugins: 'preview importcss searchreplace autolink autosave HandleSave directionality code visualblocks visualchars fullscreen image codesample table  pagebreak nonbreaking  insertdatetime advlist lists wordcount help  quickbars',
        editimage_cors_hosts: ['picsum.photos'],
        toolbar: "undo redo blocks fontfamily fontsize | \ " +
            "bold italic underline align outdent indent lineheight numlist bullist |\ " +
            "quickimage table code codesample forecolor backcolor removeformat | pagebreak | fullscreen preview",
        importcss_append: true,
        quickbars_selection_toolbar: '',
        height: 200,
        width:800,
        image_caption: true,
        noneditable_class: 'mceNonEditable',
        toolbar_mode: 'sliding',
        skin: useDarkMode ? 'oxide-dark' : 'oxide',
        content_css: useDarkMode ? 'dark' : 'default',
        content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:20px }'
      }

    };
  },
  mounted() {
    console.log("initialContent in mounted:", this.initialContent);
    this.editorContent = this.initialContent;
  },
  methods:{
    handleFunction(){
      console.log(this.editorContent);
    },
    // 获取编辑器内容的方法
    getEditorContent() {
      return this.editorContent;
    },
  },
  watch: {
    // 监听编辑器内容变化，并触发事件
    editorContent(newValue, oldValue) {
      this.$emit('editorContentChange', newValue);
    },
    initialContent(newVal) {//监听编辑器内容变化
      console.log('initialContent 发生变化:', newVal);
      this.editorContent = newVal;
      newVal = '';
    }
  }
};
</script>

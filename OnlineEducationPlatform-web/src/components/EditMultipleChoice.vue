<!--单选题编辑组件-->
<script>
import TinyMCEEditor from "../components/TinyMCEEditor.vue";


export default {
  name: 'SingleChoice',
  data() {
    return {
      selectedOption:[], // 存储用户选择的选项标签
      options: [
        { label: 'A', content: '' }, // A选项
        { label: 'B', content: '' }, // B选项
        { label: 'C', content: '' }, // C选项
        { label: 'D', content: '' }  // D选项
      ],
      selectedDifficulty: '简单', // 存储选中的难易程度
      difficultyOptions: [
        { value: '简单', label: '简单' },
        { value: '中等', label: '中等' },
        { value: '困难', label: '困难' }
      ],
      creator: '',
      currentDate: '',
      parentId: 0,
      stemContent: '',
      courseId:0,
      operatorType:0,  //题目编辑操作类型,0表示添加题目,1表示编辑题目
      questionItem:'',
      user:null
    };
  },
  props:{
    question:{
      type: Object, // 类型为对象
      required: true // 必须要传递
    },
  },
  components: {
    TinyMCEEditor
  },
  mounted() {
    this.creator = localStorage.getItem("username");
    this.currentDate = new Date().toISOString().split('T')[0];
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
    this.questionItem = JSON.parse(localStorage.getItem('questionItem'));
    this.user = JSON.parse(localStorage.getItem("user"));
    this.parentId = this.folderItem.id;
    this.courseId = localStorage.getItem("courseID");
    if(this.question!=null){//如果quesiton存在则进入题目编辑状态否则进入添加题目状态
      this.operatorType = 1;
      this.stemContent = this.question.content;
      this.selectedOption = this.question.answer.split(',');
      this.selectLevel();
      this.options[0].content = this.question.option1;
      this.options[1].content = this.question.option2;
      this.options[2].content = this.question.option3;
      this.options[3].content = this.question.option4;

    }
  },
  methods: {
    selectLevel(){//获得题目难度
      this.selectedDifficulty = this.question.level;
    },
    updateEditorContent(type, index, content){//从子组件获得更新消息
      if(type === 'stem'){
        this.stemContent = content;
      }else if(type === 'option'){
        this.options[index].content = content;
      }
    },
    // 保存问题
    saveQuestion() {//保存题目或更新题目
      if(this.operatorType===0) {
        this.addQuestion();
      }else{
        this.updateQuestion();
      }
    },
    addQuestion(){//添加题目
      if (!this.selectedOption) {
        console.error("请选择答案");
        return;
      }
      this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
      this.parentId = this.folderItem.id;
      let formData = new FormData();
      formData.append("answer", this.selectedOption);
      formData.append("option1", this.options[0].content);
      formData.append("option2", this.options[1].content);
      formData.append("option3", this.options[2].content);
      formData.append("option4", this.options[3].content);
      formData.append("questionType", 1);
      formData.append("type",'question');
      formData.append("level", this.selectedDifficulty);
      formData.append("content", this.stemContent);
      formData.append("folderId", this.parentId);
      formData.append("createTime", this.currentDate);
      formData.append("creator", this.creator);
      formData.append("courseId", this.courseId);
      formData.append("userId",this.user.id);

      this.$axios.post('/question/add', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('多选题保存成功');
              localStorage.setItem("courseActiveTab","Resource");
              this.$router.push('/teacher-course-detail');
            }
          }).catch(error => {
            console.error('多选题保存失败', error);
      });
    },
    updateQuestion(){//更新题目
      let formData = new FormData();
      formData.append("answerContent", this.selectedOption);
      formData.append("option1", this.options[0].content);
      formData.append("option2", this.options[1].content);
      formData.append("option3", this.options[2].content);
      formData.append("option4", this.options[3].content);
      formData.append("questionId",this.questionItem.id);
      formData.append("stemContent", this.stemContent);
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/question/updateQuestion', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('多选题编辑成功');
              localStorage.setItem("courseActiveTab","Resource");
              this.$router.push('/teacher-course-detail');
            }
          }).catch(error => {
        console.error('多选题编辑失败', error);
      });
    },
  }
};
</script>

<template>
  <div>
    <div class="stem" style="margin-top: 10px">
      <span class="question-text">题干:</span>
      <TinyMCEEditor v-model="stemContent" :initialContent="stemContent" @editorContentChange="updateEditorContent('stem',0,$event)"></TinyMCEEditor>
    </div>

  </div>
  <div>
    <div class="option" v-for="(option, index) in options" :key="index" :style="{ marginTop: '60px' }">
      <el-checkbox v-model="selectedOption" :label="option.label" style="margin-bottom: 10px;">{{ option.label }}</el-checkbox>
      <TinyMCEEditor v-model="option.content" :initialContent="option.content" @editorContentChange="updateEditorContent('option',index,$event)"></TinyMCEEditor>
    </div>
  </div>
  <div style="margin-top: 30px;float: left;">
    <span class="question-text1">难易程度:</span>
    <el-select v-model="selectedDifficulty" placeholder="请选择难易程度" style="width: 150px;">
      <el-option
          v-for="item in difficultyOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      ></el-option>
    </el-select>
  </div>

  <div class="container" style="margin-top: 100px;margin-left: 150px;">
    <!-- 保存和继续出题按钮 -->
    <div class="action-buttons" >
      <el-button type="primary" @click="saveQuestion ">保存出题</el-button>
    </div>
  </div>

</template>

<style scoped>
.right-links el-link {
  margin-left: 20px; /* 可根据需要调整间距 */
}

.question-type button {
  background-color: #fff;
  color: #000;
  padding: 5px 10px;
  border: 1px solid #ccc;
  cursor: pointer;
}

.question-type button.selected {
  background-color: #8bc34a;
  color: #fff;
}

.action-buttons button {
  margin-right: 10px;
  padding: 10px 20px;
  cursor: pointer;
}
.container {
  display: flex;
  float: left;
  flex-direction: column;
  align-items: center;
}
.options-container div {
  margin-bottom: 10px; /* Adjust the margin as needed */
}
.stem{
  display: flex;
  align-items: center;
}
.option{
  display: flex;
  align-items: center;
  margin-left: 30px;
}
.question-text{
  margin-right: 10px;
  font-size: 18px;
  margin-left: 30px;
}
.question-text1{
  margin-right: 10px;
  font-size: 18px;
  margin-left: 80px;

}

</style>

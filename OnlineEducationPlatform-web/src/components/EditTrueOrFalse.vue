<!--判断题编辑组件-->
<script>
import TinyMCEEditor from "../components/TinyMCEEditor.vue";
export default {
  name:'TrueOrFalse',
  data(){
    return{
      FacilityValue:'',
      FacilityOptions:[
        { value: '正确', label: '正确' },
        { value: '错误', label: '错误' }
      ],
      selectedDifficulty: '简单', // 存储选中的难易程度
      difficultyOptions: [
        { value: '简单', label: '简单' },
        { value: '中等', label: '中等' },
        { value: '困难', label: '困难' }
      ],
      selectedFailure:'',
      FailureOptions:[
        { value: '正确', label: '正确' },
        { value: '错误', label: '错误' },
      ],
      stemContent:'',
      creator: '',
      currentDate: '',
      parentId: 0,
      answerContent:'',
      courseId:0,
      operatorType:0,  //题目编辑操作类型,0表示添加题目,1表示编辑题目
      questionItem:'',
      user:null
    }
  },
  mounted() {
    this.creator = localStorage.getItem("username");
    this.currentDate = new Date().toISOString().split('T')[0];
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
    this.questionItem = JSON.parse(localStorage.getItem('questionItem'));
    console.log("EditSingleChoice mounted folderItem: ",this.folderItem);
    this.parentId = this.folderItem.id;
    this.courseId = localStorage.getItem("courseID");
    this.user = JSON.parse(localStorage.getItem("user"));

    if(this.question!=null){//如果quesiton存在则进入题目编辑状态否则进入添加题目状态
      this.operatorType = 1;
      this.stemContent = this.question.content;
      this.selectAnswer();
      this.selectLevel();
    }
  },
  components:{
    TinyMCEEditor
  },
  props:{
    question:{
      type: Object, // 类型为对象
      required: true // 必须要传递
    },
  },
  methods: {
    selectLevel(){//获取题目难度
      this.selectedDifficulty = this.question.level;
    },
    selectAnswer(){获取题目答案
      this.selectedFailure = this.question.answer;
    },
    updateEditorContent(content) {//从子组件获得更新消息
      this.stemContent = content;
    },
    saveQuestion() {//保存题目或更新题目
      if(this.operatorType===0) {
        this.addQuestion();
      }else{
        this.updateQuestion();
      }
    },
    addQuestion(){//添加题目
      this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
      this.parentId = this.folderItem.id;

      let formData = new FormData();
      formData.append("answer", this.selectedFailure);
      formData.append("option1",'');
      formData.append("option2", '');
      formData.append("option3", '');
      formData.append("option4", '');
      formData.append("questionType", 2);
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
              console.log('判断题保存成功');
              localStorage.setItem("courseActiveTab","Resource");
              this.$router.push('/teacher-course-detail');
            }
          }).catch(error => {
        console.error('判断题保存失败', error);
      });
    },
    updateQuestion(){//更新题目
      let formData = new FormData();
      formData.append("answerContent", this.selectedFailure);
      formData.append("option1", '');
      formData.append("option2", '');
      formData.append("option3", '');
      formData.append("option4",'');
      formData.append("questionId",this.questionItem.id);
      formData.append("stemContent", this.stemContent);
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/question/updateQuestion', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('判断题编辑成功');
              localStorage.setItem("courseActiveTab","Resource");
              this.$router.push('/teacher-course-detail');
            }
          }).catch(error => {
        console.error('判断题编辑失败', error);
      });
    },

  }
};
</script>

<template>
  <div>
    <div class="stem" style="margin-top: 10px">
      <span class="question-text">题干:</span>
      <TinyMCEEditor v-model="stemContent" :initialContent="stemContent" @editorContentChange="updateEditorContent($event)"></TinyMCEEditor>
    </div>
    <div style="margin-top: 30px; margin-bottom:  20px;display: flex;">
      <span class="question-text">答案:</span>
      <el-select v-model="selectedFailure" placeholder="请选择答案" style="width: 150px;">
        <el-option
            v-for="item in FailureOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        ></el-option>
      </el-select>
    </div>
    <div style="margin-top: 30px;display: flex;">
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


    <div class="container">
      <!-- 保存和继续出题按钮 -->
      <div class="action-buttons" style="margin-top: 100px;margin-left: 50px;">
        <el-button type="primary" @click="saveQuestion">保存出题</el-button>
      </div>
    </div>
  </div>
</template>

<style>
right-links el-link {
  margin-left: 20px; /* 可根据需要调整间距 */
}

.question-type button {
  background-color: #fff;
  color: #000;
  padding: 5px 10px;
  border: 1px solid #ccc;
  cursor: pointer;
}

.question-type button {
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

.stem {
  display: flex;
  align-items: center;
}

.question-text{
  margin-right: 10px;
  font-size: 18px;
  margin-left: 200px;
}
.question-text1{
  margin-right: 10px;
  font-size: 18px;
  margin-left: 200px;

}
</style>

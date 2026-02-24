<!--题目信息预览组件-->
<template>
  <div class="preview-container">
    <!-- 题目预览标题 -->
    <div class="preview-title">
      <h4  class="show-view">题目预览</h4>
      <el-icon class="edit-button" @click="EditQuestion"><Edit /></el-icon>
      <el-link class="question-goback" type="primary" @click="handleBack">返回</el-link>
    </div>

    <!-- 题目类型和内容 -->
    <el-card v-if="question" class="square-card">
      <div class="question-preview">
        <h2 class="question-type">{{  switchQuestionType( this.questionItem.questiontype) }}</h2>
        <p v-html="question.content" class="question-stem"></p>
        <div class="options">
          <div v-if="question.option1" class="option">
            <span class="option-label">A:</span> <span v-html="question.option1" class="option-content"></span>
          </div>
          <div v-if="question.option2" class="option">
            <span class="option-label">B:</span> <span v-html="question.option2" class="option-content"></span>
          </div>
          <div v-if="question.option3" class="option">
            <span class="option-label">C:</span> <span v-html="question.option3" class="option-content"></span>
          </div>
          <div v-if="question.option4" class="option">
            <span class="option-label">D:</span> <span v-html="question.option4" class="option-content"></span>
          </div>
        </div>
        <div class="answer">
          <span class="answer-label">正确答案:</span>
          <span class="answer-content" v-html="question.answer"></span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      question: null,
      questionItem: null,
    };
  },
  mounted() {
    // 获取保存在localStorage中的JSON字符串形式的对象
    this.questionItem = JSON.parse(localStorage.getItem('questionItem'));
    this.selectQuestionById();
  },
  methods:{
    switchQuestionType(questionType) {//把数字转为汉字
      switch(questionType) {
        case 0:return "单选题";
        case 1:return "多选题";
        case 2:return "判断题";
        case 3:return "填空题";
        case 4:return "简答题";
        default:return "未知题型";
      }
    },

    selectQuestionById(){//查询某道题的题目信息
      let formData = new FormData();
      formData.append("questionId", this.questionItem.id);
      this.$axios.post('/question/select', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.question = response.data.data;
              console.log('题目信息保存成功');
            }
          }).catch(error => {
        console.error('题目信息保存失败', error);
      });
    },
    handleBack(){//返回上一页
      localStorage.setItem("courseActiveTab","Resource");
      this.$router.push('/teacher-course-detail');
    },
    EditQuestion(){//编辑题目信息
      // 将question对象存储到 localStorage,需要先转成JSON字符串
      localStorage.setItem('questionItem', JSON.stringify(this.questionItem));
      // 如果是题目，跳转到编辑页面
      this.$router.push('/questionList-edit');
    }
  },
};
</script>

<style scoped>
.preview-title{
  text-align: left; /* 将内容左对齐 */
}
.show-view{
  color: gray;
  display: inline-block;
  margin-right: 10px;
  font-size: 24px;
}
.edit-button{
  font-size:24px;
  color:blue
}
.square-card{
  width: 1200px;
  height: 1300px;
  margin-top: 10px;
}
.question-preview {
  text-align: left; /* 将内容左对齐 */
}
.question-type{
  color: #409EFF;
  font-size: 24px;
}
.question-stem{
  margin-top: 15px;
  font-size: 22px;
  font-weight: bold;
}
.option {
  display: flex; /* 使用 Flex 布局 */
  align-items: center; /* 垂直居中选项内容 */
  margin-bottom: 10px; /* 添加选项之间的垂直间距 */
  margin-top: 10px;
  font-size: 20px;
}

.option-label {
  flex: 0 0 auto; /* 禁止选项标签自动扩展 */
  margin-right: 5px; /* 右侧间距 */
  line-height: 1; /* 将行高设置为1，以解决可能的对齐问题 */
}

.option-content {
  flex: 1; /* 选项内容自动填充剩余空间 */
  margin-top: 15px;
}

.options {
  display: flex; /* 使用 Flex 布局 */
  flex-direction: column; /* 垂直排列选项 */
}
.answer {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-top: 10px;
  font-size: 20px;
  font-weight: bold; /* 加粗 */
  text-align: left; /* 将内容左对齐 */
}

.answer-label {
  flex: 0 0 auto; /* 禁止自动扩展 */
  margin-right: 5px; /* 右侧间距 */
  margin-top: 16px;
}

.answer-content{
  margin-top: 16px;
  margin-left: 10px;
}
.question-goback{
  float: right;
  font-size: 24px;
}
</style>

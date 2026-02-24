<!--创建作业界面-->
<template>
  <div id="task-dashboard">
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
          <TeacherDetailHeader></TeacherDetailHeader>
      </el-header>
      <div style="display: flex; align-items: center; margin-bottom: 20px; margin-top: 20px;">
        <span style="font-size: 20px;">标题:<el-input type="text" class="custom-input" v-model="title"></el-input></span>
        <el-button type="primary" style="margin-left: auto; margin-right: 10px; font-size: 20px;" @click="saveTask()">保存并返回</el-button>
        <el-button type="primary" style="font-size: 20px;">预览</el-button>
      </div>
      <el-container>
        <!-- 侧边栏 -->
        <el-aside class="sidebar">
          <span style="font-size: 20px">题量:{{count}}</span>
          <span style="margin-left: 30px;font-size: 20px">总分:{{total}}</span>
          <div class="left-list">
            <ul>
              <li v-for="(item, index) in questionList" :key="index" @click="handleListItem(index,item)" >{{index + 1 }}.{{ showStemContent(item.stem) }}
                <el-icon @click="deleteQuestion(index)"><Delete /></el-icon>
              </li>

            </ul>

          </div>
        </el-aside>
        <!-- 中间部分 -->
        <el-main class="main">
          <div class="right-list">
            <el-button type="primary" @click="handleQuestionTypeClick(0)" class="question-button" style="background: #3498db;color: white">单选题</el-button>
            <el-button type="primary" @click="handleQuestionTypeClick(1)" class="question-button" style="background: #3498db;color: white">多选题</el-button>
            <el-button type="primary" @click="handleQuestionTypeClick(2)" class="question-button" style="background: #3498db;color: white">判断题</el-button>
            <el-button type="primary" @click="handleQuestionTypeClick(3)" class="question-button" style="background: #3498db;color: white">填空题</el-button>
            <el-button type="primary" @click="handleQuestionTypeClick(4)" class="question-button" style="background: #3498db;color: white">简答题</el-button>
          </div>
          <div>
            <div v-if="this.question.type != -1">
              <div class="question-container">
                <div class="stem" style="margin-top: 10px;margin-left: 200px">
                  <span>打分:<el-input type="text" placeholder="5" class="custom-input1" v-model="score"></el-input></span>
                </div>
                <div>
                  <div class="stem" style="margin-top: 10px">
                    <span style="" class="question-text">题干:</span>
                    <TinyMCEEditor v-model="question.stem" :initialContent="question.stem" @editorContentChange="updateEditorContent('stem',0,$event)"></TinyMCEEditor>
                  </div>
                </div>
                <!--选择题选项控件-->
                <div class="options-container" v-if="this.question.type === 0 || this.question.type === 1">
                  <div class="option" v-for="(option, index) in question.options" :key="index" :style="{ marginTop: '60px' }">
                    <el-radio v-if="this.question.type === 0" v-model="question.answer" :label="option.label" class="question-text">{{ option.label }}</el-radio>
                    <el-checkbox v-if="this.question.type === 1" v-model="question.selectOptions" :label="option.label" class="question-text">{{ option.label }}</el-checkbox>
                    <TinyMCEEditor v-model="option.content"  :initialContent="option.content" @editorContentChange="updateEditorContent('option',index,$event)"></TinyMCEEditor>
                  </div>
                </div>
                <!--判断题控件-->
                <div style="margin-top: 30px; margin-bottom:  20px;display: flex;" v-if="this.question.type === 2">
                  <span class="question-text">答案:</span>
                  <el-select v-model="question.answer" placeholder="请选择答案" style="width: 150px;">
                    <el-option
                        v-for="item in FailureOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    ></el-option>
                  </el-select>
                </div>
                <!--简答题/填空题控件-->
                <div class="stem" style="margin-top: 10px" v-if="this.question.type === 3 || this.question.type === 4">
                  <span  class="question-text">答案:</span>
                  <TinyMCEEditor v-model="question.answer"  :initialContent="question.answer" @editorContentChange="updateEditorContent('answer',0,$event)"></TinyMCEEditor>
                </div>
              </div>
              <!--难易程度-->
              <div style="margin-top: 30px;float: left;">
                <span class="question-text1"> 难易程度:</span>
                <el-select v-model="question.level" placeholder="请选择难易程度" style="width: 100px;margin-left: 10px;font-size: 20px" >
                  <el-option
                      v-for="item in difficultyOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </div>
            <!-- 保存出题按钮 -->
            <div class="container" style="margin-top: 100px;margin-left: 150px;">
              <div class="action-buttons" style="margin-top: 100px;text-align: center">
                <el-button type="primary" @click="saveQuestion">保存该题</el-button>
              </div>
            </div>
          </div>

        </el-main>
      </el-container>
      <!-- 尾部 -->
      <el-footer class="footer" >
      </el-footer>

    </el-container>
  </div>
</template>

<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
import TinyMCEEditor from "../components/TinyMCEEditor.vue";

export default {
  components: {
    TinyMCEEditor,
    TeacherDetailHeader,
  },
  data() {
    return {
      questionList:[],//右边栏题目列表数据
      selectedOption: [], // 存储用户选择的选项标签
      difficultyOptions: [
        { value: '简单', label: '简单' },
        { value: '中等', label: '中等' },
        { value: '困难', label: '困难' }
      ],
      FailureOptions:[
        { value: '正确', label: '正确' },
        { value: '错误', label: '错误' }
      ],
      creator: '',
      currentDate: '',
      courseId:0,
      score:5,
      question:{
        id:-1,
        type:-1,
        score: 5,
        stem:'',
        options: [
          { label: 'A', content: '' }, // A选项
          { label: 'B', content: '' }, // B选项
          { label: 'C', content: '' }, // C选项
          { label: 'D', content: '' }  // D选项
        ],
        answer:'',
        selectOptions:[],//为多选题准备答案数组
        level:'容易',
      },
      currentIndex:0,//左边栏列表当前选项
      count:0,//当前作业题目数
      total:0,//当前作业总分
      title:'',
      user:null
    };
  },
  watch: {
    score(newValue, oldValue) {//观察tavView的信息是否改变
      this.total -=this.questionList[this.currentIndex].score;
      console.log("score",this.questionList[this.currentIndex].score);
      console.log("newValue",newValue);
      console.log("oldValue",oldValue);
      this.total +=  parseInt(newValue);
      this.questionList[this.currentIndex].score =  parseInt(newValue);
    }
  },
  created() {
    this.creator = localStorage.getItem("username");
    this.currentDate = new Date().toISOString().split('T')[0];
    this.courseId = parseInt(localStorage.getItem("courseID"));
    this.user = JSON.parse(localStorage.getItem("user"));
    this.genTaskTitle();
  },
  methods: {
    handleListItem(index,item){//处理点击列表项
      this.question = item;
      this.currentIndex = index;
      this.score = item.score;
    },
    clearQuestionInfo(){
      this.question={
          id:-1,
          type:0,
          score: 5,
          stem:'',
          options: [
            { label: 'A', content: '' }, // A选项
            { label: 'B', content: '' }, // B选项
            { label: 'C', content: '' }, // C选项
            { label: 'D', content: '' }  // D选项
          ],
          answer:'',
          selectOptions: [],
          level:'容易',
      };
    },
    handleQuestionTypeClick(type) {//创建不同类型的题目
      this.clearQuestionInfo();//清空当前题目
      this.score = 5;
      this.question.type = type;
      // 将选中的题型添加到数组中
      this.question.stem = this.getQuestionTypeName(type);
      this.questionList.push(this.question);

      this.currentIndex = this.questionList.length-1;
      this.count = this.questionList.length;
      this.total +=  parseInt(this.score);
    },
    getQuestionTypeName(type) {
      switch (type) {
        case 0:return '单选题';
        case 1:return '多选题';
        case 2:return '判断题';
        case 3:return '填空题';
        case 4:return '简答题';
        default:return '';
      }
    },
    // 保存问题
    saveQuestion() {
      let formData = new FormData();
      console.log("saveQuestion", this.question);
      formData.append("questionType", this.question.type);
      formData.append("content", this.question.stem);
      formData.append("answer", this.question.answer);
      if(this.question.type===1)
        formData.append("answer", this.question.selectOptions);
      formData.append("option1", this.question.options[0].content);
      formData.append("option2", this.question.options[1].content);
      formData.append("option3", this.question.options[2].content);
      formData.append("option4", this.question.options[3].content);
      formData.append("type", 'question');
      formData.append("level", this.question.level);

      formData.append("folderId", 0);
      formData.append("createTime", this.currentDate);
      formData.append("creator", this.creator);
      formData.append("courseId", this.courseId);
      formData.append("userId",this.user.id);
      this.$axios.post('/question/add', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.question.id = response.data.data;//得到新建questionId
              this.questionList[this.currentIndex] = this.question;//更新右边栏列表数据
              this.questionList[this.currentIndex].score =  parseInt(this.score);//保存每道题的分数
              console.log('保存题目成功');
            }
          }).catch(error => {
        console.error('保存题目失败', error);
      });
    },
    deleteQuestion(index){//删除某移到题目
      this.count--;
      this.total -= this.questionList[index].score;
      this.questionList.splice(index,1);//删除列表中的某一项
    },
    updateEditorContent(type, index, content){//编辑器内容更新处理时间
      if(type === 'stem'){//保存题干信息
        this.question.stem = content;
      }else if(type === 'option'){//保存单选多选选项信息
        this.question.options[index].content = content;
      }else if(type=='answer'){//保存答案
        this.question.answer = content;
      }
    },
    saveTask(){//保存作业
      const data ={
        taskName:this.title,
        totalScore:this.total,
        courseId:this.courseId,
        createTime:this.currentDate,
        creator:this.creator,
        folderId:0,
        questions:this.getQuestionData(),
        userId:this.user.id
      }
      this.$axios.post('/task/saveTask', data)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('保存作业成功');
              localStorage.setItem("courseActiveTab","Resource");
              localStorage.setItem('resourceActiveTab','taskBank');
              this.$router.push('/teacher-course-detail');

            }
          }).catch(error => {
        console.error('保存作业失败', error);
      });
    },
    showStemContent(html){//显示题干文字(去掉html元素)
      // 创建一个临时 div 元素
      const tempDiv = document.createElement('div');
      // 将 HTML 字符串赋值给临时 div 的 innerHTML 属性
      tempDiv.innerHTML = html;
      // 查找第一个段落元素并提取其纯文本内容
      const paragraphs = tempDiv.querySelectorAll('p');
      if (paragraphs.length > 0) {
        const text = paragraphs[0].textContent || paragraphs[0].innerText || '';
        if(text.length<20)
          return text;
        else
          return text.slice(0,5)+'...';
      } else {
        return html;
      }
    },
    getCurrentDataTime(){//获取当前时间
      const currentDate = new Date();
      // 获取当前日期
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始计数，需要加1
      const day = String(currentDate.getDate()).padStart(2, '0');

      // 获取当前时间
      const hour = String(currentDate.getHours()).padStart(2, '0');
      const minute = String(currentDate.getMinutes()).padStart(2, '0');
      const second = String(currentDate.getSeconds()).padStart(2, '0');

      // 格式化日期和时间
      let currentDateTime = `${year}${month}${day}${hour}${minute}${second}`;
      return currentDateTime;
    },
    genTaskTitle(){
      let currentTime = this.getCurrentDataTime();
      this.title ='新建作业'+currentTime;
    },
    getQuestionData(){
      let questions = [];
      for(let i=0;i<this.questionList.length;i++){
        let item = {
          id:this.questionList[i].id,
          score:this.questionList[i].score,
        };
        questions.push(item);
      }
      return questions;
    }
  },
};
</script>

<style scoped>

.left-list {
  margin-top: 20px;
  display: flex;
}

.question-button {
  margin-right: 10px;
  font-size: 20px;
}
.right-list {
  align-items: center;
  display: flex;
  top: 0;
  left: 0;
  width: 200px; /* Adjust width as needed */
  padding: 10px;
  margin-left: 220px; /* Adjust margin to create space for the fixed div */
}

.left-list {

  display: flex;
}
.left-list ul {
  list-style-type: none; /* Remove bullet points */
}
.header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #409EFF;
}
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


.action-buttons button {
  margin-right: 10px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 20px
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
}
.options{
  display: flex;
  align-items: center;
  margin-left: 200px;
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
.question-container{
  min-height: 40vh; /* 确保容器足够高 */
}
.options-container{
  max-height: 40vh; /* 限制最大高度 */
  overflow: auto; /* 超出部分可滚动 */

}
.custom-input1{
  width:100px;
  font-size: 20px;
}
.custom-input {
  width: 300px; /* 调整输入框的大小 */
  font-size: 20px;
}
.sidebar {
  width: 200px;
}
.main{
  min-width: 1000px;
  min-height: 1000px;
}
.footer {
  background-color: #F0F2F5;
  padding: 10px;
  text-align: center;
  margin-top: 100px
}

</style>

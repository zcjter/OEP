<!--自动组卷组件-->
<script>
import TeacherDetailHeader from "../components/TeacherDetailHeader.vue";
export default {
  data() {
    return {
      controlParam:[//用来控制后台请求次数
        {
          radioValue:0,
          method:[
            {flag:0},
            {flag:0},
          ],
        },
        {
          radioValue:0,
          method:[
            {flag:0},
            {flag:0},
          ],
        },
        {
          radioValue:0,
          method:[
            {flag:0},
            {flag:0},
          ],
        },
        {
          radioValue:0,
          method:[
            {flag:0},
            {flag:0},
          ],
        },
        {
          radioValue:0,
          method:[
            {flag:0},
            {flag:0},
          ],
        },
      ],
      questionInfo: [//用来保存本地选题信息，用于后台请求数据
        {
          type:0,  	//单选题
          score:0,  	//题型总分
          method:-1,	//选题方式：从题库选(0)，从文件夹选(1)
          count: 0,	//从题库抽题数量
          //文件夹列表  用于从文件夹选题
          items: []
        },
        {
          type:1,  	//多选题
          score:0,  	//题型总分
          method:-1,	//选题方式：从题库选(0)，从文件夹选(1)
          count: 0,	//从题库抽题数量
          //文件夹列表  用于从文件夹选题
          items: []
        },
        {
          type:2,  	//判断题
          score:0,  	//题型总分
          method:-1,	//选题方式：从题库选(0)，从文件夹选(1)
          count: 0,	//从题库抽题数量
          //文件夹列表  用于从文件夹选题
          items: []
        },
        {
          type:3,  	//填空题
          score:0,  	//题型总分
          method:-1,	//选题方式：从题库选(0)，从文件夹选(1)
          count: 0,	//从题库抽题数量
          //文件夹列表  用于从文件夹选题
          items: []
        },
        {
          type:4,  	//简答题
          score:0,  	//题型总分
          method:-1,	//选题方式：从题库选(0)，从文件夹选(1)
          count: 0,	//从题库抽题数量
          //文件夹列表  用于从文件夹选题
          items: []
        },
      ],
      examInfo:{//试卷信息
        title:'',//试卷标题
        score:100,//总分
        count:0,//试卷份数
        level:'简单',
        courseId:this.courseId,
        creator:this.creator,
        createtime:this.currentDate,
        folderid:0,
      },
      courseName:'',
      courseId:0,
      creator:'',
      currentDate:'',
      responseData:[
        {
          questionCount:0,//从题库选题时，该题库选择题总量
          folderExData:null, //从文件夹选题时，选择题的文件夹列表
        },
        {
          questionCount:0,//从题库选题时，该题库某类题型总量
          folderExData:null, //从文件夹选题时，某类题型的文件夹列表
        },
        {
          questionCount:0,//从题库选题时，该题库某类题型总量
          folderExData:null, //从文件夹选题时，某类题型的文件夹列表
        },
        {
          questionCount:0,//从题库选题时，该题库某类题型总量
          folderExData:null, //从文件夹选题时，某类题型的文件夹列表
        },
        {
          questionCount:0,//从题库选题时，该题库某类题型总量
          folderExData:null, //从文件夹选题时，某类题型的文件夹列表
        },
      ],
      user:{}
    };
  },
  components:{
      TeacherDetailHeader
  },
  created() {
    this.courseName = localStorage.getItem("courseName");
    this.courseId = parseInt(localStorage.getItem("courseID"));
    this.creator = localStorage.getItem("username");
    this.currentDate = new Date().toISOString().split('T')[0];
    this.user.id = localStorage.getItem("userID");
    this.clearQuestion();
    this.genTaskTitle();
  },
  methods:{
    SetSelectQuestionMethod(type, method){//设置选题方法
      this.questionInfo[type].method = method;
      if(method===0){
        if(this.controlParam[type].method[method].flag===0)//只有选题方式没有选过时，才从后端获取
          this.getQuestionCount(type);
      }
      else{
        if(this.controlParam[type].method[method].flag===0)
          this.getQuestionCountFromFolder(type);
        this.questionInfo[type].items.clear();
      }
      this.controlParam[type].method[method].flag = 1;//标注该种选题方式已经选过
    },
    getQuestionCount(type) {//得到某种题型题目数量
        let formData = new FormData();
        formData.append("questionType", type);
        formData.append("courseId",this.courseId);
        this.$axios.post('/question/countQuestionType', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.responseData[type].questionCount = response.data.data; // 设置题目数量
                console.log('统计题目类型数量成功');
              }
            }).catch(error => {
              console.error('统计题目类型数量失败', error);
        });
    },
    getQuestionCountFromFolder(type){//得到某个文件夹下题目数量
      let formData = new FormData();
      formData.append("questionType", type);
      formData.append("courseId",this.courseId);
      this.$axios.post('/folder/selectAllByQuestionType', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.responseData[type].folderExData = response.data.data; // 设置题目数量
              console.log("folderExData",this.responseData[type].folderExData);
              console.log('获取该题型文件夹数量成功');
            }
          }).catch(error => {
        console.error('获取该题型文件夹数量失败', error);
      });
    },
    showQuestionTypeName(type){//把数字变为汉字
      switch (type){
        case 0:return "单选题";
        case 1:return "多选题";
        case 2:return "判断题";
        case 3:return "填空题";
        case 4:return "简答题";
        default:return "未知题型";
      }
    },
    handleSave(){//保存并创建试卷
      this.getQuestionFolderInfo();
      const data={
        examInfo:this.examInfo,
        questionInfo:this.questionInfo,
        userId:this.user.id,
        courseId:this.courseId
      };
      console.log("classItems questionList",data.questionInfo);
      this.$axios.post('/exam/create', data)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('创建试卷成功');
              localStorage.setItem("courseActiveTab","Resource");
              localStorage.setItem('resourceActiveTab','examPaperBank');
              this.$router.push('/teacher-course-detail');
            }
          }).catch(error => {
            console.error('创建试卷失败', error);
      });
    },
    getQuestionFolderInfo(){//获取文件夹抽题方式的文件夹信息
      this.examInfo.count = parseInt(this.examInfo.count);
      this.examInfo.score = parseInt(this.examInfo.score);
      this.examInfo.courseId = parseInt(this.courseId);
      this.examInfo.creator = this.creator;
      this.examInfo.createtime = this.currentDate;
      this.examInfo.folderid = 0;
      for(let i=0;i<5;i++){
        this.questionInfo[i].score = parseInt(this.questionInfo[i].score);
        this.questionInfo[i].count = parseInt(this.questionInfo[i].count);
        if(this.questionInfo[i].method===1){
          this.responseData[i].folderExData.forEach(item=>{
            console.log("item:",item);
            if(item.selcount>0) {
              let section = {
                folderId: item.id,
                count: parseInt(item.selcount),
              };
              this.questionInfo[i].items.push(section);
            }
          });
        }
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
    genTaskTitle(){//得到试卷标题
      let currentTime = this.getCurrentDataTime();
      this.examInfo.title ='新建试卷'+currentTime;
    },
    clearQuestion(){//清空试卷内容
      this.examInfo.title = '';
      this.examInfo.count = 0;
      this.examInfo.courseId =0;
      for(let i=0;i<5;i++){
        this.questionInfo[i].score = 0;
        this.questionInfo[i].count = 0;
        this.questionInfo[i].method = -1;
        this.questionInfo[i].items = [];
        this.responseData[i].folderExData=null;
      }
    },
    handleBack(){//返回上一级
      this.$router.push('/exam-paper-create')
    }
  }
};
</script>

<template>
  <el-header class="header">
    <TeacherDetailHeader></TeacherDetailHeader>
  </el-header>
  <el-main>
    <div class="header-content">
      <!-- 左侧文本 -->
      <span>设置试卷结构 </span>
      <!-- 右侧返回链接 -->
      <el-link type="primary" class="header-right" icon="el-icon-arrow-left" @click="handleBack">返回</el-link>
    </div>
    <hr class="underline" />
    <div class="main-content">
        <div class="input-wrapper">
          <span>试卷标题:</span>
          <el-input  v-model="examInfo.title" type="text" placeholder="请输入4-40个字符" class="custom-input"></el-input>
        </div>
        <div class="input-wrapper">
          <span>满&ensp;&ensp;&ensp;&ensp;分:</span>
          <el-input  v-model="examInfo.score" type="text" placeholder="100" class="custom-input1"></el-input>
          <span>分</span>
        </div>
        <div class="input-wrapper">
          <span>随机组卷数量:</span>
          <el-input  v-model="examInfo.count" type="text" class="custom-input2"></el-input>
        </div>
        <div class="input-wrapper">
          <span>试卷难易程度:</span>
          <el-select v-model="examInfo.level" placeholder="请选择" class="custom-input2">
            <el-option label="简单" value="简单"></el-option>
            <el-option label="中等" value="中等"></el-option>
            <el-option label="困难" value="困难"></el-option>
          </el-select>
        </div>
    </div>
    <div v-for="pos in 5" :key="pos" >
      <div class="bordered-section">
        <div class="form-content">
          <el-form class="el-form-content">
            <div class="input-wrapper1">
              <span>{{showQuestionTypeName(pos-1)}}</span>
            </div>
            <div class="input-wrapper1">
              <span>总分:</span>
              <el-input  v-model="questionInfo[pos-1].score" type="text" class="custom-input3"></el-input>
            </div>
            <div class="radio-wrapper">
                <el-radio v-model="controlParam[pos-1].radioValue" label="选项一" size="large" @change="SetSelectQuestionMethod(pos-1,0)">
                  <span class="radio-span">从题库选择</span>
                </el-radio>
              <div v-if="questionInfo[pos-1].method===0" class="content-wrapper">
                <span>共{{ responseData[pos-1].questionCount }}道</span>
                <span>抽</span>
                <el-input  v-model="questionInfo[pos-1].count" type="text" class="custom-input3"></el-input>
                <span>道</span>
              </div>
            </div>
            <div class="radio-wrapper">
              <el-radio v-model="controlParam[pos-1].radioValue" label="选项卡2" size="large" @change="SetSelectQuestionMethod(pos-1,1)">
                <span class="radio-span">从题库指定目录选题 </span>
              </el-radio>
              <div v-if="questionInfo[pos-1].method===1" class="content-wrapper">
                <div v-for="(folderItem, index) in responseData[pos-1].folderExData" :key="index" class="folder-item">
                  <span>{{folderItem.name}}(共{{ folderItem.count }}道)</span>
                  <span>抽</span>
                  <el-input v-model="folderItem.selcount" class="custom-input3" type="text"></el-input>
                  <span>道</span>
                </div>
              </div>
            </div>
          </el-form>
        </div>
      </div>
    </div>
    <div class="el-bottom">
      <el-button type="primary" class="el-bottom-button" @click="handleSave">保存</el-button>
      <el-button type="primary" class="el-bottom-button">保存并发放</el-button>
    </div>
  </el-main>
</template>

<style scoped>
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 20px;
}

.header-right{
  font-size: 20px;
}
.underline {
  border: none;
  height: 1px;
  background-color: gray;
  margin-top: 20px; /* 调整下划线与按钮组之间的间距 */
}

header-links {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
}
.right-links el-link {
  margin-left: 20px; /* 可根据需要调整间距 */
}

.action-buttons button {
  margin-right: 10px;
  padding: 10px 20px;
  cursor: pointer;
}
header-top {
  flex: 1;
}
.header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #409EFF;
}
.input-wrapper {
  margin-top: 20px;
  margin-left: 100px;
  display: flex;
  align-items: center;
  font-size: 20px;
}

.custom-input {
  width: 300px; /* 调整输入框的大小 */
  margin-left: 60px; /* 调整输入框与前面 span 元素之间的间距 */
  font-size: 20px;
}
.custom-input1{
  width:100px;
  margin-left: 60px; /* 调整输入框与前面 span 元素之间的间距 */
  font-size: 20px;
  margin-right: 10px;
}
.custom-input2{
  width: 150px;
  margin-left: 20px;
  font-size: 20px;
}
.main-content{
  margin-top: 40px;
}

.form-content{
  margin-top: -1px;
  display: flex;

}
.el-form-content{
  float: left;
  margin-left:100px ;
}
.input-wrapper1{
  display: flex;
  align-items: center;
  font-size: 20px;
  margin-top: 20px;
}
.input-wrapper1 span {
  margin-right: 10px; /* Adjust spacing between elements */
}
.custom-input3{
  width:100px;
  margin-left: 20px; /* 调整输入框与前面 span 元素之间的间距 */
  font-size: 20px;
  margin-right: 30px;
}
.radio-span{
  font-size: 20px;
}
.radio-wrapper {
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* Allow items to wrap if necessary */
}

.content-wrapper {
  display: flex;
  flex-wrap: wrap;
  font-size: 18px;
}

.folder-item {
  margin-top: 10px; /* 添加 margin-top */
  display: flex;
  align-items: center;
  font-size:18px;
}

.folder-item > span {
  white-space: nowrap;
}

.bordered-section {
  border: 1px solid #ccc;
  margin-top: 20px;
  margin-left: 80px;
  width:900px;
  height: 300px;
  display: flex;
}
.el-bottom{
  margin-top: 40px;
  float: right;
  margin-right: 40px;
}
.el-bottom-button{
  font-size: 20px;
}
/* 改变选中状态时的圆点颜色 */
.el-radio.is-checked .el-radio__inner::after {
  background-color: #007bff; /* 选中状态下的圆点色 */
}
</style>

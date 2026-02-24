<!--题库首页显示组件-->
<script>
import {ElMessage, ElMessageBox} from "element-plus";
import PopQuestionBankMove from "./PopQuestionBankMove.vue"



export default {
  name: 'QuestionInformation',
  components: {
    PopQuestionBankMove
  },
  data() {
    return {
      courseName:'',
      QuestionBank:false,
      JobLibrary:false,
      ExamPaperLibrary:false,
      AllQuestionTypes: '',
      FacilityValue:'',
      YesOrNo:'',
      searchText:'',
      tableData: [],
      courseId:0,
      creator:'',
      currentDate:'',
      folderItem: {
        id:0,
        name:'所有目录',
      },
      selectedItem: null,
      user:null
    };
  },
  mounted() {
    this.courseId = localStorage.getItem("courseID");
    this.creator = localStorage.getItem("username");
    this.currentDate = new Date().toISOString().split('T')[0];
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
    this.user = JSON.parse(localStorage.getItem("user"))
    this.getData();
  },
  methods: {
    addItem() {//添加文件夹操作操作
      const newFolder = {
        name: '新目录',
        operator: 'add',
        editing: true, // 设置为编辑模式
        // 其他必要的属性
      };
      // 在第一条记录后插入新记录
      this.tableData.splice(1, 0, newFolder);
    },
    moveItem(message){//移动操作
      const classItem = JSON.parse(message);//将JSON格式的字符串转换成对象数据
      if (classItem.folderId === null) {
        ElMessage.error('请选择目标文件夹');
        return;
      }
      if(classItem.folderId===this.folderItem.id){
        ElMessage.error('已经在该文件夹下，不需要移动');
        return;
      }
      let formData = new FormData();
      formData.append("folderId",classItem.folderId);
      formData.append("id",classItem.id);
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      var urlstr = '/question/questionMove';
      // 发送移动请求到后端
      if(classItem.type === 'folder'){
        urlstr = '/folder/move';
      }
      this.$axios.post(urlstr, formData)
          .then(response => {
            if (response.data.code === 20000) {
              // 处理成功上传的响应
              ElMessage.success('移动操作成功');
              this.getData();
            }
          }).catch(error => {
        console.error('移动操作错误:', error);
      });
    },
    copyItem(item) {//复制题目信息
      let formData = new FormData();
      formData.append("questionId", item.id); // 添加文件夹的 ID
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/question/copy', formData)
          .then(response => {
            if (response.data.code === 20000) {
              // 更新 folder 数据，比如添加 id
              this.getData();
              ElMessage({
                type: 'success',
                message: '复制题目成功',
              })
            }
          }).catch(error => {
        console.error('复制题目错误', error);
      });

    },
    editItem(item) {//编辑题目或文件夹
      if (item.type === 'question') {
        // 将question对象存储到 localStorage,需要先转成JSON字符串
        localStorage.setItem('questionItem', JSON.stringify(item));

        // 如果是题目，跳转到编辑页面
        this.$router.push('/questionList-edit');
      } else if (item.type === 'folder') {
        item.editing = true;
      }
    },
    deleteItem(item) {//删除题目或文件夹信息
      if(item.type === "folder"){
        this.deleteFolder(item);
      }else if (item.type === 'question'){
        this.deleteQuestion(item);
      }
    },
    renameItem(item) {//重命名文件夹
      item.editing = false; // 结束编辑状态
      // 这里可以实现文件夹重命名的逻辑
      if (item.operator === 'add') {
        this.addFolder(item.name);
      } else {
        this.updateFolder(item);
      }
      console.log('重命名文件夹:', item.name);
    },
    updateFolder(item){
      let formData = new FormData();
      formData.append("folderName", item.name);
      formData.append("folderId", item.id); // 添加文件夹的 ID
      formData.append("userId",this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/folder/updateName', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('更新目录成功');
              // 更新 folder 数据，比如添加 id
              this.getData();
              ElMessage({
                type: 'success',
                message: '更新目录成功',
              })
            }
          }).catch(error => {
        console.error('更新目录错误', error);
      });
    },
    addFolder(name) {//保存文件夹
      let formData = new FormData();
      formData.append("FolderName", name);
      formData.append("parentId", this.folderItem.id);
      formData.append("courseId", this.courseId);
      formData.append("createTime", this.currentDate);
      formData.append("creator", this.creator);
      formData.append("type", 'folder');
      formData.append("folderType", 0);
      formData.append("userId",this.user.id);
      this.$axios.post('/folder/add', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('文件夹保存成功');
              // 更新 folder 数据，比如添加 id
              this.getData();
              ElMessage({
                type: 'success',
                message: '添加目录成功',
              })
            }
          }).catch(error => {
        console.error('文件夹保存错误', error);
      });
    },
    deleteFolder(item){
      ElMessageBox.confirm(
          '确定要将选中的文件夹放入回收站吗？',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {//把目录移到回收站
        let formData = new FormData();
        formData.append("folderId", item.id); // 添加文件夹的 ID
        formData.append("dirty",1);
        console.log(item.id);
        this.$axios.post('/folder/updateDirty', formData)
            .then(response => {
              if (response.data.code === 20000) {
                console.log('目录移到回收站成功');
                // 更新 folder 数据，比如添加 id
                this.getData();
                ElMessage({
                  type: 'success',
                  message: '目录移到回收站成功',
                })
              }
            }).catch(error => {
          console.error('目录移到回收站错误', error);
        });
      }).catch(() => {

      })

    },
    deleteQuestion(item){
      ElMessageBox.confirm(
          '确定要将选中的题目放入回收站吗？',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {//把题目移到回收站
        let formData = new FormData();
        formData.append("questionId", item.id); // 添加文件夹的 ID
        formData.append("dirty",1);
        console.log(item.id);
        this.$axios.post('/question/updateDirty', formData)
            .then(response => {
              if (response.data.code === 20000) {
                console.log('目录移到回收站成功');
                // 更新 folder 数据，比如添加 id
                this.getData();
                ElMessage({
                  type: 'success',
                  message: '题目移到回收站成功',
                })
              }
            }).catch(error => {
          console.error('题目移到回收站错误', error);
        });
      }).catch(() => {

      })

    },
    addQuestion() {//进入添加题目界面
      localStorage.removeItem("questionItem");//进入添加题目前清空questionItem
      //跳转到添加题目界面
      this.$router.push({name:"AddQuestionBank"});
    },
    getData() {//获取指定课程的题库文件夹下的文件夹和题目列表
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      formData.append("parentId", this.folderItem.id);
      this.$axios.post('/folder/selectAllQuestion', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              console.log(this.tableData);
              console.log("查询目录信息成功")
            }
          }).catch(error => {
        console.error('查询目录列表失败', error);
      });
    },
    getQuestionType(){
      const typeMapping = {
        '单选题': 0,
        '多选题': 1,
        '判断题': 2,
        '填空题': 3,
        '简答题': 4
      };

      const questionType = typeMapping[this.AllQuestionTypes]; // 获取选择的题型对应的数据库类型代码
      let formData = new FormData();
      formData.append("questionType",questionType);
      formData.append("courseId", this.courseId);
      this.$axios.post('/question/selectQuestionType', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              console.log('查询题型成功');
            }
          }).catch(error => {
        console.error('查询题型失败', error);
      });

    },
    searchInformation(){
      let formData = new FormData();
      formData.append("searchText",this.searchText);
      formData.append("courseId", this.courseId);
      this.$axios.post('/folder/search', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              console.log('模糊查询成功');
            }
          }).catch(error => {
        console.error('模糊查询失败', error);
      });

    },
    getQuestionLevel(){
      let formData = new FormData();
      formData.append("questionLevel",this.FacilityValue);
      formData.append("courseId", this.courseId);
      this.$axios.post('/question/selectQuestionLevel', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              console.log('查询题目难度成功');
            }
          }).catch(error => {
        console.error('查询题目难度失败', error);
      });
    },
    handleFolderClick(row, column, event) {//单击文件夹进入子文件夹

      this.folderItem.id = row.id;
      this.folderItem.name = row.name;
      localStorage.setItem("folderItem",JSON.stringify(this.folderItem));//更新缓存

      console.log("QuestionBank handleFolderClick folderItem: ",this.folderItem);
      this.getData();
    },
    handleQuestionClick(question) {//单击题目处理进行题目预览功能
      console.log("点击题目", question.id);
      // 将question对象存储到 localStorage,需要先转成JSON字符串
      localStorage.setItem('questionItem', JSON.stringify(question));
      // 跳转到题目预览页面
      this.$router.push({ name: 'QuestionPreView' });
    },
    handleReturnClick() {//返回上一级目录
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      formData.append("parentId", this.folderItem.id);
      this.$axios.post('/folder/goback', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              this.folderItem.id = this.tableData[0].pid;//更新当前父目录id
              this.folderItem.name = '选择目录'; //此处可能还要修改
              localStorage.setItem("folderItem",JSON.stringify(this.folderItem));//更新缓存

              console.log("QuestionBank handleReturnClick folderItem: ",this.folderItem);
              console.log('返回上一级目录成功');
            }
          }).catch(error => {
        console.error('返回上一级目录失败', error);
      });
    },
    genDisplayStem(html){
      // 创建一个临时 div 元素
      const tempDiv = document.createElement('div');
      // 将 HTML 字符串赋值给临时 div 的 innerHTML 属性
      tempDiv.innerHTML = html;
      // 查找第一个段落元素并提取其纯文本内容
      const paragraphs = tempDiv.querySelectorAll('p');
      if (paragraphs.length > 0) {
        const text = paragraphs[0].textContent || paragraphs[0].innerText || '';
        return text.slice(0, 20)+"...";
      } else {
        return '';
      }
    },
    switchQuestionType(questionType) {
      switch(questionType) {
        case 0: return "单选题";
        case 1: return "多选题";
        case 2: return "判断题";
        case 3: return "填空题";
        case 4: return "简答题";
        default: return "";
      }
    },
  },

};
</script>

<template>
  <el-main>
    <div class="search-container" style="clear: both;">
      <el-select v-model="AllQuestionTypes" placeholder="全部题型" class="que-query-select" @change="getQuestionType">
        <el-option value="单选题" class="que-option"></el-option>
        <el-option value="判断题" class="que-option"></el-option>
        <el-option value="多选题" class="que-option"></el-option>
        <el-option value="填空题" class="que-option"></el-option>
        <el-option value="简答题" class="que-option"></el-option>
      </el-select>
      <!-- Second Dropdown -->
      <el-select v-model="FacilityValue" placeholder="难易度" class="que-query-select" @change="getQuestionLevel">
        <el-option value="简单" class="que-option"></el-option>
        <el-option value="中等" class="que-option"></el-option>
        <el-option value="困难" class="que-option"></el-option>
      </el-select>

      <el-input v-model="searchText" placeholder="按标题搜索"  class="que-search-input" ></el-input>
      <el-icon  class="que-search-btn" @click="searchInformation"><Search /></el-icon>

      <el-button type="primary"  class="que-nav-button">批量导入</el-button>
      <el-button type="primary"  class="que-nav-button" @click="addItem" >添加目录</el-button>
      <el-button type="primary"  class="que-nav-button"@click="addQuestion">添加题目</el-button>
      <el-button type="primary"  class="que-nav-button1">查看题目详情</el-button>
      <el-button type="primary" class="que-nav-button">题型设置</el-button>
    </div>
    <!-- Table -->
    <div v-if="folderItem.id!=0">
      <el-button type="primary" @click="handleReturnClick">返回</el-button>
    </div>
    <el-table :data="tableData" style="margin-top: 20px;" :header-background-color="'#B0BEC5'"  class="que-table">
      <el-table-column type="index" label="序号" width="100px"></el-table-column>
      <el-table-column label="目录" editable>
        <template v-slot="scope">
          <div v-if="scope.row.editing">
            <el-input v-model="scope.row.name" @keyup.enter.native="renameItem(scope.row)" placeholder="输入目录名称"></el-input>
          </div>
          <div v-else>
            <!-- 如果是题目类型，则直接显示名称 -->
            <span  v-if="scope.row.type === 'folder'" @click="handleFolderClick(scope.row)">
                    {{ scope.row.name }}
                 </span >
            <span  v-else @click="handleQuestionClick(scope.row)">
                   {{ genDisplayStem(scope.row.content) }}
                </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="creator" label="创建者"></el-table-column>
      <el-table-column prop="level" label="难易度"></el-table-column>
      <el-table-column prop="questiontype" label="题型" :formatter="row => switchQuestionType(row.questiontype)"></el-table-column>
      <el-table-column prop="createtime" label="创建日期"></el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <PopQuestionBankMove :item="scope.row"  @messageToParent="moveItem"></PopQuestionBankMove>
          <el-icon  v-if="scope.row.type === 'question'" type="primary" @click="copyItem(scope.row)" class="que-op-link"><Crop /></el-icon>
          <el-icon  type="primary" @click="editItem(scope.row)" class="que-op-link"><Edit /></el-icon>
          <el-icon type="danger" @click="deleteItem(scope.row)" class="que-op-link1"><Delete /></el-icon>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-link type="primary" class="recycle-bin">回收站</el-link>
    </div>
  </el-main>
</template>

<style scoped>
.que-nav-button{
  float: right;
  font-size: 18px;
  margin-bottom: 20px;
}
.que-nav-button1{
  float: right;
  margin-right: 10px;
  font-size: 18px;
}
.que-op-link{
  margin-left: 10px;
  font-size: 18px;
  color:blue;
}
.que-op-link1{
  margin-left: 10px;
  font-size: 18px;
  color:red;
}
.que-table{
  font-size: 18px;
}
.que-search-btn{
  float: left;
  font-size: 18px;
  margin-bottom: 20px;
  color:blue;
  margin-left: 20px;
}
.que-search-input{
  width: 200px;
  float: left;
  font-size: 18px;
  margin-bottom: 20px;
}
.que-query-select{
  width: 100px;
  margin-right: 10px;
  float: left;
  margin-bottom: 20px;
}
.que-option{
  font-size: 16px;
}
.recycle-bin{
  font-size: 18px;
  float: right;
}
</style>

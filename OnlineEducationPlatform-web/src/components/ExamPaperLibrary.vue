<!--试卷文件夹信息展示-->
<script>
  import {ElMessage} from "element-plus";
  export default{
    name:"ExamPaperLibrary",
    data(){
      return{
        tableData:[],
        folderItem: {
          id:0,
          name:'所有目录',
        },
        courseId:0,
        creator:'',
        currentDate:'',
        dialogVisible:false,
        password:'',
        confirmPassword:'',
        currentExam:null,
        operatorType:0,
        unsealPassword:'',
        user:{}
      }
    },
    mounted() {
      this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
      this.courseId = localStorage.getItem("courseID");
      this.creator = localStorage.getItem("username");
      this.currentDate = new Date().toISOString().split('T')[0];
      this.user.id = localStorage.getItem("userID");
      this.getData();
    },
    methods:{
      getData() {//获取指定课程的试卷文件夹下的文件夹和试卷列表
        let formData = new FormData();
        formData.append("courseId", this.courseId);
        formData.append("parentId", this.folderItem.id);
        this.$axios.post('/folder/selectAllExam', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.tableData = response.data.data;
                console.log("查询目录信息成功")
              }
            }).catch(error => {
          console.error('查询目录列表失败', error);
        });
      },
      showDialog(item){//解封,封存试卷
        this.dialogVisible = true;
        this.currentExam = item;
        if(item.sealflag===1){//原试卷为封存状态
          item.sealflag = 0;
          this.operatorType = 1;//进行解封操作
        }else{//原试卷为解封状态
          item.sealflag = 1;
          this.operatorType = 0;//进行封存操作
        }
      },
      handleExamClick(exam){//查看某张试卷信息
        localStorage.setItem('examPaperItem', JSON.stringify(exam));
        this.$router.push('/exam-paper-bank');
      },
      addItem(){//添加目录信息
        const newFolder = {
          name: '新目录',
          operator: 'add',
          editing: true, // 设置为编辑模式
          // 其他必要的属性
        };
        // 在第一条记录后插入新记录
        this.tableData.splice(1, 0, newFolder);
      },
      editItem(item) {//编辑目录或试卷
        if (item.type === 'exam') {

        } else if (item.type === 'folder') {
          item.editing = true;
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
      },
      release(item){//进入试卷发布界面
        localStorage.setItem("examPaperItem",JSON.stringify(item));
        this.$router.push('/exam-paper-release');
      },
      addFolder(name) {//保存文件夹
        let formData = new FormData();
        formData.append("FolderName", name);
        formData.append("parentId", this.folderItem.id);
        formData.append("courseId", this.courseId);
        formData.append("createTime", this.currentDate);
        formData.append("creator", this.creator);
        formData.append("type", 'folder');
        formData.append("folderType", 2);
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
      updateFolder(item){//更新文件夹信息
        let formData = new FormData();
        formData.append("folderName", item.name);
        formData.append("folderId", item.id); // 添加文件夹的 ID
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
      handleSubmit(){//封存试卷
        this.dialogVisible = false;
        if(this.operatorType === 0){
          if(this.password!=this.confirmPassword){
            ElMessage({
              type: 'info',
              message: '密码不匹配',
            })
          }else{
            ElMessage({
              type: 'info',
              message: '密码匹配',
            })
            // 处理提交逻辑
            let formData = new FormData();
            formData.append("examPaperId", this.currentExam.id);
            formData.append("accessPassword", this.confirmPassword);
            formData.append("userId",this.user.id);
            formData.append("courseId",this.courseId);
            this.$axios.post("/exam/seal", formData)
                .then(response => {
                  if (response.data.code === 20000) {
                    // 处理成功上传的响应
                    console.log('封存密码成功');
                    this.elLinkName = '解封'
                  } else {
                    // 处理上传失败的情况
                    console.error('封存密码失败');

                  }
                }).catch(error => {
            });
          }
        }else{//解封试卷
          let formData = new FormData();
          formData.append("examPaperId", this.currentExam.id);
          formData.append("accessPassword", this.unsealPassword);
          formData.append("userId",this.user.id);
          formData.append("courseId",this.courseId);
          this.$axios.post("/exam/unseal", formData)
              .then(response => {
                if (response.data.code === 20000) {
                  // 处理成功上传的响应
                  console.log('解封成功');
                  this.elLinkName = '封存'

                } else {
                  // 处理上传失败的情况
                  console.error('解封失败');

                }
              }).catch(error => {
          });
        }
      }
    },
  }
</script>

<template>
  <div class="search-container" style="clear: both;">
    <el-button type="primary"  class="que-nav-button">导出全部</el-button>
    <el-button type="primary"  class="que-nav-button">导入试卷</el-button>
    <el-button type="primary"  class="que-nav-button">创建试卷</el-button>
    <el-button type="primary" class="que-nav-button" @click="addItem" >添加目录</el-button>
  </div>

  <!-- Table -->
  <el-table :data="tableData" style="margin-top: 20px;" :header-background-color="'#B0BEC5'"  class="que-table">
    <el-table-column type="index" width="100px" label="序号"></el-table-column>
    <el-table-column  label="题目">
      <template v-slot="scope" editable>
        <div v-if="scope.row.editing">
          <el-input v-model="scope.row.name" @keyup.enter.native="renameItem(scope.row)" placeholder="输入目录名称"></el-input>
        </div>
        <div v-else>
          <!-- 如果是试卷类型，则直接显示名称 -->
          <span  v-if="scope.row.type === 'exam'" @click="handleExamClick(scope.row)">
                    {{ scope.row.name }}
                 </span >
          <span  v-else @click="handleFolderClick(scope.row)">
                   {{scope.row.name }}
          </span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="level" label="难度"></el-table-column>
    <el-table-column prop="creator" label="创建者"></el-table-column>
    <el-table-column prop="createtime" label="创建时间"></el-table-column>
    <el-table-column label="操作">
      <template v-slot="scope">
        <el-icon class="operate"><Top /></el-icon>
        <el-icon   @click="editItem(scope.row)"  class="operate"><Edit /></el-icon>
        <el-icon  class="operate1"><Delete /></el-icon>
        <el-icon v-if="scope.row.type === 'exam'" class="operate" @click="release(scope.row)"><Promotion /></el-icon>
        <template v-if="scope.row.sealflag === 1">
          <el-icon v-if="scope.row.type === 'exam'" @click="showDialog(scope.row)" class="operate"><Unlock /></el-icon>
        </template>
        <template v-else>
          <el-icon v-if="scope.row.type === 'exam'" @click="showDialog(scope.row)" class="operate"><Lock /></el-icon>
        </template>
      </template>
    </el-table-column>
  </el-table>
  <el-dialog v-model="dialogVisible" title="试卷封存" width="500">
    <div v-if="operatorType===0">
      <el-form
          label-width="100px"
          ref="classForm"
          class="form"
      >
        <span style="margin-bottom: 10px">封存后任何人员或者业务模块将不能直接访问此试卷，如需查看修改题目需要输入封存密码</span>
        <el-form-item label="设置密码">
          <el-input v-model="password" type="password" placeholder="6为以上数字组合"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="confirmPassword" type="password" placeholder="6为以上数字组合"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="operatorType===1">
      <el-form
          label-width="100px"
          ref="classForm"
          class="form"
      >
        <span style="margin-bottom: 10px">你已设置了封存，修改其设置必须密码验证，从而确保你的试卷安全</span>
        <el-form-item label="安全密码">
          <el-input v-model="unsealPassword" type="password" placeholder="6为以上数字组合"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <el-button type="primary" @click="handleSubmit">确定</el-button>
    <el-button @click="dialogVisible = false">取消</el-button>
  </el-dialog>
  <div class="search-container" style="clear: both;">
    <el-checkbox  style="float: left;margin-right:20px">全选当前页</el-checkbox>
    <el-button type="primary" style="float: left;font-size: 20px">全部导出</el-button>
  </div>
</template>

<style scoped>
.operate {
  margin-right: 10px; /* 调整间距大小 */
  font-size: 18px;
  color: blue;
}
.operate1{
  margin-right: 10px; /* 调整间距大小 */
  font-size: 18px;
  color: red;
}
.que-table{
  font-size: 18px;
}
.que-nav-button{
  float: right;
  font-size: 18px;
  margin-bottom: 20px;
  margin-right: 10px;
}
</style>

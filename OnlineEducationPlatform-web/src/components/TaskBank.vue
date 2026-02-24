<!--作业首页组件-->
<script>

export default {
  name:'TaskBank',
  data(){
    return{
      tableData:[],
      folderItem: {
        id:0,
        name:'所有目录',
      },
      courseId:0,
    }
  },
  mounted() {
    this.folderItem = JSON.parse(localStorage.getItem("folderItem"));
    this.courseId = localStorage.getItem("courseID");
    this.getData();
  },
  methods:{
    getData() {//获取指定课程的作业文件夹下的文件夹和试卷列表
      let formData = new FormData();
      formData.append("courseId", this.courseId);
      formData.append("parentId", this.folderItem.id);
      this.$axios.post('/folder/selectAllTask', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.tableData = response.data.data;
              console.log("查询目录信息成功")
            }
          }).catch(error => {
        console.error('查询目录列表失败', error);
      });
    },
    handleTaskClick(task){//进入某一张作业查看界面
      localStorage.setItem("taskItem",JSON.stringify(task));
      this.$router.push('/task-preview');
    },
    release(item){//进入发放作业界面
      localStorage.setItem("taskItem",JSON.stringify(item));
      this.$router.push('/task-release')
    },
  }
}
</script>

<template>
  <div class="search-container" style="clear: both;">
    <el-button type="primary" class="que-nav-button">导入作业</el-button>
    <el-button type="primary" class="que-nav-button">导出全部</el-button>
    <el-button type="primary" class="que-nav-button">新建作业</el-button>
    <el-button type="primary" class="que-nav-button">添加目录</el-button>
  </div>

  <!-- Table -->
  <el-table :data="tableData" style="margin-top: 20px;" :header-background-color="'#B0BEC5'"  class="que-table">
    <el-table-column type="index" width="100px" label="序号"></el-table-column>
    <el-table-column prop="name" label="作业标题">
      <template v-slot="scope" editable>
        <div v-if="scope.row.editing">
          <el-input v-model="scope.row.name"  placeholder="输入目录名称"></el-input>
        </div>
        <div v-else>
          <!-- 如果是作业类型，则直接显示名称 -->
          <span  v-if="scope.row.type === 'task'" @click="handleTaskClick(scope.row)">
                    {{ scope.row.name }}
                 </span >
          <span  v-else >
                   {{scope.row.name }}
          </span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="creator" label="创建者"></el-table-column>
    <el-table-column prop="createtime" label="创建时间"></el-table-column>
    <el-table-column label="操作" >
      <template v-slot="scope">
        <el-icon  type="primary"  class="operate"><Edit /></el-icon>
        <el-icon class="operate1"><Delete /></el-icon>
        <el-icon v-if="scope.row.type === 'task'" class="operate" @click="release(scope.row)"><Promotion /></el-icon>
      </template>
    </el-table-column>
  </el-table>

  <div class="search-container" style="clear: both;">
    <el-checkbox style="float: left;margin-right:20px; ">全选当前页</el-checkbox>
    <el-button type="primary"  class="que-nav-button">全部导出</el-button>
  </div>
</template>

<style scoped>
.que-nav-button{
  float: right;
  font-size: 18px;
  margin-bottom: 20px;
  margin-left: 10px;
}
.que-table{
  font-size: 18px;
}
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
</style>

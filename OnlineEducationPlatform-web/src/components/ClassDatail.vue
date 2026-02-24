<!--课程班级学生列表信息组件-->
<template>
  <div>
    <!-- 第一行 -->
    <div class="header" style="float: left">
      <span>{{classItems.name}}</span>
      <span style="margin-right: 10px;"></span>
      <!-- 新建班级弹框组件 -->
      <PopClassRename :classItem="classItems"></PopClassRename>
    </div>
    <div style="clear: both;"></div>
    <!-- 第二行 -->
    <div class="search-bar" >
      <div class="search-input">
        <el-input v-model="searchText" placeholder="请输入搜索内容"  class="el-input-text"></el-input>
        <el-icon @click="search" color="blue" size="20" style="margin-left: 20px"><Search /></el-icon>
      </div>
      <div class="action-buttons">
        <!--添加班级学生弹框组件-->
        <PopStudentAdd :classItems="classItems" @messageToParent="updateStudentList"></PopStudentAdd>
        <el-button type="primary" @click="exportList" class="el-button-style">导出名单</el-button>
      </div>
    </div>

    <!-- 第三行 -->
    <div class="table">
      <el-table :data="pagedStudents" style="width: 100%;font-size: 18px" @selection-change="handleSelectionChange">
        <el-table-column type="selection" ></el-table-column>
        <el-table-column type="index" label="序号" width="100"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="id" label="学号"></el-table-column>
        <el-table-column prop="deptname" label="院系"></el-table-column>
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-icon @click="accessLog(scope.row)" style="font-size: 18px;color: blue;margin-right: 20px"><Reading /></el-icon>
            <el-icon @click="removeStudent(scope.row)" style="font-size: 18px;color: red"><Delete /></el-icon>
          </template>
        </el-table-column>
      </el-table>
      <!--分页控件-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          :total="total"
      >
      </el-pagination>
      <el-link type="danger" @click="removeStudent(null)" style="float: left">移除</el-link>
    </div>
  </div>
</template>

<script>
import PopStudentAdd from './PopStudentAdd.vue'
import PopClassRename from "./PopClassRename.vue";
import { ElMessage, ElMessageBox } from 'element-plus'
export default {
  props:{
    classItems:{
      type: Object, // 类型为对象
      required: true // 必须要传递
    },
  },
  watch: {
    classItems(newValue, oldValue) {// 当props的值发生变化时，触发自定义事件
      this.fetchClassStudentList();
    }
  },
  components:{
    PopStudentAdd,PopClassRename
  },
  data() {
    return {
      courseId:0,//当前课程id
      searchText: '',
      students: [],
      pagedStudents: [], // 当前页显示的学生数据
      selectedRows: [], // 存储选中的数据
      total: 0, // 总学生数
      currentPage: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      user:{},
    };
  },
  mounted() {
    this.courseId = localStorage.getItem("courseID");//获取courseId
    this.user.id = localStorage.getItem("userID");
    this.fetchClassStudentList();//获取班级学生列表数据
  },
  methods: {
    search() {//模糊查询
      // 执行搜索逻辑
      let formData = new FormData();
      formData.append("searchText",this.searchText);
      formData.append("classId",this.classItems.id);
      this.$axios.post('/class/searchStudent',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.students = response.data.data;
              this.total = this.students.length;
              this.updatePagedStudents();
            }
          }).catch(error => {
        console.error('模糊查询', error);
      });
    },
    removeStudent(student) {//从班级移除学生
      // 移除学生逻辑
      ElMessageBox.confirm(
          '请确认是否移除学生?',
          '移除学生',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      )
          .then(() => {
            //准备学生id列表数据
            let idList = [];
            if(student===null){
              this.selectedRows.forEach(function (student){
                idList.push(student.id.toString());
              });
            }else{
              idList.push(student.id.toString());
            }
            let formData = new FormData();
            // 将列表数据添加到 FormData 中
            formData.append("dataList",idList);
            // 将班级ID数据添加到 FormData 中
            formData.append('classId', this.classItems.id);
            formData.append('userId',this.user.id);
            formData.append("courseId",this.courseId);
            this.$axios.post('/class/removeStudent',formData,{
              headers: {
                'Content-Type': 'multipart/form-classItems'
              }
            })
                .then(response => {
                  if (response.data.code === 20000) {
                    console.log('数据删除成功');
                    this.fetchClassStudentList();
                  }
                }).catch(error => {
              console.error('从班级移除学生操作失败', error);
            });

            ElMessage({
              type: 'success',
              message: '移除学生成功',
            })
          })
          .catch(() => {

          })
    },
    fetchClassStudentList(){//获取班级学生信息
      let formData = new FormData();
      formData.append("classId",this.classItems.id);
      this.$axios.post('/class/fetchClassStudents',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.students = response.data.data;
              this.total = this.students.length;
              this.updatePagedStudents();
            }
          }).catch(error => {
             console.error('获取班级学生信息失败', error);
      });
    },
    exportList() {
      // 执行导出名单逻辑
      console.log('导出该班级学生名单功能暂未实现 zcj  2024.2.29');
    },
    accessLog(student) {
      // 访问日志逻辑
      localStorage.setItem("userId",student.id);
      localStorage.getItem("activeTab");
      this.$router.push('/student-log');
    },
    updateStudentList(message){//从PopStudentAdd子组件回传的消息处理函数
      this.fetchClassStudentList();//重新获取班级学生列表
      console.log(message);
    },
    //分页相关处理函数
    handleSizeChange(val) {
      this.pageSize = val;
      this.updatePagedStudents();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.updatePagedStudents();
    },
    updatePagedStudents() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pagedStudents = this.students.slice(startIndex, endIndex);
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection;
    }
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  font-size: 18px;
}

.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.search-input {
  display: flex;
  align-items: center;
}

.action-buttons {
  display: flex;
  align-items: center;
}
.table {
  margin-top: 20px;
}
.el-input-text{
  font-size: 20px;
  width: 200px;
}
.el-button-style{
  font-size: 20px;
}
</style>

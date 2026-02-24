<!--添加学生弹框组件,用于课程班级管理页面添加学生操作-->
<template>
  <div>
    <el-button type="primary" plain @click="openModal" style="font-size: 20px">添加学生</el-button>

    <el-dialog v-model="dialogVisible" title="添加学生">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="从学生库添加">
          <!-- 从学生库添加的内容 -->
          <div class="tab-content">
            <el-form :model="studentForm" class="float-left">
              <div class="flex-container">
                <el-form-item class="flex-item">
                  <el-select v-model="studentForm.deptId" placeholder="请选择院系" style="width: 150px;" @change="handleSelectChange">
                    <el-option v-for="department in departments" :key="department.id" :label="department.name" :value="department.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-select v-model="studentForm.major" placeholder="请选择专业" style="width: 150px;">
                    <el-option v-for="major in majors" :key="major.id" :label="major.name" :value="major.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-select v-model="studentForm.class" placeholder="请选择班级" style="width: 150px;">
                    <el-option v-for="classItem in classes" :key="classItem.id" :label="classItem.name" :value="classItem.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-input v-model="studentForm.keyword" placeholder="请输入关键字" style="width: 150px;"></el-input>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-icon @click="searchStudents" color="blue" size="20"><Search /></el-icon>
                </el-form-item>
              </div>
            </el-form>
            <el-table :data="pagedStudents" style="width: 100%" ref="studentTable"  @selection-change="handleSelectionChange">
              <el-table-column type="selection" v-model="selectedRows"></el-table-column>
              <el-table-column type="index" label="序号"  width="100"></el-table-column>
              <el-table-column prop="id" label="学号"></el-table-column>
              <el-table-column prop="name" label="姓名"></el-table-column>

            </el-table>
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pageSize"
                :total="total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="从课程班级添加">
          <!-- 从课程班级添加的内容 -->
          <div class="tab-content">
            <el-form :model="studentForm" class="float-left">
              <div class="flex-container">
                <el-form-item class="flex-item">
                  <el-select v-model="studentForm.course" placeholder="请选择课程" style="width: 150px;">
                    <el-option v-for="courseItem in course" :key="courseItem.id" :label="courseItem.name" :value="courseItem.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-select v-model="studentForm.class" placeholder="请选择班级" style="width: 150px;">
                    <el-option v-for="classItem in classes" :key="classItem.id" :label="classItem.name" :value="classItem.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-input v-model="studentForm.keyword" placeholder="请输入关键字" style="width: 150px;"></el-input>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-button type="primary" @click="searchStudents">搜索</el-button>
                </el-form-item>
              </div>
            </el-form>
            <el-table :data="courses" style="width: 100%" >
              <el-table-column type="selection"></el-table-column>
              <el-table-column type="index" label="序号"  width="100"></el-table-column>
              <el-table-column prop="id" label="学号"></el-table-column>
              <el-table-column prop="name" label="姓名"></el-table-column>

            </el-table>
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pageSize"
                :total="total">
            </el-pagination>
          </div>

        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addStudents">添 加</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  props:{
    classItems:{
      type: Object, // 类型为对象
      required: true // 必须要传递
    },
  },
  data() {
    return {
      courseId:0,//当前课程id
      dialogVisible: false,
      activeTab: 'student',
      studentForm: {
        deptId: 1,
        major: '',
        class: '',
        keyword: '',
        course:'',
      },
      students: [], // 从学生库添加的学生数据
      pagedStudents: [], // 当前页显示的学生数据
      selectedRows: [], // 存储选中的数据
      total: 0, // 总学生数
      currentPage: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      departments: [], // 院系数据
      majors: [
        {id: 1, name: '软件工程'},
        {id: 2, name: '网络工程'},
        {id: 3, name: '信息安全'}
      ], // 专业数据
      classes: [
        {id: 1, name: '计算机1801'},
        {id: 2, name: '计算机1802'},
        {id: 3, name: '信息1801'}
      ], // 班级数据
      course:[
        { id: 1, name: '课程A' },
        { id: 2, name: '课程B' },
        { id: 3, name: '课程C' }
      ],
      courses:[
        { id: 1, name: '学生A' },
        { id: 2, name: '学生B' },
        { id: 3, name: '学生C' }
      ],
      user:{},
    }
  },
  mounted() {
    this.user.id = localStorage.getItem("userID");
    this.courseId = localStorage.getItem("courseID");
    console.log(this.courseId);
  },
  methods: {
    openModal(){
      this.dialogVisible = true;
      this.selectedRows = [];
      this.students = [];
      this.pagedStudents =[];
      this.total=0; // 总学生数
      this.getDepartment();
      this.foundStudent();
    },
    addStudents() {//批量添加学生事件
      if(this.selectedRows.length===0){
        ElMessage({
          type: 'info',
          message: '请选择学生后再进行添加操作',
        })
        return;
      }
      this.dialogVisible = false;
      console.log("选中的数据：", this.selectedRows);
      //准备学生id列表数据
      var idList = [];
      this.selectedRows.forEach(function (student){
        idList.push(student.id.toString());
      });

      let formData = new FormData();
      // 将列表数据添加到 FormData 中
      formData.append("dataList",idList);
      // 将班级ID数据添加到 FormData 中
      formData.append('classId', this.classItems.id);
      formData.append('userId',this.user.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/class/addStudents',formData,{
        headers: {
          'Content-Type': 'multipart/form-classItems'
        }
       })
      .then(response => {
            if (response.data.code === 20000) {
              console.log('学生添加成功');
              this.$emit('messageToParent', '请更新班级学生');
            }
          }).catch(error => {
              console.error('学生添加失败', error);
          });
    },
    searchStudents() {//模糊查询
        let formData = new FormData();
        formData.append("searchText",this.studentForm.keyword);
        formData.append('classId', this.classItems.id);
        formData.append("courseId",this.courseId);
      this.$axios.post('/class/searchStudentNotInClass',formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.students = response.data.data;
                this.total = this.students.length;
                this.updatePagedStudents();
              }
            }).catch(error => {
          console.error('添加学生模糊查询失败', error);
        });
      },
      foundStudent() {//从学生库添加学生tabview初始化获取学生列表
        let formData = new FormData();
        formData.append("classId", this.classItems.id);
        formData.append("courseId",this.courseId);
        this.$axios.post('/class/studentsNotinClass',formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.students = response.data.data;
                this.total = this.students.length;
                this.updatePagedStudents();
              }
            }).catch(error => {
          console.error('获取学生列表失败', error);
        });
      },
    getDepartment() {//获取选择框的部门数据
      this.$axios.post('/class/get')
          .then(response => {
            if (response.data.code === 20000) {
              this.departments = response.data.data;
            }
          }).catch(error => {
        console.error('获取部门列表数据失败', error);
      });
    },
    handleSelectChange(){//按部门选择学生事件响应
      let formData = new FormData();
      console.log(this.studentForm.deptId);
      formData.append("deptId", this.studentForm.deptId);
      formData.append("classId", this.classItems.id);
      formData.append("courseId",this.courseId);
      this.$axios.post('/class/studentsNotinClassByDepartment',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.students = response.data.data;
              this.total = this.students.length;
              this.updatePagedStudents();
            }
          }).catch(error => {
        console.error('按部门查询学生操作失败', error);
      });
    },
    handleSizeChange(val) {//分页相关事件
      this.pageSize = val;
      this.updatePagedStudents();
    },
    handleCurrentChange(val) {//分页相关事件
      this.currentPage = val;
      this.updatePagedStudents();
    },
    updatePagedStudents() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pagedStudents = this.students.slice(startIndex, endIndex);
    },
    handleSelectionChange(selection) {//选择多条记录事件
      this.selectedRows = selection;
    }
  }
}
</script>

<style scoped>
.tab-content {
  padding: 20px;
}

.dialog-footer {
  text-align: center;
}

.flex-container {
  display: flex;
  justify-content: space-between; /* 调整项目之间的间距 */
}

.flex-item {
  flex: 1; /* 灵活的布局 */
  margin-right: 20px; /* 可以根据实际情况调整项目之间的间距 */
}
</style>

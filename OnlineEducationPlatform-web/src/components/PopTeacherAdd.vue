<!--添加教师弹框组件,用于教师团队管理页面添加教师操作-->
<template>
  <div>
    <el-button type="primary" plain @click="openModal">添加教师</el-button>
    <el-dialog v-model="dialogVisible" title="添加教师">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="从教师库添加">
          <div class="tab-content">
            <el-form :model="teacherForm" class="float-left">
              <div class="flex-container">
                <el-form-item class="flex-item">
                  <el-select v-model="teacherForm.deptId" placeholder="请选择院系" style="width: 150px;" @change="handleSelectChange">
                    <el-option v-for="department in departments" :key="department.id" :label="department.name" :value="department.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-input v-model="teacherForm.keyword" placeholder="请输入关键字" style="width: 150px;"></el-input>
                </el-form-item>
                <el-form-item class="flex-item">
                  <el-icon @click="searcheTeacher" color="blue" size="20"><Search /></el-icon>
                </el-form-item>
              </div>
            </el-form>
            <el-table :data="pageTeachers" style="width: 100%" ref="studentTable" @selection-change="handleSelectionChange">
              <el-table-column type="selection"></el-table-column>
              <el-table-column type="index" label="序号"  width="100"></el-table-column>
              <el-table-column prop="id" label="工号"></el-table-column>
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
        <el-button type="primary" @click="addTeacher">添 加</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      courseId:0,
      dialogVisible: false,
      activeTab: 'teacher',
      teacherForm: {
        deptId: 1,
        major: '',
        class: '',
        keyword: '',
        course: '',
      },
      teachers: [], // 从老师库添加的老师数据
      pageTeachers: [], // 当前页显示的老师数据
      selectedRows: [], // 存储选中的数据
      total: 0, // 总学生数
      currentPage: 1, // 当前页码
      pageSize: 10, // 每页显示条数
      departments: [], // 院系数据
      searchText: '',
      user:{},
    }
  },
  methods: {
    openModal() {
      this.dialogVisible = true;
      this.selectedRows = [];
      this.teachers = [];
      this.pageTeachers = [];
      this.total = 0; // 总学生数
      this.courseId = localStorage.getItem("courseID");
      this.user.id = localStorage.getItem("userID");
      this.getDepartment();
      this.foundTeacher();
    },
    getDepartment() {//获取选择框的部门数据
      this.$axios.post('/class/get')
          .then(response => {
            if (response.data.code === 20000) {
              this.departments = response.data.data;
            }
          }).catch(error => {
        console.error('获取部门列表操作失败', error);
      });
    },
    foundTeacher(){//查询教师列表信息
      let formData = new FormData();
      formData.append("courseId",this.courseId);
      this.$axios.post('/course/teachers',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.teachers = response.data.data;
              this.total = this.teachers.length;
              this.updatePagedTeacher();
            }
          }).catch(error => {
        console.error('查询教师列表信息失败', error);
      });
    },
    addTeacher() {//批量添加教师事件
      if(this.selectedRows.length===0){
        ElMessage({
          type: 'info',
          message: '请选择教师后再进行添加操作',
        })
        return;
      }
      this.dialogVisible = false;
      console.log("选中的数据：", this.selectedRows);
      //准备教师id列表数据
      var idList = [];
      this.selectedRows.forEach(function (student){
        idList.push(student.id.toString());
      });

      let formData = new FormData();
      // 将列表数据添加到 FormData 中
      formData.append("dataList",idList);
      formData.append("courseId",this.courseId);
      formData.append("userId",this.user.id);
      this.$axios.post('/course/addTeacher',formData,{
        headers: {
          'Content-Type': 'multipart/form-classItems'
        }
      })
          .then(response => {
            if (response.data.code === 20000) {
              console.log('批量添加教师成功');
              this.$emit('messageToParent', '请更新课程教师');
            }
          }).catch(error => {
          console.error('批量添加教师失败', error);
      });
    },
    searcheTeacher(){//模糊查询
      // 执行搜索逻辑
      let formData = new FormData();
      formData.append("searchText",this.teacherForm.keyword);
      formData.append("courseId",this.courseId);
      this.$axios.post('/course/searchTeacherNotInCourse',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.teachers = response.data.data;
              this.total = this.teachers.length;
              this.updatePagedTeacher();
            }
          }).catch(error => {
        console.error('教师模糊查询失败', error);
      });

    },
    handleSelectChange(){//按部门查询教师
      let formData = new FormData();
      console.log(this.teacherForm.deptId);
      formData.append("deptId", this.teacherForm.deptId);
      formData.append("courseId",this.courseId);
      this.$axios.post('/course/TeacherNotinCourseByDepartment',formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.teachers = response.data.data;
              this.total = this.teachers.length;
              this.updatePagedTeacher();
            }
          }).catch(error => {
        console.error('按部门查询教师失败', error);
      });
    },
    handleSizeChange(val) {//分页相关事件
      this.pageSize = val;
      this.updatePagedTeacher();
    },
    handleCurrentChange(val) {//分页相关事件
      this.currentPage = val;
      this.updatePagedTeacher();
    },
    updatePagedTeacher() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pageTeachers = this.teachers.slice(startIndex, endIndex);
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

<!--管理员用户管理界面组件-->
<script>
import PopUserAdd from "../components/PopUserAdd.vue";
export default {
  name:'MarkExamPapers',
  data() {
    return {
      currentPage: 1, // 当前页码
      pageSize:10, // 每页显示条数,
      total:0,//考试记录人数
      pageStudent:[],
      studentData:[],
      pageTeacher:[],
      teacherData:[],
      activeTab: 'student', // Default active tab
      userType:0,
    };
  },
  components:{
    PopUserAdd
  },
  watch: {
    activeTab(newTab, oldTab) {//观察tavView的信息是否改变
      if (newTab !== oldTab) {
        this.loadUser();
      }
    }
  },
  created() {
    this.getStudent();
  },
  methods:{
    loadUser(){
      if (this.activeTab === 'student') {
        this.getStudent();
        this.userType=0;
      } else if (this.activeTab === 'teacher') {
        this.getTeacher();
        this.userType=1;
      }
    },
    getStudent(){//获取学生数据信息
      this.$axios.post('/manage/selectStudentByType')
          .then(response => {
            if (response.data.code === 20000) {
              this.studentData = response.data.data;
              this.total = this.studentData.length;
              this.updateStudentId();
              console.log("统计学生数据成功");
            }
          }).catch(error => {
        console.error('统计学生数据失败', error);
      });
    },
    getTeacher(){//获取教师数据
      this.$axios.post('/manage/selectTeacherByType')
          .then(response => {
            if (response.data.code === 20000) {
              this.teacherData = response.data.data;
              this.total = this.teacherData.length;
              this.updateTeacherId();
              console.log("统计教师数据成功");
            }
          }).catch(error => {
        console.error('统计教师数据失败', error);
      });
    },
    handleSizeChange(val) {//分页相关事件
      this.pageSize = val;
      this.updateStudentId();
    },
    handleCurrentChange(val) {//分页相关事件
      this.currentPage = val;
      this.updateStudentId();
    },
    updateStudentId() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pageStudent = this.studentData.slice(startIndex, endIndex);
    },
    handleSizeChangeTeacher(val) {//分页相关事件
      this.pageSize = val;
      this.updateTeacherId();
    },
    handleCurrentChangeTeacher(val) {//分页相关事件
      this.currentPage = val;
      this.updateTeacherId();
    },
    updateTeacherId() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pageTeacher = this.teacherData.slice(startIndex, endIndex);
    },
    updateStudentList(message){//给子组件发消息
      this.getStudent();
      console.log(message);
    },
    updateTeacherList(message){//给子组件发消息
      this.getTeacher();
      console.log(message);
    },
    disabledUser(user){//禁用一个用户
      let formData = new FormData();
      formData.append("userId",user.id);
      this.$axios.post('/manage/disabledUser', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('用户被禁用成功');
              if(user.type===1)
                this.getTeacher();
              else
                this.getStudent();
            }
            this.$message.success(response.data.msg)
          }).catch(error => {
            console.error('用户被禁用错误', error);
      });
    },
    enableUser(user){//启用一个用户
      let formData = new FormData();
      formData.append("userId",user.id);
      this.$axios.post('/manage/enableUser', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('用户被启用成功');
              if(user.type===1)
                this.getTeacher();
              else
                this.getStudent();
            }
            this.$message.success(response.data.msg)
          }).catch(error => {
            console.error('用户被启用错误', error);
      });
    }
  }
}
</script>

<template>
  <el-tabs v-model="activeTab">
    <el-tab-pane label="学生管理" name="student">
      <div class="new-course-btn-container">
        <PopUserAdd :type="userType" @messageToParent="updateStudentList"></PopUserAdd>
      </div>
      <div style="margin-top: 40px">
        <el-table :data="pageStudent" style="width: 100%;font-size: 18px" ref="studentTable">
          <el-table-column prop="name" label="姓名" ></el-table-column>
          <el-table-column prop="id" label="学号"> </el-table-column>
          <el-table-column prop="email" label="邮箱"></el-table-column>
          <el-table-column prop="phone" label="电话"></el-table-column>
          <el-table-column prop="sname" label="学校"></el-table-column>
          <el-table-column prop="dname" label="部门"></el-table-column>
          <el-table-column label="操作" width="100">
            <template v-slot="scope" >
              <el-icon style="font-size: 18px;color: red" v-if="scope.row.state===1" @click="disabledUser(scope.row)"><Lock /></el-icon>
              <el-icon style="font-size: 18px;color: blue" v-if="scope.row.state===0" @click="enableUser(scope.row)"><Unlock /></el-icon>
            </template>
          </el-table-column>
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
    <el-tab-pane label="教师管理" name="teacher">
      <div class="new-course-btn-container">
          <PopUserAdd :type="userType" @messageToParent="updateTeacherList"></PopUserAdd>
      </div>
      <div style="margin-top: 40px">
        <el-table :data="pageTeacher" style="width: 100%;font-size: 18px" ref="studentTable">
          <el-table-column prop="name" label="姓名" ></el-table-column>
          <el-table-column prop="id" label="学号"> </el-table-column>
          <el-table-column prop="email" label="邮箱"></el-table-column>
          <el-table-column prop="phone" label="电话"></el-table-column>
          <el-table-column prop="sname" label="学校"></el-table-column>
          <el-table-column prop="dname" label="部门"></el-table-column>
          <el-table-column label="操作" width="100">
            <template v-slot="scope" >
              <el-icon style="font-size: 18px;color: red" v-if="scope.row.state===1" @click="disabledUser(scope.row)"><Lock /></el-icon>
              <el-icon style="font-size: 18px;color: blue" v-if="scope.row.state===0" @click="enableUser(scope.row)"><Unlock /></el-icon>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            @size-change="handleSizeChangeTeacher"
            @current-change="handleCurrentChangeTeacher"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="pageSize"
            :total="total">
        </el-pagination>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped>
.new-course-btn-container {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999; /* Ensure button is on top */
}
</style>

<!--教师团队信息管理界面-->
<script>
  import AddTeacher from "./PopTeacherAdd.vue";
  import {ElMessage, ElMessageBox} from "element-plus";
  export default {
    name:'TeacherManagement',
    components:{
      AddTeacher
    },
    data() {
      return {
        teachers: [],
        courseId:1,
        user:{}
      };
    },
    mounted() {
      this.courseId = localStorage.getItem("courseID");
      this.user.id = localStorage.getItem("userID");
      this.showTeacherBycourse();
    },
    methods:{
      accessLog(){

      },
      showTeacherBycourse(){//展示教师团队信息
        let formData = new FormData();
        formData.append("courseId",this.courseId);
        this.$axios.post('/course/selectTeacherByCourse',formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.teachers = response.data.data;
              }
            }).catch(error => {
                console.error('展示教师团队信息失败', error);
        });
      },
      updateTeacherList(message){
        console.log(message);
        this.showTeacherBycourse();

      },
      removeTeacher(teacher){//移除团队信息成员
        ElMessageBox.confirm(
            '请确认是否移除教师',
            '移除教师',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
            }
        ).then(() => {//删除课程教师信息
            //准备教师id列表数据
          let formData = new FormData();
          formData.append("teacherId",teacher.id);
          formData.append("courseId",this.courseId);
          this.$axios.post('/course/deleteTeacher',formData)
             .then(response => {
                    if (response.data.code === 20000) {
                      console.log('删除课程教师信息成功');
                      this.showTeacherBycourse();
                    }
                  }).catch(error => {
                    console.error('删除课程教师信息失败', error);
              });

              ElMessage({
                type: 'success',
                message: '更新信息成功',
              })
            })
            .catch(() => {
            })
      }
    }
  };
</script>

<template>
  <el-row>
    <el-col :span="24" class="class-management-header">
      <h3 class="class-management-title">教师团队管理</h3>
    </el-col>
    <el-col :span="24" class="button-row">
      <div class="button-container">
        <AddTeacher  @messageToParent="updateTeacherList"></AddTeacher>
      </div>
    </el-col>
    <el-table :data="teachers" style="width: 100%;font-size: 18px">
      <el-table-column type="index" label="序号" width="100"></el-table-column>
      <el-table-column prop="name" label="教师"></el-table-column>
      <el-table-column prop="id" label="工号"></el-table-column>
      <el-table-column label="操作" >
        <template v-slot="scope" >
          <el-icon @click="accessLog(scope.row)" style="font-size: 18px;color: blue;margin-right: 20px"><Reading /></el-icon>
          <el-icon @click="removeTeacher(scope.row)" style="font-size: 18px;color: red"><Delete /></el-icon>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-checkbox v-model="permission" class="checkbox-left">权限设置</el-checkbox>
      <el-checkbox v-model="showInPortal" class="checkbox-left">显示到课门户</el-checkbox>
    </div>
  </el-row>

</template>

<style scoped>
.class-management-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.class-management-title {
  margin: 0;
}
header {
  background-color: #409EFF;
  color: #FFFFFF;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}
.button-row {
  margin-top: 10px; /* 添加按钮行的顶部间距 */
}

.button-container {
  display: flex;
  justify-content: flex-end;
}
.checkbox-left {
  float: left;
  margin-right: 20px;
}

</style>

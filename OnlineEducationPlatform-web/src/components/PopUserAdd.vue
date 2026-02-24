<!--管理员添加用户信息弹框组件-->
<script>
export default {
  data() {
    return {
      dialogVisible: false,
      form:{
        userId:'',
        userName:'',
        password:'',
        email:'',
        phone:'',
        deptId:1,
        schoolId:1,
      },
      departments:[],
      schools:[],
    }
  },
  props:{
    type:{
      type: Number, // 类型为数字
      required: true // 必须要传递
    },
  },
  created() {
    this.getDepartment();
    this.getSchool();
  },
  mounted() {
    console.log("type",this.type);
  },
  methods: {
    submitForm(){//插入用户记录
      this.dialogVisible = false;
      let formData = new FormData();
      formData.append("userId",this.form.userId);
      formData.append("userName",this.form.userName);
      formData.append("password",this.form.password);
      formData.append("email",this.form.email);
      formData.append("phone",this.form.phone);
      formData.append("state",1);
      formData.append("schoolId",this.form.schoolId);
      formData.append("deptId",this.form.deptId);
      if(this.type===0){
        formData.append("type",0);
      }else{
        formData.append("type",1);
      }
      this.$axios.post('/manage/insert', formData)
          .then(response => {
            if (response.data.code === 20000) {
              console.log('添加用户信息成功');
              this.$message.success(response.data.msg)
              this.$emit('messageToParent', '请更新用户信息');
            }
          }).catch(error => {
            console.error('添加用户信息错误', error);
      });
    },
    getDepartment(){//获取部门列表信息
      this.$axios.post('/class/get').then(response=>{
        if (response.data.code === 20000){
          this.departments = response.data.data
        }
      }).catch(error => {
        console.error('获取部门列表信息错误', error);
      });
    },
    getSchool(){
      this.$axios.post('/manage/get').then(response=>{
        if (response.data.code === 20000){
          this.schools = response.data.data
        }
      }).catch(error => {
        console.error('获取学校列表信息错误', error);
      });
    }
  }
}
</script>

<template>
  <el-button plain @click="dialogVisible = true" type="primary" class="">添加用户</el-button>
  <el-dialog title="添加用户" v-model="dialogVisible">
    <div class="create-course-container">
      <el-card class="create-course-card">
        <el-form :model="form" label-width="120px" @submit.native.prevent="submitForm" ref="courseForm" class="custom-form">
          <el-form-item label="学号" prop="userId">
            <el-input v-model="form.userId"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="userName">
            <el-input  v-model="form.userName"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input  v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input  v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item label="教学部门" prop="department">
            <el-select v-model="form.deptId" placeholder="请选择部门">
              <el-option v-for="department in departments" :key="department.id" :label="department.name" :value="department.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属学校" prop="school">
            <el-select v-model="form.schoolId" placeholder="请选择部门">
              <el-option v-for="school in schools" :key="school.id" :label="school.name" :value="school.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </el-dialog>
</template>

<style scoped>

</style>

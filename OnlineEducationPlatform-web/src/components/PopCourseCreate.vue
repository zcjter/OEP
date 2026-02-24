<!--创建课程弹框组件,用于教师首页中创建课程操作-->
<template>
  <el-button plain @click="dialogVisible = true" type="primary" class="">新建课程</el-button>
  <el-dialog title="新建课程" v-model="dialogVisible">
    <div class="create-course-container">
      <el-card class="create-course-card">
        <el-form :model="form" label-width="120px" @submit.native.prevent="createCourse" ref="courseForm" class="custom-form">
          <el-form-item label="课程名称" prop="courseName">
            <el-input v-model="form.courseName" placeholder="请输入课程名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="课程教师">
            <el-input  v-model="user.name" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="课程描述" prop="description">
            <el-input type="textarea" v-model="form.descr" placeholder="请输入课程描述" clearable></el-input>
          </el-form-item>
          <el-form-item label="教学部门" prop="department">
            <el-select v-model="form.deptId" placeholder="请选择部门">
              <el-option v-for="department in departments" :key="department.id" :label="department.name" :value="department.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程封面">
            <input type="file" @change="handleFileChange">
            <div class="avatar">
              <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar-img">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="createCourse">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      form: {
        courseName: '',
        teacher: '', // 假设当前用户为某某老师
        descr: '',
        deptId: 1,
        coverFile:null,
        imageUrl: ''

      },
      departments:[],
      user:[],
      dialogVisible: false,
    };
  },
  mounted() {
    this.getDepartment();
    this.user.name = localStorage.getItem("username");// 深度复制query对象
    this.user.id = localStorage.getItem("userID");
  },
  methods: {
    getDepartment(){//获取部门列表信息
      this.$axios.post('/class/get').then(response=>{
        if (response.data.code === 20000){
          this.departments = response.data.data
        }
      }).catch(error => {
        console.error('获取部门列表信息错误', error);
      });
    },
    createCourse() {//创建课程
      this.dialogVisible = false;
      // 创建一个FormData对象
      let formData = new FormData();
      formData.append("courseName",this.form.courseName);
      formData.append("teaId",this.user.id);
      formData.append("deptId",this.form.deptId);
      formData.append("descr",this.form.descr);
      formData.append("coverFile",this.form.coverFile);


      this.$axios.post('/course/create', formData, {
        headers: {
          'Content-Type': 'multipart/form-classItems'
        }
      }).then(response => {
        if (response.data.code === 20000) {
          // 处理成功上传的响应
          console.log('课程创建成功');
          this.$emit('messageToParent', '请更新课程信息');//往父组件发通知消息
        } else {
          // 处理上传失败的情况
          console.error('课程创建失败');

        }
      }).catch(error => {
        // 处理上传过程中的错误
        console.error('课程创建错误:', error);
      });
    },
    handleFileChange(event) {//课程封面图片更新
      const file = event.target.files[0];
      this.form.coverFile = file;
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.form.imageUrl = reader.result;
      };
    }
  }
};
</script>

<style scoped>
.avatar {
  width: 175px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-img {
  width: 100%;
  height: 100%;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}


.custom-form .el-form-item__content .el-input,
.custom-form .el-form-item__content .el-select__input {
  font-size: 20px !important; /* 调整输入框和选择框字体大小 */
}

.custom-form .el-button {
  font-size: 20px !important; /* 调整按钮字体大小 */
}

.custom-form .el-select__input{
  font-size: 20px;
}
</style>

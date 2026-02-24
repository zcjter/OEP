<!--添加通知信息弹框组件-->
<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        noticeName: '',
        courseId:0,
        classSearch:'',
        classItems:[],
        checkedItems:[],
        selectClass:'',
        studentList:'',
        selectedRows:[],
        pagedStudents: [], // 当前页显示的学生数据
        total: 0, // 总学生数
        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示条数
        noticeType:0,//默认按班级发通知
        title:'',
        content:'',
        user:{}
      };
    },
    created() {
      this.courseId = parseInt(localStorage.getItem("courseID"));
      this.user.name = localStorage.getItem("username");
      this.user.id = localStorage.getItem("userID");
      this.getClassList();
    },
    methods:{
      getClassList(){//获得班级列表信息
        let formData = new FormData();
        formData.append("courseId", this.courseId);
        this.$axios.post('/class/list', formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.classItems = response.data.data;
                console.log("查询课程班级信息成功");
              }
            }).catch(error => {
          console.error('查询课程班级信息失败', error);
        });
      },
      updateStudentList(){//获取学生列表
        let formData = new FormData();
        formData.append("classId", this.selectClass);
//        formData.append("courseId",this.courseId);
        this.$axios.post('/notice/selectStudentInClass',formData)
            .then(response => {
              if (response.data.code === 20000) {
                this.studentList = response.data.data;
                this.total = this.studentList.length;
                this.updatePagedStudents();
              }
            }).catch(error => {
          console.error('获取学生列表失败', error);
        });
      },
      updateReviewRecord(){//发送通知
        this.dialogVisible = false;
        if(this.classSearch==='按人发放'){
          this.noticeType=1;
        }
        let currentTime = this.getCurrentDataTime();
        let classItem={
          noticeType:this.noticeType,
          title:this.title,
          content:this.content,
          releaseTime:currentTime,
          sender:this.user.name,
          senderid:this.user.id,
          courseId:this.courseId
        }
        if(this.noticeType===0){//按班级发送通知
          classItem.classIdList = this.getClassIdList();
        }else{//按照个人发通知
          classItem.studentIdList = this.getStudentIdList();
        }
        this.$axios.post('/notice/releaseNotice',classItem)
            .then(response => {
              if (response.data.code === 20000) {
                console.log("添加通知信息成功");
                this.$emit('messageToParent', '请更新通知信息');
              }
            }).catch(error => {
              console.error('添加通知信息失败', error);
        });
      },
      getStudentIdList(){//得到学生列表数据
        var idList = [];
        this.selectedRows.forEach(function (student){
          idList.push({id:student.id.toString(),
                       name:student.name});
        });
        return idList;
      },
      getClassIdList(){//得到班级列表数据
        const selected = [];
        for(let index=0;index<this.classItems.length;index++){
          const item = this.classItems[index];
          if(this.checkedItems[index]){//当前班级已经选中
            selected.push({id:item.id,name:item.name});
          }
        }
        return selected;
      },
      getCurrentDataTime(){//获取当前时间
        const currentDate = new Date();

        // 获取当前日期
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 月份从0开始计数，需要加1
        const day = String(currentDate.getDate()).padStart(2, '0');

        // 获取当前时间
        const hour = String(currentDate.getHours()).padStart(2, '0');
        const minute = String(currentDate.getMinutes()).padStart(2, '0');
        const second = String(currentDate.getSeconds()).padStart(2, '0');

        // 格式化日期和时间
        let currentDateTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
        return currentDateTime;
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
        this.pagedStudents = this.studentList.slice(startIndex, endIndex);
      },
      handleSelectionChange(selection) {//选择多条记录事件
        this.selectedRows = selection;
      },
    }
  }
</script>

<template>
  <el-button plain @click="dialogVisible = true">新建</el-button>

  <el-dialog v-model="dialogVisible" title="发布通知" width="600" >
    <el-form ref="noticeForm" class="form" :style="{ textAlign: 'left', margin: '0' }">
      <el-form-item label="发送对象:" >
        <div style="display: flex;">
          <el-radio label="全部班级" v-model="classSearch"></el-radio>
          <el-radio label="部分班级" v-model="classSearch"></el-radio>
          <el-radio label="按人发放" v-model="classSearch"></el-radio>
        </div>
        <div v-if="this.classSearch === '部分班级'">
          <div style="display: flex; flex-wrap: wrap;">
            <el-checkbox v-for="(item,index) in classItems" :key="item.id" v-model="checkedItems[index]" style="flex-basis: calc(33.33% - 10px); margin: 5px;">
              {{ item.name }}
            </el-checkbox>
          </div>
        </div>
        <div v-if="this.classSearch === '按人发放'">
          <el-select v-model="selectClass" placeholder="请选择班级" style="width: 150px;margin-left: 10px;font-size: 20px"  @change="updateStudentList">
            <el-option
                v-for="item in classItems"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            ></el-option>
          </el-select>
          <div style="display: flex;">
            <el-table :data="pagedStudents" v-if="studentList.length > 0" @selection-change="handleSelectionChange">
              <el-table-column type="selection" v-model="selectedRows"></el-table-column>
              <el-table-column type="index" label="序号"  width="100"></el-table-column>
              <el-table-column prop="id" label="学号" width="110"></el-table-column>
              <el-table-column prop="name" label="姓名"></el-table-column>
            </el-table>
          </div>
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="pageSize"
              :total="total">
          </el-pagination>
        </div>
      </el-form-item>
      <el-form-item label="标题:">
        <el-input type="text" width="150" v-model="title"></el-input>
      </el-form-item>
      <el-form-item label="内容:">
        <el-input type="textarea" width="150" v-model="content"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="updateReviewRecord">发布</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>

</style>

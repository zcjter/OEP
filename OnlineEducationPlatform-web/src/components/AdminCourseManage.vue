<!--管理员课程管理界面组件-->
<script>
export default {
  name:'AdminCourseManage',
  data() {
    return {
      currentPage: 1, // 当前页码
      pageSize:10, // 每页显示条数,
      total:0,//考试记录人数
      pageCourse:[],
      courseData:[],
    };
  },
  created() {
    this.getCourse();
  },
  methods:{
    getCourse(){//获取教师创建课程信息
      this.$axios.post('/manage/selectAlCourse')
          .then(response => {
            if (response.data.code === 20000) {
              this.courseData = response.data.data;
              this.total = this.courseData.length;
              this.updateCourse();
              console.log("统计课程信息成功");
            }
          }).catch(error => {
        console.error('统计课程信息失败', error);
      });
    },
    handleSizeChange(val) {//分页相关事件
      this.pageSize = val;
      this.updateCourse();
    },
    handleCurrentChange(val) {//分页相关事件
      this.currentPage = val;
      this.updateCourse();
    },
    updateCourse() {//分页相关事件
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pageCourse = this.courseData.slice(startIndex, endIndex);
    },
  }
}
</script>

<template>
      <div style="margin-top: 40px">
        <el-table :data="pageCourse" style="width: 100%;font-size: 18px" ref="studentTable">
          <el-table-column prop="name" label="课程名" ></el-table-column>
          <el-table-column prop="descr" label="课程描述"> </el-table-column>
          <el-table-column prop="uname" label="创建者"></el-table-column>
          <el-table-column prop="sname" label="学校"></el-table-column>
          <el-table-column prop="dname" label="部门"></el-table-column>
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
</template>

<style scoped>

</style>

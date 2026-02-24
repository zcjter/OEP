<!--管理员系统日志界面组件-->
<script>
  export default {
    name: 'AdminSystemLog',
    data() {
      return {
        systemLogData:[],
        pageSystemLog:[],
        currentPage: 1, // 当前页码
        pageSize:10, // 每页显示条数,
        total:0,//考试记录人数
      }
    },
    created() {
      this.getSystemLog();
    },
    methods: {
      getSystemLog() {//获取教师系统日志信息
        this.$axios.post('/manage/selectAllSystemLog')
            .then(response => {
              if (response.data.code === 20000) {
                this.systemLogData = response.data.data;
                this.total = this.systemLogData.length;
                this.updateSystemLog();
                console.log("统计日志信息成功");
              }
            }).catch(error => {
              console.error('统计日志信息失败', error);
        });
      },
      formatDateTime(dateTimeString) {//把T换成空格，把后面的10个字符去掉
        return dateTimeString.slice(0, -10).replace("T", " ");
      },
      handleSizeChange(val) {//分页相关事件
        this.pageSize = val;
        this.updateSystemLog();
      },
      handleCurrentChange(val) {//分页相关事件
        this.currentPage = val;
        this.updateSystemLog();
      },
      updateSystemLog() {//分页相关事件
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.pageSystemLog = this.systemLogData.slice(startIndex, endIndex);
      },
    }
  };
</script>

<template>
  <div style="margin-top: 40px">
    <el-table :data="pageSystemLog" style="width: 100%;font-size: 18px" ref="studentTable">
      <el-table-column prop="name" label="姓名" ></el-table-column>
      <el-table-column prop="date" label="操作时间">
        <template v-slot="scope">
          {{ formatDateTime(scope.row.date) }}
        </template>
      </el-table-column>
      <el-table-column prop="descr" label="描述"> </el-table-column>
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

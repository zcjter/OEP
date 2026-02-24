<!--课程资料界面-->
<script setup>

</script>

<template>
  <div class="search-container" style="clear: both;">
    <el-input v-model="searchText" placeholder="按标题搜索"  class="que-search-input" ></el-input>
    <el-button  type="primary"  class="que-search-btn" @click="searchInformation">搜索</el-button>

    <el-button type="primary"  class="que-nav-button">添加资料</el-button>
    <el-button type="primary"  class="que-nav-button" >新建文件夹</el-button>
  </div>
  <el-table :data="tableData" style="margin-top: 20px;" :header-background-color="'#B0BEC5'"  class="que-table">
    <el-table-column type="index" label="序号" width="100px"></el-table-column>
    <el-table-column label="文件名" editable></el-table-column>
    <el-table-column prop="creator" label="上传者"></el-table-column>
    <el-table-column prop="level" label="大小"></el-table-column>
    <el-table-column prop="questiontype" label="上传时间" :formatter="row => switchQuestionType(row.questiontype)"></el-table-column>
    <el-table-column label="操作" width="200">
      <template v-slot="scope">
        <QuestionBankRemove :item="scope.row"  @messageToParent="moveItem"></QuestionBankRemove>
        <el-link v-if="scope.row.type === 'question'" type="primary" @click="copyItem(scope.row)" class="que-op-link">复制</el-link>
        <el-link type="primary" @click="editItem(scope.row)" class="que-op-link">编辑</el-link>
        <el-link type="danger" @click="deleteItem(scope.row)" class="que-op-link">删除</el-link>

      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.que-nav-button{
  float: right;
  font-size: 18px;
  margin-bottom: 20px;
}
.que-op-link{
  margin-left: 10px;
  font-size: 16px;
}
.que-table{
  font-size: 18px;
}
.que-search-btn{
  float: left;
  font-size: 18px;
  margin-bottom: 20px;
}
.que-search-input{
  width: 200px;
  float: left;
  font-size: 18px;
  margin-bottom: 20px;
}


</style>

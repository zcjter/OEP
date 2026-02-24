<!--教师首页课程信息展示-->
<template>
  <div class="container">
    <!-- Tab view -->
    <el-tabs v-model="activeTab">
      <el-tab-pane label="我教的课" name="Teach" >
        <div class="new-course-btn-container">
          <CreateCourse @messageToParent="updateCourseList"></CreateCourse>
        </div>
        <div class="course-info-container">
          <!-- Course cards -->
          <div class="row" v-for="(row, index) in Math.ceil(courses.length / 3)" :key="index">
            <div class="col-md-4" v-for="(course, idx) in courses.slice(index * 3, (index + 1) * 3)" :key="idx">
              <div class="card">
                <img :src="course.cover" class="card-img-top" @click="openCourse(course)" alt="Course Cover Image">
                <div class="card-body">
                  <h5 class="card-title">{{ course.name }}</h5>
                  <p class="card-text">{{ course.description }}</p>
                  <p class="card-text">{{ user.name }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="我参与的课" name="participate" >
        <div class="course-info-container">
          <!-- Course cards -->
          <div class="row" v-for="(row, index) in Math.ceil(courses.length / 3)" :key="index">
            <div class="col-md-4" v-for="(course, idx) in courses.slice(index * 3, (index + 1) * 3)" :key="idx">
              <div class="card">
                <img :src="course.cover" class="card-img-top" @click="openCourse(course)" alt="Course Cover Image">
                <div class="card-body">
                  <h5 class="card-title">{{ course.name }}</h5>
                  <p class="card-text">{{ course.description }}</p>
                  <p class="card-text">{{ user.name }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import CreateCourse from "./PopCourseCreate.vue";

export default {
  name: "CourseList",
  data() {
    return {
      user: {},
      courses: [],
      activeTab: 'Teach' // Default active tab
    };
  },
  components: {
    CreateCourse
  },
  created() {
    this.user.id = localStorage.getItem("userID");
    this.user.name = localStorage.getItem("username");
    console.log(this.user.name);
    if (this.user.name == null)
      this.$router.push('/login');
    else {
      this.loadCourses();
    }
  },
  watch: {
    activeTab(newTab, oldTab) {//观察tavView的信息是否改变
      if (newTab !== oldTab) {
        this.loadCourses();
      }
    }
  },
  methods: {
    loadCourses() {//获取两个tabView的信息
      if (this.activeTab === 'Teach') {
        this.getTeacherCourses();
      } else if (this.activeTab === 'participate') {
        this.getParticipateCourse();
      }
    },
    getTeacherCourses() {//获取老师创建的课的课程信息
      let formData = new FormData();
      formData.append("teacherId", this.user.id);
      this.$axios.post('/course/fetchCourseList', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.courses = response.data.data;
            }
          }).catch(error => {
        console.error('获取老师创建的课的课程信息失败', error);
      });
    },
    getParticipateCourse(){//获取老师参与的课程信息
      let formData = new FormData();
      formData.append("teacherId", this.user.id);
      this.$axios.post('/course/fetchParticipateCourseList', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.courses = response.data.data;
            }
          }).catch(error => {
        console.error('获取老师参与的课程信息失败', error);
      });
    },
    openCourse(course) {//打开教师课程细节界面
      localStorage.setItem("courseID", course.id);
      localStorage.setItem("courseName", course.name);
      localStorage.setItem("courseActiveTab","HomePage");
      this.$router.push('/teacher-course-detail');
    },
    updateCourseList(message){
      console.log(message);
      this.getTeacherCourses();
    },
  },

};
</script>

<style scoped>
.new-course-btn-container {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999; /* Ensure button is on top */
}
.container{
  min-height: 70vh; /* 确保容器足够高 */
}
.course-info-container{
  margin-top: 40px; /* Adjust top margin to accommodate button */
  max-height: 70vh; /* 限制最大高度 */
  overflow: auto; /* 超出部分可滚动 */

}
.course-info-container .row {
  margin-bottom: 10px; /* 调整课程行之间的间距 */
}

</style>

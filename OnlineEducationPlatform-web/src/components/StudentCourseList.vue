<!--学生首页课程界面组件-->
<template>
  <div class="container">
    <!-- Tab view -->
        <div class="course-info-container">
          <!-- Course cards -->
          <div class="row" v-for="(row, index) in Math.ceil(courses.length / 3)" :key="index">
            <div class="col-md-4" v-for="(course, idx) in courses.slice(index * 3, (index + 1) * 3)" :key="idx">
              <div class="card">
                <img :src="course.cover" class="card-img-top" @click="openCourse(course)" alt="Course Cover Image">
                <div class="card-body">
                  <h5 class="card-title">{{ course.name }}</h5>
                  <p class="card-text">{{ course.description }}</p>
                  <p class="card-text">{{course.teacherName }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
  </div>
</template>

<script>


export default {
  name: "CourseList",
  data() {
    return {
      user: {},
      courses: [],
      teacherName: ''
    };
  },
  created() {
    this.user.id = localStorage.getItem("userID");
    this.user.name = localStorage.getItem("username");
    console.log(this.user.name);
    if (this.user.name == null)
      this.$router.push('/login');
  },
  mounted() {
    this.getStudentCourse();
  },
  methods: {//进入学生细节界面
    openCourse(course){
      //将学生访问的课程id，班级id写入本地缓存，方便获取使用
      localStorage.setItem("courseID", course.id);
      localStorage.setItem("courseName", course.name);
      localStorage.setItem("classId",course.classId);
      this.$router.push('/student-course-detailView');
    },
    getStudentCourse(){//获得学生课程信息
      let formData = new FormData();
      formData.append("studentId", this.user.id);
      this.$axios.post('/course/fetchStudentCourseList', formData)
          .then(response => {
            if (response.data.code === 20000) {
              this.courses = response.data.data;
              this.teacherName = response.data.teacherName
            }
          }).catch(error => {
        console.error('获得学生课程信息失败', error);
      });
    }
  },

};
</script>

<style scoped>

</style>

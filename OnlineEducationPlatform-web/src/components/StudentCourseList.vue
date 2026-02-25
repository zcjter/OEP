<!--学生首页课程界面组件-->
<template>
  <div class="course-list-container">
    <div class="page-header">
      <h2 class="page-title">我的课程</h2>
      <p class="page-subtitle">您已选修的所有课程</p>
    </div>
    
    <div class="course-grid" v-if="courses.length > 0">
      <div 
        class="course-card" 
        v-for="course in courses" 
        :key="course.id"
        @click="openCourse(course)"
      >
        <div class="card-cover">
          <img :src="course.cover || '/cover-image.jpg'" :alt="course.name">
          <div class="card-overlay">
            <el-icon class="enter-icon"><ArrowRight /></el-icon>
          </div>
        </div>
        <div class="card-content">
          <h3 class="course-name">{{ course.name }}</h3>
          <p class="course-desc">{{ course.description || '暂无课程描述' }}</p>
          <div class="course-info">
            <div class="teacher-info">
              <el-avatar :size="24" class="teacher-avatar">{{ course.teacherName ? course.teacherName.charAt(0) : 'T' }}</el-avatar>
              <span class="teacher-name">{{ course.teacherName || '未知教师' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <el-empty v-else description="暂无课程信息" :image-size="200"></el-empty>
  </div>
</template>

<script>
import { ArrowRight } from '@element-plus/icons-vue'

export default {
  name: "CourseList",
  components: {
    ArrowRight
  },
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
    if (this.user.name == null)
      this.$router.push('/login');
  },
  mounted() {
    this.getStudentCourse();
  },
  methods: {
    openCourse(course){
      localStorage.setItem("courseID", course.id);
      localStorage.setItem("courseName", course.name);
      localStorage.setItem("classId",course.classId);
      this.$router.push('/student-course-detailView');
    },
    getStudentCourse(){
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
.course-list-container {
  padding: 0;
}

.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px;
}

.page-subtitle {
  color: #6b7280;
  font-size: 14px;
  margin: 0;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.course-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  cursor: pointer;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

.card-cover {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.course-card:hover .card-cover img {
  transform: scale(1.05);
}

.card-overlay {
  position: absolute;
  inset: 0;
  background: rgba(102, 126, 234, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.course-card:hover .card-overlay {
  opacity: 1;
}

.enter-icon {
  font-size: 32px;
  color: white;
}

.card-content {
  padding: 20px;
}

.course-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.course-desc {
  font-size: 13px;
  color: #6b7280;
  margin: 0 0 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.course-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.teacher-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.teacher-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 12px;
}

.teacher-name {
  font-size: 13px;
  color: #4b5563;
}
</style>

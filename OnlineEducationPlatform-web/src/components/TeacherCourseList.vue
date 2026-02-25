<!--教师首页课程信息展示-->
<template>
  <div class="teacher-course-list">
    <div class="page-header">
      <h2 class="page-title">我的课程</h2>
      <p class="page-subtitle">管理您教授和参与的课程</p>
    </div>
    
    <el-tabs v-model="activeTab" class="course-tabs">
      <el-tab-pane label="我教的课" name="Teach">
        <div class="tab-header">
          <CreateCourse @messageToParent="updateCourseList"></CreateCourse>
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
              <div class="card-footer">
                <el-tag type="primary" size="small">教授中</el-tag>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无课程，点击上方按钮创建课程" :image-size="160"></el-empty>
      </el-tab-pane>
      
      <el-tab-pane label="我参与的课" name="participate">
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
              <div class="card-footer">
                <el-tag type="success" size="small">参与中</el-tag>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无参与的课程" :image-size="160"></el-empty>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import CreateCourse from "./PopCourseCreate.vue";
import { ArrowRight } from '@element-plus/icons-vue'

export default {
  name: "CourseList",
  components: {
    CreateCourse,
    ArrowRight
  },
  data() {
    return {
      user: {},
      courses: [],
      activeTab: 'Teach'
    };
  },
  created() {
    this.user.id = localStorage.getItem("userID");
    this.user.name = localStorage.getItem("username");
    if (this.user.name == null)
      this.$router.push('/login');
    else {
      this.loadCourses();
    }
  },
  watch: {
    activeTab(newTab, oldTab) {
      if (newTab !== oldTab) {
        this.loadCourses();
      }
    }
  },
  methods: {
    loadCourses() {
      if (this.activeTab === 'Teach') {
        this.getTeacherCourses();
      } else if (this.activeTab === 'participate') {
        this.getParticipateCourse();
      }
    },
    getTeacherCourses() {
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
    getParticipateCourse(){
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
    openCourse(course) {
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
.teacher-course-list {
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

.course-tabs :deep(.el-tabs__header) {
  margin-bottom: 24px;
}

.course-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}

.course-tabs :deep(.el-tabs__item.is-active) {
  color: #667eea;
}

.course-tabs :deep(.el-tabs__active-bar) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.tab-header {
  margin-bottom: 20px;
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

.card-footer {
  display: flex;
  justify-content: flex-end;
}
</style>

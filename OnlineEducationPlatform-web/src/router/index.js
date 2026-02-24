import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "../views/LoginView.vue";
import StudentCourseView from "../views/StudentCourseView.vue";
import TeacherCourseView from "../views/TeacherCourseView.vue";
import CourseResourceView from "../views/CourseResourceView.vue";
import AddQuestionBankView from "../views/AddQuestionBankView.vue";
import QuestionBank from "../components/QuestionBank.vue";
import Notice from "../components/NoticeHomePage.vue";
import CourseManageView from "../views/CourseManageView.vue";
import StudentCourseDetailView from "../views/StudentCourseDetailView.vue";
import Test from "../components/Test.vue";
import CoursePreview from "../components/CoursePreview.vue"
import CourseHomePage from "../components/CourseHomePage.vue";
import TinyMCEEditor from "../components/TinyMCEEditor.vue";
import QuestionPreView from "../components/QuestionPreView.vue";
import EditQuestionBankView from "../views/EditQuestionBankView.vue";
import CourseEdit from "../components/CourseEdit.vue"
import TeacherCourseDetailView from "../views/TeacherCourseDetailView.vue";
import ExamPaperCreate from "../components/ExamPaperCreate.vue";
import ExamPaperAutoExtract from "../components/ExamPaperAutoExtract.vue";
import ExamPreView from "../components/ExamPreView.vue";
import ExamHomePange from "../components/TeacherExamHomePage.vue";
import ExamPaperRelease from "../components/ExamPaperRelease.vue";
import studentExam from "../components/StudentExamHomePage.vue"
import StudentExamReminder from "../components/StudentExamNotice.vue";
import StudentAnsweringView from "../views/StudentAnsweringView.vue";
import SubmissionSucessful from "../components/StudentExamSubmiteSucessPrompt.vue";
import MarkExamPapers from "../components/TeacherExamRecordList.vue";
import TeacherReviewRecords from "../components/TeacherReviewRecords.vue";
import TeacherExamRecordList from "../components/TeacherExamRecordList.vue";
import Inbox from "../components/Inbox.vue";
import NoticeContent from "../components/NoticeContent.vue";
import AddHomeWork from "../views/EditTaskView.vue";
import TaskPreview from "../components/TaskPreview.vue";
import TaskRelease from "../components/TaskRelease.vue";
import StudentTaskAnswering from "../views/StudentTaskAnsweringView.vue";
import TeacherTaskRecordList from "../components/TeacherTaskRecordList.vue";
import TeacherTaskReviewRecord from "../components/TeacherTaskReviewRecord.vue";
import AdminManageView from "../views/AdminManageVIew.vue"
import AdminUserManage from "../components/AdminUserManage.vue";
import AdminCourseMangeView from "../views/AdminCourseMangeView.vue";
import AdminSystemLogView from "../views/AdminSystemLogView.vue";
import ScoreStatistics from "../components/ScoreStatistics.vue"
import ChapterStatistics from "../components/ChapterStatistics.vue"
import StudentLog from "../components/StudentLog.vue"
import TeacherCheckTask from "../components/TeacherCheckTask.vue";
import UnsubmittedHomeWork from "../components/UnsubmittedHomeWork.vue";
import StudentTaskCheck from "../components/StudentTaskCheck.vue";
import ExamPaperBank from "../components/ExamPaperBank.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login' // 默认重定向到登录页面
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView
    },
    {
      path: '/student',
      name:'Student',
      component:StudentCourseView,
      meta: { requiresAuth: true, role: 'STUDENT' } // 可以添加元信息，用于验证权限
    },
    {
      path:'/teacher',
      name:'TeacherCourseView',
      component: TeacherCourseView
    },
    {
      path:'/admin',
      name:'AdminManageView',
      component:AdminManageView
    },
    {
      path: '/information',
      name:"information",
      component:CourseResourceView,
      meta: { requiresAuth: true }
    },
    {
      path:'/add-questionList-bank',
      name:'AddQuestionBank',
      component:AddQuestionBankView,
      props: true
    },
    {
      path:'/questionList-bank',
      name:'QuestionBank',
      component:QuestionBank
    },
    {
      path:'/Notice',
      name: 'Notice',
      component: Notice
    },
    {
      path:'/manage-view',
      name:'CourseManageView',
      component:CourseManageView
    },
    {
      path:'/teacher-course-detail',
      name:'TeacherCourseDetailView',
      component:TeacherCourseDetailView
    },

    {
      path:'/teacher-home',
      name:'TeacherHome',
      component:TeacherCourseView
    },
    {
      path:'/student-course-detailView',
      name:'StudentCourseDetailView',
      component:StudentCourseDetailView
    },
    {
      path:'/test',
      name:'test',
      component:Test
    },
    {//课程教程预览页面
      path:'/course-preview',
      name:'CoursePreview',
      component:CoursePreview
    },
    {
      path:'/course-edit',
      name:'CourseEdit',
      component:CourseEdit
    },
    {
      path:'/course-homepage',
      name:'CourseHomePage',
      component:CourseHomePage
    },
    {
      path:'/tiny-mce-editor',
      name:'TinyMCEEeditor',
      component:TinyMCEEditor
    },
    {
      path:'/questionList-preview',
      name:'QuestionPreView',
      component:QuestionPreView
    },
    {
      path:'/questionList-edit',
      name:'QuestionEdit',
      component:EditQuestionBankView
    },
    {
      path:'/exam-paper-create',
      name:'ExamPaperCreate',
      component:ExamPaperCreate
    },
    {
      path:'/exam-paper-auto-extract',
      name:'ExamPaperAutoExtract',
      component:ExamPaperAutoExtract
    },
    {
      path:'/exam-paper-release',
      name:'ExamPaperRelease',
      component:ExamPaperRelease
    },
    {
      path:'/exam-paper-bank',
      name:'ExamPaperBank',
      component:ExamPaperBank
    },
    {
      path:'/exam-pre-view',
      name:'ExamPreView',
      component:ExamPreView
    },
    {
      path:'/exam-home-page',
      name:'ExamHomePage',
      component:ExamHomePange
    },
    {
      path:'/student-exam',
      name:'studentExam',
      component:studentExam
    },
    {
      path:'/student-exam-reminder',
      name:'studentExamReminder',
      component:StudentExamReminder
    },
    {
      path:'/student-answering-view',
      name:'studentAnsweringView',
      component:StudentAnsweringView
    },
    {
      path:'/submission-sucessful',
      name:'submissionSucessful',
      component:SubmissionSucessful
    },
    {
      path:'/mark-exam-papers',
      name:'MarkExamPapers',
      component:MarkExamPapers
    },
    {
      path: '/teacher-review-record',
      name: 'TecherReviewRecord',
      component: TeacherReviewRecords
    },
    {
      path:'/teacher-exam-record-list',
      name:'TeacherExamRecordList',
      component:TeacherExamRecordList
    },
    {
      path:'/inbox',
      name:"Inbox",
      component:Inbox
    },
    {
      path:'/notice-content',
      name:'NoticeContent',
      component:NoticeContent
    },
    {
      path:'/add-homework',
      name:'AddHomework',
      component:AddHomeWork
    },
    {
      path:'/task-preview',
      name:'TaskPreview',
      component:TaskPreview
    },
    {
      path:'/task-release',
      name:'TaskRelease',
      component:TaskRelease
    },
    {
      path:'/student-task-answering',
      name:'StudentTaskAnswering',
      component:StudentTaskAnswering
    },
    {
      path:'/teacher-task-record-list',
      name:'TeacherTaskRecordList',
      component:TeacherTaskRecordList
    },
    {
      path:'/teacher-task-review-record',
      name:'TeacherTaskReviewRecord',
      component:TeacherTaskReviewRecord
    },
    {
      path:'/admin-user-manage',
      name:'AdminUserManage',
      component:AdminUserManage
    },
    {
      path:'/admin-course-manage-view',
      name:'AdminCourseMangeView',
      component:AdminCourseMangeView
    },
    {
      path:'/admin-system-log',
      name:'AdminSystemLogView',
      component:AdminSystemLogView
    },
    {
      path:'/admin-manage-view',
      name:'AdminManageView',
      component:AdminManageView
    },
    {
      path:'/score-statics',
      name:'ScoreStatistics',
      component:ScoreStatistics
    },
    {
      path:'/chapter-statics',
      name:'ChapterStatistics',
      component:ChapterStatistics
    },
    {
      path:'/student-log',
      name:'StudentLog',
      component:StudentLog
    },
    {
      path:'/teacher-check-task',
      name:'TeacherCheckTask',
      component:TeacherCheckTask
    },
    {
      path:'/un-submitted-homework',
      name:'UnsubmittedHomeWork',
      component:UnsubmittedHomeWork
    },
    {
      path:'/student-task-check',
      name:'StudentTaskCheck',
      component:StudentTaskCheck
    }
  ]
})
router.beforeEach((to, from, next) => {
  // 检查路由是否需要登录权限

  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查用户登录状态
    if (!isLoggedIn()) {
      console.log("用户未登录，重定向到登录页面");
      // 用户未登录，重定向到登录页面
      next({
        path: '/login',
        query: { redirect: to.fullPath } // 保存用户原本想要访问的页面，在登录成功后跳转回去
      })
    } else {
      console.log("用户已登录，允许访问目标页面");
      // 用户已登录，允许访问目标页面
      next()
    }
  } else {
    // 路由不需要登录权限，直接允许访问
    next()
  }

})
// 检查用户是否已登录的简单示例
async function isLoggedIn() {
  // 你可以根据实际情况检查用户的登录状态
  return localStorage.getItem('username') !== null;
}

export default router



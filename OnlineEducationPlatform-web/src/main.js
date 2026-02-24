import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import 'element-plus/dist/index.css';
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.component('QuillEditor', QuillEditor)

app.config.globalProperties.$axios = axios;
axios.defaults.baseURL = '/api'; // 后端地址

app.use(router);
app.use(ElementPlus);

app.mount('#app');


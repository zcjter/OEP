<!--网页页尾显示组件-->
<template>
  <div class="footer-container">
    <div class="footer-content">
      <span class="copyright">Copyright © 2024 南京理工大学紫金学院 - 在线教育平台</span>
      <span class="divider">|</span>
      <span class="version">OEP v1.0</span>
    </div>
  </div>
</template>
<script>
  export default {
    name:'Footer'
  };
</script>
<style scoped>
.footer-container {
  background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
  padding: 20px 0;
}

.footer-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.copyright {
  color: rgba(255, 255, 255, 0.7);
}

.divider {
  color: rgba(255, 255, 255, 0.3);
}

.version {
  color: rgba(255, 255, 255, 0.5);
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  font-size: 12px;
}
</style>

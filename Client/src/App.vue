<!--原本的-->
<!--<script setup>-->

<!--</script>-->

<!--<template>-->
<!--  <RouterView/>-->
<!--</template>-->

<!--<style scoped>-->

<!--</style>-->

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isActive = ref(false)
const mousePosition = ref({ x: 0, y: 0 })
const ripples = ref([])

// 页面加载动画
onMounted(() => {
  isActive.value = true
  window.addEventListener('mousemove', handleMouseMove)
})

onUnmounted(() => {
  window.removeEventListener('mousemove', handleMouseMove)
})

// 路由变化时的动画处理
router.afterEach(() => {
  isActive.value = false
  setTimeout(() => {
    isActive.value = true
  }, 50)
})

// 鼠标移动处理
const handleMouseMove = (e) => {
  mousePosition.value = { x: e.clientX, y: e.clientY }
  createRipple(e.clientX, e.clientY)
}

// 创建水波纹效果
const createRipple = (x, y) => {
  const ripple = {
    id: Date.now(),
    x,
    y,
    size: Math.random() * 100 + 50,
    opacity: 1
  }

  ripples.value.push(ripple)

  // 3秒后移除波纹
  setTimeout(() => {
    ripples.value = ripples.value.filter(r => r.id !== ripple.id)
  }, 3000)
}

// 鼠标点击添加更大的波纹
const handleClick = (e) => {
  createRipple(e.clientX, e.clientY)
  createRipple(e.clientX + 10, e.clientY + 10) // 添加第二个波纹增加效果
}
</script>

<template>
  <div
      class="interactive-background"
      @mousemove="handleMouseMove"
      @click="handleClick"
  >
    <!-- 水波纹元素 -->
    <div
        v-for="ripple in ripples"
        :key="ripple.id"
        class="ripple"
        :style="{
        left: `${ripple.x}px`,
        top: `${ripple.y}px`,
        width: `${ripple.size}px`,
        height: `${ripple.size}px`,
        opacity: ripple.opacity
      }"
    />

    <div :class="['router-container', { 'page-active': isActive }]">
      <RouterView v-slot="{ Component }">
        <Transition name="fade" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </div>
  </div>
</template>

<style scoped>
/* 交互背景层 */
.interactive-background {
  position: relative;
  overflow: hidden;
  min-height: 100vh;
}

/* 水波纹效果 */
.ripple {
  position: absolute;
  transform: translate(-50%, -50%);
  background: radial-gradient(
      circle,
      rgba(255, 255, 255, 0.2) 0%,
      rgba(255, 255, 255, 0) 70%
  );
  border-radius: 50%;
  pointer-events: none;
  animation: rippleEffect 3s ease-out forwards;
  z-index: 0;
}

@keyframes rippleEffect {
  0% {
    transform: translate(-50%, -50%) scale(0.2);
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(3);
    opacity: 0;
  }
}

/* 基础布局 - 更新为浮动在波纹上方 */
.router-container {
  min-height: 100vh;
  background: linear-gradient(
      135deg,
      rgba(245, 247, 250, 0.9) 0%,
      rgba(195, 207, 226, 0.9) 100%
  );
  backdrop-filter: blur(5px);
  padding: 2rem;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.6s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
  z-index: 1;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.page-active {
  opacity: 1;
  transform: translateY(0);
}

/* 增强路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* 动态背景元素 */
.router-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 8px;
  background: linear-gradient(90deg, #ff8a00, #e52e71, #1e90ff);
  z-index: 1000;
  opacity: 0.8;
  animation: rainbow 8s ease infinite;
  background-size: 400% 400%;
}

/* 添加粒子背景效果 */
.router-container::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='20' height='20' viewBox='0 0 20 20' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='%239C92AC' fill-opacity='0.05' fill-rule='evenodd'%3E%3Ccircle cx='3' cy='3' r='3'/%3E%3Ccircle cx='13' cy='13' r='3'/%3E%3C/g%3E%3C/svg%3E");
  z-index: -1;
  opacity: 0.5;
}

@keyframes rainbow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .router-container {
    padding: 1rem;
  }
}

/* 增强链接效果 */
.router-container :deep(a) {
  position: relative;
  color: #1e90ff;
  text-decoration: none;
  transition: all 0.3s ease;
  font-weight: 500;
}

.router-container :deep(a):hover {
  color: #e52e71;
  text-shadow: 0 0 5px rgba(229, 46, 113, 0.3);
}

.router-container :deep(a)::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: currentColor;
  transition: width 0.3s ease;
}

.router-container :deep(a):hover::after {
  width: 100%;
}

/* 增强卡片效果 */
.router-container :deep(.card) {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
}

.router-container :deep(.card:hover) {
  transform: translateY(-5px) rotate(1deg);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
}

/* 添加按钮悬停效果 */
.router-container :deep(button) {
  transition: all 0.3s ease;
  transform: perspective(1px) translateZ(0);
}

.router-container :deep(button:hover) {
  transform: perspective(1px) translateZ(0) scale(1.05);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.router-container :deep(button:active) {
  transform: perspective(1px) translateZ(0) scale(0.98);
}
</style>

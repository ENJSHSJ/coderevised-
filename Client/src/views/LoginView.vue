<!--<script setup lang="ts">-->

<!--import {reactive} from "vue";-->
<!--import request from "@/utils/request.js";-->
<!--import {useRouter} from "vue-router";-->
<!--import {useUserStore} from "@/stores/user.js";-->
<!--import {ElMessageBox} from "element-plus";-->

<!--const userStore = useUserStore()-->
<!--const router = useRouter()-->

<!--const form = reactive({-->
<!--  id: '',-->
<!--  password: ''-->
<!--})-->

<!--const onSubmit = () => {-->
<!--  const user = new FormData()-->
<!--  user.append('id', form.id)-->
<!--  user.append('password', form.password)-->
<!--  request.post('/login',user).then(-->
<!--      res => {-->
<!--          if (res.success) {-->
<!--            userStore.login({-->
<!--              id: form.id,-->
<!--              name: res.data.name,-->
<!--              token: res.data.token,-->
<!--              role: res.data.role,-->
<!--              section: res.data.section-->
<!--            })-->
<!--            res.data.role === 'ROLE_USER' ? router.push('/user') : router.push('/admin')-->
<!--          }else{-->
<!--            ElMessageBox.alert(res.message, '提示', {-->
<!--              confirmButtonText: '确定'-->
<!--            })-->
<!--          }-->
<!--      }-->
<!--  )-->
<!--}-->

<!--</script>-->


<!--<template>-->
<!--  <div class="login-container">-->
<!--    <el-card class="login-card">-->
<!--      <div slot="header" class="login-header">-->
<!--        <span>登录</span>-->
<!--      </div>-->
<!--      <el-form label-position="top" @keyup.enter="onSubmit">-->
<!--        <el-form-item>-->
<!--          <el-input v-model="form.id" placeholder="请输入学号"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-input-->
<!--              type="password"-->
<!--              v-model="form.password"-->
<!--              placeholder="请输入密码"-->
<!--              autocomplete="off"-->
<!--              show-password-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
<!--        <div class="form-actions">-->
<!--          <el-button type="primary" @click="onSubmit">登录</el-button>-->
<!--        </div>-->
<!--        <div class="register-link">-->
<!--          <el-button link type="primary" @click="router.push('/register')">还没有账号？去注册</el-button>-->
<!--        </div>-->
<!--      </el-form>-->
<!--    </el-card>-->
<!--  </div>-->
<!--</template>-->

<!--<style scoped>-->
<!--.login-container {-->
<!--  width: 100%;-->
<!--  height: 100vh;-->
<!--  background: url('@/assets/loginB.jpg') no-repeat center center;-->
<!--  background-size: cover;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--  position: relative;-->
<!--}-->

<!--.login-card {-->
<!--  width: 400px;-->
<!--  border-radius: 10px;-->
<!--  opacity: 0.8-->
<!--}-->

<!--.login-header {-->
<!--  text-align: center;-->
<!--  font-size: 24px;-->
<!--  font-weight: bold;-->
<!--  margin-bottom: 10px;-->
<!--  color: #0085ef;-->
<!--}-->

<!--.form-actions {-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  margin-bottom: 10px;-->
<!--}-->

<!--.register-link {-->
<!--  display: flex;-->
<!--  justify-content: flex-end;-->
<!--}-->

<!--.el-input{-->
<!--  &#45;&#45;el-input-text-color:#0085ef;-->
<!--  &#45;&#45;el-input-border-color:#cee8ff;-->
<!--  &#45;&#45;el-input-placeholder-color: #acc2ef;-->

<!--}-->
<!--</style>-->
<script setup lang="ts">
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user.js";
import { ElMessageBox } from "element-plus";

const userStore = useUserStore();
const router = useRouter();
const showShake = ref(false);

const form = reactive({
  id: '',
  password: ''
});

const onSubmit = () => {
  const user = new FormData();
  user.append('id', form.id);
  user.append('password', form.password);
  request.post('/login', user).then(
      res => {
        if (res.success) {
          userStore.login({
            id: form.id,
            name: res.data.name,
            token: res.data.token,
            role: res.data.role,
            section: res.data.section
          });
          res.data.role === 'ROLE_USER' ? router.push('/user') : router.push('/admin');
        } else {
          // 登录失败时触发震动效果
          showShake.value = true;
          setTimeout(() => showShake.value = false, 500);

          ElMessageBox.alert(res.message, '提示', {
            confirmButtonText: '确定'
          });
        }
      }
  );
};
</script>

<template>
  <div class="login-container">
    <!-- 粒子背景层 -->
    <div class="particles-bg"></div>

    <el-card
        class="login-card"
        :class="{'shake-animation': showShake}"
        @mouseenter="hoverCard = true"
        @mouseleave="hoverCard = false"
    >
      <div slot="header" class="login-header">
        <span class="title-text">登录</span>
      </div>
      <el-form label-position="top" @keyup.enter="onSubmit">
        <el-form-item>
          <el-input
              v-model="form.id"
              placeholder="请输入学号"
              class="animated-input"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
              type="password"
              v-model="form.password"
              placeholder="请输入密码"
              autocomplete="off"
              show-password
              class="animated-input"
          ></el-input>
        </el-form-item>
        <div class="form-actions">
          <el-button
              type="primary"
              @click="onSubmit"
              class="submit-btn"
          >
            登录
          </el-button>
        </div>
        <div class="register-link">
          <el-button
              link
              type="primary"
              @click="router.push('/register')"
              class="register-btn"
          >
            还没有账号？去注册
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  background: url('@/assets/loginB.jpg') no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

/* 粒子背景样式 */
.particles-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  z-index: 0;
}

.particles-bg::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle, transparent 20%, #000 20%, #000 80%, transparent 80%, transparent),
  radial-gradient(circle, transparent 20%, #000 20%, #000 80%, transparent 80%, transparent) 50px 50px;
  background-size: 100px 100px;
  opacity: 0.05;
  animation: particleMove 50s linear infinite;
}

@keyframes particleMove {
  0% { background-position: 0 0, 50px 50px; }
  100% { background-position: 100px 0, 150px 50px; }
}

.login-card {
  width: 400px;
  border-radius: 10px;
  opacity: 0.9;
  transition: all 0.3s ease;
  transform: translateY(0);
  z-index: 1;
  box-shadow: 0 5px 15px rgba(0, 133, 239, 0.1);
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 133, 239, 0.2);
  opacity: 0.95;
}

/* 震动动画 */
.shake-animation {
  animation: shake 0.5s cubic-bezier(.36,.07,.19,.97) both;
}

@keyframes shake {
  10%, 90% { transform: translateX(-1px); }
  20%, 80% { transform: translateX(2px); }
  30%, 50%, 70% { transform: translateX(-4px); }
  40%, 60% { transform: translateX(4px); }
}

.login-header {
  text-align: center;
  margin-bottom: 10px;
}

.title-text {
  font-size: 24px;
  font-weight: bold;
  color: #0085ef;
  display: inline-block;
  position: relative;
}

.title-text::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #0085ef, transparent);
  animation: titleUnderline 3s ease infinite;
}

@keyframes titleUnderline {
  0% { transform: scaleX(0); opacity: 0; }
  50% { transform: scaleX(1); opacity: 1; }
  100% { transform: scaleX(0); opacity: 0; }
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.register-link {
  display: flex;
  justify-content: flex-end;
}

.el-input {
  --el-input-text-color: #0085ef;
  --el-input-border-color: #cee8ff;
  --el-input-placeholder-color: #acc2ef;
  transition: all 0.3s ease;
}

/* 输入框动画 */
.animated-input:focus-within {
  transform: scale(1.02);
  box-shadow: 0 0 0 2px rgba(0, 133, 239, 0.2);
}

.submit-btn {
  transition: all 0.3s ease;
  letter-spacing: 1px;
}

.submit-btn:hover {
  letter-spacing: 2px;
  transform: translateY(-2px);
}

.register-btn {
  transition: all 0.3s ease;
}

.register-btn:hover {
  text-decoration: underline;
  text-underline-offset: 3px;
}
</style>
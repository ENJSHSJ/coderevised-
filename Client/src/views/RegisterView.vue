<!--<script setup>-->
<!--import { reactive } from 'vue'-->
<!--import request from "@/utils/request";-->
<!--import { useRouter } from "vue-router";-->
<!--import {ElMessageBox} from "element-plus";-->
<!--import validate from "@/utils/validate.js";-->

<!--const router = useRouter()-->
<!--// do not use same name with ref-->
<!--const form = reactive({-->
<!--  id: '',-->
<!--  username: '',-->
<!--  password: '',-->
<!--  confirmed: '',-->
<!--  invitationCode: ''-->
<!--})-->

<!--const onSubmit = () => {-->
<!--  const errors = validate(form)-->
<!--  if (errors) {-->
<!--    ElMessageBox.alert(errors, '提示', {-->
<!--      confirmButtonText: '确定'-->
<!--    })-->
<!--    return-->
<!--  }-->
<!--  const user = {-->
<!--    id: form.id,-->
<!--    username: form.username,-->
<!--    password: form.password,-->
<!--    code: form.invitationCode-->
<!--  }-->
<!--  request.post('/user', user).then(-->
<!--      res => {-->
<!--        if (res.success) {-->
<!--          ElMessageBox.alert('注册成功', '提示', {-->
<!--            confirmButtonText: '确定',-->
<!--            callback: action => {-->
<!--              router.push('/login')-->
<!--            }-->
<!--          })-->
<!--        } else {-->
<!--          ElMessageBox.alert(res.message, '提示', {-->
<!--            confirmButtonText: '确定'-->
<!--          })-->
<!--        }-->
<!--      }-->
<!--  ).catch(-->
<!--      err => console.log(err)-->
<!--  )-->
<!--}-->
<!--</script>-->

<!--<template>-->
<!--  <div class="login-container">-->
<!--    <el-card class="login-card">-->
<!--      <div slot="header" class="login-header">-->
<!--        <span>注册</span>-->
<!--      </div>-->
<!--      <el-form label-position="top" @keyup.enter="onSubmit">-->
<!--        <el-form-item>-->
<!--          <el-input v-model="form.id" placeholder="请输入学号"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>-->
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
<!--        <el-form-item>-->
<!--          <el-input-->
<!--              type="password"-->
<!--              v-model="form.confirmed"-->
<!--              placeholder="请确认密码"-->
<!--              autocomplete="off"-->
<!--              show-password-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-input v-model="form.invitationCode" placeholder="请输入班级邀请码"></el-input>-->
<!--        </el-form-item>-->
<!--        <div class="form-actions">-->
<!--          <el-button type="primary" @click="onSubmit">注册</el-button>-->
<!--        </div>-->
<!--      </el-form>-->
<!--    </el-card>-->
<!--  </div>-->
<!--</template>-->

<!--<style scoped>-->
<!--.login-container {-->
<!--  width: 100%;-->
<!--  height: 100vh;-->
<!--  background: url('@/assets/registryB.jpg') no-repeat center center;-->
<!--  background-size: cover;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--}-->

<!--.login-card {-->
<!--  width: 600px;-->
<!--  border-radius: 10px;-->
<!--  opacity: 0.95-->
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
<!--}-->
<!--.el-input{-->
<!--  &#45;&#45;el-input-text-color:#0085ef;-->
<!--  &#45;&#45;el-input-border-color:#cee8ff;-->
<!--  &#45;&#45;el-input-placeholder-color: #acc2ef;-->
<!--  &#45;&#45;el-input-focus-border-color: #0085ff;-->

<!--}-->
<!--</style>-->


<script setup>
import { reactive } from 'vue'
import request from "@/utils/request";
import { useRouter } from "vue-router";
import {ElMessageBox} from "element-plus";
import validate from "@/utils/validate.js";

const router = useRouter()
const form = reactive({
  id: '',
  username: '',
  password: '',
  confirmed: '',
  invitationCode: ''
})

// 新增：表单错误状态
const errorState = reactive({
  id: false,
  username: false,
  password: false,
  confirmed: false,
  invitationCode: false
})

const onSubmit = () => {
  const errors = validate(form)
  if (errors) {
    // 新增：设置错误状态
    Object.keys(errorState).forEach(key => {
      errorState[key] = !!form[key] && errors.includes(key)
    })

    ElMessageBox.alert(errors, '提示', {
      confirmButtonText: '确定'
    })
    return
  }

  const user = {
    id: form.id,
    username: form.username,
    password: form.password,
    code: form.invitationCode
  }

  // 新增：提交按钮加载状态
  const submitBtn = document.querySelector('.submit-btn')
  if (submitBtn) {
    submitBtn.classList.add('loading')
  }

  request.post('/user', user).then(
      res => {
        if (res.success) {
          ElMessageBox.alert('注册成功', '提示', {
            confirmButtonText: '确定',
            callback: action => {
              router.push('/login')
            }
          })
        } else {
          ElMessageBox.alert(res.message, '提示', {
            confirmButtonText: '确定'
          })
        }
      }
  ).catch(
      err => console.log(err)
  ).finally(() => {
    if (submitBtn) {
      submitBtn.classList.remove('loading')
    }
  })
}

// 新增：输入框验证状态
const validateField = (field) => {
  const errors = validate({[field]: form[field]})
  errorState[field] = !!form[field] && !!errors
}
</script>

<template>
  <div class="login-container">
    <!-- 新增：粒子背景效果 -->
    <div class="particles"></div>

    <!-- 新增：浮动气泡背景 -->
    <div class="bubbles">
      <div v-for="i in 10" :key="i" class="bubble" :style="bubbleStyle(i)"></div>
    </div>

    <el-card class="login-card" :class="{'shake': hasErrors}">
      <div slot="header" class="login-header">
        <span class="title-text">注册</span>
      </div>
      <el-form label-position="top" @keyup.enter="onSubmit">
        <el-form-item>
          <el-input
              v-model="form.id"
              placeholder="请输入学号"
              @blur="validateField('id')"
              :class="{'error-input': errorState.id}"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              @blur="validateField('username')"
              :class="{'error-input': errorState.username}"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
              type="password"
              v-model="form.password"
              placeholder="请输入密码"
              autocomplete="off"
              show-password
              @blur="validateField('password')"
              :class="{'error-input': errorState.password}"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
              type="password"
              v-model="form.confirmed"
              placeholder="请确认密码"
              autocomplete="off"
              show-password
              @blur="validateField('confirmed')"
              :class="{'error-input': errorState.confirmed}"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
              v-model="form.invitationCode"
              placeholder="请输入班级邀请码"
              @blur="validateField('invitationCode')"
              :class="{'error-input': errorState.invitationCode}"
          ></el-input>
        </el-form-item>
        <div class="form-actions">
          <el-button
              type="primary"
              @click="onSubmit"
              class="submit-btn"
          >
            <span class="btn-text">注册</span>
            <span class="loading-dots"></span>
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
  background: url('@/assets/registryB.jpg') no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

/* 新增：粒子背景 */
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  z-index: 0;
}

.particles::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle, transparent 20%, rgba(0, 133, 239, 0.1) 20%, rgba(0, 133, 239, 0.1) 80%, transparent 80%, transparent),
  radial-gradient(circle, transparent 20%, rgba(0, 133, 239, 0.1) 20%, rgba(0, 133, 239, 0.1) 80%, transparent 80%, transparent) 50px 50px;
  background-size: 100px 100px;
  opacity: 0.3;
  animation: particleMove 50s linear infinite;
}

@keyframes particleMove {
  0% { background-position: 0 0, 50px 50px; }
  100% { background-position: 100px 0, 150px 50px; }
}

/* 新增：浮动气泡 */
.bubbles {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.bubble {
  position: absolute;
  bottom: -100px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: rise 15s infinite ease-in;
}

@keyframes rise {
  0% {
    bottom: -100px;
    transform: translateX(0);
  }
  50% {
    transform: translateX(100px);
  }
  100% {
    bottom: 1080px;
    transform: translateX(-200px);
  }
}

.login-card {
  width: 600px;
  border-radius: 10px;
  opacity: 0.95;
  transition: all 0.3s ease;
  transform: translateY(0);
  z-index: 2;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
}

/* 新增：错误震动效果 */
.shake {
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
}

/* 新增：输入框错误状态 */
.error-input {
  --el-input-border-color: #ff4d4f;
  animation: pulse 0.5s ease;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.02); }
  100% { transform: scale(1); }
}

/* 新增：按钮加载效果 */
.submit-btn {
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.submit-btn .btn-text {
  display: inline-block;
  transition: all 0.3s ease;
}

.submit-btn .loading-dots {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0;
  transition: all 0.3s ease;
}

.submit-btn.loading .btn-text {
  opacity: 0;
}

.submit-btn.loading .loading-dots {
  opacity: 1;
}

.loading-dots::after {
  content: '...';
  animation: loading 1.5s infinite steps(4, end);
}

@keyframes loading {
  0% { content: '.'; }
  33% { content: '..'; }
  66% { content: '...'; }
}

/* 原有样式保持不变 */
.el-input{
  --el-input-text-color:#0085ef;
  --el-input-border-color:#cee8ff;
  --el-input-placeholder-color: #acc2ef;
  --el-input-focus-border-color: #0085ff;
  transition: all 0.3s ease;
}

.el-input:hover {
  --el-input-border-color: #0085ff;
}

.el-input:focus-within {
  box-shadow: 0 0 0 2px rgba(0, 133, 255, 0.2);
}
</style>

<script>
// 新增：气泡样式计算
export default {
  methods: {
    bubbleStyle(index) {
      const size = Math.random() * 50 + 20;
      const left = Math.random() * 100;
      const delay = Math.random() * 5;
      const duration = Math.random() * 10 + 10;

      return {
        width: `${size}px`,
        height: `${size}px`,
        left: `${left}%`,
        animationDelay: `${delay}s`,
        animationDuration: `${duration}s`
      }
    }
  }
}
</script>
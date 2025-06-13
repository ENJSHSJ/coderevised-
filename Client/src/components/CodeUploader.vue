<!--&lt;!&ndash; src/components/CodeUploader.vue &ndash;&gt;-->
<!--<script setup>-->
<!--import { ref } from 'vue';-->
<!--import { generateExercises } from '@/utils/aiService';-->

<!--const code = ref('');-->
<!--const exercises = ref('');-->

<!--const handleSubmit = async () => {-->
<!--  exercises.value = await generateExercises(code.value);-->
<!--};-->
<!--</script>-->

<!--<template>-->
<!--  <div>-->
<!--    <textarea v-model="code" placeholder="粘贴你的代码..."></textarea>-->
<!--    <button @click="handleSubmit">生成练习题</button>-->
<!--    <div class="exercises" v-if="exercises">-->
<!--      <h3>生成的练习题：</h3>-->
<!--      <pre>{{ exercises }}</pre>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
<!-- src/components/CodeUploader.vue -->
<script setup>
import { ref, computed } from 'vue';
import { generateExercises } from '@/services/aiService';
import { useLoading } from '@/composables/useLoading';

// 响应式数据
const code = ref('');
const exercises = ref([]);
const error = ref(null);
const { isLoading, withLoading } = useLoading();

// 计算属性
const hasExercises = computed(() => exercises.value.length > 0);

// 提交处理
const handleSubmit = async () => {
  error.value = null;

  if (!code.value.trim()) {
    error.value = '请输入有效代码';
    return;
  }

  try {
    exercises.value = await withLoading(
        generateExercises(code.value)
    );
  } catch (err) {
    error.value = '生成练习题失败，请重试';
    console.error('AI服务调用失败:', err);
  }
};

// 重置功能
const handleReset = () => {
  code.value = '';
  exercises.value = [];
  error.value = null;
};
</script>

<template>
  <div class="code-uploader">
    <div class="editor-section">
      <label for="code-input">输入需要分析的代码：</label>
      <textarea
          id="code-input"
          v-model="code"
          placeholder="粘贴你的代码（支持C/Java/Python）..."
          rows="10"
          class="code-editor"
      ></textarea>

      <div class="action-buttons">
        <button
            @click="handleSubmit"
            :disabled="isLoading || !code.trim()"
            class="submit-btn"
        >
          <span v-if="isLoading">生成中...</span>
          <span v-else>生成练习题</span>
        </button>

        <button
            @click="handleReset"
            class="reset-btn"
        >
          重置
        </button>
      </div>

      <div v-if="error" class="error-message">
        {{ error }}
      </div>
    </div>

    <div v-if="hasExercises" class="results-section">
      <h3>根据你的代码生成的练习题：</h3>
      <ul class="exercise-list">
        <li
            v-for="(exercise, index) in exercises"
            :key="index"
            class="exercise-item"
        >
          <div class="exercise-title">题目 {{ index + 1 }}：</div>
          <pre class="exercise-content">{{ exercise }}</pre>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.code-uploader {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.editor-section {
  margin-bottom: 30px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #333;
}

.code-editor {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  resize: vertical;
  margin-bottom: 16px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}

.submit-btn {
  background-color: #1890ff;
  color: white;
}

.submit-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.submit-btn:hover:not(:disabled) {
  background-color: #096dd9;
}

.reset-btn {
  background-color: #f5f5f5;
  color: #666;
}

.reset-btn:hover {
  background-color: #e1e1e1;
}

.error-message {
  color: #f5222d;
  margin-top: 12px;
  padding: 8px;
  background-color: #fff1f0;
  border-radius: 4px;
}

.results-section {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

h3 {
  color: #333;
  margin-bottom: 16px;
}

.exercise-list {
  list-style-type: none;
  padding: 0;
}

.exercise-item {
  background-color: #f9f9f9;
  border-left: 3px solid #1890ff;
  padding: 12px 16px;
  margin-bottom: 12px;
  border-radius: 2px;
}

.exercise-title {
  font-weight: 600;
  margin-bottom: 8px;
  color: #1890ff;
}

.exercise-content {
  white-space: pre-wrap;
  margin: 0;
  font-family: 'Courier New', monospace;
  line-height: 1.5;
}
</style>
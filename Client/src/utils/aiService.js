// src/utils/aiService.js
// import axios from 'axios';
//
// export const generateExercises = async (code) => {
//     try {
//         const response = await axios.post('/api/ai/generate-exercise', {
//             code: code
//         });
//         return response.data;
//     } catch (error) {
//         console.error('AI生成失败:', error);
//         return "生成练习题时发生错误";
//     }
// };
import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

/**
 * 基于代码生成练习题
 * @param {string} code - 需要分析的源代码
 * @param {object} options - 配置选项
 * @param {'beginner'|'intermediate'|'advanced'} [options.difficulty] - 题目难度
 * @param {number} [options.count=3] - 生成题目数量
 * @returns {Promise<Array<string>>} 生成的练习题数组
 */
export const generateExercises = async (code, options = {}) => {
    try {
        const authStore = useAuthStore();
        const defaultOptions = {
            difficulty: 'intermediate',
            count: 3,
            language: detectLanguage(code),
            ...options
        };

        const response = await axios.post(
            `${import.meta.env.VITE_API_BASE}/ai/exercises`,
            {
                code,
                options: defaultOptions
            },
            {
                headers: {
                    'Authorization': `Bearer ${authStore.token}`,
                    'Content-Type': 'application/json'
                },
                timeout: 30000 // 30秒超时
            }
        );

        if (!response.data.success) {
            throw new Error(response.data.message || '生成练习题失败');
        }

        return response.data.exercises;
    } catch (error) {
        console.error('[AI Service] 生成练习题失败:', error);
        throw new Error(formatErrorMessage(error));
    }
};

/**
 * 检测代码语言
 * @param {string} code
 * @returns {'c'|'java'|'python'|'unknown'}
 */
function detectLanguage(code) {
    if (code.includes('#include')) return 'c';
    if (code.includes('public class')) return 'java';
    if (code.includes('def ') || code.includes('import ')) return 'python';
    return 'unknown';
}

/**
 * 格式化错误信息
 * @param {Error|import('axios').AxiosError} error
 * @returns {string}
 */
function formatErrorMessage(error) {
    if (axios.isAxiosError(error)) {
        if (error.response) {
            return `服务器错误: ${error.response.data?.message || error.response.statusText}`;
        }
        return '网络连接异常，请检查网络设置';
    }
    return error.message || '未知错误';
}

// 其他AI相关服务函数
export const analyzeCodeQuality = async (code) => {
    // 实现代码质量分析逻辑...
};

export default {
    generateExercises,
    analyzeCodeQuality
};
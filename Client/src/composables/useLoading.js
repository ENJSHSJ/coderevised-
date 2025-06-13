import { ref } from 'vue';

/**
 * 加载状态管理Hook
 * @param {object} [options] - 配置选项
 * @param {boolean} [options.initialState=false] - 初始状态
 * @param {number} [options.minLoadingTime=300] - 最小加载时间(ms，防止闪烁)
 * @returns {{
 *   isLoading: import('vue').Ref<boolean>,
 *   withLoading: <T>(promise: Promise<T>) => Promise<T>,
 *   startLoading: () => void,
 *   endLoading: () => void
 * }}
 */
export function useLoading(options = {}) {
    const {
        initialState = false,
        minLoadingTime = 300
    } = options;

    const isLoading = ref(initialState);
    let loadingStartTime = 0;

    /**
     * 包装异步函数，自动处理加载状态
     * @template T
     * @param {Promise<T>} promise
     * @returns {Promise<T>}
     */
    const withLoading = async (promise) => {
        startLoading();
        try {
            const result = await promise;
            ensureMinLoadingTime();
            return result;
        } finally {
            endLoading();
        }
    };

    const startLoading = () => {
        loadingStartTime = Date.now();
        isLoading.value = true;
    };

    const endLoading = () => {
        ensureMinLoadingTime().then(() => {
            isLoading.value = false;
        });
    };

    /**
     * 确保最小加载时间
     * @returns {Promise<void>}
     */
    const ensureMinLoadingTime = () => {
        const elapsed = Date.now() - loadingStartTime;
        if (elapsed >= minLoadingTime) {
            return Promise.resolve();
        }
        return new Promise(resolve => {
            setTimeout(resolve, minLoadingTime - elapsed);
        });
    };

    return {
        isLoading,
        withLoading,
        startLoading,
        endLoading
    };
}
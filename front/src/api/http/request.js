import axios from 'axios'
import {CURRENT_CONFIG} from './config'
import {ELocalStorageKey, ERouterName, EUserType} from '/src/types/enum'
import {refreshToken} from "@/api/manage";
import {config} from "@vue/cli-plugin-eslint/eslintOptions";
import router from "@/router";

export * from './type'

const REQUEST_ID = 'X-Request-Id'

// 刷新Token相关状态
let isRefreshing = false
let failedQueue= [],

// 处理队列中的请求
processQueue = (error, token) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error)
        } else {
            prom.resolve(token)
        }
    })
    failedQueue = []
}



function getAuthToken() {
    return localStorage.getItem(ELocalStorageKey.Token)
}

const instance = axios.create({ // 其中的AxiosInstance定义了创建的实例必须有哪些方法和属性
    // withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
    // timeout: 12000,
})

// 请求拦截器
instance.interceptors.request.use(
    config => { // 成功回调
        config.headers[ELocalStorageKey.Token] = getAuthToken()
        config.baseURL = CURRENT_CONFIG.baseURL
        return config
    },
    error => { // 错误回调
        return Promise.reject(error)
    },
)
// 响应拦截器
instance.interceptors.response.use(
    response => {
        console.info('URL: ' + response.config.baseURL + response.config.url, '\nData: ', response.data, '\nResponse:', response)
        if (response.data.code && response.data.code !== 0) {
            console.error(response.data.message)
        }
        return response
    },
    async (err)  => {
        const originalRequest = err.config
        const requestId = err?.config?.headers && err?.config?.headers[REQUEST_ID]
        if (requestId) {
            console.info(REQUEST_ID, '：', requestId)
        }
        console.info('url: ', err?.config?.url, `【${err?.config?.method}】 \n>>>> err: `, err)

        let description = '-'
        if (err.response?.data && err.response.data.message) {
            description = err.response.data.message
        }
        // eslint-disable-next-line no-empty
        if (err.response?.data && err.response.data.result) {
        }
        // @See: https://github.com/axios/axios/issues/383
        if (!err.response || !err.response.status) {
            console.error('The network is abnormal, please check the backend service and try again')
            return
        }
        if (err.response?.status !== 200) {
            console.error(`ERROR_CODE: ${err.response?.status}`)
        }
        // if (err.response?.status === 403) {
        //   // window.location.href = '/'
        // }
//         if (err.response?.status === 401) {
// console.log("错误401")
//
//         }

        // Token过期处理 (401)
        if (err.response?.status === 401 && !originalRequest._retry) {

            // 如果已经在刷新token，将请求加入队列
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject })
                }).then(token => {
                    originalRequest.headers[ELocalStorageKey.Token] = token
                    return instance(originalRequest)
                }).catch(err => {
                    return Promise.reject(err)
                })
            }

            originalRequest._retry = true
            isRefreshing = true

            try {
                // 调用刷新token接口
                const response = await refreshToken()
                const newToken = response.data.access_token

                // 存储新token
                localStorage.setItem(ELocalStorageKey.Token, newToken)

                // 更新Authorization header
                instance.defaults.headers.common[ELocalStorageKey.Token] = newToken

                // 处理队列中的请求
                processQueue(null, newToken)

                // 重试原始请求
                originalRequest.headers[ELocalStorageKey.Token] = newToken
                return instance(originalRequest)

            } catch (refreshError) {
                // 刷新token失败，清空本地token并跳转到登录页
                processQueue(refreshError, null)
                localStorage.removeItem(ELocalStorageKey.Token)

                // 跳转到登录页面
                router.push(ERouterName.Login)
                return Promise.reject(refreshError)
            } finally {
                isRefreshing = false
            }
        }


        return Promise.reject(err)
    }
)

export default instance

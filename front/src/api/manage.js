import request from '/src/api/http/request'
import { CURRENT_CONFIG } from '@/api/http/config'

//登录
const HTTP_PREFIX=CURRENT_CONFIG.HTTP_PREFIX
export const login = async function (body) {
    const url = `${HTTP_PREFIX}/login`
    const result = await request.post(url, body)
    return result.data
}

//加载用户列表
export const upDatauser= async function (body) {
    const url = `user/updatauser`
    const result = await request.post(url, body)
    return result.data
}

//用户添加
export const addUser= async function (body) {
    const url = `user/adduser`
    const result = await request.post(url, body)
    return result.data
}

//修改用户
export const userModify= async function (body) {
    const url = `user/userModify`
    const result = await request.post(url, body)
    return result.data
}

//删除用户
export const userDelete= async function (body) {
    const url = `user/userDelete`
    const result = await request.post(url, body)
    return result.data
}


//refreshtoken
export const refreshToken = async function () {
    const url = `${HTTP_PREFIX}/token/refresh`
    const result = await request.post(url)
    return result.data
}

//新闻新增
export const saveNewsEntity = async function (body) {
    const url = `/news/saveNewsEntity`
    const result = await request.post(url, body)
    return result.data
}
//修改新闻
export const updateNewsEntity = async function (body) {
    const url = `/news/updateNewsEntity`
    const result = await request.put(url, body)
    return result.data
}
//获取新闻列表
export const getNewsList = async function (body) {
    const url = `/news/list`
    const result = await request.post(url, body)
    return result.data
}
//通过id修改新闻
export const updateNewsById = async function (id) {
    const url = `/news/${id}`
    const result = await request.get(url)
    return result.data
}
//删除新闻
export const deleteNewsById = async function (id) {
    const url = `/news/${id}`
    const result = await request.delete(url)
    return result.data
}
//设置新闻显示状态
export const setShowStatus = async function (body) {
    const url = `/news/setShowStatus`
    const result = await request.post(url, body)
    return result.data
}

//展示新闻
export const fetchNewsDetail = async function (id) {
    const url = `/news/${id}`
    const result = await request.get(url)
    return result.data
}

//记录新闻阅读量
export const saveActionEntity = async function (body) {
    const url = `/action-operation/saveActionEntity`
    const result = await request.post(url, body)
    return result.data
}
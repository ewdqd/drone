<template>
  <div class="admin-login-container">
    <el-card class="login-card">
      <h2 class="login-title">低空研究院管理端</h2>
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        label-width="80px"
        class="login-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入管理员账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%" :visiblie=onchange>登录</el-button>
        </el-form-item>
      </el-form>
      <div >
        <el-button type="text" @click="showRegisterDialog">修改密码</el-button>

      </div>
    </el-card>


  </div>
</template>

<script>
import {login, refreshToken} from '@/api/manage.js'
import {ELocalStorageKey, ERouterName} from "@/types/enum";
import {encryptAES} from "@/api/http/aes";

export default {
  name: 'AdminLogin',
  data() {

    return {
      onchange:true,
      loginForm: { username: '胡拓拓', password: '801222' ,iv:'' },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },

    }
  },

  methods: {
    showRegisterDialog() {

    },
    //登录
    handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          const res= encryptAES(this.loginForm.password)
          this.loginForm.password=res.data
          this.loginForm.iv=res.iv
          try { // 增加错误处理逻辑
            const result = await login(this.loginForm)
            if (result.code === 0) {
              localStorage.setItem(ELocalStorageKey.Token,result.data.access_token)
              localStorage.setItem(ELocalStorageKey.Username,result.data.username)
              localStorage.setItem(ELocalStorageKey.UserId,result.data.id)
              this.$message.success('登录成功！')
              this.$router.push(ERouterName.HOME) // 修正路由跳转路径
            }
          } catch {
            this.$message.info('网络连接错误，请联系管理员')

          }
        } else {
          this.$message.info('请输入用户名和密码！')
        }
      })
    },

  }
}
</script>

<style scoped>
.admin-login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
}
.login-card {
  width: 400px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.login-title {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 30px;
}
.login-form {
  margin-top: 20px;
}
</style>

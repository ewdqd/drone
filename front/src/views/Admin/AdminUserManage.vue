<template>
  <div class="admin-user-manage">
    <div style="margin-bottom: 5px;">
      <el-input v-model="username" placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search"
                @keyup.enter.native="loadUserList"></el-input>
      <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px;">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button style="margin-left: 5px;" type="primary" @click="loadUserList">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button style="margin-left: 5px;" type="primary" @click="add">新增</el-button>
    </div>
    <el-table
        :data="tableData"
        class="hover-effect-table"
        stripe
        style="width: 100%">
      <el-table-column
          align="center"
          label="编号"
          width="100">
        <template #default="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="姓名"
          prop="username"
          width="150">
        <template #default="scope">
          <div style="display: flex; align-items: center;">
            <el-avatar
                :size="50"
                :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${scope.row.username}`"
                style="margin-right: 10px; flex-shrink: 0;"
            ></el-avatar>
            <span style="flex: 1;">{{ scope.row.username }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="性别"
          prop="sex"
          width="90">
        <template #default="scope">
        <span :class="scope.row.sex === '1' ? 'male-text' : 'female-text'">
            {{ scope.row.sex === '1' ? '男' : '女' }}
        </span>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="账号"
          prop="loginid"
          width="180">
      </el-table-column>
      <el-table-column
          align="center"
          label="电话"
          prop="phone"
          width="180">
      </el-table-column>
      <el-table-column
          align="center"
          label="用户状态"
          prop="userstatus"
          width="150">
        <template #default="scope">
          <el-tag
              :type="scope.row.userstatus === '0' ? 'danger' : 'primary'"
              disable-transitions
              style="width: 80px; text-align: center;">
            {{ scope.row.userstatus === '0' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="密码"
          prop="password">
        <template #default="scope">
          <span v-if="!scope.row.showPassword">******</span>
          <span v-else>{{ scope.row.password }}</span>
          <el-button
              icon="el-icon-view"
              type="text"
              @click="togglePassword(scope.row)">
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="操作"
          prop="operate">
        <template #default="scope">
          <el-button
              class="hover-btn"
              icon="el-icon-edit"
              size="small"
              style="border-radius: 5px"
              type="primary"
              @click="mod(scope.row)"> 编辑
          </el-button>
          <el-popconfirm
              style="margin-left: 5px;"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button
                slot="reference"
                icon="el-icon-delete"
                size="small"
                style="border-radius: 5px"
                type="danger">删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        :current-page="query.current"
        :page-size="query.size"
        :page-sizes="[8, 10, 20,30]"
        :total="total"
        layout="->,total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog
        :visible.sync="centerDialogVisible"
        center
        title="提示"
        width="30%"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" style="border-radius: 10px">
        <el-form-item label="名字" prop="username">
          <el-col :span="20">
            <el-input v-model="form.username"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号" prop="loginid">
          <el-col :span="20">
            <el-input v-model="form.loginid"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="用户状态" prop="userstatus">
          <el-radio-group v-model="form.userstatus">
            <el-radio label="1">普通用户</el-radio>
            <el-radio label="0">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="concel">取 消</el-button>
    <el-button type="primary" @click="confirm">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import {Popconfirm} from 'element-ui';
import {addUser, upDatauser, userDelete, userModify} from "@/api/manage";

export default {
  components: {
    [Popconfirm.name]: Popconfirm
  },
  name: 'AdminUserManage',
  data() {
    return {
      username: '',
      sex: '',
      sexs: [
        {
          label: '男',
          value: '1'
        },
        {
          label: '女',
          value: '2'
        },
      ],
      total: 0,
      query: {
        current: 1,
        size: 8,
        name: '',
        sex: '',
      },
      centerDialogVisible: false,
      tableData: [],
      form: {
        id: '',
        username: '',
        sex: '',
        loginid: '',
        phone: '',
        userstatus: '',
        password: '',
      },
      rules: {
        username: [
          {required: true, message: '请输入名字', trigger: 'blur'}
        ],
        sex: [
          {required: true, message: '请选择性别', trigger: 'change'}
        ],
        loginid: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {trigger: 'blur'}
        ],
        phone: [
          {required: true, message: "手机号不能为空", trigger: "blur"},
          {
            pattern: /^1[3-9]\d{9}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        userstatus: [
          {
            required: true, message: "请输入用户状态"
          }
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
      }
    }
  },


  methods: {

    resetForm() {
      this.$refs.form.resetFields()
    },

    resetParam() {
      this.username = ''
      this.sex = ''
      this.loadUserList()
    },

    async userModify() {

      const result = await userModify(this.form)
      if (result.code === 0) {
        this.$message({
          message: "修改成功",
          type: 'success'
        })
        this.centerDialogVisible = false
        this.loadUserList()
        this.resetForm()

      } else {
        this.$message({
          error: "修改失败",
          type: 'error'
        })
      }


    },

    async del(row) {
      const result = await userDelete(row)
      if (result.code === 0) {
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        if (this.tableData.length === 1 && this.query.current > 1) {
          this.query.current -= 1

        }
        this.loadUserList()

      } else {
        this.$message({
          error: '删除失败',
          type: 'error'
        })
      }


    },

    async userSave() {

      const result = await addUser(this.form)
      if (result.code === 0) {
        this.$message({
          message: '添加成功！',
          type: 'success'
        });
        this.centerDialogVisible = false;
        this.loadUserList()
        this.resetForm()
      } else {
        this.$message(
            {
              error: "编辑失败",
              type: 'error'
            }
        )
      }


    },


    confirm() {

      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            this.userModify();
          } else {
            this.userSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });


    },

    concel() {

      this.centerDialogVisible = false
      this.resetForm()

    },


    add() {
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm()
      })


    },

    togglePassword(row) {
      this.$set(row, 'showPassword', !row.showPassword);
    },


    handleSizeChange(size) {
      this.query.size = size;
      this.query.current = 1;
      this.loadUserList()

    },

    handleCurrentChange(currentPage) {
      this.query.current = currentPage;
      this.loadUserList();

    },

    loadUserList() {
      this.query.username = this.username
      this.query.sex = this.sex
      upDatauser(this.query).then((res) => {
        if (res.code === 0) {
          this.tableData = res.data.records;
          this.total = res.data.total;
        } else {
          console.log("获取文件失败")
        }
      })
    },


    mod(row) {
      this.centerDialogVisible = true
      this.$nextTick(() => {

        this.form.id = row.id
        this.form.username = row.username
        this.form.sex = row.sex
        this.form.password = row.password || ''
        this.form.loginid = row.loginid;
        this.form.phone = row.phone
        this.form.userstatus = row.userstatus
      })
    },

  },
  mounted() {
    this.loadUserList();
  },

}
</script>

<style scoped>
.admin-user-manage {
  padding: 20px;
}

/*男性字体样式*/
.male-text {
  color: #0550ed;
  font-weight: 500;
}

/*女性别字体样式*/
.female-text {
  color: #e76cf5;
  font-weight: 500;
}

/*表格字体样式*/
::v-deep .el-table .cell {
  font-size: 17px;
  font-weight: 500;
}

/* 表格行悬停效果 */
:deep(.el-table .el-table__body tr) {
  transition: all 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  cursor: pointer;
  will-change: transform, box-shadow;
  position: relative;
}

:deep(.el-table .el-table__body tr:hover) {
  transform: translateY(-1px) scale(1.005);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12),
  0 4px 10px rgba(0, 0, 0, 0.06);
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
  z-index: 1;
}

:deep(.el-table .el-table__body tr:hover td) {
  border-bottom: 0.5px solid #e0e0e0;
  background: transparent !important;
  transition: border-bottom-color 0.3s ease;
}

:deep(.el-table .el-table__body tr) {
  transition: transform 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94),
  box-shadow 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94),
  background 0.4s ease;
}


</style>


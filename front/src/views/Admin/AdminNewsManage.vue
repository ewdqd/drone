<template>
  <div class="container">
    <div class="top-header">
      <div class="nav-left">
        <TextTab :buttons="[
          { label: '全部', value: 'null' },
          { label: '展示状态', value: '1' },
          { label: '隐藏状态', value: '0' }
        ]" initialActive="null" @change="handleChange" />
      </div>
      <div class="nav-right">
        <div>
          <AutoInput placeholder="搜索校园新闻" @listener="listener" />
        </div>
        <div class="primary-bt" @click="saveNewsOperation">
          <i class="el-icon-plus"></i>
          新增新闻
        </div>
      </div>
    </div>

    <!-- 表格及分页信息 -->
    <div>
      <el-table :data="apiResult.data">
        <el-table-column prop="cover" width="100" label="封面">
          <template #default="scope">
            <img style="width: 60px;height: 40px;border-radius: 5px;" :src="scope.row.cover" alt="">
          </template>
        </el-table-column>

        <el-table-column prop="title" label="标题">
          <template #default="scope">
            <div class="over-text">
              <el-tooltip class="item" effect="dark" :content="scope.row.title" placement="right">
                <div class="text-ellipsis">
                  {{ scope.row.title }}
                </div>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>

        <!-- <el-table-column prop="newsTypeName" width="108" label="类别"></el-table-column> -->
        <el-table-column prop="viewCount" :sortable="true" width="110" label="阅读量">
          <template #default="scope">
            <div>
              {{ scope.row.viewCount }}人阅读
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="collectionCount" :sortable="true" width="110" label="收藏量">
          <template #default="scope">
            <div>
              {{ scope.row.collectionCount }}人收藏
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="upvoteCount" :sortable="true" width="110" label="点赞量">
          <template #default="scope">
            <div>
              {{ scope.row.upvoteCount }}人点赞
            </div>
          </template>

        </el-table-column>
        <el-table-column prop="isShow" width="100" label="展出状态">
          <template #default="scope">
            <div>
              <el-switch @change="handleShowChange(scope.row)" v-model="scope.row.isShow" active-color="#13ce66"
                inactive-color="#f1f1f1">
              </el-switch>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" :sortable="true" width="168" label="发布时间"></el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <div class="operate-buttons">
              <el-dropdown trigger="click" placement="bottom-end">
                <span class="el-dropdown-link">
                  <i class="el-icon-more"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="newsClick(scope.row)" icon="el-icon-view">查看详情</el-dropdown-item>
                  <el-dropdown-item @click.native="handleEdit(scope.row)" icon="el-icon-edit">修改</el-dropdown-item>
                  <el-dropdown-item @click.native="handleDelete(scope.row)" icon="el-icon-delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件区域 -->
      <div class="pager">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="newsQueryDto.current" :page-sizes="[10, 20]" :page-size="newsQueryDto.size"
          layout="total, sizes, prev, pager, next, jumper" :total="apiResult.total"></el-pagination>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <el-dialog title="删除校园新闻" :show-close="false" :visible.sync="dialogDeletedVisible" width="20%">
      <span>确定删除校园新闻数据？</span>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="dialogDeletedVisible = false">取消</el-button>
        <el-button size="mini" type="primary" @click="confirmDeleted">确定</el-button>
      </span>
    </el-dialog>

    <!-- 新闻操作抽屉 -->
    <el-drawer :title="isCreateModel ? '新增新闻' : '修改新闻内容'" :visible.sync="drawer" size="45%" :direction="direction"
      :before-close="handleClose">
      <div style="padding: 10px 20px;">
        <el-form ref="form" :model="news" label-width="80px">
          <el-form-item label="*新闻标题">
            <div>
              <el-input placeholder="请输入标题(30字以内)" v-model="news.title" clearable>
              </el-input>
            </div>
          </el-form-item>
          <el-form-item label="*新闻摘要">
            <div>
              <el-input type="textarea" :rows="7" placeholder="请输入摘要" v-model="news.summary">
              </el-input>
            </div>
          </el-form-item>
          <el-form-item label="*是否展出">
            <div>
              <el-switch v-model="news.isShow" active-color="#13ce66" inactive-color="#f1f1f1">
              </el-switch>
            </div>
          </el-form-item>

          <el-form-item label="*展出封面">
            <div style="display: flex;gap: 10px;">
              <img style="width: 120px;height: 80px;border-radius: 5px;" v-if="cover" :src="cover || ''" alt="">
              <el-upload style="font-size: 50px;" action="http://localhost:21090/api/v1.0/campus-news-api/file/upload"
                :show-file-list="false" :on-success="handleImageSuccess">
                <i class="el-icon-camera-solid"></i>
              </el-upload>
            </div>
          </el-form-item>

          <el-form-item label="*新闻内容">
            <div>
              <TextEditor @on-listener="onListener" :receiveContent="content" height="300px" :api="fileUploadPath" />
            </div>
          </el-form-item>
          
          <el-form-item>
            <el-button v-if="isCreateModel" size="mini" type="primary" @click="onSubmit">立即提交</el-button>
            <el-button v-else size="mini" type="primary" @click="onUpdate">立即修改</el-button>
            <el-button size="mini" @click="handleClose">取消</el-button>
          </el-form-item>
        </el-form>
      </div>

    </el-drawer>

  </div>
</template>

<script>
import { saveNewsEntity } from '@/api/manage.js';
import { updateNewsEntity } from '@/api/manage.js';
import { getNewsList } from '@/api/manage.js';
import { setShowStatus } from '@/api/manage.js';
import AutoInput from "@/components/AutoInput.vue"; // 自己封装好的输入框组件
import TextEditor from "@/components/TextEditor.vue";
import TextTab from "@/components/TextTab.vue" // 导入封装好的Tab组件
export default {
  components: { AutoInput, TextEditor, TextTab }, // 注册组件
  data() {
    return {
      isCreateModel: true, // 是不是新增模式
      fileUploadPath: '/api/v1.0/campus-news-api/file/upload',
      drawer: false, // 操作抽屉的控制开关变量
      direction: 'rtl', // 操作弹窗显示方向：从右往左
      id: 0, // 页面即将删除的数据ID
      apiResult: { // 后端返回的查询数据的响应数据
        data: [], // 数据项
        total: 0, // 符合条件的数据总想 - 初始赋值为0
      },
      news: {
        newsTypeId: null,
        title: '',
        summary: '',
        isShow: true, // 初始是展出
      },
      content: '', // 新闻内容
      cover: '', // 新闻封面
      newsQueryDto: { // 搜索条件
        current: 1, // 当前页 - 初始是第一页
        size: 10, // 页面显示大小 - 初始是10条
      },
      dialogDeletedVisible: false, // 删除弹窗控制开关变量 - 初始是关（false）
      newsTypes: [],
    };
  },
  created() {
    this.fetchFreshData();
    // this.fetchFreshNewsType();
  },
  methods: {
    newsClick(item) {
      window.open(`/admin-news-detail?id=${item.id}`);
    },
    async handleShowChange(data) {
      try {
        const news = {
          id: data.id,
          isShow: data.isShow
        }
        await setShowStatus(news);
        this.$message.success(data.isShow ? "新闻展出成功" : '新闻已隐藏');
      } catch (error) {
        this.$message.warning(error.message);
        console.error('修改新闻展出状态异常:', error);
      }
    },
    // 修改操作
    async handleEdit(param) {
      try {
        const { data } = await this.$axios.get(`/news/${param.id}`);
        // 打开抽屉
        this.drawer = true;
        // 设置成编辑模式
        this.isCreateModel = false;
        // 设置初始值
        this.news = { ...data };
        // 设置封面
        this.cover = data.cover;
        // 设置内容
        this.content = data.content;
      } catch (error) {
        this.$message.warning(error.message);
        console.error('查询新闻详情异常:', error);
      }

    },
    handleChange(obj) {
      this.newsQueryDto.isShow = Number(obj.value); // 转成数值类型，再赋值
      this.fetchFreshData(); // 重新加载用户数据
    },
    // 富文本内容回传
    onListener(text) {
      this.content = text;
    },
    // 封面上传响应
    handleImageSuccess(res) {
      // 通知提示
      this.$notify({
        title: '封面上传',
        type: res.code === 200 ? 'success' : 'error',
        message: res.code === 200 ? '上传成功' : res.data,
        position: 'buttom-right',
        suration: 1000,
      })
      if (res.code === 200) {
        this.cover = res.data; // 响应里面的data，即后端返回的上传后的图片链接
      }
    },
    async onUpdate() {
      try {
        this.news.cover = this.cover;
        this.news.content = this.content;
        await updateNewsEntity(this.news);
        this.$message.success('新闻修改成功');
        this.handleClose();
        this.fetchFreshData();
      } catch (error) {
        this.$message.warning(error.message);
        console.error('修改新闻异常:', error);
      }
    },
    async onSubmit() {
      try {
        this.news.cover = this.cover;
        this.news.content = this.content;
        await saveNewsEntity(this.news);
        this.$message.success('新闻新增成功');
        this.handleClose();
        this.fetchFreshData();
      } catch (error) {
        this.$message.warning(error.message);
        console.error('新增新闻异常:', error);
      }
    },
    saveNewsOperation() {
      this.drawer = true;
    },
    handleClose() {
      this.drawer = false; // 关闭弹窗
      this.cover = ''; // 清除封面
      this.content = ''; // 清除内容
      this.isCreateModel = true; // 恢复成新增模式
      this.news = {}; // 新闻数据清除
    },
    // 输入框组件输入回传
    listener(text) {
      this.newsQueryDto.title = text; // 赋值查询条件的内容
      this.fetchFreshData(); // 重新加载数据
    },
    // 查询新闻类别
    // async fetchFreshNewsType() {
    //   try {
    //     const { data } = await this.$axios.get('/news/types');
    //     this.newsTypes = data;
    //   } catch (error) {
    //     console.error('查询新闻类别信息异常:', error);
    //   }
    // },

    // 查询校园新闻数据
    async fetchFreshData() {
      try {
        const { data, total } = await getNewsList(this.newsQueryDto);
        this.apiResult.data = data;
        this.apiResult.total = total;
      } catch (error) {
        console.error('查询校园新闻信息异常:', error);
      }
    },
    
    // 分页 - 处理页面页数切换
    handleSizeChange(size) {
      this.newsQueryDto.size = size; // 当前页面大小重置
      this.newsQueryDto.currrent = 1; // 当前页设置为第一页
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 分页 - 处理页面当前页切换
    handleCurrentChange(current) {
      this.newsQueryDto.current = current; // 当前页选中
      this.fetchFreshData(); // 重新加载页面数据
    },
    // 表格点击删除校园新闻
    handleDelete(row) {
      this.dialogDeletedVisible = true; // 开启删除弹窗确认
      this.id = row.id;
    },
    // 校园新闻删除
    async confirmDeleted() {
      try {
        const { code } = await this.$axios.delete(`/news/${this.id}`);
        if (code === 200) {
          this.$notify.success({
            title: '校园新闻删除',
            message: '删除成功',
            position: 'buttom-right',
            suration: 1000,
          });
          this.dialogDeletedVisible = false; // 关闭删除确认弹窗
          this.id = null; // 将标识ID置位
          this.fetchFreshData(); // 删除校园新闻数据之后，重新加载校园新闻数据
        }
      } catch (error) {
        console.log("删除校园新闻数据异常：", error);
      }
    }
  },
};
</script>

<style scoped lang="scss">
.text-ellipsis {
  white-space: nowrap;
  /* 禁止换行 */
  overflow: hidden;
  /* 隐藏溢出内容 */
  text-overflow: ellipsis;
  /* 显示省略号 */
  max-width: 150px;
  /* 根据需要设置最大宽度 */
}

.pager {
  margin-block: 20px;
}

/* 默认隐藏操作按钮 */
.operate-buttons {
  opacity: 0;
  transition: opacity 0.3s;
  /* 添加过渡效果 */
  cursor: pointer;

  i {
    padding: 8px;
    border-radius: 6px;
    transition: all .5s ease;

    &:hover {
      background-color: rgb(236, 237, 238);
    }
  }

}

/* 行悬停时显示操作按钮 */

.container {
  margin: 10px 20px;
}

.top-header {
  margin-block: 10px;
  padding-inline: 10px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .nav-left,
  .nav-right {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 10px;
  }

  .nav-left {
    display: flex;
  }

  .nav-right.primary-bt {
    cursor: pointer;
  }

}
</style>
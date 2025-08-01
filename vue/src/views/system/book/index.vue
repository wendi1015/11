<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="关键字" prop="keywords">
          <el-input
            v-model="queryParams.keywords"
            placeholder="图书编号/名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item label="出版日期" prop="publishDateRange">
          <el-date-picker
            v-model="queryParams.publishDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item class="search-buttons">
          <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
          <el-button icon="refresh" @click="handleResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="hover" class="data-table">
      <div class="data-table__toolbar">
        <div class="data-table__toolbar--actions">
          <el-button type="success" icon="plus" @click="handleAddClick()">新增</el-button>
          <el-button
            type="danger"
            :disabled="ids.length === 0"
            icon="delete"
            @click="handleDelete()"
          >
            删除
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        highlight-current-row
        :data="tableData"
        border
        class="data-table__content"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="图书编号" prop="bookNo" width="120" />
        <el-table-column label="图书名称" prop="bookName" />
        <el-table-column label="出版日期" prop="publishDate" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.publishDate) }}
          </template>
        </el-table-column>
        <el-table-column label="价格(元)" prop="price" width="120" align="right">
          <template #default="scope">
            {{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center" width="150">
          <template #default="scope">
            <el-button
              type="primary"
              link
              size="small"
              icon="edit"
              @click.stop="handleEditClick(scope.row.id)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              link
              size="small"
              icon="delete"
              @click.stop="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="fetchData"
      />
    </el-card>

    <!--图书弹窗-->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="handleCloseDialog"
    >
      <el-form ref="dataFormRef" :model="formData" :rules="computedRules" label-width="100px">
        <el-card shadow="never">
          <el-form-item label="图书编号" prop="bookNo">
            <el-input v-model="formData.bookNo" placeholder="请输入图书编号" />
          </el-form-item>

          <el-form-item label="图书名称" prop="bookName">
            <el-input v-model="formData.bookName" placeholder="请输入图书名称" />
          </el-form-item>

          <el-form-item label="出版日期" prop="publishDate">
            <el-date-picker
              v-model="formData.publishDate"
              type="date"
              placeholder="选择出版日期"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>

          <el-form-item label="价格(元)" prop="price">
            <el-input-number
              v-model="formData.price"
              :precision="2"
              :step="0.1"
              :min="0"
              controls-position="right"
            />
          </el-form-item>

          <el-form-item label="简介">
            <el-input v-model="formData.description" type="textarea" placeholder="请输入图书简介" />
          </el-form-item>
        </el-card>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmitClick">确 定</el-button>
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "BookList",
  inherititems: false,
});

import BookAPI, { BookPageQuery, BookPageVO, BookForm } from "@/api/library/book.api";
import { formatDate } from "@/utils/date";
import router from "@/router";

const queryFormRef = ref();
const dataFormRef = ref();

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<BookPageQuery>({
  pageNum: 1,
  pageSize: 10,
  keywords: "",
  publishDateRange: [],
});

const tableData = ref<BookPageVO[]>();

const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<BookForm>({
  bookNo: "",
  bookName: "",
  publishDate: "",
  price: 0,
  description: "",
});

const computedRules = computed(() => {
  const rules: Partial<Record<string, any>> = {
    bookNo: [{ required: true, message: "请输入图书编号", trigger: "blur" }],
    bookName: [{ required: true, message: "请输入图书名称", trigger: "blur" }],
    publishDate: [{ required: true, message: "请选择出版日期", trigger: "blur" }],
    price: [{ required: true, message: "请输入价格", trigger: "blur" }],
  };
  return rules;
});

// 获取数据
function fetchData() {
  loading.value = true;
  BookAPI.getPage(queryParams)
    .then((data) => {
      tableData.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

// 查询（重置页码后获取数据）
function handleQuery() {
  queryParams.pageNum = 1;
  fetchData();
}

// 重置查询
function handleResetQuery() {
  queryFormRef.value.resetFields();
  queryParams.pageNum = 1;
  fetchData();
}

// 行选择
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

// 新增图书
function handleAddClick() {
  dialog.visible = true;
  dialog.title = "新增图书";
}

/**
 * 编辑图书
 *
 * @param id 图书ID
 */
function handleEditClick(id: string) {
  dialog.visible = true;
  dialog.title = "修改图书";
  BookAPI.getFormData(id).then((data) => {
    Object.assign(formData, data);
  });
}

// 提交图书表单
function handleSubmitClick() {
  dataFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      loading.value = true;
      const id = formData.id;
      if (id) {
        BookAPI.update(id, formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        BookAPI.create(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

// 关闭图书弹窗
function handleCloseDialog() {
  dialog.visible = false;

  dataFormRef.value.resetFields();
  dataFormRef.value.clearValidate();

  formData.id = undefined;
  formData.bookNo = "";
  formData.bookName = "";
  formData.publishDate = "";
  formData.price = 0;
  formData.description = "";
}

/**
 * 删除图书
 *
 * @param id 图书ID
 */
function handleDelete(id?: number) {
  const bookIds = [id || ids.value].join(",");
  if (!bookIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      BookAPI.deleteByIds(bookIds).then(() => {
        ElMessage.success("删除成功");
        handleResetQuery();
      });
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
}

onMounted(() => {
  handleQuery();
});
</script>

<style scoped>
.search-container {
  margin-bottom: 20px;
}
.data-table {
  margin-top: 20px;
}
.data-table__toolbar {
  margin-bottom: 10px;
}
</style>
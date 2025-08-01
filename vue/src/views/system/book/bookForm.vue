<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="80px"
      style="margin-top: 20px"
    >
      <el-form-item label="图书编号" prop="bookNo">
        <el-input v-model="form.bookNo" placeholder="请输入图书编号" />
      </el-form-item>
      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" placeholder="请输入图书名称" />
      </el-form-item>
      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker
          v-model="form.publishDate"
          type="date"
          placeholder="请选择出版日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input
          v-model="form.price"
          placeholder="请输入价格"
          type="number"
          step="0.01"
          min="0"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "BookForm",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      // 表单参数
      form: {
        id: null,
        bookNo: "",
        bookName: "",
        publishDate: "",
        price: null
      },
      // 表单校验
      rules: {
        bookNo: [
          { required: true, message: "图书编号不能为空", trigger: "blur" }
        ],
        bookName: [
          { required: true, message: "图书名称不能为空", trigger: "blur" }
        ],
        publishDate: [
          { required: true, message: "出版日期不能为空", trigger: "change" }
        ],
        price: [
          { required: true, message: "价格不能为空", trigger: "blur" },
          { type: "number", message: "价格必须为数字", trigger: "blur" },
          { validator: this.checkPrice, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 价格校验
    checkPrice(rule, value, callback) {
      if (value < 0) {
        callback(new Error("价格不能为负数"));
      } else {
        callback();
      }
    },
    // 重置表单
    resetForm() {
      this.form = {
        id: null,
        bookNo: "",
        bookName: "",
        publishDate: "",
        price: null
      };
      this.$refs.form.clearValidate();
    },
    // 设置表单数据
    setForm(row) {
      this.resetForm();
      this.form = { ...row };
    },
    // 表单验证
    validate() {
      return new Promise(resolve => {
        this.$refs.form.validate(valid => {
          resolve(valid);
        });
      });
    },
    // 提交表单
    handleSubmit() {
      this.$emit("submit");
    },
    // 取消操作
    handleCancel() {
      this.$emit("close");
    }
  }
};
</script>

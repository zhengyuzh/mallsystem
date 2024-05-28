<!--
 * 商品信息
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
  <div style="width: 1000px; margin: 50px auto">
    <el-form :model="good">
      <el-form-item label="商品名称" label-width="150px">
        <el-input
          v-model="good.name"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>
      <el-form-item label="商品描述" label-width="150px">
        <el-input
          v-model="good.description"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>
      <el-form-item label="规格" label-width="150px">
        <el-tag
          closable
          @close="handleClose(standard)"
          :disable-transitions="true"
          style="margin-right: 10px"
          v-for="(standard, index) in standards"
          :key="index"
          @click="editStandard(index)"
          >{{ standard.value }}</el-tag
        >
        <el-button icon="el-icon-plus" circle @click="addStandard"></el-button>
      </el-form-item>

      <el-form-item label="折扣" label-width="150px">
        <el-input
          v-model="good.discount"
          autocomplete="off"
          style="width: 80%"
        ></el-input>
      </el-form-item>

      <el-form-item label="分类" label-width="150px">
        <el-select v-model="good.categoryId" placeholder="请选择">
          <el-option
            v-for="item in categoryItems"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品图片" label-width="150px">
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="baseApi + '/file/upload'"
          :file-list="fileList"
          :on-change="handleChange"
          :limit="1"
          :on-remove="handleRemove"
          :on-success="handleImgSuccess"
          :auto-upload="false"
        >
          <el-button slot="trigger" size="small" type="primary"
            >选取文件</el-button
          >
          <div slot="tip" class="el-upload__tip">
            只能上传一个jpg/png文件，且不超过500kb
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label-width="150px">
        <el-button type="success" @click="submit">提交</el-button>
      </el-form-item>
    </el-form>
    <!-- 弹窗   -->
    <el-dialog
      title="规格信息"
      :visible.sync="dialogFormVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="standard">
        <el-form-item label="规格名称" label-width="150px">
          <el-input
            v-model="standard.value"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
        <el-form-item label="价格" label-width="150px">
          <el-input
            v-model="standard.price"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
        <el-form-item label="库存" label-width="150px">
          <el-input
            v-model="standard.store"
            autocomplete="off"
            style="width: 80%"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveStandard">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import API from "@/utils/request";
const url = "/api/good";
export default {
  name: "GoodInfo",
  data() {
    return {
      baseApi: this.$store.state.baseApi,
      good: {
        discount: "1",
      },
      standards: [],
      index: 0,
      standard: {},
      fileList: [],
      dialogFormVisible: false,
      categoryItems: [],
      checkedCategory: {},
    };
  },
  created() {
    this.request.get("/api/category").then((res) => {
      if (res.code === "200") {
        this.categoryItems = res.data;
      }
      if (this.$route.query.good) {
        this.good = JSON.parse(this.$route.query.good);
        this.fileList = [{ name: "原始图片", url: this.good.imgs }];
        this.checkedCategory = this.categoryItems[this.good.categoryId];
        //从服务器获取商品规格信息
        this.request.get(url + "/standard/" + this.good.id).then((res) => {
          if (res.code === "200") {
            let standards = JSON.parse(res.data);
            this.standards = standards;
          } else {
            //没有规格
          }
        });
      }
    });
  },
  methods: {
    /*
    规格操作
     */
    editStandard(index) {
      this.standard = this.standards[index];
      this.index = index;
      this.dialogFormVisible = true;
    },
    addStandard() {
      this.standard = {};
      this.index = this.standards.length;
      this.dialogFormVisible = true;
    },
    saveStandard() {
      if (this.standard.value == undefined || this.standard.value == "") {

        this.$message({
          type: "error",
          message: "请输入规格名称",
          showClose: true,
        });
        return;
      }
      if (this.standard.price == undefined || this.standard.price == "") {
        this.$message({
          type: "error",
          message: "请输入价格",
          showClose: true,
        });
        return;
      }
      if (this.standard.store == undefined || this.standard.store == "") {
        this.$message({
          type: "error",
          message: "请输入库存",
          showClose: true,
        });
        return;
      }
      this.standards[this.index] = this.standard;
      this.dialogFormVisible = false;
    },
    handleClose(standard) {
      this.standards.splice(this.standards.indexOf(standard), 1);
    },
    /*
    文件上传操作
     */
    handleImgSuccess(res) {
      this.good.imgs = res.data;
      this.save();
    },
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
    handleRemove() {
      this.fileList.pop();
    },
    /*
    保存数据操作
     */
    //点击提交按钮
    submit() {
      if (this.good.name == undefined || this.good.name.trim() == "") {
        this.$message({
          type: "error",
          message: "请输入商品名称",
          showClose: true,
        });
        return false;
      }
      if (
        this.good.description == undefined ||
        this.good.description.trim() == ""
      ) {
        this.$message({
          type: "error",
          message: "请输入商品描述",
          showClose: true,
        });
        return false;
      }
      if (this.standards.length === 0) {
        this.$message({
          type: "error",
          message: "请至少添加一个规格",
          showClose: true,
        });
        return false;
      }
      if (this.good.discount == undefined || this.good.discount === "") {
        this.$message({
          type: "error",
          message: "请输入商品折扣",
          showClose: true,
        });
        return false;
      }
      if (
        this.good.categoryId == undefined
      ) {
        this.$message({
          type: "error",
          message: "请选择商品分类",
          showClose: true,
        });
        return false;
      }
      if (this.fileList.length === 0) {
        this.$message({
          type: "error",
          message: "请上传图片",
          showClose: true,
        });
        return false;
      }
      // 先上传图片再保存数据
      if (this.fileList[0].status === "ready") {
        this.$refs.upload.submit();
      } else if (this.fileList[0].status === "success") {
        //没有修改初始的图片。直接保存数据
        this.save();
      }
    },
    //保存数据
    save() {
      API.post(url, this.good).then((res2) => {
        if (res2.code === "200") {
          this.good.id = res2.data;
          this.request
            .post(url + "/standard?goodId=" + this.good.id, this.standards)
            .then((res) => {
              if (res.code === "200") {
                this.$message({
                  type: "success",
                  message: "操作成功",
                  showClose: true,
                });
                this.$router.go(-1);
              } else {
                this.$message({
                  type: "error",
                  message: "操作失败",
                  showClose: true,
                });
              }
            });
        } else {
          this.$message({
            type: "error",
            message: res2.msg,
          });
        }
      });
    },
  },
};
</script>

<style scoped>
</style>

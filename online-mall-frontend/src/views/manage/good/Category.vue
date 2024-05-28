<!--
 * 后台商品分类管理
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
  <div>

    <div style="width: 100%; margin: 0px auto">

      <!--新增按钮-->
      <el-button @click="addDialogFormVisible = true" round type="success" style="margin: 10px;width: 150px">
        <i class="el-icon-circle-plus"style="padding-right: 6px"></i>新增
      </el-button>

      <el-table :data="icons" stripe border fixed>
        <!--下级分类表-->
        <el-table-column type="expand" label="下级分类" width="100px">

          <template slot-scope="scope">

            <el-table
              :data="scope.row.categories"
              :header-cell-style="{ background: '#cbefea', color: 'black' }"
            >

              <el-table-column label="分类id" prop="id"></el-table-column>
              <el-table-column label="分类名称" prop="name"></el-table-column>

              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleEditCategory(scope.row)"
                    >修改</el-button
                  >

                  <el-popconfirm
                    @confirm="deleteCategory(scope.row)"
                    title="确定删除？"
                  >
                    <el-button
                      type="danger"
                      size="mini"
                      slot="reference"
                      >删除</el-button
                    >
                  </el-popconfirm>
                </template>
              </el-table-column>

            </el-table>
          </template>
        </el-table-column>

        <el-table-column label="id" prop="id" width="60px"></el-table-column>
        <el-table-column label="icon">
          <template slot-scope="scope">
            <i class="iconfont" v-html="scope.row.value"></i>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              circle
              @click="handleEditIcon(scope.row)"
            ></el-button>
            <el-button
              type="success"
              icon="el-icon-plus"
              circle
              @click="handleAddCategory(scope.row)"
            ></el-button>

            <el-popconfirm
              @confirm="deleteIcon(scope.row.id)"
              title="确定删除？"
            >
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                slot="reference"
                style="margin-left: 10px"
              ></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>

    </div>

    <!--icon修改弹窗-->
    <el-dialog title="修改上级分类" :visible.sync="dialogFormVisible">

      <el-form :model="icon">
        <el-form-item label="图标" label-width="100px">
          <i class="iconfont" v-html="icon.value"></i>
        </el-form-item>

        <el-form-item label="更改图标" label-width="100px">
          <el-select placeholder="请选择图标" v-model="icon.value">
            <el-option v-for="item in iconStore" :value="item" :key="item">
              <i class="iconfont" v-html="item"></i>
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editIcon">确 定</el-button>
      </div>

    </el-dialog>

    <el-dialog title="新增上级分类" :visible.sync="addDialogFormVisible">

     <el-form :model="addIcon">
        <el-form-item label="图标" label-width="100px">
          <i class="iconfont" v-html="addIcon.value"></i>
        </el-form-item>

        <el-form-item label="更改图标" label-width="100px">
          <el-select placeholder="请选择图标" v-model="addIcon.value">
            <el-option v-for="item in iconStore" :value="item" :key="item">
              <i class="iconfont" v-html="item"></i>
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveIcon">确 定</el-button>
      </div>

    </el-dialog>

  </div>
</template>

<script>
import icons from "@/utils/icons";
export default {
  name: "Category",

  //页面初始化数据
  data() {
    return {
      options: [],
      searchText: "",
      user: {},
      //从icons.js中引入常量iconStore
      iconStore: icons.iconStore,
      icons: [],
      icon: {},
      addIcon: {},
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false,
      addDialogFormVisible: false,
    };
  },
  //页面创建完成动作
  created() {
    this.user = localStorage.getItem("user")
      ? JSON.parse(sessionStorage.getItem("user"))
      : {};
    this.load();
  },
  //方法
  methods: {
    //加载页面
    load() {
      this.request.get("/api/icon").then((res) => {
        this.icons = res.data;
      });
    },
    //编辑分类
    handleEditCategory(category) {
      this.$prompt("请输入修改后的名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      }).then(({ value }) => {
        category.name = value;
        this.request.post("/api/category", category).then((res) => {
          if (res.code === "200") {
            this.$message.success("修改成功");
          } else {
            this.$message.error("修改失败");
          }
        });
      });
    },
    //添加分类
    handleAddCategory(icon) {
      this.$prompt("请输入新增的下级分类名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      }).then(({ value }) => {
        this.request
          .post("/api/category/add", { name: value, iconId: icon.id })
          .then((res) => {
            if (res.code === "200") {
              this.$message.success("新增成功");
              this.load();
            } else {
              this.$message.error("新增失败");
            }
          });
      });
    },
    //编辑图标弹窗
    handleEditIcon(icon) {
      this.icon = JSON.parse(JSON.stringify(icon));
      this.dialogFormVisible = true;
    },
    //编辑图标
    editIcon() {
      //删除无用的属性
      delete this.icon.categories;
      this.request.post("/api/icon", this.icon).then((res) => {
        if (res.code === "200") {
          this.$message.success("修改成功");
          this.dialogFormVisible = false;
        } else {
          this.$message.error("修改失败");
        }
      });
    },
    //保存图标
    saveIcon() {
      //新增上级分类
      if (this.addIcon.value == undefined) {
        this.$message.error("请选择上级分类图标");
        return;
      }
      this.request.post("/api/icon", this.addIcon).then((res) => {
        if (res.code === "200") {
          this.$message.success("新增成功");
          this.addDialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("新增失败");
        }
      });
    },
    //删除图标
    deleteIcon(iconId) {
      //删除上级分类
      this.request.get("/api/icon/delete?id=" + iconId).then((res) => {
        if (res.code == "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    //删除分类
    deleteCategory(category) {
      //删除下级分类
      this.request.get("/api/category/delete?id=" + category.id).then((res) => {
        if (res.code == "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};

</script>

<style scoped>
@import "../../../resource/css/icon.css";
</style>

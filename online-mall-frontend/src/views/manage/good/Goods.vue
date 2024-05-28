<!--
 * 商品管理
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="searchText" @keyup.enter.native="load" style="width: 200px">
        <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="load" round type="primary" style="margin: 5px">搜索</el-button>
      <el-button @click="reset" round type="info" style="margin: 5px">重置</el-button>
      <el-button @click="add" round type="success" style="margin: 5px">新增</el-button>
    </div>

    <el-table :data="tableData" stripe border fixed style="width: 100%">
      <el-table-column prop="id" label="商品id" width="80px"></el-table-column>

      <el-table-column prop="name" label="商品名称"></el-table-column>

      <el-table-column label="商品图片" width="120px">
        <template slot-scope="scope">
          <img :src="baseApi + scope.row.imgs" style="width: 90px;height: 80px">
        </template>
      </el-table-column>

      <el-table-column prop="description" label="商品描述"></el-table-column>

      <el-table-column prop="discount" label="折扣"></el-table-column>

      <el-table-column prop="sales" label="销量"></el-table-column>

      <el-table-column prop="saleMoney" label="销售额（元)"></el-table-column>

      <el-table-column prop="createTime" label="创建时间">
        <template slot-scope="scope">
          {{scope.row.createTime.replace(" ","&nbsp;&nbsp;")}}
        </template>

      </el-table-column>

      <el-table-column label="推荐" width="150" >
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.recommend"
              @change="handleRecommend(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle  @click="edit(scope.row)"></el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="确定删除？"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference" style="margin-left: 10px"></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--  分页控件  -->
    <div style="margin-top: 10px">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[3, 5, 8, 10]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <!--新增商品弹窗-->
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">
      <el-form :model="entity">

        <el-form-item label="商品名称" label-width="150px">
          <el-input v-model="entity.name" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="商品描述" label-width="150px">
          <el-input v-model="entity.description" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="折扣" label-width="150px">
          <el-input v-model="entity.discount" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="分类id" label-width="150px">
          <el-input v-model="entity.categoryId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="商品图片" label-width="150px">
          <el-upload
              class="upload-demo"
              ref="upload"
              :action="baseApi + '/file/upload'"
              :file-list="fileList"
              :on-change="handleChange"
              :limit="2"
              :on-success="handleImgSuccess"
              :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import API from '../../../utils/request'
const url = "/api/good/"

export default {
  name: "Goods",
  //页面初始化数据
  data() {
    return {
      baseApi: this.$store.state.baseApi,
      fileList: [],
      options: [],
      searchText: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      dialogFormVisible: false
    };
  },
  //页面加载完成
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.load()

  },
  //方法
  methods: {
    //页面变化
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    //翻页
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    //修改商品推荐
    handleRecommend(good){
      API.get(url + "recommend", {
        params: {
          id: good.id,
          isRecommend : good.recommend,
        }
      }).then(res => {
        if(res.code==='200'){
          this.$message.success("修改成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    //页面加载
    load() {
       API.get(url + "fullPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            searchText: this.searchText
          }
       }).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
       })
    },
    //重置搜索
    reset(){
      this.searchText = '';
      this.load()
    },
    //新增
    add() {
      this.$router.push("goodInfo")
    },
    //编辑
    edit(obj) {
      this.entity = JSON.parse(JSON.stringify(obj))
      this.$router.push({name: 'goodInfo',query:{good: JSON.stringify(this.entity)}})
    },
    //图片上传成功
    handleImgSuccess(res){
      this.entity.imgs = res.data;
      API.post(url, this.entity).then(res2 => {
        if (res2.code === '200') {
          this.$message({
            type: "success",
            message: "操作成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res2.msg
          })
        }
        this.load()
        this.dialogFormVisible = false
      })
    },
    //保存商品
    save() {
      //上传图片
      if(this.fileList.length!==0){
        this.$refs.upload.submit();
      }else{
        //不上传图片
        API.post(url, this.entity).then(res2 => {
          if (res2.code === '200') {
            this.$message({
              type: "success",
              message: "操作成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res2.msg
            })
          }
          this.load()
          this.dialogFormVisible = false
        })
      }
    },
    //删除商品
    del(id) {
      API.delete(url + id).then(res => {
        this.$message({
          type: "success",
          message: "操作成功"
        })
        this.load()
      })
    },
    handleChange(file, fileList){
      this.fileList = fileList.slice(-3);
    },

  }

};
</script>

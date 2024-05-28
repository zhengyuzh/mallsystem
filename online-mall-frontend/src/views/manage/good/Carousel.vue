<!--
 * 后台轮播图管理
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
  <div>
    <!--新增按钮-->
    <div style="text-align: left">
      <el-button @click="add" round type="success" style="margin: 10px;width: 150px">
        <i class="el-icon-circle-plus"style="padding-right: 6px"></i>新增</el-button>
    </div>

    <div>

      <!--表格-->
      <el-table :data="tableData" stripe border fixed style="width: 100%;margin: 2px auto">

        <el-table-column width="400" label="商品">
          <template slot-scope="scope">
            <a :href="'/goodView/'+scope.row.goodId">{{scope.row.goodName}}</a>
          </template>
        </el-table-column>

        <el-table-column width="350"  label="图片" >
          <template  slot-scope="scope">
            <img :src="baseApi + scope.row.img" width="200" height="180" />
          </template>
        </el-table-column>

        <el-table-column prop="showOrder" width="150" label="轮播顺序"></el-table-column>

        <el-table-column
            fixed="right"
            label="操作"
            width="400">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit"   @click="edit(scope.row)"></el-button>
            <el-popconfirm
                @confirm="del(scope.row.id)"
                title="确定删除？"
            >
              <el-button type="danger" icon="el-icon-delete"  slot="reference" style="margin-left: 10px"></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>
    </div>

    <!-- 新增轮播图弹窗   -->
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false">

      <el-form :model="entity">

        <el-form-item label="商品id" label-width="150px">
          <el-input v-model="entity.goodId" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="轮播顺序" label-width="150px">
          <el-select v-model="entity.showOrder">
            <el-option v-for="index in tableData.length" :key="index" :label="index" :value="index">
            </el-option>
          </el-select>
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
const url = "/api/carousel/"

export default {
  name: "Carousel",
  //初始化数据
  data() {
    return {
      baseApi: this.$store.state.baseApi,
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
    this.load()
  },
  //犯法
  methods: {
    //分页页面变化
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    //翻页
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    //页面加载
    load() {
      API.get(url).then(res => {
        this.tableData = res.data || []
      })
    },
    //新增轮播图弹出
    add() {
      this.entity = {}
      this.tableData.length++;
      this.dialogFormVisible = true
    },
    //编辑
    edit(row) {
      this.entity = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    //保存轮播图
    save() {
      if(this.entity.goodId == undefined || this.entity.goodId === "") {
          this.$message.error("商品id不能为空")
          return;
      }
      if(this.entity.showOrder == undefined) {
          this.$message.error("轮播顺序不能为空")
          return;
      }

      //调用后台接口
      API.post(url, this.entity).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.load()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }

      })
    },
    //删除轮播图
    del(id) {
      API.delete(url + id).then(res => {
        if(res.code==='200'){
          this.$message({
            type: "success",
            message: "删除成功",
          });
          this.load();
        }else {
          this.$message.error(res.msg);
        }
      })
    }
  },
};
</script>

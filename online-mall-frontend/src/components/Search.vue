<!--
 * 前台首页搜索栏
 *
 * @Author: ShanZhu
 * @Date: 2024-3-11
-->
<template>
  <!--搜索栏-->
  <el-row>
    <el-col :span="24" style="text-align: center;">
      <div class="search" style="display: inline-block">
        <div style="height: 60px;margin-left: 30px">
          <input @keydown.enter="$emit('search',searchText)" type="text" placeholder="请输入想要购买的商品" v-model="searchText">
          <button @click="$emit('search',searchText)"></button>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Search",
  data(){
    return{
      searchText:'',
    }
  }

}
</script>

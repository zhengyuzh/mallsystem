<!--
 * 销售额页面
 *
 * @Author: ShanZhu
 * @Date: 2024-3-11
-->
<template>
  <div>
    <div class="header">
      销量第{{index}}
    </div>
    <div class="body">
      <div style="display: inline-block;margin-left: 40px">
        <router-link :to="'/goodview/'+good.id">
          <img :src="baseApi + good.imgs" style="width: 120px;height:120px">
        </router-link>
      </div>
      <div style="display: inline-block;line-height: 40px;padding: 20px" >
        <table>
          <tr>
            <th><span style="color: #ffffff">商品id</span></th>
            <th style="width: 300px"><span style="color: #ffffff">商品名称</span></th>
            <th><span style="color: #ffffff">销售额</span></th>
          </tr>
          <tr>
            <td><span style="color: #ffffff">{{good.id}}</span></td>
            <router-link :to="'/goodview/'+good.id">
              <td style="width: 300px"><span style="color: #ffffff">{{good.name}}</span></td>
            </router-link>
            <td><span style="color: #ffffff">￥{{good.saleMoney}}</span></td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "IncomeItem",
  props:{
    good: Object,
    index: Number,
    categories: Array
  },
  data() {
    return {
      baseApi: this.$store.state.baseApi,
    }
  }
}
</script>

<style scoped>
.header{
  background-image: linear-gradient(to bottom right, #e75c09, rgb(231, 92, 9));
  padding: 10px;
  color: #ffffff;
  font-size: 20px;
  font-weight: bolder;
}
.body{
  background-color: #3186cb;
  padding: 5px;
}
td{
  width: 120px;
  text-align: center;
  font-size: 20px;
  font-weight: lighter;
}
th{
  width: 120px;
  text-align: center;
  font-size: 18px;
  color: #00b7ff;
  font-weight: normal;
}
a{
text-decoration: none;

}
</style>

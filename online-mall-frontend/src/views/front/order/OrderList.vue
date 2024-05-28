<!--
 * 订单列表
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
<div class="list" style="width: 55%;height:100%;margin: 20px auto">

  <div v-if="orders.length === 0" class="empty-box">
    <span style="font-family: 华文彩云;font-size: 40px" >没有订单记录</span>
  </div>
  <template v-for="order in orders">
    <order-item :order="order" :key="order.id" style="margin-bottom: 5px"></order-item>
  </template>

</div>
</template>

<script>
import OrderItem from "@/components/OrderItem";
import API from "@/utils/request";
export default {
  name: "OrderList",
  components:{
    'order-item': OrderItem,
  },
  data() {
    return{
      orders: {},
    }
  },
  created() {
    API.get("/userid").then(res=>{
      API.get("/api/order/userid/"+ res).then(res=>{
        if(res.code==='200'){
          this.orders = res.data;
          for(var i = 0; i < this.orders.length; ++i) {
            this.orders[i].create_time = this.orders[i].create_time.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
          }
        }
      })
    })
  }
}
</script>

<style scoped>
.list{
  width: 60%;
  margin: 10px auto;
}

.empty-box{
  border-radius: 30px;
  text-align: center;
  background-color: white;
  padding: 100px;
}
</style>

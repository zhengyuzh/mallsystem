<!--
 * 我的购物车页面
 *
 * @Author: ShanZhu
 * @Date: 2023-11-11
-->
<template>
<div style="width: 55%;height:100%;margin: 20px auto">
  <!--购物车为空时-->
  <div v-if="carts.length === 0" class="empty-box">
    <span style="font-family: 华文彩云;font-size: 40px" >购物车是空的哦</span>
  </div>
  <!--购物车商品-->
  <template v-for="cart in carts">
    <cart-item :cart="cart" @delete="delItem" :key="cart.id" style="margin-bottom: 10px"></cart-item>
  </template>

</div>
</template>

<script>
import CartItem from "@/components/CartItem";
export default {
  name: "Cart",  data(){
    return{
      userId: Number,
      carts: [],
    }
  },
  components:{
    'cart-item': CartItem,
  },
  created() {
    this.request.get("/userid").then(res=> {
      this.userId = res;
      this.request.get("/api/cart/userid/"+this.userId).then(res=>{
        if(res.code==='200'){
          this.carts = res.data;
          for(var i = 0; i < this.carts.length; ++i) {
            this.carts[i].createTime = this.carts[i].createTime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
          }
        }
      })
    })

  },
  methods:{
    delItem(id){
      this.carts = this.carts.filter(item => item.id != id)
    }
  },
}
</script>

<style scoped>
.empty-box{
  border-radius: 30px;
  text-align: center;
  background-color: white;
  padding: 100px;
}
</style>

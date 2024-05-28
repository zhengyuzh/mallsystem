<!--
 * 左侧滑动-菜单栏
 *
 * @Author: ShanZhu
 * @Date: 2024-3-11
-->
<template>
  <el-menu :default-openeds="['2', 'good']" style="height: 100%;"
           background-color="rgb(28,28,28)"
           text-color="#fff"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >

    <!-- 标题，图标等   -->
    <div style="height: 60px;margin-left: 30px; line-height: 60px">
      <router-link to="/manage/home">
        <img src="../resource/logo.png" style="width: 40px;position: relative; top: 13px;right: 6px">
      </router-link>
      <span slot="title" style="color: aliceblue;font-size: 20px" v-show="!isCollapse">商城管理系统</span>
    </div>

    <!-- 主页跳转 -->
    <el-menu-item index="/manage/home">
      <i class="el-icon-house"></i><span slot="title">主页</span>
    </el-menu-item>

    <!-- 前台跳转   -->
    <el-menu-item index="/">
      <i class="el-icon-house"></i><span slot="title">前台</span>
    </el-menu-item>

    <!-- 系统菜单栏  -->
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i><span slot="title">系统管理</span></template>
        <el-submenu v-show="userGroup" index="user">

        <template slot="title">账户相关</template>
          <el-menu-item index="/manage/user" v-if="menuFlags.userMenu">用户管理</el-menu-item>
        </el-submenu>

        <el-submenu v-if="fileGroup" index="file">
          <template slot="title">文件相关</template>
          <el-menu-item index="/manage/file" v-if="menuFlags.fileMenu">文件管理</el-menu-item>
          <el-menu-item index="/manage/avatar" v-if="menuFlags.avatarMenu">头像管理</el-menu-item>
        </el-submenu>

        <el-submenu v-if="GoodGroup" index="good">
          <template slot="title">商城管理</template>
          <el-menu-item index="/manage/category" v-if="menuFlags.categoryMenu">商品分类管理</el-menu-item>
          <el-menu-item index="/manage/carousel" v-if="menuFlags.carouselMenu">轮播图管理</el-menu-item>
          <el-menu-item index="/manage/good" v-if="menuFlags.goodMenu">商品管理</el-menu-item>
          <el-menu-item index="/manage/order" v-if="menuFlags.orderMenu">订单管理</el-menu-item>
        </el-submenu>

      <el-submenu v-if="incomeGroup" index="income">
        <template slot="title">销售额统计</template>
        <el-menu-item index="/manage/incomeChart" v-if="menuFlags.incomeChartMenu">图表分析</el-menu-item>
        <el-menu-item index="/manage/incomeRank" v-if="menuFlags.incomeRankMenu">收入排行榜</el-menu-item>
      </el-submenu>

    </el-submenu>
  </el-menu>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
  },

  /*初始化数据*/
  data() {
    return {
      role: 'user',
      /*菜单打开状态*/
      menuFlags: {
        userMenu: false,
        fileMenu: false,
        avatarMenu: false,
        goodMenu: false,
        carouselMenu: false,
        orderMenu: false,
        categoryMenu: false,
        incomeChartMenu: false,
        incomeRankMenu: false,
      }
    }
  },

  computed: {
    userGroup: function () {
      return this.menuFlags.userMenu
    },
    fileGroup: function () {
      return this.menuFlags.fileMenu || this.menuFlags.avatarMenu
    },
    GoodGroup: function () {
      return this.menuFlags.goodMenu || this.menuFlags.orderMenu || this.menuFlags.categoryMenu || this.menuFlags.carouselMenu
    },
    incomeGroup: function () {
      return this.menuFlags.incomeChartMenu || this.menuFlags.incomeRankMenu
    }
  },
  created() {
    /*获取用户角色*/
    request.post("http://localhost:8888/role").then(res => {
      if (res.code === '200') {
        this.role = res.data;
        /*只有管理员有权限*/
        if (this.role === 'admin') {
          this.menuFlags.userMenu = true
          this.menuFlags.fileMenu = true
          this.menuFlags.avatarMenu = true
          this.menuFlags.categoryMenu = true
          this.menuFlags.goodMenu = true
          this.menuFlags.carouselMenu = true
          this.menuFlags.orderMenu = true
          this.menuFlags.incomeChartMenu = true
          this.menuFlags.incomeRankMenu = true
        } else if (this.role === 'user') {

        }
      }
    })
  }
}
</script>


<template>
  <el-container class="layout-container">
    <el-header>
      <div class="header-left">
        <h1>日记系统</h1>
      </div>
      <div class="header-right">
        <span>{{ userStore.userInfo?.nickname }}</span>
        <el-button text @click="handleLogout">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu :default-active="route.path" router>
          <el-menu-item index="/diary">
            <el-icon><Edit /></el-icon>
            <span>今日日记</span>
          </el-menu-item>
          <el-menu-item index="/calendar">
            <el-icon><Calendar /></el-icon>
            <span>日历查看</span>
          </el-menu-item>
          <el-menu-item index="/category">
            <el-icon><Setting /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  try {
    await userStore.fetchUserInfo()
  } catch {
    userStore.logout()
    router.push('/login')
  }
})

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.el-aside {
  border-right: 1px solid #eee;
}
</style>

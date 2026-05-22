<template>
  <div class="calendar-page">
    <el-calendar v-model="currentDate">
      <template #date-cell="{ data }">
        <div class="calendar-cell" :class="{ 'has-diary': isDiaryDate(data.day) }"
          @click="handleDateClick(data.day)">
          {{ data.day.split('-')[2] }}
          <div v-if="isDiaryDate(data.day)" class="diary-dot"></div>
        </div>
      </template>
    </el-calendar>

    <el-dialog v-model="dialogVisible" :title="selectedDate + ' 日记'" width="60%">
      <div v-if="selectedDiary">
        <h3>清单</h3>
        <div v-for="category in selectedCategories" :key="category.id" class="category-group">
          <h4>{{ category.name }}</h4>
          <div v-for="item in getItemsByCategory(category.id)" :key="item.id" class="item-row">
            <el-checkbox :model-value="item.isDone === 1" disabled>
              <span :class="{ done: item.isDone === 1 }">{{ item.content }}</span>
            </el-checkbox>
          </div>
        </div>
        <el-empty v-if="selectedItems.length === 0" description="无清单" />

        <h3 style="margin-top: 20px;">日记</h3>
        <p>{{ selectedDiary.textContent || '无内容' }}</p>

        <h3 style="margin-top: 20px;">图片</h3>
        <div class="image-list">
          <el-image v-for="img in selectedImages" :key="img.id" :src="img.imageUrl"
            fit="cover" :preview-src-list="selectedImages.map(i => i.imageUrl)" />
        </div>
        <el-empty v-if="selectedImages.length === 0" description="无图片" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import dayjs from 'dayjs'
import { getCalendarDates, getCategories, getItems, getContent, getImages } from '../api/diary'

const currentDate = ref(new Date())
const diaryDates = ref([])
const dialogVisible = ref(false)
const selectedDate = ref('')
const selectedDiary = ref(null)
const selectedItems = ref([])
const selectedCategories = ref([])
const selectedImages = ref([])

function isDiaryDate(date) {
  return diaryDates.value.includes(date)
}

function getItemsByCategory(categoryId) {
  return selectedItems.value.filter(item => item.categoryId === categoryId)
}

async function loadCalendarDates() {
  const year = dayjs(currentDate.value).year()
  const month = dayjs(currentDate.value).month() + 1
  try {
    const res = await getCalendarDates(year, month)
    diaryDates.value = res.data
  } catch (e) {
    console.error('加载日历日期失败', e)
  }
}

async function handleDateClick(date) {
  selectedDate.value = date
  try {
    const [catRes, itemRes, contentRes, imgRes] = await Promise.allSettled([
      getCategories(),
      getItems(date),
      getContent(date),
      getImages(date)
    ])
    if (catRes.status === 'fulfilled') selectedCategories.value = catRes.value.data
    if (itemRes.status === 'fulfilled') selectedItems.value = itemRes.value.data
    if (contentRes.status === 'fulfilled') selectedDiary.value = contentRes.value.data
    if (imgRes.status === 'fulfilled') selectedImages.value = imgRes.value.data
    dialogVisible.value = true
  } catch (e) {
    console.error('加载日记详情失败', e)
  }
}

watch(currentDate, loadCalendarDates)
onMounted(loadCalendarDates)
</script>

<style scoped>
.calendar-page {
  padding: 20px;
}
.calendar-cell {
  height: 100%;
  cursor: pointer;
}
.has-diary {
  font-weight: bold;
}
.diary-dot {
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  margin: 2px auto 0;
}
.category-group {
  margin-bottom: 10px;
}
.category-group h4 {
  color: #666;
  margin-bottom: 5px;
}
.item-row {
  padding: 3px 0;
}
.done {
  text-decoration: line-through;
  color: #999;
}
.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.image-list .el-image {
  width: 100px;
  height: 100px;
}
</style>

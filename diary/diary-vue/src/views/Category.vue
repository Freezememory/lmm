<template>
  <div class="category-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="showAdd">新增分类</el-button>
        </div>
      </template>
      <el-table :data="categories" stripe>
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isPreset === 1 ? 'success' : 'info'">
              {{ row.isPreset === 1 ? '预设' : '自定义' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="row.isPreset !== 1" text type="primary" @click="showEdit(row)">
              编辑
            </el-button>
            <el-button v-if="row.isPreset !== 1" text type="danger" @click="handleDelete(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="1" :max="99" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../api/diary'

const categories = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = reactive({ name: '', sortOrder: 1 })

async function loadData() {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error('加载分类失败', e)
  }
}

function showAdd() {
  isEdit.value = false
  form.name = ''
  form.sortOrder = categories.value.length + 1
  dialogVisible.value = true
}

function showEdit(row) {
  isEdit.value = true
  editId.value = row.id
  form.name = row.name
  form.sortOrder = row.sortOrder
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!form.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  try {
    if (isEdit.value) {
      await updateCategory(editId.value, { name: form.name, sortOrder: form.sortOrder })
    } else {
      await createCategory({ name: form.name, sortOrder: form.sortOrder })
    }
    dialogVisible.value = false
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    loadData()
  } catch (e) {
    ElMessage.error('操作失败，请重试')
    console.error('提交分类失败', e)
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' })
    await deleteCategory(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') console.error('删除分类失败', e)
  }
}

onMounted(loadData)
</script>

<style scoped>
.category-page {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

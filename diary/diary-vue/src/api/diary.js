import request from '../utils/request'

export function getCategories() {
  return request.get('/diary/categories')
}

export function createCategory(data) {
  return request.post('/diary/categories', data)
}

export function updateCategory(id, data) {
  return request.put(`/diary/categories/${id}`, data)
}

export function deleteCategory(id) {
  return request.delete(`/diary/categories/${id}`)
}

export function getItems(date) {
  return request.get('/diary/items', { params: { date } })
}

export function createItem(data) {
  return request.post('/diary/items', data)
}

export function updateItem(id, data) {
  return request.put(`/diary/items/${id}`, data)
}

export function toggleItem(id) {
  return request.put(`/diary/items/${id}/toggle`)
}

export function deleteItem(id) {
  return request.delete(`/diary/items/${id}`)
}

export function getContent(date) {
  return request.get('/diary/content', { params: { date } })
}

export function saveContent(date, data) {
  return request.put('/diary/content', data, { params: { date } })
}

export function getImages(date) {
  return request.get('/diary/images', { params: { date } })
}

export function uploadImage(date, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(`/diary/images/upload?date=${date}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function deleteImage(id) {
  return request.delete(`/diary/images/${id}`)
}

export function getCalendarDates(year, month) {
  return request.get('/diary/calendar', { params: { year, month } })
}

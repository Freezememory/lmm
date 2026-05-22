import request from '../utils/request'

export function register(data) {
  return request.post('/user/register', data)
}

export function login(data) {
  return request.post('/user/login/account', data)
}

export function getUserInfo() {
  return request.get('/user/info')
}

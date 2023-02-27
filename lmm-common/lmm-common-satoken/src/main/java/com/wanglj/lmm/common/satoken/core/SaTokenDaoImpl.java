package com.wanglj.lmm.common.satoken.core;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.wanglj.lmm.common.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sa-Token持久层接口(使用框架自带RedisUtils实现 协议统一)
 *
 */
@Component
public class SaTokenDaoImpl implements SaTokenDao {

	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 获取Value，如无返空
	 */
	@Override
	public String get(String key) {
		return redisUtils.getCacheObject(key);
	}

	/**
	 * 写入Value，并设定存活时间 (单位: 秒)
	 */
	@Override
	public void set(String key, String value, long timeout) {
		if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		// 判断是否为永不过期
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			redisUtils.setCacheObject(key, value);
		}
		else {
			redisUtils.setCacheObject(key, value, Duration.ofSeconds(timeout));
		}
	}

	/**
	 * 修修改指定key-value键值对 (过期时间不变)
	 */
	@Override
	public void update(String key, String value) {
		long expire = getTimeout(key);
		// -2 = 无此键
		if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		this.set(key, value, expire);
	}

	/**
	 * 删除Value
	 */
	@Override
	public void delete(String key) {
		redisUtils.deleteObject(key);
	}

	/**
	 * 获取Value的剩余存活时间 (单位: 秒)
	 */
	@Override
	public long getTimeout(String key) {
		long timeout = redisUtils.getTimeToLive(key);
		return timeout < 0 ? timeout : timeout / 1000;
	}

	/**
	 * 修改Value的剩余存活时间 (单位: 秒)
	 */
	@Override
	public void updateTimeout(String key, long timeout) {
		// 判断是否想要设置为永久
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			long expire = getTimeout(key);
			if (expire == SaTokenDao.NEVER_EXPIRE) {
				// 如果其已经被设置为永久，则不作任何处理
			}
			else {
				// 如果尚未被设置为永久，那么再次set一次
				this.set(key, this.get(key), timeout);
			}
			return;
		}
		redisUtils.expire(key, Duration.ofSeconds(timeout));
	}

	/**
	 * 获取Object，如无返空
	 */
	@Override
	public Object getObject(String key) {
		return redisUtils.getCacheObject(key);
	}

	/**
	 * 写入Object，并设定存活时间 (单位: 秒)
	 */
	@Override
	public void setObject(String key, Object object, long timeout) {
		if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		// 判断是否为永不过期
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			redisUtils.setCacheObject(key, object);
		}
		else {
			redisUtils.setCacheObject(key, object, Duration.ofSeconds(timeout));
		}
	}

	/**
	 * 更新Object (过期时间不变)
	 */
	@Override
	public void updateObject(String key, Object object) {
		long expire = getObjectTimeout(key);
		// -2 = 无此键
		if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
			return;
		}
		this.setObject(key, object, expire);
	}

	/**
	 * 删除Object
	 */
	@Override
	public void deleteObject(String key) {
		redisUtils.deleteObject(key);
	}

	/**
	 * 获取Object的剩余存活时间 (单位: 秒)
	 */
	@Override
	public long getObjectTimeout(String key) {
		long timeout = redisUtils.getTimeToLive(key);
		return timeout < 0 ? timeout : timeout / 1000;
	}

	/**
	 * 修改Object的剩余存活时间 (单位: 秒)
	 */
	@Override
	public void updateObjectTimeout(String key, long timeout) {
		// 判断是否想要设置为永久
		if (timeout == SaTokenDao.NEVER_EXPIRE) {
			long expire = getObjectTimeout(key);
			if (expire == SaTokenDao.NEVER_EXPIRE) {
				// 如果其已经被设置为永久，则不作任何处理
			}
			else {
				// 如果尚未被设置为永久，那么再次set一次
				this.setObject(key, this.getObject(key), timeout);
			}
			return;
		}
		redisUtils.expire(key, Duration.ofSeconds(timeout));
	}

	/**
	 * 搜索数据
	 */
	@Override
	public List<String> searchData(String prefix, String keyword, int start, int size) {
		Collection<String> keys = redisUtils.keys(prefix + "*" + keyword + "*");
		List<String> list = new ArrayList<>(keys);
		return SaFoxUtil.searchList(list, start, size);
	}

}

package com.huoli.openapi.cache;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.huoli.openapi.util.Constant;

@Component
public class MemcachedClient {
//	private static final Logger logger = LoggerFactory
//			.getLogger(MemcachedClient.class);

	@Resource(name = "XMemcached")
	private Cached xmemcachedClient;

	/**
	 * 新增操作
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @param desc
	 *            这个操作的描述
	 * @return
	 */
	public boolean set(String key, Object value, Integer expireTime) {
		if (value == null) {
			return false;
		}

		xmemcachedClient.set(key, expireTime, value);
		return true;

	}

	public boolean set(String key, Object value) {
		return this.set(key, value, Constant.MC_EXPIRE_TIME_DEDAUTLT);
	}

	/**
	 * get 操作
	 * 
	 * @param key
	 * @param desc
	 *            这个操作的缓存的
	 * @return 一个任意类别的对象
	 */
	public Object get(final String key) {
		Object result;
		result = xmemcachedClient.get(key);
		return result;

	}

	/**
	 * 删除操作，删除一次失败，进行第二次删除操作
	 * 
	 * @param key
	 * @param desc
	 */
	public boolean delete(String key) {
		boolean isDelete = true;
		xmemcachedClient.delete(key);
		return isDelete;
	}

	/**
	 * 生成memcached的key
	 * 
	 * @param uid
	 * @param memcachedKeySuffix
	 * @return
	 */
	public String getMemcachedKey(Long id, String memcachedKeySuffix) {
		String key = String.valueOf(id);
		if (memcachedKeySuffix != null) {
			key = (new StringBuilder(key)).append(memcachedKeySuffix)
					.toString();
		}
		return key;
	}

	/**
	 * 生成memcached的key
	 * 
	 * @param uid
	 * @param memcachedKeySuffix
	 * @return
	 */
	public String getMemcachedKey(String hotelType, String hotelId, String date) {
		        return new StringBuilder().append(hotelType).append("|").append(hotelId).append("&").append(date).toString();
		    }
	
}

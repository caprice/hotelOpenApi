package com.huoli.openapi.cache;

import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.util.Constant;
import com.huoli.openapi.util.Tools;



@Component
public class XMemcached implements Cached{

	private static final Logger logger = LoggerFactory.getLogger(XMemcached.class);

	@Resource(name = "xmemcachedClient")
	private XMemcachedClient xmemcachedClient;

	/**
	 * 新增操作
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @param desc
	 *            这个操作的描述
	 * @return
	 * @throws java.util.concurrent.TimeoutException 
	 */

	@Override
	public void set(String key, int expireTime, Object value)  {
		if (value == null) {
			return ;
		}
		try {
			xmemcachedClient.set(key, expireTime, value);
			return ;
		} catch (TimeoutException e) {
			logger.error(Tools.exceptionMsg(e));
			 delete(key);
		} catch (InterruptedException e) {
			logger.error(Tools.exceptionMsg(e));
			 delete(key);
		} catch (MemcachedException e) {
			logger.error(Tools.exceptionMsg(e));
			 delete(key);
		}
	}

	public void set(String key, Object value) {
		 this.set(key, Constant.MC_EXPIRE_TIME_DEDAUTLT, value);
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
		try {
			result = xmemcachedClient.get(key);
			return result;
		} catch (TimeoutException e) {
			logger.error(Tools.exceptionMsg(e));
		} catch (InterruptedException e) {
			logger.error(Tools.exceptionMsg(e));
		} catch (MemcachedException e) {
			logger.error(Tools.exceptionMsg(e));
		}
		return null;
	}

	/**
	 * 删除操作，删除一次失败，进行第二次删除操作
	 * 
	 * @param key
	 * @param desc
	 */
	public boolean delete(String key) {

		boolean isDelete = true;
		try {
			xmemcachedClient.delete(key);
			return true;
		} catch (TimeoutException e) {
			logger.error(Tools.exceptionMsg(e));
			isDelete = false;
		} catch (InterruptedException e) {
			logger.error(Tools.exceptionMsg(e));
			isDelete = false;
		} catch (MemcachedException e) {
			logger.error(Tools.exceptionMsg(e));
			isDelete = false;
		} finally {
			try {
				if (!isDelete) {
					xmemcachedClient.deleteWithNoReply(key);
				}
				return true;
			} catch (InterruptedException e) {
				logger.error(Tools.exceptionMsg(e));
			} catch (MemcachedException e) {
				logger.error(Tools.exceptionMsg(e));
			}
		}
		return false;
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
			key = (new StringBuilder(key)).append(memcachedKeySuffix).toString();
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
	public String getMemcachedKey(String id, String memcachedKeySuffix) {
		String key = String.valueOf(id);
		if (memcachedKeySuffix != null) {
			key = (new StringBuilder(key)).append(memcachedKeySuffix).toString();
		}
		return key;
	}
	
	
	@Override
	public void deleteWithNoReply(String key) {
		this.delete(key);
		
	}

}

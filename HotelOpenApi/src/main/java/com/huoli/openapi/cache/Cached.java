package com.huoli.openapi.cache;

public interface Cached {
	public void set(String key, int expireTime, Object value);

	public Object get(String key);

	public boolean delete(String key);

	public void deleteWithNoReply(String key);

}

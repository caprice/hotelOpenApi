package com.huoli.openapi.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotelRequestLog {
	private static final Logger logger = LoggerFactory.getLogger(HotelRequestLog.class);

	/**
	 * 打印日志信息
	 * 
	 * @param str
	 * @param objs
	 */
	public static void logInfo(String str, Object[] objs) {
		logger.info(str, objs);
	}
}

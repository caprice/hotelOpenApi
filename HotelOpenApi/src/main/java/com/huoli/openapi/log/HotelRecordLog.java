package com.huoli.openapi.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: OrderRecordLog
* @Description: TODO(这里用一句话描述这个类的作用)
* @author dongj@huoli.com
* @date 2012-2-7 上午10:31:22
*
 */
public class HotelRecordLog {
	private static final Logger logger = LoggerFactory.getLogger(HotelRecordLog.class);
	public static final String LOGFLAG = "HLSYSMON";
	/**
	 * 打印日志信息
	 * 
	 * @param str
	 * @param objs
	 */
	public static void logInfo(String str, Object[] objs) {
		logger.info(str, objs);
	}
	
	public static void logInfo(String str){
		logger.info(str);
	}
}

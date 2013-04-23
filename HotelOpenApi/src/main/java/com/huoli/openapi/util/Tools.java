package com.huoli.openapi.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import com.huoli.openapi.exception.NotFindRequiredParamException;

public class Tools {
	private static ThreadLocal<Map<String, Object>> threadLocal;

	public static Object getRequiredParamByKey(String key, Map<String, Object> params) {
		Object result = params.get(key);
		if (result == null)
			throw new NotFindRequiredParamException("Required String param +" + key + "not find");
		return result;
	}

	public static Object getParamByKey(String key, Map<String, Object> params) {
		return params.get(key);
	}

	private static ThreadLocal<Map<String, Object>> getThreadLocalObject() {
		if (threadLocal == null) {
			threadLocal = new ThreadLocal<Map<String, Object>>();
		}
		return threadLocal;
	}
	 public static boolean checkNick(String nick) {
	        if (StringUtils.isBlank(nick)) {
	            return false;
	        }
	        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]{2,4}");
	        Matcher m = pattern.matcher(nick);
	        return m.matches();
	    }
	 
	 /**
		 * 校验手机电话号码
		 * 
		 * @param phoneNumber
		 * @return
		 */
		public static boolean phoneNumberCheck(String phoneNumber) {
			if (StringUtils.isBlank(phoneNumber)
					|| !StringUtils.isNumeric(phoneNumber)) {
				return false;
			}
			Pattern phonePattern = Pattern
					.compile("^1(([35][0-9])|(47)|[8][01236789])\\d{8}$");
			Matcher matcher1 = phonePattern.matcher(phoneNumber);
			return matcher1.find();

		}
	public static Object getThreadLocalValueByName(String key){
		if(getThreadLocalObject().get()==null)
			getThreadLocalObject().set(new HashMap<String,Object>());
		return getThreadLocalObject().get().get(key);
	}
	public static void setThreadLocalValue(String key,Object value){
		if(getThreadLocalObject().get()==null)
			getThreadLocalObject().set(new HashMap<String,Object>());
		getThreadLocalObject().get().put(key, value);
	}

	public static void removeHtreadLocalValue(){
		getThreadLocalObject().remove();
	}
	/**
	 * 输出详细错误信息
	 * 
	 * @param e
	 * @return
	 */
	public static String exceptionMsg(Exception e) {
		StringWriter output = new StringWriter();
		PrintWriter writer = new PrintWriter(output, true);
		try {
			e.printStackTrace(writer);
			return output.toString();
		} finally {
			writer.close();
		}
	}

	public static boolean isEmail(String str){
		return str.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		
	}
	/**
     * 取得4位随机校验码
     * 
     * @author Alvise
     * @return
     */
    public static String getVerifyCode() {
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 4);
        String fixLenthString = String.valueOf(pross);
        return fixLenthString.substring(1, 5);
    }
	/**
	 * 
	 * @return
	 */
	public static synchronized String makeOrderId() {
		return DateUtil.formatDate(new Date(), "yyMMddHHmmssSSS");
	}

	/**
	 * 转换json参数为map类型
	 * 
	 * @param json
	 * @return
	 */
	public static Map getParamsFromJson(String json) {
		Map<String, String> params = new HashMap();
		JSONObject jsonObject = JSONObject.fromObject(json);
		Iterator<String> items = jsonObject.keys();
		while (items.hasNext()) {
			String key = items.next();
			params.put(key, jsonObject.getString(key));
		}
		return params;
	}
	/**
	 * 
	 * @param date
	 */
	public static String[] getLastArriveTime(Date date){
		String[] result = new String[2];
		Date now = new Date();
		Date time14 = new Date(date.getYear(),date.getMonth(),date.getDate(),14,0);
		Date time18 = new Date(date.getYear(),date.getMonth(),date.getDate(),18,0);
		if(now.before(time14)){
			result[0]="14";
			result[1]="18";
		}else if(now.after(time14)&&now.before(time18)){
			result[0]=String.valueOf(now.getHours()+1);
			result[1]="18";
		}else if(now.after(time18)&&now.getHours()<23){
			result[0]=String.valueOf(now.getHours()+1);
			result[1]=String.valueOf(now.getHours()+1);

		}else{
			result[0]="14";
			result[1]="23";
		}
		return result;
		
		
	}

	public static void main(String[] args) {
//		System.out.println(OrderTools.makeOrderId());
//		String[] ss = getLastArriveTime(new Date(2012,7,24));
//		System.out.println(ss[0]);
//		System.out.println(ss[1]);
		boolean s = isEmail("l@sfssfs.d");
		System.out.println(s);
	}
}

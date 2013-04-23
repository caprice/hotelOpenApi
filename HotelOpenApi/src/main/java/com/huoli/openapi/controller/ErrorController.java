package com.huoli.openapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huoli.openapi.log.HotelRecordLog;
import com.huoli.openapi.util.Constant;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.result.BaseDataResult;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @ClassName: ErrorControoller
 * @Description:订单系统错误请求控制器
 * @author dongj@huoli.com
 * @date 2011-10-20 下午02:12:06
 * 
 */
@Controller
public class ErrorController {
	Logger logger = LoggerFactory.getLogger(ErrorController.class);

	/**
	 * 服务器内部错误返回客服端code为500
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/server/error500")
	public String serverError500Dispose(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			Exception e = (Exception) request.getAttribute("exception");
			if (e != null)
				logger.error("{}", Tools.exceptionMsg(e));
			BaseDataResult error = new BaseDataResult();
			error.setCode("33");
			String msg = request.getParameter("msg");
			if (msg == null) {
				msg = e.getMessage();
				if (msg != null && msg.indexOf(">") != -1) {
					String msgs[] = msg.split(">");
					msg = msgs[1];
				}
			}

			msg = msg == null ? "服务器端异常" : msg;
			if (msg != null && msg.length() > 30) {
				msg = "服务器端异常";
			}
			error.setMsg(msg);
			String result = JSONSerializer.toJSON(error).toString();
			request.setAttribute("msg", this.beanToXml(error));
			System.out.println(result);
			logger.info(result);
			HotelRecordLog.logInfo(
					"{}|{}|{}|{}",
					new Object[] { HotelRecordLog.LOGFLAG,
							Tools.getThreadLocalValueByName("channelId"),
							Tools.getThreadLocalValueByName("event"), 2 });
			if(request.getRequestURI().indexOf("wap")!=-1){
				return "exception";
			}
			return "message";
		} catch (Exception e) {
			logger.error("++++++++++++++++++++++++++++++");
			HotelRecordLog.logInfo("{}|{}|{}|{}", new Object[] {
					HotelRecordLog.LOGFLAG, "system", "errorController", 2 });
			return null;
		}
	}

	/**
	 * 没有权限返回300错误
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/server/error300")
	public String serverError300Dispose(HttpServletRequest request,
			HttpServletResponse response) {
		Exception e = (Exception) request.getAttribute("exception");
		if (e != null)
			logger.error("{}", Tools.exceptionMsg(e));
		BaseDataResult error = new BaseDataResult();
		error.setCode("99");
		error.setStatus(Constant.RESULT_STATUS_FORBIDDEN);
		String msg = request.getParameter("msg");
		if (StringUtils.isBlank(msg) && !StringUtils.isBlank(e.getMessage())) {
			msg = e.getMessage();
		}
		msg = msg == null ? "Without permission" : msg;
		if(msg.length()>30){
			msg = "Without permission";
		}
		error.setMsg(msg);
		request.setAttribute("msg", this.beanToXml(error));
		return "message";
	}

	/**
	 * 请求参数不正确，或是请求不存在返回400错误
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/server/error400", method = RequestMethod.GET)
	public String serverError400Dispose(HttpServletRequest request,
			HttpServletResponse response) {
		Exception e = (Exception) request.getAttribute("exception");
		if (e != null)
			logger.error("{}", Tools.exceptionMsg(e));
		BaseDataResult error = new BaseDataResult();
		error.setCode("44");
		String msg = request.getParameter("msg");
		msg = msg == null ? "请求不存在" : msg;
		error.setMsg(msg);
		request.setAttribute("msg", this.beanToXml(error));
		return "message";
	}

	public String beanToXml(Object object) {
		// 对象序列化
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		String xml = xstream.toXML(object);
		System.out.println(xml);
		return xml;
	}
}

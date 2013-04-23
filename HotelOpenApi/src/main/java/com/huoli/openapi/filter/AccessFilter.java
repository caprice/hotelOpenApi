package com.huoli.openapi.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.exception.ForbiddenException;
import com.huoli.openapi.exception.PrivilegeVerifyException;
import com.huoli.openapi.log.HotelRecordLog;
import com.huoli.openapi.log.HotelRequestLog;
import com.huoli.openapi.mapper.ChannelUserMapper;
import com.huoli.openapi.util.Constant;
import com.huoli.openapi.util.MD5;
import com.huoli.openapi.vo.data.ChannelUser;
import com.huoli.openapi.vo.data.RequestResult;

/**
 * 权限验证
 * 
 * @ClassName: AccessFilter
 * @Description:
 * @author dongj@huoli.com
 * @date 2012-2-19 下午04:41:04
 * 
 */
@Component
public class AccessFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(AccessFilter.class);
	@Resource
	private ChannelUserMapper channelUserMapper;
	@Resource
	private Constant constant;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp,
			FilterChain chain) throws IOException, ServletException {
		
		long start = System.currentTimeMillis();
		HttpServletRequest request = (HttpServletRequest) req;
		String channelId = request.getHeader("channelId");
		System.out.println(channelId);
		String timeStamp = request.getHeader("timeStamp");
		String businessType = request.getHeader("businessType");
		String lat = request.getHeader("lat");
		String lnt = request.getHeader("lnt");
		String platform = request.getHeader("platform");
		String imei = request.getHeader("imei");
		String ptype = request.getHeader("ptype");
		String signature = request.getHeader("signature");

		if (!this.ipChecking(request, rsp)) {
			throw new PrivilegeVerifyException("without Privilege!");
		}
		if(request.getRequestURI().indexOf("server")!=-1){
			chain.doFilter(req, rsp);
			return;
		}
		if (channelId == null || channelId.isEmpty()) {
			String channelIdParam = request.getParameter("channelid");
			if (channelIdParam == null || channelIdParam.isEmpty())
				channelIdParam = request.getParameter("channelId");
			channelId = channelIdParam;
		}
		if (channelId != null) {
			ChannelUser user = channelUserMapper.selectChannelUserInfo(Long.valueOf(channelId));
			if(user==null){
				throw new ForbiddenException("channel invalid!");
			}
			request.setAttribute("channelId", Long.valueOf(channelId));
			request.getSession().setAttribute("channelId",
					Long.valueOf(channelId));
		}
		StringBuilder url = new StringBuilder(request.getRequestURI());
		if (request.getQueryString() != null
				&& !request.getQueryString().equals("")) {
			url.append("?").append(request.getQueryString());
		}
		String method = request.getMethod().toLowerCase();
		if (method.equals("post")) {
			url.append("{");
			Enumeration<String> keys = request.getParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				url.append(key).append("=").append(request.getParameter(key))
						.append("&");
			}
			url.append("}");
		}

		RequestResult rr = new RequestResult();
		rr.setBusinessType(businessType);
		rr.setChannelId(channelId);
		rr.setImei(imei);
		rr.setLat(lat);
		rr.setLnt(lnt);
		rr.setMethod(method);
		rr.setPlatform(platform);
		rr.setPtype(ptype);
		rr.setSignature(signature);
		rr.setTimeStamp(timeStamp);
		rr.setUrl(url.toString());
		HotelRecordLog.logInfo("request is:" + rr.toString());

		if (!StringUtils.isBlank(lat)) {
			request.setAttribute("lat", lat);
		}
		if (!StringUtils.isBlank(lnt)) {
			request.setAttribute("lnt", lnt);
		}
		if (!StringUtils.isBlank(imei)) {
			request.setAttribute("imei", imei);
		}

		if (request.getRequestURI().indexOf("wap") != -1
				|| request.getRequestURI().indexOf("images") != -1) {
//			if (request.getSession().getAttribute("channelId") == null) {
//				// throw new PrivilegeVerifyException("authentication failed!");
//				request.getSession().setAttribute("channelId", 100011205l);
//			}
		} else {
			if (!StringUtils.isBlank(channelId)) {
				request.setAttribute("channelId", Long.valueOf(channelId));
			} else {
				throw new ForbiddenException("channelId is not null!");
			}
			if (!this.checking(Long.valueOf(channelId), timeStamp,
					businessType, signature)) {
				throw new PrivilegeVerifyException("authentication failed!");

			}
		}

		chain.doFilter(req, rsp);
		HotelRequestLog.logInfo("{}`{}",
				new Object[] { url.toString(),
						System.currentTimeMillis() - start });
		HotelRecordLog.logInfo("[{}]return message is:[{}]", new Object[] {
				timeStamp, request.getAttribute("msg") });
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	private boolean checking(Long channelId, String timeStamp,
			String businessType, String signature) {
		ChannelUser user = channelUserMapper.selectChannelUserInfo(channelId);
		if (user == null) {
			throw new ForbiddenException("channel invalid!");
		}
		StringBuffer source = new StringBuffer();
		source.append(timeStamp).append(channelId)
				.append(MD5.md5(user.getToken())).append(businessType);
		String signatureFlag = MD5.md5(source.toString());
		if (signatureFlag.equals(signature)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean ipChecking(ServletRequest req, ServletResponse rsp) {
		String ip = this.getIpAddr((HttpServletRequest) req);
		if (constant.getClientWhiteList().indexOf(ip) != -1
				|| constant.getClientWhiteList().indexOf("test") != -1) {
			return true;
		}
		logger.info("without Privilege:ip=[{}]", ip);
		return false;

	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		logger.info("ip:{}",ip);
		String[] ips = ip.split("\\.");
		if (ips != null && ips.length >= 4) {
			 ip= ips[0] + "." + ips[1] + "." + ips[2];
		}
		return ip;
	}

	public static void main(String[] args) {
		String ip = "12.12.12.12";
		// System.out.println(ips.substring(0, -2));
		// System.out.println(ips.substring(-2));

		String[] ips = ip.split("\\.");
		if (ips != null && ips.length >= 4) {
			String ipt = ips[0] + "." + ips[1] + "." + ips[2];
			System.out.println(ipt);
		}

	}
}

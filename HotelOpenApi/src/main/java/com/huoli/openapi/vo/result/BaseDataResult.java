package com.huoli.openapi.vo.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 
 * @项目名称：HotelOpenApi
 * @类名称：BaseDataResult
 * @类描述：返回客户端信息基类
 * @创建人：dongjun
 * @创建时间：2012-10-31 下午4:30:12
 * @修改人：dongjun
 * @修改时间：2012-10-31 下午4:30:12
 * @修改备注：
 * @version v1.0
 * 
 */
@XStreamAlias("result")
public class BaseDataResult {
	private String referenceID;
	private String status;
	private String code;
	private String msg;
	private long timestamp = System.currentTimeMillis();

	public String getReferenceID() {
		return referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getTimestamp() {
		return timestamp;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

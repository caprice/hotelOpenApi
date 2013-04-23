package com.huoli.openapi.vo.data;

import java.io.Serializable;
import java.util.Date;

public class HotelChainChannelMapping implements Serializable {

	private static final long serialVersionUID = 3574109424261790207L;
	private Long id;
	private String hotelType;
	private String chainName;
	private String channelId;
	private String channelName;
	
	private int prior;
	private int mapprior;
	private int fullflag;
	private int status;
	private String valid;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public int getPrior() {
		return prior;
	}

	public void setPrior(int prior) {
		this.prior = prior;
	}

	public int getMapprior() {
		return mapprior;
	}

	public void setMapprior(int mapprior) {
		this.mapprior = mapprior;
	}

	public int getFullflag() {
		return fullflag;
	}

	public void setFullflag(int fullflag) {
		this.fullflag = fullflag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}

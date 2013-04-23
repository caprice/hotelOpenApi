package com.huoli.openapi.vo.data;

public class HotelChainInfo {
	private Long id;
	private String hotelType;
	private String chainName;
	private String chainLogo;
	private String officialPhone;
	private int status;
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
	public String getChainLogo() {
		return chainLogo;
	}
	public void setChainLogo(String chainLogo) {
		this.chainLogo = chainLogo;
	}
	public String getOfficialPhone() {
		return officialPhone;
	}
	public void setOfficialPhone(String officialPhone) {
		this.officialPhone = officialPhone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}

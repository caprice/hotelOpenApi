package com.huoli.openapi.roomStatus;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
@XStreamAlias("hotel")
public class RoomSimpleStatus implements Serializable {
    private static final long serialVersionUID = -7643636205336860336L;
    private String hotelId;
    private String hotelType;
    @XStreamAlias("minPrice")
    private Integer price;
    private int status;
    private Double distance=0d;
    @XStreamOmitField
    private String dateTime;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public double getDistance() {
		return distance;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	

	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

   

}

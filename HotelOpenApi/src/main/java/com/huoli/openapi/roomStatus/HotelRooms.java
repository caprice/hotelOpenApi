package com.huoli.openapi.roomStatus;

import java.io.Serializable;
import java.util.List;

public class HotelRooms implements Serializable {
    private static final long serialVersionUID = 2587535694752892600L;
    private String hotelType;
    private String hotelId;
    private List<RoomStatus> rooms;

    public HotelRooms() {

    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public List<RoomStatus> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomStatus> rooms) {
        this.rooms = rooms;
    }

}

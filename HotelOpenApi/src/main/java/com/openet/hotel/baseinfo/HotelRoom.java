package com.openet.hotel.baseinfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.huoli.openapi.util.DateUtil;


/**
 * 房间信息
 * 
 * @author alvise
 * 
 */
public class HotelRoom implements Serializable {

    private static final long serialVersionUID = 3624954808842980513L;
    private String date;
    private String typeid;
    private String type;
    private int marketPrice = -1;
    private int memberPrice = -1;
    private int vouchSet = 0;// 是否需要担保 0 不需要 1需要
    private int isRecord = 0;// 是否固化到数据库中的数据 0 不是 1 是
    private String dateTime = DateUtil.getCurrentDate();
    private Map<String, String> extra;

    /**
     * 是否有房，0表没房,1表有房，2表未知
     */
    private int status;

    public HotelRoom() {
        setExtra(new HashMap<String, String>());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(int memberPrice) {
        this.memberPrice = memberPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void addExtra(String key, String value) {
        if (getExtra() == null) {
            setExtra(new HashMap<String, String>());
        }
        getExtra().put(key, value);
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public int getVouchSet() {
        return vouchSet;
    }

    public void setVouchSet(int vouchSet) {
        this.vouchSet = vouchSet;
    }

    public int getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(int isRecord) {
        this.isRecord = isRecord;
    }

}

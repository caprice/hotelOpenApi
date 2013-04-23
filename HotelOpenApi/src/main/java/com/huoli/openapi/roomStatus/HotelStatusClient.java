package com.huoli.openapi.roomStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.exception.HotelStatusException;
import com.huoli.openapi.help.PropertyConfig;

//@Component
public class HotelStatusClient {
	@PropertyConfig("hotel.roomstatus.service")
    private String host;
	@PropertyConfig("hotel.roomstatus.service.port")
    private int port;
	@PropertyConfig("hotel.roomstatus.service.timeout")
    private int timeout;

    private String searchHotelByDay = "/Hotel_RoomSearch/searchHotelByDay";
    private String searchHotelByDays = "/Hotel_RoomSearch/searchHotelByDays";
    private String searchHotelBySize = "/Hotel_RoomSearch/searchHotelBySize";
    private String searchHotelByDate = "/Hotel_RoomSearch/searchHotelByDate";
    private String searchRoomPriceByDays = "/Hotel_RoomSearch/searchRoomPriceByDays";
    private String searchHotelByCache = "/Hotel_RoomSearch/searchHotelByCache";

    private static final Logger logger = LoggerFactory.getLogger(HotelStatusClient.class);

    public HotelStatusClient(String host, int port, int timeout) {
        this.port = port;
        this.host = host;
        this.timeout = timeout;
    }

    public HotelStatusClient() {
    }

    public RoomStatusResult searchHotelByDay(String hotelCate, String hotelId, String date, boolean noCache)
            throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("hotelCate", hotelCate);
        paramMap.put("hotelId", hotelId);
        paramMap.put("date", date);
        paramMap.put("noCache", noCache ? "1" : "0");
        String rsp = this.executePostMethod(searchHotelByDay, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("hotelCate=%s hotelId=%s,  date=%s return = |%s|", hotelCate, hotelId, date, rsp));
        }
        return RoomStatusResult.formJsonStr(rsp);
    }

    public RoomStatusResult searchHotelByDays(String uid, String sesid, String hotelCate, String hotelId,
            String beginDate, String endDate, boolean noCache, String timeOut) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("hotelCate", hotelCate);
        paramMap.put("hotelId", hotelId);
        paramMap.put("beginDate", beginDate);
        paramMap.put("endDate", endDate);
        paramMap.put("noCache", noCache ? "1" : "0");
        paramMap.put("timeOut", timeOut);
        paramMap.put("onlyOfficial", "1");
        String rsp = this.executePostMethod(searchHotelByDays, paramMap);
        System.out.println(rsp);
        if (logger.isInfoEnabled()) {
            logger.info(String
                    .format("ROOM_STATUS_LOG|searchHotelByDays| uid=%s, sesid=%s, hotelCate=%s,hotelId=%s,  date=%s-%s, return = |%s|",
                            uid, sesid, hotelCate, hotelId, beginDate, endDate, rsp));
        }
        return RoomStatusResult.formJsonStr(rsp);
    }

    public RoomStatusResult searchHotelBySize(String query, String date, int resize) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("query", query);
        paramMap.put("date", date);
        paramMap.put("resize", String.valueOf(resize));
        String rsp = this.executePostMethod(searchHotelBySize, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("searchHotelBySize: query=%s ,hotelId=%s,  date=%s, return =|%s| ", query, date,
                    rsp));
        }
        return RoomStatusResult.formJsonStr(rsp);
    }

    public RoomStatusResult searchHotelByDate(String exHotelIds, String date) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("query", exHotelIds);
        paramMap.put("date", date);
        String rsp = this.executePostMethod(searchHotelByDate, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info(String
                    .format("searchHotelByDate: exHotelIds=%s,  date=%s return = |%s|", exHotelIds, date, rsp));
        }
        return RoomStatusResult.formJsonStr(rsp);
    }

    private String executePostMethod(String url, Map<String, String> paramMap) {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(host, port, "http");
        client.getParams().setIntParameter("http.socket.timeout", timeout);
        BufferedReader reader = null;
        PostMethod method = null;
        try {
            method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
            client.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
            for (String key : paramMap.keySet()) {
                NameValuePair pair = new NameValuePair(key, paramMap.get(key));
                paramList.add(pair);
            }
            method.setRequestBody(paramList.toArray(new NameValuePair[paramList.size()]));
            client.executeMethod(method);
            String response = new String(method.getResponseBodyAsString());
            response = new String(response.getBytes("utf-8"), "utf-8");
            return response;
        } catch (UnsupportedEncodingException e) {
            throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
                    .append(" response translate fail ").append(e.toString()).toString());
        } catch (HttpException e) {
            throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
                    .append(" HttpException ").append(e.toString()).toString());
        } catch (IOException e) {
            throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
                    .append(" IOException ").append(e.toString()).toString());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

            } catch (Exception e) {
                logger.error(" close is error", e);
            }
            if (null != method) {
                method.releaseConnection();
            }
        }
    }

    public RoomStatusResult searchRoomPriceByDays(String hotelCate, String hotelId, String beginDate, String endDate,
            String userRank, String roomType) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("hotelCate", hotelCate);
        paramMap.put("hotelId", hotelId);
        paramMap.put("beginDate", beginDate);
        paramMap.put("endDate", endDate);
        paramMap.put("userRank", userRank);
        paramMap.put("roomType", roomType);
        paramMap.put("noCache", "1");
        String rsp = this.executePostMethod(searchRoomPriceByDays, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info(String
                    .format("searchRoomPriceByDays :hotelCate=%s,hotelId=%s,  date=%s-%s, userRank = %s, roomType = %s,return = |%s|",
                            hotelCate, hotelId, beginDate, endDate, userRank, roomType, rsp));
        }
        return RoomStatusResult.formJsonStr(rsp);
    }

    public RoomSimpleStatusResult searchHotelByCache(String query, String date) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("query", query);
        paramMap.put("date", date);
        String rsp = this.executePostMethod(searchHotelByCache, paramMap);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("searchHotelByCache :query=%s, date=%s,return = |%s|", query, date, rsp));
        }
        return RoomSimpleStatusResult.formJsonStr(rsp);
    }

    public static void main(String[] args) throws Exception {
        // HotelStatusClient client = new HotelStatusClient("127.0.0.1", 80,
        // 18000);
        // RoomStatusResult res = client.searchHotelByDays(null, null, "008",
        // "170438", "2012-08-10", "2012-08-1", false,
        // "4000");
        //
        // // RoomSimpleStatusResult res1 =
        // // client.searchHotelByCache("0076281","2012-06-19");
        // System.out.println(res);

    }
}

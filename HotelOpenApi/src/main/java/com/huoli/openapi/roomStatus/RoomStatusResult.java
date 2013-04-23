package com.huoli.openapi.roomStatus;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class RoomStatusResult implements Serializable {
    private static final long serialVersionUID = -6861880789622141016L;
    private String result;
    private String msg;
    private List<HotelRooms> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<HotelRooms> getData() {
        return data;
    }

    public void setData(List<HotelRooms> data) {
        this.data = data;
    }

    public static RoomStatusResult formJsonStr(String jsonStr) throws JsonParseException, JsonMappingException,
            IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        RoomStatusResult data;
        data = objectMapper.readValue(jsonStr, RoomStatusResult.class);
        return data;

    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        RoomStatusResult result = RoomStatusResult.formJsonStr("{\"result\":\"1\",\"msg\":\"查询成功\"}");
        System.out.println(result.getMsg());
    }

}

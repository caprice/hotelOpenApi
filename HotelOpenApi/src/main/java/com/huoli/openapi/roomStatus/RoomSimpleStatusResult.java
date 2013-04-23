package com.huoli.openapi.roomStatus;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class RoomSimpleStatusResult implements Serializable {
    private static final long serialVersionUID = -6861880789622141016L;
    private String result;
    private String msg;
    private List<RoomSimpleStatus> data;

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

    public static RoomSimpleStatusResult formJsonStr(String jsonStr) throws JsonParseException, JsonMappingException,
            IOException {
        // JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        // RoomSimpleStatusResult bean = (RoomSimpleStatusResult)
        // JSONObject.toBean(jsonObject,
        // RoomSimpleStatusResult.class);
        // MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        // morpherRegistry.registerMorpher(new
        // BeanMorpher(RoomSimpleStatus.class, morpherRegistry));
        // List<RoomSimpleStatus> output = new ArrayList<RoomSimpleStatus>();
        // if (bean.getData() != null) {
        // for (Iterator<RoomSimpleStatus> i = bean.getData().iterator();
        // i.hasNext();) {
        // RoomSimpleStatus hotel = (RoomSimpleStatus)
        // morpherRegistry.morph(RoomSimpleStatus.class, i.next());
        // output.add(hotel);
        // }
        // }
        // bean.setData(output);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        RoomSimpleStatusResult bean = objectMapper.readValue(jsonStr, RoomSimpleStatusResult.class);
        return bean;
    }

    public static void main(String[] args) {
        String source = "{\"result\":\"1\",\"data\":[{\"price\":null,\"dateTime\":\"2012-06-19 16:11:59:440\",\"status\":1,\"hotelType\":\"007\",\"hotelId\":\"6281\"},{\"price\":284,\"dateTime\":\"2012-06-19 16:04:48:378\",\"status\":1,\"hotelType\":\"007\",\"hotelId\":\"7102\"},{\"dateTime\":\"2012-06-19 16:13:32:066\",\"status\":2,\"hotelType\":\"007\",\"hotelId\":\"8009\"}],\"msg\":\"查询成功\"}";
        // RoomSimpleStatusResult result = RoomSimpleStatusResult.formJsonStr();
        // RoomSimpleStatusResult result = new RoomSimpleStatusResult();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        RoomSimpleStatusResult result;
        try {
            result = objectMapper.readValue(source, RoomSimpleStatusResult.class);
            System.out.println(result.getMsg());
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public List<RoomSimpleStatus> getData() {
        return data;
    }

    public void setData(List<RoomSimpleStatus> data) {
        this.data = data;
    }

}

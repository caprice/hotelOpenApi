package com.huoli.openapi.order.extra;

import java.util.Map;

/**
 * 生成订单时候的附加信息
 * 
 * @author Alvise
 * 
 */
public interface OrderExtraInfo {
    Map<String, String> getCreateOrderExtraInfo( String  hotelType, String hotelId, Map<String,String> params,
            String roomId,String beginDate,String endDate);

    String hotelTypeReplace(String hotelType);
}

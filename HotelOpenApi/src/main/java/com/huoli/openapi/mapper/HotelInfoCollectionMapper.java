package com.huoli.openapi.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.GpsQuery;
import com.huoli.openapi.vo.data.HotelBaseInfo;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.HotelInfoCollectionSimple;
@Component
public interface HotelInfoCollectionMapper {
    HotelInfoCollection selectByHotelId(HotelInfoCollection query);
    List<HotelInfoCollection> selectByHotelType(List<String> query);
    List<HotelInfoCollection> queryNearByGps(GpsQuery query);
    String selectChannelLogo(String hotelType);
    List<HotelInfoCollectionSimple> querySimpleNearByOfBigConfines(GpsQuery query);

    List<HotelInfoCollectionSimple> querySimpleNearByOfBigConfinesBaidu(GpsQuery query);
    
    HotelInfoCollection selectById(Long id);
    
    List<HotelBaseInfo> selectHotelsBaseInfo(List<String> query);
    List<HotelBaseInfo> selectAllHotelBaseInfo();
    
    List<HotelInfoCollection> selectOfficialOrderHotels(String channelId);
}
package com.huoli.openapi.dao;

import java.util.List;

import com.huoli.openapi.vo.data.GpsQuery;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.HotelInfoCollectionSimple;

public interface HotelInfoCollectionDao {
	HotelInfoCollection selectByHotelId(HotelInfoCollection query);

    List<HotelInfoCollection> queryNearByGps(GpsQuery query);

    List<HotelInfoCollectionSimple> querySimpleNearByOfBigConfines(GpsQuery query);

    List<HotelInfoCollectionSimple> querySimpleNearByOfBigConfinesBaidu(GpsQuery query);
    
}

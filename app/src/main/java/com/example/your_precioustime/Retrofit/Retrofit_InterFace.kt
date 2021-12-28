package com.example.your_precioustime.Retrofit

import com.example.your_precioustime.Model.*
import com.example.your_precioustime.Model.OdsayModel.OdasyModel
import com.example.your_precioustime.Model.PoiModel.MapPoiModel
import com.example.your_precioustime.Model.SubwayModel.ErrorMessage
import com.example.your_precioustime.Model.SubwayModel.SubwayModel


import com.example.your_precioustime.Url
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Header

interface Retrofit_InterFace {

    @GET(Url.BUS_GET_URL)
    fun BusGet(
//        @Header("serviceKey") serviceKey:String,
        @Query("cityCode") cityCode:String,
        @Query("nodeId") nodeId:String

    ): Call<Bus>


    @GET(Url.BUS_NAME_SEARCH)
    suspend fun StationNameGet(
        @Query("cityCode") cityCode:String,
        @Query("nodeNm") staionName:String?,
//        @Query("nodeNo") nodeNo: Int?
    ) : Response<StationBus>


    @GET(Url.BUS_CITY_URL)
    fun CityGet(
//        @Query("serviceKey") serviceKey:String
    ) :Call<City>

    @GET(Url.TMAP_LOCATION_URL)
    fun MapLocationGet(
        @Query("version") version:Int=1,
        @Query("appKey") appKey:String= Url.TMAP_API_KEY,
        @Query("searchKeyword") searchKeyword:String
    ): Call<MapPoiModel>

    @GET(Url.ODSAY_POI_URL)
    fun ODSAYMapLocationGet(
//        @Query("apiKey") apiKey:String ="ZkqhFIasLHBpaDaOdYx5CYwRwh8r2UFgYg7NazJgWXw",
        @Query("lang") lang:Int = 0,
        @Query("x") xm:Double?,
        @Query("y") ym:Double?,
//        @Query("stationClass") stationClass:Int = 2
    ):Call<JsonElement>


    @GET(Url.SUBWAY_PATH_URL)
    fun SUBWAYGET(
        @Path("KEY") KEY :String = "6749736c6b6b696d38365266596579",
        @Path("TYPE") TYPE:String = "json",
        @Path("SERVICE") SERVICE:String="realtimeStationArrival",
        @Path("START_INDEX") START_INDEX:Int = 0,
        @Path("END_INDEX") END_INDEX:Int = 5,
        @Path("statnNm") statnNm:String
    ):Call<SubwayModel>


    //    trainLineNm : 도착지방면
//
//    statnNm : 역이름
//
//    barvlDt : 남은시간(초)
//
//bstatnNm : 종착역
//
//arvlMsg2 : 첫번째 도착 메세지
//
//arvlMsg3 : 두번째 도착메세지
//
//arvlCd : 도착코드
//(0:진입, 1:도착, 2:출발, 3:전역출발, 4:전역진입, 5:전역도착, 99:운행중)






}


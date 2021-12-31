package com.example.your_precioustime.Retrofit

import com.example.your_precioustime.Model.Bus
import com.example.your_precioustime.Model.StationBus
import com.example.your_precioustime.Model.StationItem
import com.example.your_precioustime.Url
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Coroutines_InterFace {

    @GET(Url.BUS_NAME_SEARCH)
    suspend fun Coroutines_BUS_NAMEGET(
        @Query("cityCode") cityCode:String,
        @Query("nodeNm") staionName:String?,
//        @Query("nodeNo") nodeNo: Int?
    ) : Response<StationBus>


    @GET(Url.BUS_GET_URL)
    suspend fun BusGet(
//        @Header("serviceKey") serviceKey:String,
        @Query("cityCode") cityCode:String,
        @Query("nodeId") nodeId:String

    ): Response<Bus>



}
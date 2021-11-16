package com.example.babuland.restApi;



import com.example.babuland.response.lists.ListResponse;
import com.example.babuland.response.lists.singlePlaces.SinglePlaceResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("find")
    Call<ListResponse>getListResponse(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("weather")
    Call<SinglePlaceResponse>getSinglePlaceByLatLng(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey

    );


}

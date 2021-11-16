package com.example.babuland.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.babuland.response.lists.ListResponse;
import com.example.babuland.response.lists.singlePlaces.SinglePlaceResponse;
import com.example.babuland.restApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private ApiInterface apiInterface;

    public Repository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public MutableLiveData<ListResponse>getListResponse(double lat,double lon,String apiKey){

        MutableLiveData<ListResponse>listResponseMutableLiveData=new MutableLiveData<>();
        apiInterface.getListResponse(lat, lon,"metric", apiKey).enqueue(new Callback<ListResponse>() {
            @Override
            public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                if (response.isSuccessful()){
                    listResponseMutableLiveData.postValue(response.body());
                    Log.e("TAG", "onFailure: "+lat );
                    Log.e("TAG", "onFailure: "+lon );
                }
            }

            @Override
            public void onFailure(Call<ListResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getLocalizedMessage() );
            }
        });

        return listResponseMutableLiveData;
    }

    public MutableLiveData<SinglePlaceResponse>getSinglePlaceResponse(double lat,double lon,String apiKey){
        MutableLiveData<SinglePlaceResponse>singlePlaceResponseMutableLiveData=new MutableLiveData<>();

        apiInterface.getSinglePlaceByLatLng(lat,lon,"metric",apiKey).enqueue(new Callback<SinglePlaceResponse>() {
            @Override
            public void onResponse(Call<SinglePlaceResponse> call, Response<SinglePlaceResponse> response) {

                if (response.isSuccessful()){
                    singlePlaceResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SinglePlaceResponse> call, Throwable t) {

                Log.e("TAG", "onFailure:  SinglePlaceResponse  ::"+t.getLocalizedMessage() );
            }
        });

        return singlePlaceResponseMutableLiveData;
    }

}

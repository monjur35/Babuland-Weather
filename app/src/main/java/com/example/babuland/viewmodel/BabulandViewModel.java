package com.example.babuland.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.babuland.repo.Repository;
import com.example.babuland.response.lists.ListResponse;
import com.example.babuland.response.lists.singlePlaces.SinglePlaceResponse;
import com.example.babuland.restApi.APIClient;
import com.example.babuland.restApi.ApiInterface;

public class BabulandViewModel extends AndroidViewModel {
    private Repository repository;
    private ApiInterface apiInterface;
    public BabulandViewModel(@NonNull Application application) {
        super(application);

        apiInterface=APIClient.getClient().create(ApiInterface.class);
        repository=new Repository(apiInterface);

    }

    public MutableLiveData<ListResponse>getListResponse(double lat,double lon,String apiKey){
        return repository.getListResponse(lat, lon, apiKey);
    }

    public MutableLiveData<SinglePlaceResponse>getSinglePlace(double lat,double lon,String apiKey){
        return repository.getSinglePlaceResponse(lat, lon, apiKey);
    }
}

package com.example.babuland.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.babuland.R;
import com.example.babuland.response.lists.singlePlaces.SinglePlaceResponse;
import com.example.babuland.viewmodel.BabulandViewModel;
import com.example.babuland.viewmodel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.intellij.lang.annotations.JdkConstants;

public class MapsFragment extends Fragment {

    double lat,ln;
    private LocationViewModel locationViewModel;
    private BabulandViewModel babulandViewModel;
    private RelativeLayout bottomSheet;
    private TextView cityName,status,windSpeed,humidity,maxTemp,minTemp,temp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationViewModel=new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        babulandViewModel=new ViewModelProvider(requireActivity()).get(BabulandViewModel.class);
        Bundle bundle=getArguments();
        lat=bundle.getDouble("lat");
        ln=bundle.getDouble("lon");

        Log.e("TAG", "onCreate: "+lat );
        Log.e("TAG", "onCreate: "+ln );

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng latLng = new LatLng(lat, ln);
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f));

            locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    LatLng userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    CircleOptions circleOptions = new CircleOptions()
                            .center(userLocation)
                            .radius(1000)
                            .strokeWidth(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.parseColor("#500084d3"));

                    googleMap.setMyLocationEnabled(true);
                    googleMap.addCircle(circleOptions);
                }
            });



        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheet=view.findViewById(R.id.bottomSheetId);
        cityName=bottomSheet.findViewById(R.id.cityName);
        status=bottomSheet.findViewById(R.id.statusInBottomSheet);
        windSpeed=bottomSheet.findViewById(R.id.windSpeed);
        maxTemp=bottomSheet.findViewById(R.id.maxTemp);
        minTemp=bottomSheet.findViewById(R.id.minTemp);
        temp=bottomSheet.findViewById(R.id.tempInBottomSheet);
        humidity=bottomSheet.findViewById(R.id.humidity);



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }




        new Thread(new Runnable() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        babulandViewModel.getSinglePlace(lat,ln,getString(R.string.weather_api)).observe(getViewLifecycleOwner(), new Observer<SinglePlaceResponse>() {
                            @Override
                            public void onChanged(SinglePlaceResponse singlePlaceResponse) {
                                if (singlePlaceResponse!=null){
                                    cityName.setText(singlePlaceResponse.getName());
                                    status.setText(singlePlaceResponse.getWeather().get(0).getMain());
                                    humidity.setText("Humidity : "+singlePlaceResponse.getMain().getHumidity());
                                    windSpeed.setText("Wind Speed : "+singlePlaceResponse.getWind().getSpeed().toString());
                                    maxTemp.setText("Max. Temp : "+singlePlaceResponse.getMain().getTempMax().toString()+"\u00B0"+" C");
                                    minTemp.setText("Min. Temp : "+singlePlaceResponse.getMain().getTempMin().toString()+"\u00B0"+" C");
                                    temp.setText(singlePlaceResponse.getMain().getTemp().toString()+"\u00B0"+" C");
                                }
                            }
                        });

                    }
                });
            }
        }).start();
    }
}
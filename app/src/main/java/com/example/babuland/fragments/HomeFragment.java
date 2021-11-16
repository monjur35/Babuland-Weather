package com.example.babuland.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.babuland.R;
import com.example.babuland.adapter.ListAdapter;
import com.example.babuland.databinding.FragmentHomeBinding;
import com.example.babuland.response.lists.ListResponse;
import com.example.babuland.response.lists.Listss;
import com.example.babuland.viewmodel.BabulandViewModel;
import com.example.babuland.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BabulandViewModel babulandViewModel;
    private LocationViewModel locationViewModel;
    private double lat =23.68;
    private double lon=90.35;
    private String weatherApiKey="06c4f7ba24ae64d790a873332d2fe658";
    private ListAdapter adapter;
    private List<Listss>locationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        babulandViewModel=new ViewModelProvider(requireActivity()).get(BabulandViewModel.class);
        locationViewModel=new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationList=new ArrayList<>();
        adapter=new ListAdapter(requireContext(),locationList);
        return binding.getRoot();

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.spinKit.setVisibility(View.VISIBLE);

        locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                lat=location.getLatitude();
                lon=location.getLongitude();
                locationList.clear();
                callForLocationList();
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireContext());
        binding.locationRV.setLayoutManager(linearLayoutManager);
        binding.locationRV.setAdapter(adapter);







    }

    private void callForLocationList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {


                        babulandViewModel.getListResponse(lat,lon,weatherApiKey).observe(getViewLifecycleOwner(), new Observer<ListResponse>() {
                            @Override
                            public void onChanged(ListResponse listResponse) {
                                if (listResponse!=null){

                                    locationList.addAll(listResponse.getList());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(requireContext(), listResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    binding.spinKit.setVisibility(View.GONE);

                                }
                                else {

                                    Toast.makeText(requireContext(), listResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    binding.spinKit.setVisibility(View.GONE);

                                }
                            }
                        });

                    }
                });


            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
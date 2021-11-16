package com.example.babuland.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babuland.R;
import com.example.babuland.response.lists.Listss;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private Context context;
    private List<Listss> getList;

    public ListAdapter(Context context, List<Listss> getList) {
        this.context = context;
        this.getList = getList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemss= LayoutInflater.from(context).inflate(R.layout.weather_list_row,parent,false);
        return new ListViewHolder(itemss);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.locationName.setText(getList.get(position).getName());
        holder.status.setText(getList.get(position).getWeather().get(0).getMain());
        holder.temperature.setText(getList.get(position).getMain().getTemp().toString()+"\u00B0"+" C");

        Bundle bundle=new Bundle();
        bundle.putDouble("lat",getList.get(position).getCoord().getLat());
        bundle.putDouble("lon",getList.get(position).getCoord().getLon());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_mapsFragment,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return getList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder{
        TextView locationName,status,temperature;
        CardView cardView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            locationName=itemView.findViewById(R.id.locationName);
            status=itemView.findViewById(R.id.locationStatus);
            temperature=itemView.findViewById(R.id.temperature);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}

package com.fit5046.wildsecured.Adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.SavedPlace;
import com.fit5046.wildsecured.Activity.SavedPlacesActivity;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.databinding.SavedPlaceItemBinding;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SavedPlaceAdapter extends RecyclerView.Adapter<SavedPlaceAdapter.ViewHolder>{

    private Context context;
    private List<SavedPlace> userSavedPlacesList;
    private SavedPlaceViewModel savedPlaceViewModel;
    private SavedPlacesActivity savedPlacesActivity;
    private HandleCardClick handleCardClick;

    public SavedPlaceAdapter(Context context, HandleCardClick handleCardClick, Application application, SavedPlacesActivity savedPlacesActivity){
        this.context = context;
        this.handleCardClick = handleCardClick;
        this.savedPlacesActivity = savedPlacesActivity;
        savedPlaceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(SavedPlaceViewModel.class);
    }

    public void setUserSavedPlacesList(List<SavedPlace> savedPlacesList){
        this.userSavedPlacesList = savedPlacesList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SavedPlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.saved_place_item, parent, false);
        SavedPlaceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.saved_place_item, parent, false);
//        return new ViewHolder(view);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPlaceAdapter.ViewHolder holder, int position) {
        if (userSavedPlacesList != null){
            holder.binding.setSavedPlace(userSavedPlacesList.get(position));
        }

        holder.binding.savedPlaceDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
                builder.setMessage("Are you sure you want to delete this place?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleCardClick.deleteSavedPlace(userSavedPlacesList.get(position));
                        Toast.makeText(v.getContext(), "Place Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });


        holder.binding.savedPlaceDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lon = String.valueOf(userSavedPlacesList.get(position).getPlaceLon());
                String lat = String.valueOf(userSavedPlacesList.get(position).getPlaceLat());
                handleCardClick.getDirection(lat,lon);
            }
        });

        holder.binding.savedPlaceDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCardClick.setDefaultLocation(userSavedPlacesList.get(position));
            }
        });

//        holder.binding.savedPlaceShowOnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(userSavedPlacesList.get(position).getPlaceLat(),userSavedPlacesList.get(position).getPlaceLon()))
//                        .title(userSavedPlacesList.get(position).getPlaceAddress()).snippet(userSavedPlacesList.get(position).getPlaceName());
//                handleCardClick.showOnMap(markerOptions);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (userSavedPlacesList == null || userSavedPlacesList.size() == 0){
            return 0;
        }else{
            return userSavedPlacesList.size();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private SavedPlaceItemBinding binding;

        public ViewHolder(@NonNull SavedPlaceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface HandleCardClick{
        void showOnMap(MarkerOptions markerOptions);
        void getDirection(String lat, String lon);
        void deleteSavedPlace(SavedPlace savedPlace);
        void setDefaultLocation(SavedPlace savedPlace);
    }
}

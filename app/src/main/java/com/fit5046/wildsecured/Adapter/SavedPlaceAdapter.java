package com.fit5046.wildsecured.Adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.CheckListActivity;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.SavedPlace;
import com.fit5046.wildsecured.SavedPlacesActivity;
import com.fit5046.wildsecured.Viewmodel.ItemViewModel;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;

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
        View view = LayoutInflater.from(context).inflate(R.layout.saved_place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPlaceAdapter.ViewHolder holder, int position) {
        holder.placeName.setText(this.userSavedPlacesList.get(position).placeName);
        holder.placeAddress.setText(this.userSavedPlacesList.get(position).placeAddress);
        holder.placeDelete.setOnClickListener(new View.OnClickListener() {
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
        holder.placeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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

        TextView placeName;
        TextView placeAddress;
        ImageView placeDirection;
        ImageView placeDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.savedPlaceName);
            placeAddress = itemView.findViewById(R.id.savedPlaceAddress);
            placeDirection = itemView.findViewById(R.id.savedPlaceDirection);
            placeDelete = itemView.findViewById(R.id.savedPlaceDelete);
        }
    }

    public interface HandleCardClick{
        void getDirection();
        void deleteSavedPlace(SavedPlace savedPlace);
    }
}

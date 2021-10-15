package com.fit5046.wildsecured.Adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.Fragment.MapFragment;
import com.fit5046.wildsecured.GooglePlaceModel;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.databinding.PlaceItemBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.errors.ApiException;

import java.util.Collections;
import java.util.List;

public class GooglePlaceAdapter extends RecyclerView.Adapter<GooglePlaceAdapter.ViewHolder> {

    private List<GooglePlaceModel> googlePlaceModelList;
    private PlacesClient placesClient;
    private HandleCardClick cardClickListener;
    private SavedPlaceViewModel savedPlaceViewModel;

    public GooglePlaceAdapter(Context context, HandleCardClick cardClickListener, Application application, MapFragment mapFragment) {
        placesClient = Places.createClient(context);
        this.cardClickListener = cardClickListener;
        savedPlaceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(SavedPlaceViewModel.class);
    }

    @NonNull
    @Override
    public GooglePlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.place_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GooglePlaceAdapter.ViewHolder holder, int position) {

        if (googlePlaceModelList != null){
            GooglePlaceModel googlePlaceModel = googlePlaceModelList.get(position);
            holder.binding.setGooglePlaceModel(googlePlaceModel);

            final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
            final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(googlePlaceModelList.get(position).getPlaceId(), fields);
            placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                final Place place = response.getPlace();
                Log.i("TAG", "Fetching photo...");

                // Get the photo metadata.
                final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                if (metadata == null || metadata.isEmpty()) {
                    Log.w("TAG", "No photo metadata.");
                    return;
                }
                final PhotoMetadata photoMetadata = metadata.get(0);

                // Get the attribution text.
                final String attributions = photoMetadata.getAttributions();

                // Create a FetchPhotoRequest.
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(100) // Optional.
                        .setMaxHeight(100) // Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    holder.binding.placeThumbnail.setImageBitmap(bitmap);
                })).addOnFailureListener((exception) -> {
                    final ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                });
            });
        }

        holder.binding.placeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lon = String.valueOf(googlePlaceModelList.get(position).getGeometry().getLocation().getLng());
                String lat = String.valueOf(googlePlaceModelList.get(position).getGeometry().getLocation().getLat());
                cardClickListener.navigationClick(lat,lon);
            }
        });
        holder.binding.placeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClickListener.cardClick(googlePlaceModelList.get(position));
            }
        });

        holder.binding.imgSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClickListener.savePlaceClick(googlePlaceModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (googlePlaceModelList != null){
            return googlePlaceModelList.size();
        }else{
            return 0;
        }
    }

    public void setGooglePlaceModels(List<GooglePlaceModel> googlePlaceModels){
        this.googlePlaceModelList = googlePlaceModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PlaceItemBinding binding;

        public ViewHolder(@NonNull PlaceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface HandleCardClick{
        void cardClick(GooglePlaceModel googlePlaceModel);
        void savePlaceClick(GooglePlaceModel googlePlaceModel);
        void navigationClick(String lat, String lon);
    }
}

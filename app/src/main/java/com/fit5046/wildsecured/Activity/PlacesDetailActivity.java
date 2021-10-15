package com.fit5046.wildsecured.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fit5046.wildsecured.GooglePlacesDetails.PlaceDetailsResponse;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Utils.GooglePlaceQuery;
import com.fit5046.wildsecured.Utils.RetrofitClient;
import com.fit5046.wildsecured.databinding.ActivityPlacesDetailBinding;
import com.google.gson.Gson;
import com.google.maps.model.PlaceDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class PlacesDetailActivity extends AppCompatActivity {

    private ActivityPlacesDetailBinding binding;
    private ProgressDialog progressDialog;
    private String placeId;
    private PlaceDetailsResponse placeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlacesDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        placeId = intent.getStringExtra("place_id");
        getPlaceDetail();

//        @GET("maps/api/place/photo?maxwidth=400&maxheight=400")
    }



    private void getPlaceDetail(){
        GooglePlaceQuery query = RetrofitClient.placeRetrofitClient().create(GooglePlaceQuery.class);
        String searchString = "website,name,formatted_phone_number,opening_hours,photos";
        query.getPlaceDetail(placeId, searchString, getResources().getString(R.string.GOOGLE_API_KEY)).enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                if (response.code() == 200){
                    placeDetails = response.body();
                    Gson gson = new Gson();
                    Log.d("TAG", "onResponse: " + gson.toJson(placeDetails));
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {

            }
        });
    }

}
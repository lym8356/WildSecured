package com.fit5046.wildsecured.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.fit5046.wildsecured.WeatherModel.CurrentCall;
import com.fit5046.wildsecured.WeatherModel.WeatherQuery;
import com.fit5046.wildsecured.WeatherModel.WeatherResponse;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentHomeBinding;
import com.fit5046.wildsecured.util.GpsTracker;
import com.fit5046.wildsecured.util.Helper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    //openweather api related
    private final String AppId = "832c9987d78cef10eb4823a73e81743c";
    private final String weatherUrl = "https://api.openweathermap.org/";
    private final String exclude = "minutely,hourly,alerts";
    private final String units = "metric";

    private final String googleAppId = "AIzaSyDKQicQAusG5symiA4KGAUiWwDlpxQgtuk";

    //location related
    String currentLon;
    String currentLat;
    private GpsTracker gpsTracker;

    // temp lon and lat
    String tempLon;
    String tempLat;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // icon variables for balloons
        LinearLayout sunIcon = binding.homeSunIcon;
        LinearLayout insectIcon = binding.homeInsectIcon;
        LinearLayout animalIcon = binding.homeAnimalIcon;
        LinearLayout clothIcon = binding.homeClothingIcon;

        // check location permission and get current location
        if(checkPermission()){
            getLocation();
        }
        getWeatherInfo(currentLon, currentLat);

        Balloon sunAdviceBalloon = new Balloon.Builder(getActivity().getApplicationContext())
                .setLayout(R.layout.sun_advice_popup)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setWidthRatio(0.55f)
                .setArrowPosition(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        binding.homeSunIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sunAdviceBalloon.showAlignTop(sunIcon);
            }
        });

        TextView textView = sunAdviceBalloon.getContentView().findViewById(R.id.sunAdviceTitle);
//        textView.setText("LALALLA");

        Balloon insectAdviceBalloon = new Balloon.Builder(getActivity().getApplicationContext())
                .setLayout(R.layout.insect_advice_popup)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setWidthRatio(0.55f)
                .setArrowPosition(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        binding.homeInsectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insectAdviceBalloon.showAlignTop(insectIcon);
            }
        });

        Balloon animalAdviceBalloon = new Balloon.Builder(getActivity().getApplicationContext())
                .setLayout(R.layout.animal_advice_popup)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setWidthRatio(0.55f)
                .setArrowPosition(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        binding.homeAnimalIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animalAdviceBalloon.showAlignTop(animalIcon);
            }
        });

        Balloon clothingAdviceBalloon = new Balloon.Builder(getActivity().getApplicationContext())
                .setLayout(R.layout.clothing_advice_popup)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setWidthRatio(0.55f)
                .setArrowPosition(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.primary_orange))
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        binding.homeClothingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clothingAdviceBalloon.showAlignTop(clothIcon);
            }
        });

        binding.homeBotDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new ForecastFragment());
            }
        });

        // Initialize google places
        Places.initialize(getActivity().getApplicationContext(), googleAppId);
        binding.homeSearchBar.setFocusable(false);
        binding.homeSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getActivity().getApplicationContext());
                startActivityForResult(intent, 100);
            }
        });
        binding.homeSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempLat != null && tempLon != null){
                    currentLon = tempLon;
                    currentLat = tempLat;
                    getWeatherInfo(currentLon, currentLat);
                }else{
                    Toast.makeText(getActivity(), "Please enter an address.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.homeSearchBar.setText(place.getAddress());
            tempLat = String.valueOf(place.getLatLng().latitude);
            tempLon = String.valueOf(place.getLatLng().longitude);

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getWeatherInfo(String lon, String lat) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherUrl).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherQuery query = retrofit.create(WeatherQuery.class);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        String lon = "144.9631", lat = "-37.8136";


        // get current weather data
        Call getCurrentCall = query.getCurrentCallData(lat, lon, units, AppId);
        getCurrentCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                int iconCode;

                if (response.code() == 200) {
                    CurrentCall currentCall = (CurrentCall) response.body();
                    assert currentCall != null;
                    iconCode = currentCall.getWeather().get(0).getId();
                    int iconToReplace = Helper.getArtResourceForWeatherCondition(iconCode);
                    binding.homeWeatherIcon.setImageResource(iconToReplace);
                    binding.homeWeatherIcon.setColorFilter(Color.rgb(255, 255, 255));
                    binding.homeLocation.setText(currentCall.getName());
                    binding.homeDay.setText(Helper.formatDate(currentCall.getDt()));
                    binding.homeWeatherDesc.setText(currentCall.getWeather().get(0).getDescription());
                    binding.homeWeatherMin.setText(String.valueOf(Math.round(currentCall.getMain().getTempMin())));
                    binding.homeWeatherMax.setText(String.valueOf(Math.round(currentCall.getMain().getTempMax())));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });

        // execute query to get forecast data
        Call getForecastCall = query.getCurrentWeatherData(lat, lon, exclude, units, AppId);
        getForecastCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;

                    //update UI
                    binding.homeWeatherUv.setText(String.valueOf(weatherResponse.getCurrentWeatherResponse().getUvi()));
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to retrieve weather data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getLocation(){
        gpsTracker = new GpsTracker(getActivity());
        if(gpsTracker.canGetLocation()){
            currentLat = String.valueOf(gpsTracker.getLatitude());
            currentLon = String.valueOf(gpsTracker.getLongitude());

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public boolean checkPermission(){
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }else{
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Access to location info granted", Toast.LENGTH_SHORT).show();
                getLocation();
            }else {
                Toast.makeText(getActivity(), "Access to location info denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchFragment(Fragment fragment){
        binding.homeSearchBar.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("lon", currentLon);
        bundle.putString("lat", currentLat);

        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment)
                .addToBackStack(null).commit();
    }
}
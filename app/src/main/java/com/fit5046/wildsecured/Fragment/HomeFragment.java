package com.fit5046.wildsecured.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit5046.wildsecured.AnimalInfoActivity;
import com.fit5046.wildsecured.DataManager.WeatherDataManager;
import com.fit5046.wildsecured.GearInfoActivity;
import com.fit5046.wildsecured.InsectInfoActivity;
import com.fit5046.wildsecured.SavedPlace;
import com.fit5046.wildsecured.SavedPlacesActivity;
import com.fit5046.wildsecured.SunInfoActivity;
import com.fit5046.wildsecured.Utils.RetrofitClient;
import com.fit5046.wildsecured.Utils.WeatherQuery;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.WeatherModel.Daily;
import com.fit5046.wildsecured.WeatherModel.WeatherResponse;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentHomeBinding;
import com.fit5046.wildsecured.Utils.GpsTracker;
import com.fit5046.wildsecured.Utils.Helper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private ProgressDialog progressDialog;

    //openWeather api parameters
    private final String exclude = "minutely,hourly,alerts";
    private final String units = "metric";

    private ArrayList<Daily> forecastList;
    private WeatherResponse weatherResponse;

    //location related
    private String currentLon;
    private String currentLat;
    private String currentCity;
    private GpsTracker gpsTracker;

    // temp lon and lat
    private String lonToSearch;
    private String latToSearch;

    private SavedPlaceViewModel savedPlaceViewModel;
    private SavedPlace previousDefaultPlace;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String googleAppId = getResources().getString(R.string.GOOGLE_API_KEY);

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        forecastList = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMax(100);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            if (bundle.getString("key").equals("tutorial")){
                showTutorial();
            }
        }

        initViewModel();

        // check location permission and get current location
        if (checkPermission()) {
            if (previousDefaultPlace != null && WeatherDataManager.weatherResponse != null && WeatherDataManager.forecastData != null){
                forecastList = WeatherDataManager.forecastData;
                currentCity = WeatherDataManager.cityName;
                System.out.println("Retrieving current city: " + currentCity);
                setUi(WeatherDataManager.weatherResponse, currentCity);
            }else if (WeatherDataManager.weatherResponse != null && WeatherDataManager.forecastData != null){
                forecastList = WeatherDataManager.forecastData;
                currentCity = WeatherDataManager.cityName;
                System.out.println("Retrieving current city: " + currentCity);
                setUi(WeatherDataManager.weatherResponse, currentCity);
            }else if (previousDefaultPlace != null){
                System.out.println("Loading from saved place");
//                currentLon = String.valueOf(previousDefaultPlace.getPlaceLon());
//                currentLat = String.valueOf(previousDefaultPlace.getPlaceLat());
                getWeatherInfo(currentLon, currentLat);
            }else{
                System.out.println("Getting user current location");
                getLocation();
                getWeatherInfo(currentLon, currentLat);
            }
        }

        binding.homeSunIcon.setOnClickListener(this);
        binding.homeInsectIcon.setOnClickListener(this);
        binding.homeAnimalIcon.setOnClickListener(this);
        binding.homeClothingIcon.setOnClickListener(this);
        binding.homeLocationWrapper.setOnClickListener(this);


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
            private long mLastClickTime = 0;
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setCountry("AU").build(getActivity().getApplicationContext());
                startActivityForResult(intent, 100);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.homeSearchBar.setText(place.getAddress());
            latToSearch = String.valueOf(place.getLatLng().latitude);
            lonToSearch = String.valueOf(place.getLatLng().longitude);

            if (lonToSearch != null && latToSearch != null) {
                currentLat = latToSearch;
                currentLon = lonToSearch;
                getWeatherInfo(lonToSearch, latToSearch);
            }

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getWeatherInfo(String lon, String lat) {
        System.out.println("Getting weather info from api.");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final String AppId = getResources().getString(R.string.OPENWEATHER_API_KEY);

        WeatherQuery query = RetrofitClient.openWeatherRetrofitClient().create(WeatherQuery.class);
        currentCity = getCityName();
        WeatherDataManager.cityName = currentCity;

        // get current weather data
        query.getWeatherData(lat, lon, exclude, units, AppId).enqueue(new Callback<WeatherResponse> () {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse>  response) {

                if (response.code() == 200) {
                    weatherResponse = response.body();
                    Gson gson = new Gson();
                    Log.d("TAG", "onResponse: " + gson.toJson(weatherResponse));
                    forecastList = (ArrayList<Daily>) weatherResponse.getDaily();
                    Log.d("TAG", "Daily: " + forecastList.get(0).getHumidity().toString());

                    WeatherDataManager.weatherResponse = weatherResponse;
                    WeatherDataManager.forecastData = forecastList;

                    setUi(weatherResponse, currentCity);
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Error fetching weather data.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
                progressDialog.dismiss();
            }

        });

    }

    private void setUi(WeatherResponse weatherResponse, String currentCity) {
        int iconCode = weatherResponse.getCurrent().getWeather().get(0).getId();
        int iconToReplace = Helper.getArtResourceForWeatherCondition(iconCode);
        binding.homeWeatherIcon.setImageResource(iconToReplace);
        binding.homeWeatherIcon.setColorFilter(Color.rgb(255, 255, 255));
        binding.homeLocation.setText(currentCity);
        binding.homeDay.setText(Helper.formatDate(weatherResponse.getCurrent().getDt()));
        binding.homeWeatherUv.setText(String.valueOf(weatherResponse.getCurrent().getUvi()));
        binding.homeWeatherDesc.setText(weatherResponse.getCurrent().getWeather().get(0).getDescription());
        binding.homeWeatherMin.setText(String.valueOf(Math.round(forecastList.get(0).getTemp().getMin())));
        binding.homeWeatherMax.setText(String.valueOf(Math.round(forecastList.get(0).getTemp().getMax())));
    }

    private String getCityName(){
        Geocoder geoCoder = new Geocoder(getActivity().getApplicationContext());
        String city = "Location";
        try {
            List<Address> matches = geoCoder.getFromLocation(Double.parseDouble(currentLat),Double.parseDouble(currentLon), 1);
            city = (matches.isEmpty() ? null: matches.get(0).getLocality());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            currentLat = String.valueOf(gpsTracker.getLatitude());
            currentLon = String.valueOf(gpsTracker.getLongitude());
            gpsTracker.stopUsingGPS();
//            Log.d("location", currentLat+currentLon);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void initViewModel(){
        savedPlaceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(SavedPlaceViewModel.class);
        savedPlaceViewModel.findCurrentDefaultPlace().thenApply(defaultPlace -> {
            if (defaultPlace != null){
                this.previousDefaultPlace = defaultPlace;
            }else{
                SavedPlace exampleSavedPlace = new SavedPlace("Flagstaff Gardens",
                        "309-311 William Street, West Melbourne",
                        "ChIJnQHt6Uld1moRgIUxBXZWBA8",
                        4.6,
                        5251,
                        -37.8105577,
                        144.9545199);
                exampleSavedPlace.setSelected(true);
                savedPlaceViewModel.insert(exampleSavedPlace);
                getDefaultPlace();
            }
            return true;
        });
        savedPlaceViewModel.getSavedPlaceList().observe(requireActivity(), new Observer<List<SavedPlace>>() {
            @Override
            public void onChanged(List<SavedPlace> savedPlaceList) {
                getDefaultPlace();
            }
        });
    }

    private void getDefaultPlace(){
        savedPlaceViewModel.findCurrentDefaultPlace().thenApply(defaultPlace -> {
            if (defaultPlace != null){
                if (previousDefaultPlace != defaultPlace){
                    this.previousDefaultPlace = defaultPlace;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Loading from saved place");
                            currentLat = String.valueOf(defaultPlace.getPlaceLat());
                            currentLon = String.valueOf(defaultPlace.getPlaceLon());
                            getWeatherInfo(String.valueOf(defaultPlace.getPlaceLon()), String.valueOf(defaultPlace.getPlaceLat()));
                        }
                    });
                }
            }else{
                System.out.println("get default place Null");
            }
            return true;
        });
    }



    public boolean checkPermission() {
        try {
            if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Access to location info granted", Toast.LENGTH_SHORT).show();
                getLocation();
                getWeatherInfo(currentLon, currentLat);
            } else {
                Toast.makeText(getActivity(), "Access to location info denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchFragment(Fragment fragment) {
        binding.homeSearchBar.setText("");

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("forecastList", forecastList);

        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment)
                .addToBackStack(null).commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home_sun_icon:
                intent = new Intent(getActivity(), SunInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.home_insect_icon:
                intent = new Intent(getActivity(), InsectInfoActivity.class);
                intent.putExtra("lat", currentLat);
                intent.putExtra("lon", currentLon);
                intent.putExtra("cityName", currentCity);
                startActivity(intent);
                break;
            case R.id.home_animal_icon:
                intent = new Intent(getActivity(), AnimalInfoActivity.class);
                intent.putExtra("lat", currentLat);
                intent.putExtra("lon", currentLon);
                intent.putExtra("cityName", currentCity);
                startActivity(intent);
                break;
            case R.id.home_clothing_icon:
                intent = new Intent(getActivity(), GearInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.homeLocationWrapper:
                intent = new Intent(getActivity(), SavedPlacesActivity.class);
                startActivity(intent);
        }
    }

    private void showTutorial(){
        new GuideView.Builder(requireContext())
                .setTitle("Your starred list.")
                .setTargetView(binding.homeLocationWrapper)
                .setContentText("The places you saved will be displayed here.")
                .setDismissType(DismissType.anywhere)
                .setGravity(Gravity.center)
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        new GuideView.Builder(requireContext())
                                .setTitle("Sun Protection Information.")
                                .setTargetView(binding.homeSunIcon)
                                .setContentText("Sun protection recommendation.")
                                .setDismissType(DismissType.anywhere)
                                .setGravity(Gravity.center)
                                .setTitleTextSize(16)
                                .setContentTextSize(14)
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {
                                        new GuideView.Builder(requireContext())
                                                .setTitle("Insect Information")
                                                .setTargetView(binding.homeInsectIcon)
                                                .setContentText("Get a list of dangerous insects that might appear in your searched area.")
                                                .setDismissType(DismissType.anywhere)
                                                .setGravity(Gravity.center)
                                                .setTitleTextSize(16)
                                                .setContentTextSize(14)
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {
                                                        new GuideView.Builder(requireContext())
                                                                .setTitle("Animal Information")
                                                                .setTargetView(binding.homeAnimalIcon)
                                                                .setContentText("Get a list of dangerous animals that might appear in your searched area.")
                                                                .setDismissType(DismissType.anywhere)
                                                                .setGravity(Gravity.center)
                                                                .setTitleTextSize(16)
                                                                .setContentTextSize(14)
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {
                                                                        new GuideView.Builder(requireContext())
                                                                                .setTitle("Backpack Organizer")
                                                                                .setTargetView(binding.homeClothingIcon)
                                                                                .setContentText("Helps you organize the items you need to pack.")
                                                                                .setDismissType(DismissType.anywhere)
                                                                                .setGravity(Gravity.center)
                                                                                .setTitleTextSize(16)
                                                                                .setContentTextSize(14)
                                                                                .setGuideListener(new GuideListener() {
                                                                                    @Override
                                                                                    public void onDismiss(View view) {
                                                                                        new GuideView.Builder(requireContext())
                                                                                                .setTitle("Weather Forecast")
                                                                                                .setTargetView(binding.homeBotDown)
                                                                                                .setContentText("Get 7 days weather forecast.")
                                                                                                .setDismissType(DismissType.anywhere)
                                                                                                .setGravity(Gravity.center)
                                                                                                .setTitleTextSize(16)
                                                                                                .setContentTextSize(14).
                                                                                                build().show();
                                                                                    }
                                                                                }).build().show();
                                                                    }
                                                                }).build().show();
                                                    }
                                                }).build().show();
                                    }
                                }).build().show();
                    }
                }).build().show();
    }

}
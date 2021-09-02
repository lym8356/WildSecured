package com.fit5046.wildsecured.Fragment;
import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.fit5046.wildsecured.AnimalInfoActivity;
import com.fit5046.wildsecured.GearInfoActivity;
import com.fit5046.wildsecured.InsectInfoActivity;
import com.fit5046.wildsecured.SunInfoActivity;
import com.fit5046.wildsecured.WeatherModel.CurrentCall;
import com.fit5046.wildsecured.WeatherModel.WeatherQuery;
import com.fit5046.wildsecured.WeatherModel.WeatherResponse;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.WildLifeDataModal.WildLifeDataResponse;
import com.fit5046.wildsecured.WildLifeDataModal.WildLifeQuery;
import com.fit5046.wildsecured.databinding.FragmentHomeBinding;
import com.fit5046.wildsecured.Utils.GpsTracker;
import com.fit5046.wildsecured.Utils.Helper;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private FragmentHomeBinding binding;
    //openweather api related
    private final String AppId = "832c9987d78cef10eb4823a73e81743c";
    private final String weatherUrl = "https://api.openweathermap.org/";
    private final String exclude = "minutely,hourly,alerts";
    private final String units = "metric";

    //Atlas of living au related
    private final String AlaUrl = "https://spatial.ala.org.au/";
    private final String radius = "10000";

    private final String googleAppId = "AIzaSyDKQicQAusG5symiA4KGAUiWwDlpxQgtuk";

    //location related
    String currentLon;
    String currentLat;
    private GpsTracker gpsTracker;

    // temp lon and lat
    String lonToSearch;
    String latToSearch;

    // store animal counts
    int insectCount;
    int wildLifeCount;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // check location permission and get current location
        if(checkPermission()){
            getLocation();
            getWeatherInfo(currentLon, currentLat);
            getWildLifeInfo(currentLon, currentLat);
        }

        binding.homeSunIcon.setOnClickListener(this);
        binding.homeInsectIcon.setOnClickListener(this);
        binding.homeAnimalIcon.setOnClickListener(this);
        binding.homeClothingIcon.setOnClickListener(this);


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
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setCountry("AU").build(getActivity().getApplicationContext());
                startActivityForResult(intent, 100);
            }
        });


        binding.homeSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lonToSearch != null && latToSearch != null){

                    getWeatherInfo(lonToSearch, latToSearch);
                    getWildLifeInfo(lonToSearch, latToSearch);

                }else{
                    Toast.makeText(getActivity(), "Please enter an address.", Toast.LENGTH_LONG).show();
                }
            }
        });

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.homeSearchBar.setText(place.getAddress());
            latToSearch = String.valueOf(place.getLatLng().latitude);
            lonToSearch = String.valueOf(place.getLatLng().longitude);

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getWeatherInfo(String lon, String lat) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherUrl).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherQuery query = retrofit.create(WeatherQuery.class);
        // show progress bar when the app is downloading info
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
                    progressDialog.dismiss();
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

    public void getWildLifeInfo(String lon, String lat){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AlaUrl).addConverterFactory(GsonConverterFactory.create()).build();
        WildLifeQuery query = retrofit.create(WildLifeQuery.class);

        Call getWildLifeDataResponse   = query.getWildLifeData(lat, lon, radius);

        getWildLifeDataResponse.enqueue(new Callback<ArrayList<WildLifeDataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<WildLifeDataResponse>>  call, Response<ArrayList<WildLifeDataResponse>> response) {

                if (response.code() == 200) {
                    ArrayList<WildLifeDataResponse> wildLifeDataResponseList = new ArrayList<>();
                    wildLifeDataResponseList.addAll(response.body());
                    assert wildLifeDataResponseList != null;
                    insectCount = Helper.getDangerousInsectCount(wildLifeDataResponseList);
                    wildLifeCount = Helper.getDangerousWildLifeCount(wildLifeDataResponseList);

                    binding.invisibleField.setText(String.valueOf(wildLifeCount));
                    binding.invisibleField2.setText(String.valueOf(insectCount));

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.home_sun_icon:
                intent = new Intent(getActivity(), SunInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.home_insect_icon:
                intent = new Intent(getActivity(), InsectInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.home_animal_icon:
                intent = new Intent(getActivity(), AnimalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.home_clothing_icon:
                intent = new Intent(getActivity(), GearInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
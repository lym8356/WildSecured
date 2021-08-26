package com.fit5046.wildsecured.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.WeatherModel.CurrentCall;
import com.fit5046.wildsecured.WeatherModel.ForecastWeatherResponse;
import com.fit5046.wildsecured.WeatherModel.WeatherQuery;
import com.fit5046.wildsecured.WeatherModel.WeatherResponse;
import com.fit5046.wildsecured.databinding.FragmentForecastBinding;
import com.fit5046.wildsecured.util.ForecastAdapter;
import com.fit5046.wildsecured.util.Helper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;
    //openweather api related
    private final String AppId = "832c9987d78cef10eb4823a73e81743c";
    private final String weatherUrl = "https://api.openweathermap.org/";
    private final String exclude = "minutely,hourly,alerts";
    private final String units = "metric";

    //location related
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentLon;
    String currentLat;
    String searchLon;
    String searchLat;

    private RecyclerView rvResults;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupRecyclerView();

        Bundle bundle = getArguments();
        if (bundle != null){
            if (bundle.getString("lat") != null && bundle.getString("lon") != null){
                currentLat = bundle.getString("lat");
                currentLon = bundle.getString("lon");
                if (bundle.getString("searchLat") != null && bundle.getString("searchLon") != null){
                    searchLat = bundle.getString("searchLat");
                    searchLon = bundle.getString("searchLon");
                    System.out.println(searchLat + searchLon);
                    getWeatherInfo(searchLon, searchLat);
                }else{
                    getWeatherInfo(currentLon, currentLat);
                }
            }

        }

        binding.forecastUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() != 0){
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    public void setupRecyclerView(){
        rvResults = (RecyclerView) binding.forecastRecycleView;
        rvResults.hasFixedSize();
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvResults.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    public void getWeatherInfo(String lon, String lat) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherUrl).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherQuery query = retrofit.create(WeatherQuery.class);
//        String lat = "-37.8136";
//        String lon = "144.9631";


        // execute query to get forecast data
        Call getForecastCall = query.getCurrentWeatherData(lat, lon, exclude, units, AppId);
        getForecastCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;
                    ForecastAdapter adapter = new ForecastAdapter(getContext(), weatherResponse.getForecastWeatherResponses());
                    rvResults.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to retrieve weather data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });
    }

}
package com.fit5046.wildsecured.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.WeatherModel.Daily;
import com.fit5046.wildsecured.databinding.FragmentForecastBinding;
import com.fit5046.wildsecured.Adapter.ForecastAdapter;


import java.util.ArrayList;


public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;

    private ArrayList<Daily> forecastList;

    private RecyclerView rvResults;

    private ProgressDialog progressDialog;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentForecastBinding.inflate(inflater, container, false);

        forecastList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMax(100);


        setupRecyclerView();

        Bundle bundle = getArguments();
        if (bundle != null){
            forecastList = bundle.getParcelableArrayList("forecastList");
            getWeatherInfo();
        }

        binding.forecastUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() != 0){
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        return binding.getRoot();
    }

    public void setupRecyclerView(){
        rvResults = (RecyclerView) binding.forecastRecycleView;
        rvResults.hasFixedSize();
        rvResults.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvResults.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    public void getWeatherInfo() {


        ForecastAdapter adapter = new ForecastAdapter();
        if (forecastList != null){
            adapter.setForecastData(forecastList);
            rvResults.setAdapter(adapter);
        }

    }

}
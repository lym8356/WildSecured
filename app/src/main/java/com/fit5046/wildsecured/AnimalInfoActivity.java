package com.fit5046.wildsecured;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.fit5046.wildsecured.Adapter.WildlifeInfoAdapter;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.Utils.Helper;
import com.fit5046.wildsecured.Utils.RetrofitClient;
import com.fit5046.wildsecured.Utils.WildLifeQuery;
import com.fit5046.wildsecured.Viewmodel.WildlifeViewModel;
import com.fit5046.wildsecured.WildlifeModel.WildLifeDataModel;
import com.fit5046.wildsecured.databinding.ActivityAnimalInfoBinding;
import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimalInfoActivity extends AppCompatActivity {

    private final String snakeFilterQuery = "&q=family:Elapidae+Typhlopidae+Pythonidae+Colubridae+Acrochordidae";
    private final String batFilterQuery = "&q=family:Vespertilionidae+Miniopteridae+Pteropodidae+Molossidae+Emballonuridae";

    private ActivityAnimalInfoBinding binding;
    private List<WildLifeDataModel> wildLifeListFromApi;
    private WildlifeInfoAdapter wildlifeInfoAdapter;
    private WildlifeViewModel wildlifeViewModel;
    private List<Wildlife> wildlifeListFromDb;
    private List<Wildlife> filteredList;
    private boolean isFABOpen;
    private boolean isNameListDesc = false;
    private boolean isDangerListDesc = false;
    private ProgressDialog progressDialog;

    private String lon, lat, cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimalInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        progressDialog = new ProgressDialog(this);
        //progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        cityName = intent.getStringExtra("cityName");
        isFABOpen = false;

        binding.cityName.setText(cityName);

        binding.animalAdviceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<String> animalGroupList = new ArrayList<>();
        animalGroupList.add("Reptiles");
        animalGroupList.add("Bats");
        animalGroupList.add("Others");
        ArrayAdapter<String> animalGroupAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, animalGroupList);
        binding.animalGroup.setAdapter(animalGroupAdapter);
        binding.animalGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                switch (selected){
                    case "Reptiles":
                        progressDialog.show();
                        binding.animalOtherInfo.setVisibility(View.GONE);
                        getAnimalInfo(selected, snakeFilterQuery);
                        break;
                    case "Bats":
                        progressDialog.show();
                        binding.animalOtherInfo.setVisibility(View.GONE);
                        getAnimalInfo("Mammals", batFilterQuery);
                        break;
                    case "Others":
                        binding.animalOtherInfo.setVisibility(View.VISIBLE);
                        getDataFromDb(selected);
                        break;

                }
            }
        });
        binding.sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFABOpen){
                    binding.sortByName.setVisibility(View.INVISIBLE);
                    binding.sortByThreat.setVisibility(View.INVISIBLE);
                    isFABOpen = false;
                }else{
                    binding.sortByName.setVisibility(View.VISIBLE);
                    binding.sortByThreat.setVisibility(View.VISIBLE);
                    isFABOpen = true;
                }
            }
        });
        binding.sortByThreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList != null){
                    if (isDangerListDesc){
                        filteredList.sort(Wildlife.sortByDangerLevelAsc);
                        wildlifeListFromDb.sort(Wildlife.sortByDangerLevelAsc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isDangerListDesc = false;
                    }else{
                        filteredList.sort(Wildlife.sortByDangerLevelDesc);
                        wildlifeListFromDb.sort(Wildlife.sortByDangerLevelDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isDangerListDesc = true;
                    }
                }else if (wildlifeListFromDb != null){
                    if (isNameListDesc){
                        wildlifeListFromDb.sort(Wildlife.sortByDangerLevelAsc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = false;
                    }else{
                        wildlifeListFromDb.sort(Wildlife.sortByDangerLevelDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = true;
                    }
                }
            }
        });
        binding.sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList != null){
                    if (isNameListDesc){
                        filteredList.sort(Wildlife.sortByNameAsc);
                        wildlifeListFromDb.sort(Wildlife.sortByNameAsc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = false;
                    }else{
                        filteredList.sort(Wildlife.sortByNameDesc);
                        wildlifeListFromDb.sort(Wildlife.sortByNameDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = true;
                    }
                }else if (wildlifeListFromDb != null){
                    if (isNameListDesc){
                        wildlifeListFromDb.sort(Wildlife.sortByNameAsc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = false;
                    }else{
                        wildlifeListFromDb.sort(Wildlife.sortByNameDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = true;
                    }
                }
            }
        });

        initViewModel();
        initRecyclerView();
    }

    private void getDataFromDb(String group){
        CompletableFuture future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                wildlifeListFromDb = wildlifeViewModel.getWildlifeInGroup(group);
                System.out.println(wildlifeListFromDb.size());
            }
        }).thenRun(new Runnable() {
            @Override
            public void run() {
                if (!group.equals("Others")){
                    getFilteredList(wildLifeListFromApi, wildlifeListFromDb);
                }
                setUi(group);
            }
        });
    }

    private void setUi(String group){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!group.equals("Others")){
                    if (wildLifeListFromApi == null || wildLifeListFromApi.size() == 0){
                        binding.noResult.setVisibility(View.VISIBLE);
                        binding.animalRecyclerView.setVisibility(View.GONE);
                    }
                    wildlifeInfoAdapter.setWildLifeList(filteredList);
                }else{
                    if (wildLifeListFromApi == null || wildLifeListFromApi.size() == 0){
                        binding.noResult.setVisibility(View.GONE);
                        binding.animalRecyclerView.setVisibility(View.VISIBLE);
                    }
                    wildlifeInfoAdapter.setWildLifeList(wildlifeListFromDb);
                }
                binding.animalRecyclerView.setAdapter(wildlifeInfoAdapter);
            }
        });
    }

    private void initRecyclerView(){
        binding.animalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.animalRecyclerView.setHasFixedSize(true);
        binding.animalRecyclerView.setItemViewCacheSize(20);
        binding.animalRecyclerView.setDrawingCacheEnabled(true);
        wildlifeInfoAdapter = new WildlifeInfoAdapter(this);
    }

    private void initViewModel(){
        wildlifeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(WildlifeViewModel.class);
    }

    private void getAnimalInfo(String animalGroup, String filterQuery){

        WildLifeQuery query = RetrofitClient.alaRetrofitClient().create(WildLifeQuery.class);
        String searchUrl = "ws/explore/group/" + animalGroup + "?" + "lat=" + lat + "&" + "lon=" + lon + "&" +"radius=20"
        +  "&pageSize=50&sort=count" + filterQuery;
        query.getWildLifeData(searchUrl).enqueue(new Callback<ArrayList<WildLifeDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WildLifeDataModel>> call, Response<ArrayList<WildLifeDataModel>> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
                Log.d("TAG", "onResponse: " + res);
                wildLifeListFromApi = response.body();
                getDataFromDb(animalGroup);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<WildLifeDataModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getFilteredList(List<WildLifeDataModel> fromApi, List<Wildlife> fromDb){
        filteredList = Helper.findCommonElements(fromApi, fromDb, false);
    }

}
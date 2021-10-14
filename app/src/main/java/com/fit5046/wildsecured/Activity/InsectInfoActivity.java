package com.fit5046.wildsecured.Activity;

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
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Utils.Helper;
import com.fit5046.wildsecured.Utils.RetrofitClient;
import com.fit5046.wildsecured.Utils.WildLifeQuery;
import com.fit5046.wildsecured.Viewmodel.WildlifeViewModel;
import com.fit5046.wildsecured.WildlifeModel.WildLifeDataModel;
import com.fit5046.wildsecured.databinding.ActivityInsectInfoBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsectInfoActivity extends AppCompatActivity {


    private final String spiderFilterQuery = "&q=family:Sparassidae+Araneidae+Salticidae+Desidae+Theridiidae+Pholcidae+Nephilidae+Oxyopidae+Nemesiidae+Hexathelidae" +
            "+Actinopodidae+Deinopidae+Nicodamidae+Clubionidae+Dysderidae+Pisauridae+Lamponidae+Lycosidae";
    private final String insectFilterQuery = "&q=family:Formicidae+Apidae+Tiphiidae+Sphecidae+Vespidae+Ichneumonidae+Chrysididae+Pompilidae";

    private ActivityInsectInfoBinding binding;
    private List<WildLifeDataModel> wildLifeListFromApi;
    private WildlifeInfoAdapter wildlifeInfoAdapter;
    private WildlifeViewModel wildlifeViewModel;
    private List<Wildlife> wildlifeListFromDb;
    private List<Wildlife> filteredList;
    private boolean isFABOpen;
    private boolean isNameListDesc = false;
    private boolean isDangerListDesc = false;
    private ProgressDialog progressDialog;

    private String lat, lon, cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsectInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        cityName = intent.getStringExtra("cityName");

        isFABOpen = false;

        binding.cityName.setText(cityName);
        binding.insectAdviceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<String> insectGroupList = new ArrayList<>();
        insectGroupList.add("Spiders");
        insectGroupList.add("Ants, bees & wasps");
        ArrayAdapter<String> insectGroupAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, insectGroupList);
        binding.insectGroup.setAdapter(insectGroupAdapter);
        binding.insectGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                switch (selected){
                    case "Spiders":
                        showProgressDialog();
                        getInsectInfo("Arthropods", spiderFilterQuery, true);
                        break;
                    case "Ants, bees & wasps":
                        showProgressDialog();
                        getInsectInfo("Arthropods", insectFilterQuery, false);
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
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isDangerListDesc = false;
                    }else{
                        filteredList.sort(Wildlife.sortByDangerLevelDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isDangerListDesc = true;
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
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = false;
                    }else{
                        filteredList.sort(Wildlife.sortByNameDesc);
                        wildlifeInfoAdapter.notifyDataSetChanged();
                        isNameListDesc = true;
                    }
                }
            }
        });

        initViewModel();
        initRecyclerView();
    }

    private void initRecyclerView(){
        binding.insectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.insectRecyclerView.setHasFixedSize(true);
        binding.insectRecyclerView.setItemViewCacheSize(20);
        binding.insectRecyclerView.setDrawingCacheEnabled(true);
        wildlifeInfoAdapter = new WildlifeInfoAdapter(this);
    }

    private void initViewModel(){
        wildlifeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(WildlifeViewModel.class);
    }

    private void getInsectInfo(String insectGroup, String filterQuery, boolean isSpider){

        WildLifeQuery query = RetrofitClient.alaRetrofitClient().create(WildLifeQuery.class);
        String searchUrl = "ws/explore/group/" + insectGroup + "?" + "lat=" + lat + "&" + "lon=" + lon + "&" +"radius=20"
                +  "&pageSize=50&sort=count" + filterQuery;
        query.getWildLifeData(searchUrl).enqueue(new Callback<ArrayList<WildLifeDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WildLifeDataModel>> call, Response<ArrayList<WildLifeDataModel>> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
                Log.d("TAG", "onResponse: " + res);
                wildLifeListFromApi = response.body();
                if (isSpider){
                    getDataFromDb("Spiders");
                }else{
                    getDataFromDb("Insects");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<WildLifeDataModel>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
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
                getFilteredList(wildLifeListFromApi, wildlifeListFromDb);
                setUi();
            }
        });
    }

    private void setUi(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (filteredList != null && filteredList.size() != 0){
                    wildlifeInfoAdapter.setWildLifeList(filteredList);
                    binding.noResult.setVisibility(View.GONE);
                    binding.insectRecyclerView.setVisibility(View.VISIBLE);
                    binding.insectRecyclerView.setAdapter(wildlifeInfoAdapter);
                }else{
                    binding.noResult.setVisibility(View.VISIBLE);
                    binding.insectRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getFilteredList(List<WildLifeDataModel> fromApi, List<Wildlife> fromDb){
        filteredList = Helper.findCommonElements(fromApi, fromDb, true);
    }

    private void showProgressDialog(){

        //progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
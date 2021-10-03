package com.fit5046.wildsecured;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimalInfoActivity extends AppCompatActivity {

    private final String snakeFilterQuery = "&q=family:Elapidae+Typhlopidae+Pythonidae+Colubridae+Acrochordidae";

    private ActivityAnimalInfoBinding binding;
    private List<WildLifeDataModel> wildLifeListFromApi;
    private WildlifeInfoAdapter wildlifeInfoAdapter;
    private WildlifeViewModel wildlifeViewModel;
    private List<Wildlife> wildlifeListFromDb;
    private List<Wildlife> filteredList;

    private String lon, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimalInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");

        binding.animalAdviceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<String> animalGroupList = new ArrayList<>();
        animalGroupList.add("Reptiles");
        animalGroupList.add("Mammals");
        ArrayAdapter<String> animalGroupAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, animalGroupList);
        binding.animalGroup.setAdapter(animalGroupAdapter);
        binding.animalGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
//                Toast.makeText(AnimalInfoActivity.this, selected, Toast.LENGTH_SHORT).show();
                getAnimalInfo(selected);
                switch (selected){
                    case "Reptiles":
                        getAnimalInfo(selected);
                    case "Mammals":
                        getAnimalInfo(selected);
                }
            }
        });

        initViewModel();
        initRecyclerView();
        performDataInsertion();
    }

    private List<Wildlife> readData() throws IOException {
        List<Wildlife> returnList = new ArrayList<>();

        InputStream is = getResources().openRawResource(R.raw.reptiles);
        InputStreamReader csvStreamReader = new InputStreamReader(is);

        CSVReader reader = new CSVReader(csvStreamReader);
        reader.skip(1);
        String[] record = null;
        String mCommonName, mScientificName, mBriefDescription, mIdentification, mBiology, mRiskToHuman, mDiet, mDangerLevel, mGroup, mImageUrl;


        while((record = reader.readNext()) != null){
            mCommonName = record[0];
            mScientificName = record[1];
            mBriefDescription = record[2];
            mIdentification = record[3];
            mBiology = record[4];
            mRiskToHuman = record[5];
            mDiet = record[6];
            mDangerLevel = record[7];
            mGroup = record[8];
            mImageUrl = record[9];

            Wildlife wildlife = new Wildlife(mCommonName, mScientificName, mBriefDescription, mIdentification,
                    mBiology, mRiskToHuman, mDiet, mDangerLevel, mGroup, mImageUrl);

            returnList.add(wildlife);

            System.out.println("Just created: " + "common name: " + mCommonName + "scientific name: " + mScientificName +
                    "brief description: " + mBriefDescription + "identification: " + mIdentification + "biology: " + mBiology +
                    "risk to human: " + mRiskToHuman + "diet: " + mDiet + "danger level: " + mDangerLevel + "group: " + mGroup +
                    "image url: " + mImageUrl);
        }
        return returnList;
    }

    private void performDataInsertion(){
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                List<Wildlife> existingList =  wildlifeViewModel.getAllWildlifeList();
                if (existingList.isEmpty()){
                    try {
                        wildlifeListFromDb = readData();
                        wildlifeViewModel.insertAll(wildlifeListFromDb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    wildlifeListFromDb = existingList;
                }
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

    private void getAnimalInfo(String animalGroup){

        WildLifeQuery query = RetrofitClient.alaRetrofitClient().create(WildLifeQuery.class);
        String searchUrl = "ws/explore/group/" + animalGroup + "?" + "lat=" + lat + "&" + "lon=" + lon + "&" +"radius=15"
        +  "&pageSize=50&sort=count" + snakeFilterQuery;
        query.getWildLifeData(searchUrl).enqueue(new Callback<ArrayList<WildLifeDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WildLifeDataModel>> call, Response<ArrayList<WildLifeDataModel>> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
                Log.d("TAG", "onResponse: " + res);
                wildLifeListFromApi = response.body();
                getFilteredList(wildLifeListFromApi, wildlifeListFromDb);
                wildlifeInfoAdapter.setWildLifeList(filteredList);
                binding.animalRecyclerView.setAdapter(wildlifeInfoAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<WildLifeDataModel>> call, Throwable t) {

            }
        });
    }

    private void getFilteredList(List<WildLifeDataModel> fromApi, List<Wildlife> fromDb){
        filteredList = Helper.findCommonElements(fromApi, fromDb);
//        System.out.println(filteredList.get(0).getCommonName());
    }

}
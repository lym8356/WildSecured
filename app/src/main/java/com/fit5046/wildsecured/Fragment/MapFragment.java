package com.fit5046.wildsecured.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.fit5046.wildsecured.Activity.PlacesDetailActivity;
import com.fit5046.wildsecured.Adapter.GooglePlaceAdapter;
import com.fit5046.wildsecured.Constant.AllConstant;
import com.fit5046.wildsecured.GoogleModel.SharedPlaceModel;
import com.fit5046.wildsecured.GooglePlaceModel;
import com.fit5046.wildsecured.GoogleModel.GoogleResponseModel;
import com.fit5046.wildsecured.GoogleModel.PlaceModel;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.SavedPlace;
import com.fit5046.wildsecured.Activity.SavedPlacesActivity;
import com.fit5046.wildsecured.Utils.GooglePlaceQuery;
import com.fit5046.wildsecured.Utils.Permissions;
import com.fit5046.wildsecured.Utils.RetrofitClient;
import com.fit5046.wildsecured.Viewmodel.CoordinateViewModel;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.databinding.FragmentMapBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GooglePlaceAdapter.HandleCardClick {

    private FragmentMapBinding binding;
    private GoogleMap mGoogleMap;
    private boolean isLocationPermissionOk;
    private ProgressDialog progressDialog;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Location searchLocation;
    private PlaceModel selectedPlaceModel;
    private List<GooglePlaceModel> googlePlaceModelList;
    private int radius = 5000;
    private GooglePlaceAdapter googlePlaceAdapter;
    private Marker currentMarker;
    private MarkerOptions showMarkerOptions;
    private MarkerOptions searchedMarker;
    private SavedPlaceViewModel savedPlaceViewModel;
    private ArrayList<String> userSavedLocationId;
    private CoordinateViewModel coordinateViewModel;
    private String initialLat, initialLon, initialAddress, initialName;
    

    public MapFragment() {
        // Required empty public constructor
    }

//    public MapFragment(MarkerOptions markerOptions) {
//        this.showMarkerOptions = markerOptions;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getActivity());
        googlePlaceModelList = new ArrayList<>();
        userSavedLocationId = new ArrayList<>();
        coordinateViewModel = ViewModelProviders.of(getActivity()).get(CoordinateViewModel.class);

        binding.zoomInBtn.setOnClickListener(view -> mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn()));

        binding.zoomOutBtn.setOnClickListener(view -> mGoogleMap.animateCamera(CameraUpdateFactory.zoomOut()));

        binding.currentLocationBtn.setOnClickListener(location ->{
            getCurrentLocation();
            moveCameraToLocation(currentLocation, 15);
            if (googlePlaceModelList != null){
                googlePlaceModelList.clear();
                googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                mGoogleMap.clear();
            }
            searchLocation = null;
            binding.mapSearchBar.setText("");
        });

        binding.mapTypeBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.map_type_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.defaultBtn:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.satelliteBtn:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.terrainBtn:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                return true;
            });

            popupMenu.show();
        });

        binding.mapPlacesGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId != -1){
                    PlaceModel placeModel = AllConstant.places.get(checkedId - 1);
                    selectedPlaceModel = placeModel;
                    if (searchLocation != null){
                        getNearByPlaces(placeModel.getPlaceType(), searchLocation, placeModel.getDrawableId());
                    }else{
                        getNearByPlaces(placeModel.getPlaceType(), currentLocation, placeModel.getDrawableId());
                    }

                }
            }
        });

        Places.initialize(getActivity().getApplicationContext(), getResources().getString(R.string.GOOGLE_API_KEY));
        binding.mapSearchBar.setFocusable(false);
        binding.mapSearchBar.setOnClickListener(new View.OnClickListener() {
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

        binding.mapStarredList.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SavedPlacesActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        for (PlaceModel placeModel : AllConstant.places){
            Chip chip = new Chip(requireContext());
            chip.setText(placeModel.getName());
            chip.setId(placeModel.getId());
            chip.setIconStartPadding(10);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.primary_orange, null));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), placeModel.getDrawableId(), null));
            chip.setCheckable(true);
            chip.setChipIconSize(50);
            chip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            chip.setTextSize(14);
            chip.setElevation(10);
            chip.setCheckedIconVisible(false);

            binding.mapPlacesGroup.addView(chip);
        }
        initViewModel();
        initRecyclerView();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        initPlace();
        
        if (Permissions.isLocationOk(requireContext())){

            isLocationPermissionOk = true;
            setUpGoogleMap();
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(requireContext()).setTitle("Location Permission")
                    .setMessage("WildSecured requires location permission to show your location on the map, continue?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocation();
                        }
                    }).create().show();
        }else{
            requestLocation();
            if (showMarkerOptions != null){
                mGoogleMap.clear();
                mGoogleMap.addMarker(showMarkerOptions);
            }
        }
    }

    public void showClickedLocation(MarkerOptions markerOptions){
        Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }
    
    private void requestLocation(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_BACKGROUND_LOCATION}, AllConstant.LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AllConstant.LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionOk = true;
                setUpGoogleMap();
            } else {
                isLocationPermissionOk = false;
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void setUpGoogleMap(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setOnMarkerClickListener(this::onMarkerClick);

        setUpLocationUpdate();

        if (initialAddress != null){
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(initialLat), Double.parseDouble(initialLon))).title(initialAddress).snippet(initialName);
            searchedMarker = markerOptions;
            mGoogleMap.addMarker(markerOptions);
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(initialLat));
            location.setLongitude(Double.parseDouble(initialLon));
            moveCameraToLocation(location, 12);
            searchLocation = location;
        }else{
            getCurrentLocationMoveCamera();
        }


    }

    private void setUpLocationUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000000);
        locationRequest.setFastestInterval(50000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("TAG", "onLocationResult: " + location.getLongitude() + " " + location.getLatitude());
                    }
                }
                super.onLocationResult(locationResult);
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        startLocationUpdates();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(requireContext(), "Location updated started", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
            }
        });
    }

    private void getCurrentLocationMoveCamera() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionOk = false;
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                moveCameraToLocation(location, 18);
            }
        });
    }

    private void moveCameraToLocation(Location location, int zoomLevel) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("TAG", "stopLocationUpdate: Location Update stop");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null){
            stopLocationUpdate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fusedLocationProviderClient != null){
            startLocationUpdates(); }
    }

    private void getNearByPlaces(String typeName, Location locationToSearch, int resourceId){
        if (isLocationPermissionOk){
            if (currentLocation != null){

                progressDialog.show();
                //progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setContentView(R.layout.progress_layout);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                GooglePlaceQuery query = RetrofitClient.placeRetrofitClient().create(GooglePlaceQuery.class);
                String location = locationToSearch.getLatitude() + "," + locationToSearch.getLongitude();
                query.getNearByPlaces(location, String.valueOf(radius), typeName, getResources().getString(R.string.GOOGLE_API_KEY))
                        .enqueue(new Callback<GoogleResponseModel>() {
                            @Override
                            public void onResponse(Call<GoogleResponseModel> call, Response<GoogleResponseModel> response) {
                                Gson gson = new Gson();
                                int resCount = 0;
                                String res = gson.toJson(response.body());
                                Log.d("TAG", "onResponse: " + res);
                                if (response.errorBody() == null) {
                                    if (response.body() != null) {
                                        if (response.body().getGooglePlaceModelList() != null && response.body().getGooglePlaceModelList().size() > 0) {

                                            googlePlaceModelList.clear();
                                            mGoogleMap.clear();
                                            for (int i = 0; i < response.body().getGooglePlaceModelList().size(); i++) {

                                                if (userSavedLocationId.contains(response.body().getGooglePlaceModelList().get(i).getPlaceId())){
                                                    response.body().getGooglePlaceModelList().get(i).setSaved(true);
                                                }
                                                googlePlaceModelList.add(response.body().getGooglePlaceModelList().get(i));
                                                addMarker(response.body().getGooglePlaceModelList().get(i), i, resourceId);
                                                resCount += 1;
                                            }
                                            Toast.makeText(requireContext(), resCount +" results found, zoom out might be required to see the results", Toast.LENGTH_SHORT).show();
                                            googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                                            if (searchedMarker != null){
                                                mGoogleMap.addMarker(searchedMarker);
                                            }

                                        } else if (response.body().getError() != null) {
                                            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mGoogleMap.clear();
                                            googlePlaceModelList.clear();
                                            googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                                            while(radius < 60000){
                                                radius += 20000;
                                                getNearByPlaces(typeName, locationToSearch, resourceId);
                                                Log.d("TAG", "onResponse: " + radius);
                                            }
                                            if (googlePlaceModelList.size() == 0){
                                                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                } else {
                                    Log.d("TAG", "onResponse: " + response.errorBody());
                                    Toast.makeText(requireContext(), "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<GoogleResponseModel> call, Throwable t) {
                                Log.d("TAG", "onFailure: " + t);
                                progressDialog.dismiss();
                            }
                        });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.mapSearchBar.setText(place.getAddress());
            double lat = place.getLatLng().latitude;
            double lon = place.getLatLng().longitude;
            Location location = new Location("");
            location.setLatitude(lat);
            location.setLongitude(lon);
            moveCameraToLocation(location, 12);
            searchLocation = location;
            if (googlePlaceModelList != null){
                googlePlaceModelList.clear();
                googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                mGoogleMap.clear();
            }
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lon)).title(place.getAddress()).snippet(place.getName());
//            markerOptions.icon(getCustomIcon());
            searchedMarker = markerOptions;
            mGoogleMap.addMarker(markerOptions);

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private BitmapDescriptor getCustomIcon(int resourceId) {

//        Drawable background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_push_pin_24);
        Drawable background = ContextCompat.getDrawable(requireContext(), resourceId);
        background.setTint(getResources().getColor(R.color.primary_orange, null));
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void addMarker(GooglePlaceModel googlePlaceModel, int position, int resourceId){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                        googlePlaceModel.getGeometry().getLocation().getLng()))
                .title(googlePlaceModel.getName())
                .snippet(googlePlaceModel.getVicinity());
        markerOptions.icon(getCustomIcon(resourceId));
        mGoogleMap.addMarker(markerOptions).setTag(position);
    }

    private void initRecyclerView(){

        binding.mapPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.mapPlaceRecyclerView.setHasFixedSize(false);
        googlePlaceAdapter = new GooglePlaceAdapter(getActivity(), this, getActivity().getApplication(), this);
        binding.mapPlaceRecyclerView.setAdapter(googlePlaceAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(binding.mapPlaceRecyclerView);

        binding.mapPlaceRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position > -1) {
                    GooglePlaceModel googlePlaceModel = googlePlaceModelList.get(position);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                            googlePlaceModel.getGeometry().getLocation().getLng()), 12));
                }
            }
        });
    }

    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null){
            int markerTag = (int) marker.getTag();
            Log.d("TAG", "onMarkerClick: " + markerTag);

            binding.mapPlaceRecyclerView.scrollToPosition(markerTag);
        }
        return false;
    }

    private void initViewModel(){
        savedPlaceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SavedPlaceViewModel.class);
        savedPlaceViewModel.getSavedPlaceList().observe(requireActivity(), new Observer<List<SavedPlace>>() {
            @Override
            public void onChanged(List<SavedPlace> savedPlaceList) {
                if (savedPlaceList != null && savedPlaceList.size() != 0){
                    userSavedLocationId.clear();
                    for (int i=0; i<savedPlaceList.size(); i++){
                        userSavedLocationId.add(savedPlaceList.get(i).placeId);
                    }
                    for (int j=0; j<googlePlaceModelList.size(); j++){
                        if (userSavedLocationId.contains(googlePlaceModelList.get(j).getPlaceId())){
                            googlePlaceModelList.get(j).setSaved(true);
                            googlePlaceAdapter.notifyDataSetChanged();
                        }else{
                            googlePlaceModelList.get(j).setSaved(false);
                            googlePlaceAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        });
    }

    private void initPlace(){
        coordinateViewModel.sharedPlace.observe(getActivity(), new Observer<SharedPlaceModel>() {
            @Override
            public void onChanged(SharedPlaceModel sharedPlaceModel) {
                initialLat = sharedPlaceModel.getLat();
                initialLon = sharedPlaceModel.getLon();
                initialAddress = sharedPlaceModel.getAddress();
                initialName = sharedPlaceModel.getName();
            }
        });
    }

    @Override
    public void cardClick(GooglePlaceModel googlePlaceModel) {
//        Intent intent = new Intent(getActivity(), PlacesDetailActivity.class);
//        intent.putExtra("place_id", googlePlaceModel.getPlaceId());
//        startActivity(intent);
    }

    @Override
    public void savePlaceClick(GooglePlaceModel googlePlaceModel) {

        if (userSavedLocationId.contains(googlePlaceModel.getPlaceId())){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove Place")
                    .setMessage("Are you sure to remove this place from your starred list?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removePlace(googlePlaceModel);
                            if (android.os.Build.VERSION.SDK_INT >=
                                    android.os.Build.VERSION_CODES.N){
                                CompletableFuture<SavedPlace> placeToGet = savedPlaceViewModel.findByPlaceIdFuture(googlePlaceModel.getPlaceId());
                                placeToGet.thenApply(placeData -> {
                                    if (placeData != null){
                                        savedPlaceViewModel.delete(placeData);
                                    }
                                    return true;
                                });
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }else{
            Double placeRating;
            Integer placeUserRatings = 0;
            if (googlePlaceModel.getRating() == null){
                placeRating = 0.0;
            }else{
                placeRating = googlePlaceModel.getRating();
            }
            if (googlePlaceModel.getUserRatingsTotal() == null){
                placeUserRatings = 0;
            }else{
                placeUserRatings = googlePlaceModel.getUserRatingsTotal();
            }

            SavedPlace savedPlace = new SavedPlace(googlePlaceModel.getName(), googlePlaceModel.getVicinity(), googlePlaceModel.getPlaceId(),
                    placeRating, placeUserRatings, googlePlaceModel.getGeometry().getLocation().getLat(),
                    googlePlaceModel.getGeometry().getLocation().getLng());

            userSavedLocationId.add(googlePlaceModel.getPlaceId());
            int index = googlePlaceModelList.indexOf(googlePlaceModel);
            googlePlaceModelList.get(index).setSaved(true);
            savedPlaceViewModel.insert(savedPlace);
            googlePlaceAdapter.notifyDataSetChanged();
        }
    }

    private void removePlace(GooglePlaceModel googlePlaceModel){
        userSavedLocationId.remove(googlePlaceModel.getPlaceId());
        int index = googlePlaceModelList.indexOf(googlePlaceModel);
        googlePlaceModelList.get(index).setSaved(false);
        googlePlaceAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigationClick(String lat, String lon) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + lat + "," + lon + "&mode=l"));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null){
            startActivity(intent);
        }
    }

}
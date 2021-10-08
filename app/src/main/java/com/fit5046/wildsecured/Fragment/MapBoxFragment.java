package com.fit5046.wildsecured.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMapBoxBinding;
import com.google.android.gms.location.LocationCallback;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

public class MapBoxFragment extends Fragment {

    private FragmentMapBoxBinding binding;
    private boolean isEndNotified;
    private ProgressBar progressBar;
    private MapView mapView;
    private OfflineManager offlineManager;

    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";


    public MapBoxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Mapbox.getInstance(requireActivity(), getString(R.string.MAPBOX_API_KEY));

        // Inflate the layout for this fragment
        binding = FragmentMapBoxBinding.inflate(inflater, container, false);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Set up the OfflineManager
                        offlineManager = OfflineManager.getInstance(requireActivity());

                        // Create a bounding box for the offline region
                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(new LatLng(-34.8859, 150.9736)) // Northeast
                                .include(new LatLng(-38.4793, 140.5956)) // Southwest
                                .build();

                        // Define the offline region
                        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                                style.getUri(),
                                latLngBounds,
                                0,
                                16,
                                requireActivity().getResources().getDisplayMetrics().density);

                        // Set the metadata
                        byte[] metadata;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(JSON_FIELD_REGION_NAME, "Yosemite National Park");
                            String json = jsonObject.toString();
                            metadata = json.getBytes(JSON_CHARSET);
                        } catch (Exception exception) {
                            Log.e("Failed to encode metadata: %s", exception.getMessage());
                            metadata = null;
                        }

                        // Create the region asynchronously
                        if (metadata != null) {
                            offlineManager.createOfflineRegion(
                                    definition,
                                    metadata,
                                    new OfflineManager.CreateOfflineRegionCallback() {
                                        @Override
                                        public void onCreate(OfflineRegion offlineRegion) {
                                            offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                                            // Display the download progress bar
                                            progressBar = binding.progressBar;
                                            startProgress();

                                            // Monitor the download progress using setObserver
                                            offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                                                @Override
                                                public void onStatusChanged(OfflineRegionStatus status) {

                                                    // Calculate the download percentage and update the progress bar
                                                    double percentage = status.getRequiredResourceCount() >= 0
                                                            ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                                            0.0;

                                                    if (status.isComplete()) {
                                                        // Download complete
                                                        endProgress("Download complete");
                                                        Toast.makeText(requireActivity(), "Download complete", Toast.LENGTH_SHORT).show();
                                                    } else if (status.isRequiredResourceCountPrecise()) {
                                                        // Switch to determinate state
                                                        setPercentage((int) Math.round(percentage));
                                                    }
                                                }

                                                @Override
                                                public void onError(OfflineRegionError error) {
                                                    // If an error occurs, print to logcat
                                                    Log.e("onError reason: %s", error.getReason());
                                                    Log.e("onError message: %s", error.getMessage());
                                                }

                                                @Override
                                                public void mapboxTileCountLimitExceeded(long limit) {
                                                    // Notify if offline region exceeds maximum tile count
                                                    Log.e("Mapbox tile count limit exceeded: %s", String.valueOf(limit));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.e("Error: %s", error);
                                        }

                                    });
                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (offlineManager != null) {
            offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
                @Override
                public void onList(OfflineRegion[] offlineRegions) {
                    if (offlineRegions.length > 0) {
                        // delete the last item in the offlineRegions list which will be yosemite offline map
                        offlineRegions[(offlineRegions.length - 1)].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                            @Override
                            public void onDelete() {
                                Toast.makeText(
                                        requireActivity(),
                                        "deleted",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("On delete error: %s", error);
                            }
                        });
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("onListError: %s", error);
                }
            });
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Progress bar methods
    private void startProgress() {

        // Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }

        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

}
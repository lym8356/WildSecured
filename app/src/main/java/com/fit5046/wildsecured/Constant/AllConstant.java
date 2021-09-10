package com.fit5046.wildsecured.Constant;


import com.fit5046.wildsecured.GoogleModel.PlaceModel;
import com.fit5046.wildsecured.R;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstant {

    int LOCATION_REQUEST_CODE = 1000;

    ArrayList<PlaceModel> places = new ArrayList<>(
            Arrays.asList(
                    new PlaceModel(1, R.drawable.ic_campsite_24, "Campsites", "campground"),
                    new PlaceModel(2, R.drawable.ic_caravan_24, "RV Parking", "rv_park"),
                    new PlaceModel(3, R.drawable.ic_tree_24, "Parks", "park"),
                    new PlaceModel(4, R.drawable.ic_baseline_local_hospital_24, "Hospitals", "hospital"),
                    new PlaceModel(5, R.drawable.ic_baseline_camera_24, "Attractions", "tourist_attraction")
            )
    );
}

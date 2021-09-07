package com.fit5046.wildsecured.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Utils.Helper;
import com.fit5046.wildsecured.WeatherModel.ForecastWeatherResponse;


import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private ArrayList<ForecastWeatherResponse> mForecastWeatherResponse;
    private Context mContext;

    public ForecastAdapter(Context context, ArrayList<ForecastWeatherResponse> forecastWeatherResponse){
        mContext = context;
        mForecastWeatherResponse = forecastWeatherResponse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ForecastWeatherResponse currentForecastWeatherResponse = mForecastWeatherResponse.get(position);
        holder.rvWeatherIcon.setImageResource(Helper.getArtResourceForWeatherCondition(currentForecastWeatherResponse.getWeather().get(0).getId()));
        holder.rvWeatherIcon.setColorFilter(Color.parseColor("#FF8047"));
        holder.rvWeatherDesc.setText(currentForecastWeatherResponse.getWeather().get(0).getDescription());
        holder.rvDay.setText(Helper.formatDate(currentForecastWeatherResponse.getDt()));
        holder.rvMinTemp.setText(String.valueOf(Math.round(currentForecastWeatherResponse.getTemp().getMin())));
        holder.rvMaxTemp.setText(String.valueOf(Math.round(currentForecastWeatherResponse.getTemp().getMax())));
        holder.rvMaxUV.setText(String.valueOf(Math.round(currentForecastWeatherResponse.getUvi())));
    }


    @Override
    public int getItemCount() {
        return mForecastWeatherResponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView rvWeatherIcon;
        private TextView rvDay;
        private TextView rvWeatherDesc;
        private TextView rvMinTemp;
        private TextView rvMaxTemp;
        private TextView rvMaxUV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvDay = itemView.findViewById(R.id.forecast_day);
            rvWeatherIcon = itemView.findViewById(R.id.forecast_weather_icon);
            rvWeatherDesc = itemView.findViewById(R.id.forecast_weather_desc);
            rvMinTemp = itemView.findViewById(R.id.forecast_temp_min);
            rvMaxTemp = itemView.findViewById(R.id.forecast_temp_max);
            rvMaxUV = itemView.findViewById(R.id.forecast_max_uv);

        }
    }
}

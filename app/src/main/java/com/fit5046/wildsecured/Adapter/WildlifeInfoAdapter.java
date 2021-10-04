package com.fit5046.wildsecured.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Utils.Helper;
import com.fit5046.wildsecured.WildlifeDetailActivity;
import com.fit5046.wildsecured.databinding.WildlifeItemBinding;
import com.google.gson.Gson;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class WildlifeInfoAdapter extends RecyclerView.Adapter<WildlifeInfoAdapter.ViewHolder> {

    private List<Wildlife> wildLifeList;
    private Context context;

    public WildlifeInfoAdapter(Context mContext){
        this.context = mContext;
    }

    @NonNull
    @Override
    public WildlifeInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WildlifeItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.wildlife_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WildlifeInfoAdapter.ViewHolder holder, int position) {
        if (wildLifeList.get(position).getCommonName() != null && !wildLifeList.get(position).getCommonName().equals("")){
            holder.binding.wildlifeCommonName.setText(wildLifeList.get(position).getCommonName());
        }else{
            holder.binding.wildlifeCommonName.setText("Unknown");
        }

        if (wildLifeList.get(position).getScientificName() != null && !wildLifeList.get(position).getScientificName().equals("")){
            holder.binding.wildlifeName.setText(wildLifeList.get(position).getScientificName());
        }else{
            holder.binding.wildlifeName.setText("Unknown");
        }

        if (wildLifeList.get(position).getImageUrl() != null && !wildLifeList.get(position).getImageUrl().equals("")){
            Glide.with(context)
                    .load(wildLifeList.get(position).getImageUrl())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.image_not_available)
                    .fitCenter()
                    .into(holder.binding.wildlifeThumbnail);
        }else{
            Glide.with(context)
                    .load(R.drawable.image_not_available)
                    .fitCenter()
                    .into(holder.binding.wildlifeThumbnail);
        }


        holder.binding.wildlifeDangerLevel.setText(Helper.getThreatLevelString(wildLifeList.get(position).getDangerLevel()));
        holder.binding.wildlifeDangerLevel.setTextColor(context.getResources().getColor(Helper.getThreatColor(wildLifeList.get(position).getDangerLevel())));

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wildlife clickedItem = wildLifeList.get(position);
                Intent intent = new Intent(context, WildlifeDetailActivity.class);
                Gson gson = new Gson();
                String wildlifeJson = gson.toJson(clickedItem);
                intent.putExtra("wildlifeObject", wildlifeJson);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (wildLifeList != null){
            return wildLifeList.size();
        }else{
            return 0;
        }
    }

    public void setWildLifeList(List<Wildlife> wildLifeList) {
        this.wildLifeList = wildLifeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private WildlifeItemBinding binding;

        public ViewHolder(@NonNull WildlifeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

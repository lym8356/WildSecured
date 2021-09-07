package com.fit5046.wildsecured.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.WeatherModel.Sys;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemLists;
    private CategoryAdapter.HandleCategoryClick itemClickListener;

    public ItemAdapter(Context context, CategoryAdapter.HandleCategoryClick clickListener){
        this.context = context;
        this.itemClickListener = clickListener;
    }

    public void setItemLists(List<Item> itemLists){
        this.itemLists = itemLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        holder.itemName.setText(this.itemLists.get(position).itemName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(itemLists.get(position));
            }
        });
        holder.itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.editItemClick(itemLists.get(position));
            }
        });
        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemClickListener.deleteItemClick(itemLists.get(position));
                        Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });

        if (this.itemLists.get(position).isChecked){
            holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.itemName.setPaintFlags(0);
        }

    }

    @Override
    public int getItemCount() {
        if (itemLists == null || itemLists.size() == 0){
            return 0;
        }else{
            return itemLists.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        ImageView itemEdit;
        ImageView itemDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemEdit = itemView.findViewById(R.id.itemEdit);
            itemDelete = itemView.findViewById(R.id.itemDelete);
        }
    }
}

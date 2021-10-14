package com.fit5046.wildsecured.Adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.Activity.CategoryViewActivity;
import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Viewmodel.ItemViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryLists;
    private HandleCategoryClick clickListener;
    private ItemViewModel itemViewModel;
    private CategoryViewActivity categoryViewActivity;



    public CategoryAdapter(Context context, HandleCategoryClick categoryClickListener, Application application, CategoryViewActivity categoryViewActivity){
        this.context = context;
        this.clickListener = categoryClickListener;
        this.categoryViewActivity = categoryViewActivity;
        itemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(ItemViewModel.class);
    }

    public void setCategoryLists(List<Category> categoryLists) {
        this.categoryLists = categoryLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.categoryTitle.setText(this.categoryLists.get(position).categoryName);

        // set up adapter for inner recyclerview
        ItemAdapter itemAdapter = new ItemAdapter(context, clickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        itemViewModel.getAllItem(categoryLists.get(position).id).observe(categoryViewActivity, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> itemList) {
                if (itemList != null || itemList.size() != 0){
                    itemAdapter.setItemLists(itemList);
                }
            }
        });

        holder.itemRecyclerView.setAdapter(itemAdapter);

        holder.cardTitleSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.titleClick(categoryLists.get(position));
            }
        });
        holder.editCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editCategoryClick(categoryLists.get(position));
            }
        });
        holder.deleteCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
                builder.setMessage("Are you sure you want to delete this category?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickListener.deleteCategoryClick(categoryLists.get(position));
                        Toast.makeText(v.getContext(), "Category Deleted", Toast.LENGTH_SHORT).show();
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
        holder.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog(categoryLists.get(position).id);
            }
        });

        if (this.categoryLists.get(position).isExpanded){
            holder.itemRecyclerView.setVisibility(View.VISIBLE);

        }else{
            holder.itemRecyclerView.setVisibility(View.GONE);
        }

        itemViewModel.getAllCheckedItemCountByCategory(categoryLists.get(position).id).observe(categoryViewActivity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                holder.categoryCheckedCount.setText(String.valueOf(integer));
            }
        });

        itemViewModel.getAllItemCountByCategory(categoryLists.get(position).id).observe(categoryViewActivity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                holder.categoryTotalCount.setText(String.valueOf(integer));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoryLists == null || categoryLists.size() == 0){
            return 0;
        }else{
            return categoryLists.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout cardTitleSection;
        TextView categoryTitle;
        ImageView addItemBtn;
        ImageView editCategoryBtn;
        ImageView deleteCategoryBtn;
        RecyclerView itemRecyclerView;
        TextView categoryCheckedCount;
        TextView categoryTotalCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitleSection = itemView.findViewById(R.id.categoryTitleSection);
            addItemBtn = itemView.findViewById(R.id.categoryItemAdd);
            editCategoryBtn = itemView.findViewById(R.id.categoryEdit);
            deleteCategoryBtn = itemView.findViewById(R.id.categoryDelete);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            itemRecyclerView = itemView.findViewById(R.id.itemRecyclerView);
            categoryCheckedCount = itemView.findViewById(R.id.categoryCheckCount);
            categoryTotalCount = itemView.findViewById(R.id.categoryTotalCount);
        }
    }

    public interface HandleCategoryClick{
        void titleClick(Category category);
        void editCategoryClick(Category category);
        void deleteCategoryClick(Category category);

        void itemClick(Item item);
        void editItemClick(Item item);
        void deleteItemClick(Item item);
    }

    private void showAddItemDialog(int categoryID){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_list_layout);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();

        TextInputLayout itemNameLayout = dialog.findViewById(R.id.addListNameLayout);
        Button addItemCancelBtn = dialog.findViewById(R.id.addListCancelBtn);
        Button addItemCreateBtn = dialog.findViewById(R.id.addListCreateBtn);
        itemNameLayout.getEditText().setHint("Please enter name for new item");


        addItemCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addItemCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameLayout.getEditText().getText().toString();
                if (itemName.isEmpty()){
                    itemNameLayout.setErrorEnabled(true);
                    itemNameLayout.setError("This field is required.");
                    return;
                }
                Item item = new Item(itemName, categoryID, false);
                itemViewModel.insert(item);
                dialog.dismiss();
            }
        });

    }

}

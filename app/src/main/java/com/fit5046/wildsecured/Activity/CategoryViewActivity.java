package com.fit5046.wildsecured.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fit5046.wildsecured.Adapter.CategoryAdapter;
import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Viewmodel.CategoryViewModel;
import com.fit5046.wildsecured.Viewmodel.ItemViewModel;
import com.fit5046.wildsecured.databinding.ActivityCategoryViewBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class CategoryViewActivity extends AppCompatActivity  implements CategoryAdapter.HandleCategoryClick{

    private ActivityCategoryViewBinding binding;
    private int listId;
    private CategoryViewModel categoryViewModel;
    private ItemViewModel itemViewModel;
    private TextView noResultTextView;
    private Category categoryToUpdate;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecyclerView;
    private Item itemToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        noResultTextView = binding.noResultText;
        categoryRecyclerView = binding.categoryRecyclerView;

        if (getIntent() != null){
            listId = getIntent().getIntExtra("list_id", 0);
            String listName = getIntent().getStringExtra("list_name");
            binding.categoryListName.setText(listName);
        }
        binding.categoryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });
        binding.categoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewModel();
        initRecyclerView();

    }

    private void initRecyclerView() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(this, this, getApplication(), this);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void initViewModel(){
        categoryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(CategoryViewModel.class);
        itemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ItemViewModel.class);
        categoryViewModel.getAllCategory(listId).observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                if (categoryList == null || categoryList.size() == 0){
                    noResultTextView.setVisibility(View.VISIBLE);
                    categoryRecyclerView.setVisibility(View.GONE);
                }else{
                    categoryAdapter.setCategoryLists(categoryList);
                    categoryRecyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_list_layout);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();

        TextInputLayout categoryNameLayout = dialog.findViewById(R.id.addListNameLayout);
        Button addCategoryCancelBtn = dialog.findViewById(R.id.addListCancelBtn);
        Button addCategoryCreateBtn = dialog.findViewById(R.id.addListCreateBtn);
        categoryNameLayout.getEditText().setHint("Please enter name for new category");

        if (isForEdit){
            addCategoryCreateBtn.setText(getResources().getString(R.string.update));
            categoryNameLayout.getEditText().setText(categoryToUpdate.categoryName);
        }

        addCategoryCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addCategoryCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameLayout.getEditText().getText().toString();
                if (categoryName.isEmpty()){
                    categoryNameLayout.setErrorEnabled(true);
                    categoryNameLayout.setError("This field is required.");
                    return;
                }
                if (isForEdit){
                    categoryToUpdate.categoryName = categoryName;
                    categoryViewModel.update(categoryToUpdate);
                }else{
                    Category category = new Category(categoryName, listId);
                    categoryViewModel.insert(category);
                }
                dialog.dismiss();
            }
        });
    }

    private void showEditItemDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_list_layout);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();

        TextInputLayout itemNameLayout = dialog.findViewById(R.id.addListNameLayout);
        Button addItemCancelBtn = dialog.findViewById(R.id.addListCancelBtn);
        Button addItemCreateBtn = dialog.findViewById(R.id.addListCreateBtn);
        itemNameLayout.getEditText().setHint("Please enter name for new item");
        addItemCreateBtn.setText(getResources().getString(R.string.update));
        itemNameLayout.getEditText().setText(itemToUpdate.itemName);

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
                itemToUpdate.itemName = itemName;
                itemViewModel.update(itemToUpdate);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void titleClick(Category category) {
        if (category.isExpanded){
            category.isExpanded = false;
        }else{
            category.isExpanded = true;
        }
        categoryViewModel.update(category);
    }

    @Override
    public void editCategoryClick(Category category) {
        this.categoryToUpdate = category;
        showAddCategoryDialog(true);
    }

    @Override
    public void deleteCategoryClick(Category category) {
        categoryViewModel.delete(category);
    }

    @Override
    public void itemClick(Item item) {

        item.isChecked = !item.isChecked;
        itemViewModel.update(item);
    }

    @Override
    public void editItemClick(Item item) {
        this.itemToUpdate = item;
        showEditItemDialog();
    }

    @Override
    public void deleteItemClick(Item item) {
        itemViewModel.delete(item);
    }
}
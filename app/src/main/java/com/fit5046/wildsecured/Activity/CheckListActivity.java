package com.fit5046.wildsecured.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fit5046.wildsecured.Adapter.UserListAdapter;
import com.fit5046.wildsecured.Entity.UserList;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Viewmodel.UserListViewModel;
import com.fit5046.wildsecured.databinding.ActivityCheckListBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class CheckListActivity extends AppCompatActivity implements UserListAdapter.HandleListClick{

    private ActivityCheckListBinding binding;
    private UserListViewModel userListViewModel;
    private UserList listToUpdate;
    private TextView noResultTextView;
    private RecyclerView userListRecyclerView;
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userListRecyclerView = binding.checkListRecyclerView;
        noResultTextView = binding.noResultText;

        binding.checkListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.checklistAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddListDialog(false);
            }
        });

        initViewModel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userListRecyclerView.setHasFixedSize(true);
        userListAdapter = new UserListAdapter(this, this, getApplication(), this);
        userListRecyclerView.setAdapter(userListAdapter);
    }

    private void initViewModel() {
        userListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserListViewModel.class);
        userListViewModel.getUserLists().observe(this, new Observer<List<UserList>>() {
            @Override
            public void onChanged(List<UserList> userLists) {
                if (userLists == null || userLists.size() == 0){
                    noResultTextView.setVisibility(View.VISIBLE);
                    userListRecyclerView.setVisibility(View.GONE);
                }else{
                    userListAdapter.setUserLists(userLists);
                    userListRecyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAddListDialog(boolean isForEdit){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_list_layout);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();

        TextInputLayout listNameLayout = dialog.findViewById(R.id.addListNameLayout);
        Button addListCancelBtn = dialog.findViewById(R.id.addListCancelBtn);
        Button addListCreateBtn = dialog.findViewById(R.id.addListCreateBtn);
        listNameLayout.getEditText().setHint("Please enter name for new list");

        if (isForEdit){
            addListCreateBtn.setText(getResources().getString(R.string.update));
            listNameLayout.getEditText().setText(listToUpdate.listName);
        }

        addListCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addListCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = listNameLayout.getEditText().getText().toString();
                if (listName.isEmpty()){
                    listNameLayout.setErrorEnabled(true);
                    listNameLayout.setError("This field is required.");
                    return;
                }
                if (isForEdit){
                    listToUpdate.listName = listName;
                    userListViewModel.update(listToUpdate);
                }else{
                    UserList userList = new UserList(listName);
                    userListViewModel.insert(userList);
                }
                dialog.dismiss();
            }
        });

    }

    @Override
    public void listClick(UserList userList) {
        Intent intent = new Intent(CheckListActivity.this, CategoryViewActivity.class);
        intent.putExtra("list_id", userList.id);
        intent.putExtra("list_name", userList.listName);

        startActivity(intent);
    }

    @Override
    public void editListClick(UserList userList) {
        this.listToUpdate = userList;
        showAddListDialog(true);
    }

    @Override
    public void deleteListClick(UserList userList) {
        userListViewModel.delete(userList);
    }
}
package com.fit5046.wildsecured.Adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.wildsecured.CheckListActivity;
import com.fit5046.wildsecured.Entity.UserList;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Viewmodel.ItemViewModel;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {


    private Context context;
    private List<UserList> userLists;
    private HandleListClick listClickListener;
    private ItemViewModel itemViewModel;
    private CheckListActivity checkListActivity;

    public UserListAdapter(Context context, HandleListClick listClickListener, Application application, CheckListActivity checkListActivity){
        this.context = context;
        this.listClickListener = listClickListener;
        this.checkListActivity = checkListActivity;
        itemViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(ItemViewModel.class);
    }

    public void setUserLists(List<UserList> userLists){
        this.userLists = userLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checklist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.listName.setText(this.userLists.get(position).listName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.listClick(userLists.get(position));
            }
        });

        holder.editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.editListClick(userLists.get(position));
            }
        });

        holder.deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
                builder.setMessage("Are you sure you want to delete this list?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listClickListener.deleteListClick(userLists.get(position));
                        Toast.makeText(v.getContext(), "List Deleted", Toast.LENGTH_SHORT).show();
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

        itemViewModel.getAllCheckedItemCountByList(userLists.get(position).id).observe(checkListActivity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                holder.listItemChecked.setText(String.valueOf(integer));
            }
        });

        itemViewModel.getAllItemCountByList(userLists.get(position).id).observe(checkListActivity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                holder.listItemTotal.setText(String.valueOf(integer));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userLists == null || userLists.size() == 0){
            return 0;
        }else{
            return userLists.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView listName;
        ImageView editList;
        ImageView deleteList;
        TextView listItemChecked;
        TextView listItemTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_title);
            editList = itemView.findViewById(R.id.list_edit);
            deleteList = itemView.findViewById(R.id.list_delete);
            listItemChecked = itemView.findViewById(R.id.listCompletedItems);
            listItemTotal = itemView.findViewById(R.id.listTotalItems);
        }
    }

    public interface HandleListClick{
        void listClick(UserList userList);
        void editListClick(UserList userList);
        void deleteListClick(UserList userList);
    }
}

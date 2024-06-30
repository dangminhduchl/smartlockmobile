package com.example.smartlockjava.ui.userManagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlockjava.R;
import com.example.smartlockjava.dto.UpdateUserRequest;
import com.example.smartlockjava.dto.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<Person> users;
    private final String authHeader;
    private final Context context;
    private final UserManagementViewModel viewModel = new UserManagementViewModel();

    public UserAdapter(Context context, List<Person> users, String authToken) {
        this.context = context;
        this.users = users;
        this.authHeader = authToken;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Person user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.id.setText(String.valueOf(user.getId()));
        holder.admind.setChecked(user.isSuperuser());
        holder.activate.setChecked(user.isActive());

        holder.saveButton.setVisibility(View.GONE);

        holder.admind.setOnCheckedChangeListener((buttonView, isChecked) -> updateSaveButtonVisibility(holder, user));
        holder.activate.setOnCheckedChangeListener((buttonView, isChecked) -> updateSaveButtonVisibility(holder, user));

        holder.saveButton.setOnClickListener(v -> updateUser(holder, user));
    }

    private void updateSaveButtonVisibility(UserViewHolder holder, Person user) {
        if (holder.activate.isChecked() != user.isActive() || holder.admind.isChecked() != user.isSuperuser()) {
            holder.saveButton.setVisibility(View.VISIBLE);
        } else {
            holder.saveButton.setVisibility(View.GONE);
        }
    }

    private void updateUser(UserViewHolder holder, Person user) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        user.setActive(holder.activate.isChecked());
        user.setSuperuser(holder.admind.isChecked());

        viewModel.updateUser(authHeader, user.getId(), new UpdateUserRequest(user.isActive(), user.isSuperuser()), new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Person updatedUser = response.body();
                    holder.activate.setChecked(updatedUser.isActive());
                    holder.admind.setChecked(updatedUser.isSuperuser());
                    holder.saveButton.setVisibility(View.GONE);
                    Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView username;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch activate;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch admind;
        Button saveButton;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            username = itemView.findViewById(R.id.username);
            saveButton = itemView.findViewById(R.id.saveButton);
            admind = itemView.findViewById(R.id.isAdmin);
            activate = itemView.findViewById(R.id.activate);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Person user);
    }
}
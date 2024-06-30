package com.example.smartlockjava.ui.historyManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlockjava.dto.Result;
import com.example.smartlockjava.dto.LockRequest;
import com.example.smartlockjava.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<Result> historyList;

    public HistoryAdapter(List<Result> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result history = historyList.get(position);
        LockRequest request = history.getRequest();

        holder.tvUserName.setText(history.getUser_name());
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            Date date = originalFormat.parse(history.getCreated_at());

            // Add 7 hours to the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 7);
            date = calendar.getTime();

            // Format the date to the desired format
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
            String formattedDate = newFormat.format(date);

            holder.tvCreatedAt.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvUser.setText(String.valueOf(history.getUser()));
        holder.tvLock.setText(request.isLock() ? "Locked" : "Unlocked");
        holder.tvDoor.setText(request.isDoor() ? "Door Closed" : "Door Opend");
//        holder.tvUpdateAt.setText(request.getUpdate_at());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvCreatedAt, tvUser, tvLock, tvDoor, tvUpdateAt;

        public ViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvCreatedAt = view.findViewById(R.id.tv_created_at);
            tvUser = view.findViewById(R.id.tv_user);
            tvLock = view.findViewById(R.id.tv_lock);
            tvDoor = view.findViewById(R.id.tv_door);
        }
    }
}
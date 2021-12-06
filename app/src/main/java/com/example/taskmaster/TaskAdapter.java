package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


    List<Task> allTask = new ArrayList<com.amplifyframework.datastore.generated.model.Task>();


    public TaskAdapter(List<Task> allTask, MainActivity mainActivity) {
       this.allTask = allTask;
    }

    public TaskAdapter(List<Task> allTask) {
        this.allTask = allTask;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public Task task;
        public View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank,parent,false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = allTask.get(position);

        TextView name = holder.itemView.findViewById(R.id.name);
        TextView body = holder.itemView.findViewById(R.id.body);
        TextView state = holder.itemView.findViewById(R.id.state);
        name.setText(holder.task.getTitle());
        body.setText(holder.task.getBody());
        state.setText(holder.task.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("tasks", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("title", holder.task.getTitle());
                editor.putString("body", holder.task.getBody());
                editor.putString("state", holder.task.getState());
                editor.putString("file",holder.task.getFile());
                editor.apply();
                Intent intent = new Intent(view.getContext(), TaskDetail.class);
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return allTask.size();
    }




}

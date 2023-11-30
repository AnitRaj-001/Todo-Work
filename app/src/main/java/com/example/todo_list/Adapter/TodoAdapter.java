package com.example.todo_list.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.AddNewTask;
import com.example.todo_list.R;
import com.example.todo_list.Utils.DataBaseHandler;
import com.example.todo_list.dashboard;
import com.example.todo_list.ui.todo.TodoModel;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private static List<TodoModel> todoList;
    private dashboard dash;
    private DataBaseHandler db;

    public TodoAdapter(DataBaseHandler db,dashboard dash) {
        this.dash = dash;
        this.db = db;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        db.openDatabase();
        TodoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(),1);
                }else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }
    public Context getContext() {
        return dash;
    }
    @SuppressLint("NotifyDataSetChanged")
    public static void setTasks(List<TodoModel> todoList) {
        TodoAdapter.todoList = todoList;
        //notifyDataSetChanged();
    }
   public void editItem(int Position){
        TodoModel item = todoList.get(Position);
       Bundle bundle = new Bundle();
       bundle.putInt("id",item.getId());
       bundle.putString("task",item.getTask());
       AddNewTask fragment = new AddNewTask();
       fragment.setArguments(bundle);
       fragment.show(dash.getSupportFragmentManager(), AddNewTask.TAG);
   }
   public  void deleteItem(int position){
        TodoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
   }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.check);

        }
    }
}

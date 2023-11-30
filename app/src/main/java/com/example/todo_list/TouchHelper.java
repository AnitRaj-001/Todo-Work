package com.example.todo_list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.Adapter.TodoAdapter;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private final TodoAdapter adapter;
    public  TouchHelper(TodoAdapter adapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter= adapter;
    }
    @Override
    public  boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
        return false;
    }
    @Override
    public  void  onSwiped(final RecyclerView.ViewHolder viewHolder , int direction){
        final  int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder =new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task");
            builder.setPositiveButton("confirm",
                    (dialog, which) -> adapter.deleteItem(position));builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
        }
        else {
            adapter.editItem(position);
        }
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onChildDraw(Canvas c , RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, float dx , float dy,int actionState,boolean isCurrentActivity){
        super.onChildDraw(c,recyclerView, viewHolder,dx,dy,actionState,isCurrentActivity);
        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dx>0){
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), com.google.android.material.R.color.design_default_color_primary_dark));
        }else {
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_delete_24);
            background = new ColorDrawable(R.color.delete);
        }
        assert icon != null;
        int iconMargin =(itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dx>0){ //swiping to the right
           int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int)dx)+backgroundCornerOffset, itemView.getBottom());
        } else if (dx<0) { //swiping to the left
            int iconLeft = itemView.getRight() - iconMargin -icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getRight()+((int)dx)-backgroundCornerOffset,itemView.getTop(),itemView.getRight(), itemView.getBottom());
            
        }else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }
}

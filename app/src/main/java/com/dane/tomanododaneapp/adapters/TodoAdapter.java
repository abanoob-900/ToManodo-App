package com.dane.tomanododaneapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dane.tomanododaneapp.R;
import com.dane.tomanododaneapp.data.ToDo;
import com.dane.tomanododaneapp.data.ToDoItemDatabase;
import com.dane.tomanododaneapp.helper.ItemTouchHelperAdapter;
import com.dane.tomanododaneapp.helper.UpdateItem;

import java.util.ArrayList;

/**
 * Created by Harshit on 05/12/16
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<ToDo> arrayList = new ArrayList<>();
    private UpdateItem updateItem;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_todo, parent, false);
        return new ViewHolder(view);
    }

    public TodoAdapter(ArrayList<ToDo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        updateItem = (UpdateItem) context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.todoTitle.setText(arrayList.get(position).getTitle());
        holder.todoContent.setText(arrayList.get(position).getContent());
        holder.dueDate.setText(arrayList.get(position).getDueDate());
        holder.dueDate.setText(arrayList.get(position).getDueDate());
        switch (arrayList.get(position).getPriority()) {
            case 0:
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.priorityLow));
                break;
            case 1:
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.priorityMedium));
                break;
            case 2:
                holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.priorityHigh));
                break;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem.displayItem(arrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onDismiss(final int position) {
        ToDoItemDatabase.getToDoItemDatabase(context).deleteToDoFromDatabase(arrayList.get(position));
        ToDo removed = arrayList.get(position);
        updateItem.displayUndoSnackbar(position, removed);
        arrayList.remove(position);
        notifyItemRemoved(position);
        updateItem.itemDeleted(arrayList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView dueDate;
        TextView todoTitle;
        TextView todoContent;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.single_item_card);
            todoContent = (TextView) itemView.findViewById(R.id.todo_content);
            todoTitle = (TextView) itemView.findViewById(R.id.todo_title);
            dueDate = (TextView) itemView.findViewById(R.id.due_date);
            cardView.setRadius(4);
        }
    }
}

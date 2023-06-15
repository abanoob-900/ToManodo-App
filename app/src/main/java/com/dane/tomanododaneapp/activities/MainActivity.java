package com.dane.tomanododaneapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dane.tomanododaneapp.R;
import com.dane.tomanododaneapp.adapters.TodoAdapter;
import com.dane.tomanododaneapp.data.ToDo;
import com.dane.tomanododaneapp.data.ToDoItemDatabase;
import com.dane.tomanododaneapp.fragments.AddTodoDialogFragment;
import com.dane.tomanododaneapp.helper.ItemOffsetDecoration;
import com.dane.tomanododaneapp.helper.SimpleTouchHelperCallback;
import com.dane.tomanododaneapp.helper.UpdateItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements UpdateItem {

    RecyclerView recyclerView;
    TodoAdapter toDoAdapter;
    ArrayList<ToDo> toDoArrayList;
    ToDoItemDatabase toDoItemDatabase;
    ImageView noToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toDoArrayList = new ArrayList<>();
        toDoItemDatabase = ToDoItemDatabase.getToDoItemDatabase(this);
        toDoArrayList = toDoItemDatabase.getToDoFromDatabase();
        Collections.reverse(toDoArrayList);
        recyclerView = (RecyclerView) findViewById(R.id.contentRV);
        noToDo = (ImageView) findViewById(R.id.noToDo);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        toDoAdapter = new TodoAdapter(toDoArrayList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toDoAdapter);
        ItemTouchHelper.Callback callback= new SimpleTouchHelperCallback(toDoAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AddTodoDialogFragment.getInstance(null, null).show(getSupportFragmentManager(), "addNote");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForEmptyList(toDoArrayList);
    }

    @Override
    public void updateItem(ToDo toDo, Integer index) {
        if (index != null) {
            Log.d("UPDATE","Index isn't null");
                toDoArrayList.set(index, toDo);
                toDoItemDatabase.updateToDoItem(toDo);
                toDoAdapter.notifyItemChanged(index);
        } else {
            Log.d("UPDATE","Index is null");
            toDoArrayList.add(0, toDo);
            toDoItemDatabase.addToDoItem(toDo);
            toDoAdapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }
        checkForEmptyList(toDoArrayList);
    }

    @Override
    public void displayItem(ToDo toDo, int index) {
        AddTodoDialogFragment.getInstance(toDo, index)
                .show(getSupportFragmentManager(), "addNote");
    }

    @Override
    public void itemDeleted(ArrayList<ToDo> toDos) {
        checkForEmptyList(toDos);
    }

    @Override
    public void displayUndoSnackbar(final int position, final ToDo removed) {
        Snackbar.make(findViewById(R.id.fab), "ToDo Deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToDoItemDatabase.getToDoItemDatabase(MainActivity.this).addToDoItem(removed);
                        toDoArrayList.add(position,removed);
                        updateItem(removed,position);
                        toDoAdapter.notifyItemInserted(position);
                    }
                }).show();
    }

    public void checkForEmptyList(ArrayList<ToDo> toDos){
        if (toDos.size() == 0){
            noToDo.setVisibility(View.VISIBLE);
        }else {
            noToDo.setVisibility(View.GONE);
        }
    }
}

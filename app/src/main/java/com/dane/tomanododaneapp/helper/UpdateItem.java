package com.dane.tomanododaneapp.helper;

import androidx.annotation.Nullable;

import com.dane.tomanododaneapp.data.ToDo;

import java.util.ArrayList;

/**
 * Created by Harshit on 21/01/17
 */

public interface UpdateItem {

    void updateItem(ToDo toDo, @Nullable Integer index);

    void displayItem(ToDo toDo, int index);

    void itemDeleted(ArrayList<ToDo> toDos);

    void displayUndoSnackbar(int position, ToDo removed);
}

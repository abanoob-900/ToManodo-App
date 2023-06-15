package com.dane.tomanododaneapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dane.tomanododaneapp.R;
import com.dane.tomanododaneapp.data.ToDo;
import com.dane.tomanododaneapp.helper.UpdateItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

/**
 * Created by Harshit on 21/01/17
 */

public class AddTodoDialogFragment extends DialogFragment implements DatePicker.OnDateChangedListener {

    Spinner prioritySpinner;
    EditText content;
    EditText title;
    String date;
    View dialogView;
    DatePicker datePicker;
    static ToDo currentToDo;
    UpdateItem updateItem;
    String[] priorities = {"Low", "Medium", "High"};
    static Integer currentIndex;
    Calendar calendar;
    static long todoCreationTime = 1;

    public static AddTodoDialogFragment getInstance(@Nullable ToDo toDo, @Nullable Integer index) {
        currentToDo = toDo;
        currentIndex = index;
        return new AddTodoDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateItem = (UpdateItem) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, priorities);
        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_todo, null);
        title = (EditText) dialogView.findViewById(R.id.todo_title_editText);
        content = (EditText) dialogView.findViewById(R.id.todo_content_edittext);
        datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        prioritySpinner = (Spinner) dialogView.findViewById(R.id.priority_spinners);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        date = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "-" + (month + 1) + "-" + year;

            }
        });
        prioritySpinner.setAdapter(priorityAdapter);
        if (currentToDo != null) {
            title.setText(currentToDo.getTitle(), TextView.BufferType.EDITABLE);
            content.setText(currentToDo.getContent(), TextView.BufferType.EDITABLE);
            prioritySpinner.setSelection(currentToDo.getPriority());
            todoCreationTime = currentToDo.getTimeOfAddition();
        }
        return new AlertDialog.Builder(getContext())
                .setTitle("Add a new ToDo")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentIndex == null)
                            todoCreationTime = calendar.getTimeInMillis();
                        if (!title.getText().toString().equals("")) {
                            Log.d("CreationTime", String.valueOf(todoCreationTime));
                            ToDo toDo = new ToDo(title.getText().toString(),
                                    content.getText().toString(), date,
                                    prioritySpinner.getSelectedItemPosition(), todoCreationTime);
                            updateItem.updateItem(toDo, currentIndex);
                        } else
                            Snackbar.make(getActivity().findViewById(R.id.fab), "Title Should not be empty", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setView(dialogView)
                .create();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}

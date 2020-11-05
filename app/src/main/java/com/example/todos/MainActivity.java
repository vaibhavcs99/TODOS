package com.example.todos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<ToDo> toDoList;
    DbAdapter dbAdapter;
    Button insert;
    Button delete;
    Button modify;
    EditText enterTodo;
    EditText deleteTodo;
    EditText modifyTodo;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = DbAdapter.getDbAdapterInstance(this);


        tv1 = findViewById(R.id.textView);
        insert = findViewById(R.id.insert);
        delete = findViewById(R.id.delete);
        modify = findViewById(R.id.modify);
        enterTodo = findViewById(R.id.enterTodo);
        deleteTodo = findViewById(R.id.deleteTodo);
        modifyTodo = findViewById(R.id.modifyTodo);

        insert.setOnClickListener(this);
        delete.setOnClickListener(this);
        modify.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNewList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                addNewToDo();
                break;
            case R.id.delete:
                deleteToDo();
                break;
            case R.id.modify:
                modifyToDo();
                break;
        }
    }

    private void modifyToDo() {
        int id = Integer.parseInt(deleteTodo.getText().toString());
        String temp = modifyTodo.getText().toString();
        dbAdapter.modify(id, temp);
        setNewList();
    }

    private void deleteToDo() {
        dbAdapter.delete(Integer.parseInt(deleteTodo.getText().toString()));
        setNewList();
    }

    private void addNewToDo() {
        dbAdapter.insert(enterTodo.getText().toString());
        setNewList();
//        Toast.makeText(this, "added new todo", Toast.LENGTH_SHORT).show();
    }

    private void setNewList() {
        tv1.setText(getToDoListString());
        Toast.makeText(this, "Executed", Toast.LENGTH_SHORT).show();
    }

    private String getToDoListString() {
        toDoList = dbAdapter.getAllToDos();
        if (toDoList != null && toDoList.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (ToDo toDo : toDoList) {
                stringBuilder.append(toDo.getId() + "," + toDo.getToDo() + "\n");
            }
            return stringBuilder.toString();
        } else {
            return "No TODOs items";
        }
    }


}
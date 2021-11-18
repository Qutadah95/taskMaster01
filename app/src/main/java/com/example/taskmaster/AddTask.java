package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button homeButton = findViewById(R.id.goBackBtn);
        Button addTaskButton = findViewById(R.id.homeAddTask);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHome);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = findViewById(R.id.editName);
                String getTitle = title.getText().toString();
                EditText body = findViewById(R.id.editBody);
                String getBody = body.getText().toString();
                EditText state = findViewById(R.id.editState);
                String getState = state.getText().toString();
                Toast.makeText(getApplicationContext(),  "submitted!", Toast.LENGTH_SHORT).show();
                Task task = new Task(getTitle,getBody,getState);
                AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());
                appDb.taskDao().insertAll(task);
            }
        });
    }


}
package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.changableTitle);
        TextView body = findViewById(R.id.bodyText);
        TextView state = findViewById(R.id.stateText);
        String strTitle = getIntent().getExtras().get("title").toString();
        String strbody = getIntent().getExtras().get("body").toString();
        String strState = getIntent().getExtras().get("state").toString();
        title.setText(strTitle);
        body.setText(strbody);
        state.setText(strState);
    }
}
package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allTaskButton = findViewById(R.id.allTask);
        Button addTaskButton = findViewById(R.id.addTask);

        allTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllTask = new Intent(MainActivity.this,AllTasks.class);
                startActivity(showAllTask);
            }
        });
addTaskButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent showAddTask = new Intent(MainActivity.this,AddTask.class);
        startActivity(showAddTask);
    }
});

        Button Button1 = (Button) findViewById(R.id.Button1);
        Button Button2=(Button) findViewById(R.id.Button2);
        Button Button3 =(Button) findViewById(R.id.Button3);
        Button settingButton=(Button) findViewById(R.id.settingButton);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String button1 = Button1.getText().toString();
                Intent goToDetailPage = new Intent(MainActivity.this,TaskDetail.class);
                goToDetailPage.putExtra("task detail",button1);
                startActivity(goToDetailPage);

            }

        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String button2 = Button2.getText().toString();
                Intent goToDetailPage = new Intent(MainActivity.this,TaskDetail.class);
                goToDetailPage.putExtra("task detail",button2);
                startActivity(goToDetailPage);

            }
        });
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String button3 = Button3.getText().toString();
                Intent goToDetailPage = new Intent(MainActivity.this,TaskDetail.class);
                goToDetailPage.putExtra("task detail",button3);
                startActivity(goToDetailPage);

            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSettingPage = new Intent(MainActivity.this,SettingsPage.class);
                startActivity(goToSettingPage);

            }

        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName","the user didn't add a name yet!");
        Toast.makeText(this, userName,Toast.LENGTH_LONG).show();
        TextView textView=findViewById(R.id.textView);
        textView.setText(userName);
    }
}
package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);







        Amplify.Storage.downloadFile(
                "fileKey",
                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
                result -> {
                    ImageView image = findViewById(R.id.imageView2);
                    image.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());},
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

        Button homeButton = findViewById(R.id.button2);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome =new Intent(TaskDetail.this,MainActivity.class);
                startActivity(goHome);
            }
        });

    }
    }

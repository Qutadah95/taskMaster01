package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;

import kotlin.Suppress;


public class AddTask extends AppCompatActivity {
    String fileName="";
    Location location;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button homeButton = findViewById(R.id.goBackBtn);
        Button addTaskButton = findViewById(R.id.homeAddTask);
        Button uploadButton = findViewById(R.id.upload);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        this.location = location;
                        Toast.makeText(this, "Location was added", Toast.LENGTH_LONG).show();
                    }
                });
        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              getFile();

                }

        });

        Spinner s = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHome);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            private Location location;

            @Override
            public void onClick(View v) {
                EditText title = findViewById(R.id.editName);
                String getTitle = title.getText().toString();
                EditText body = findViewById(R.id.editBody);
                String getBody = body.getText().toString();
                EditText state = findViewById(R.id.editState);
                String getState = state.getText().toString();
//
                String team =  s.getSelectedItem().toString();


                Intent intent = getIntent();
//                Bundle bundle = intent.getExtras();
//                Uri data = (Uri)bundle.get(Intent.EXTRA_STREAM);
                if (intent.getType() != null) {
//                    System.out.println(data);
                    System.out.println("fileName0");
                    System.out.println(fileName);
                    Amplify.DataStore.query(
                            Team.class,Team.NAME.contains(team),
                            items -> {
                                while (items.hasNext()) {
                                    Team item = items.next();
                                    Task item1 = Task.builder().title(getTitle).body(getBody).state(getState).file(fileName).location(this.location.getLatitude()+","+this.location.getLongitude()).teamId(item.getId()).build();
                                    Amplify.DataStore.save(
                                            item1,
                                            success -> Log.i("COMO", "Saved item: "),
                                            error -> Log.e("COMO", "Could not save item to DataStore", error)
                                    );
                                    Log.i("COMO", "Id was stored " );
                                    Log.i("COMO", "Id " + item.getId());
                                }
                            },
                            failure -> Log.e("COMO", "Could not query DataStore", failure)
                    );

                    Toast.makeText(getApplicationContext(),  "submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

    }
    public void getFile(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent=Intent.createChooser(intent,"get file");
        startActivityForResult(intent,1234);


}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            assert data != null;
            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());

            fileName = data.getData().getPath().toString();


            Amplify.Storage.uploadInputStream(
                    data.getData().getPath().toString(),
                    exampleInputStream,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        }  catch (FileNotFoundException error) {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }
}
package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthSession;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity   {
    List<Task> tasks = new ArrayList<>();
    TaskAdapter adapter = new TaskAdapter(tasks);
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }
        Amplify.DataStore.observe(Task.class,
                started -> Log.i("Tutorial", "Observation began."),
                change -> Log.i("Tutorial", change.item().toString()),
                failure -> Log.e("Tutorial", "Observation failed.", failure),
                () -> Log.i("Tutorial", "Observation complete.")
        );



//        RecyclerView recyclerView = findViewById(R.id.dddd);

//        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//
//            @Override
//            public boolean handleMessage(@NonNull Message message) {
//                recyclerView.getAdapter().notifyDataSetChanged();
//                return false;
//            }
//        });

//        List allTask = new ArrayList();
//        Amplify.DataStore.query(
//                Task.class,
//                items -> {
//                    while (items.hasNext()) {
//                        Task item = items.next();
//                        allTask.add(item);
//                        Log.i("Amplify", "Id " + item.getId());
//                    }
//
//                },
//                failure -> Log.e("Amplify", "Could not query DataStore", failure)
//        );
        Amplify.Auth.signInWithWebUI(
                MainActivity.this,
                result -> Log.i("AuthQuickStart", result.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );
        Button allTaskButton = findViewById(R.id.allTask);
        Button addTaskButton = findViewById(R.id.addTask);
        Button logOutButton = findViewById(R.id.logout);
        Button logInButton = findViewById(R.id.login);
        Button DetailButton = findViewById(R.id.Detail);
        DetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDetail = new Intent(MainActivity.this,TaskDetail.class);
                startActivity(showDetail);

            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signInWithWebUI(
                        MainActivity.this,
                        result -> Log.i("AuthQuickStart", result.toString()),
                        error -> Log.e("AuthQuickStart", error.toString())
                );

            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
//                finish();
//                startActivity(getIntent());

            }
        });


        System.out.println("user");

        System.out.println(Amplify.Auth.getCurrentUser());
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


        Button settingButton=(Button) findViewById(R.id.settingButton);


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSettingPage = new Intent(MainActivity.this,SettingsPage.class);
                startActivity(goToSettingPage);

            }

        });
        RecyclerView recyclerView = findViewById(R.id.dddd);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TaskAdapter(allTask));

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName","the user didn't add a name yet!");
        Toast.makeText(this, userName,Toast.LENGTH_LONG).show();
        TextView textView=findViewById(R.id.textView);
        textView.setText(userName);
        String team = sharedPreferences.getString("team","team");

        Amplify.DataStore.query(
                Team.class,Team.NAME.contains(team),
                items -> {
                    while (items.hasNext()) {
                        Team item = items.next();

                        Amplify.DataStore.query(
                                Task.class,Task.TEAM_ID.eq( item.getId()),
                                itemss -> {
                                    tasks.clear();
                                    while (itemss.hasNext()) {
                                        Task item1 = itemss.next();
                                        tasks.add(item1);
                                        Log.i("DUCK", "list " + item1.getTeamId());

                                    }
                                    handler.post(runnable);
                                },
                                failure -> Log.e("Amplify", "Could not query DataStore", failure)
                        );
                        Log.i("Amplify", "Id " + item.getId());
                    }
                    handler.post(runnable);
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
    }

}

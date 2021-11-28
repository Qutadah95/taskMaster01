package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;


public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button homeButton = findViewById(R.id.goBackBtn);
        Button addTaskButton = findViewById(R.id.homeAddTask);
//        Team item = Team.builder()
//                .name("Team1")
//                .build();
//        Amplify.DataStore.save(
//                item,
//                success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
//                error -> Log.e("Amplify", "Could not save item to DataStore", error)
//        );
//        Team item2 = Team.builder()
//                .name("Team2")
//                .build();
//        Amplify.DataStore.save(
//                item2,
//                success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
//                error -> Log.e("Amplify", "Could not save item to DataStore", error)
//        );
//        Team item3 = Team.builder()
//                .name("Team3")
//                .build();
//        Amplify.DataStore.save(
//                item3,
//                success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
//                error -> Log.e("Amplify", "Could not save item to DataStore", error)
//        );
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
            @Override
            public void onClick(View v) {
                EditText title = findViewById(R.id.editName);
                String getTitle = title.getText().toString();
                EditText body = findViewById(R.id.editBody);
                String getBody = body.getText().toString();
                EditText state = findViewById(R.id.editState);
                String getState = state.getText().toString();
//               Task t= Task.builder().title(getTitle).body(getBody).state(getState).build();
//               Amplify.DataStore.save(
//                       t,
//                       response -> Log.i("TaskMaster", "Added Task with id: " + t.getId()),
//                       error -> Log.e("TaskMaster", "Create failed", error));
                String team =  s.getSelectedItem().toString();

                Amplify.DataStore.query(
                        Team.class,Team.NAME.contains(team),
                        items -> {
                            while (items.hasNext()) {
                                Team item = items.next();
                                Task item1 = Task.builder().title(getTitle).body(getBody).state(getState).teamId(item.getId()).build();
                                Amplify.DataStore.save(
                                        item1,
                                        success -> Log.i("COMO", "Saved item: "),
                                        error -> Log.e("Amplify", "Could not save item to DataStore", error)
                                );
                                Log.i("Qutadah", "Id was stored " );
                                Log.i("Amplify", "Id " + item.getId());
                            }
                        },
                        failure -> Log.e("Amplify", "Could not query DataStore", failure)
                );
                Toast.makeText(getApplicationContext(),  "submitted!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }


}
package com.example.edeze_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "PostQuestionActivity";
    private FirebaseAuth mAuth;

    String[] navbar_temp_array;
    private EditText title_input, description_input;
    FirebaseDatabase database;
    DatabaseReference questionRef;
    DatabaseReference userRef;
    String userString;
    int next_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        Resources res = getResources();
        navbar_temp_array = res.getStringArray(R.array.navbar_array);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove title from action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        title_input = findViewById(R.id.question_title_input);
        description_input = findViewById(R.id.question_description_input);

        Spinner dropdown = findViewById(R.id.navSpinner);
        dropdown.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navbar_array, android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        questionRef = database.getReference("question");
        userRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userString = currentUser.toString();

        questionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                QuestionClass questionClassPosted = dataSnapshot.getValue(QuestionClass.class);
                System.out.println("ID: " + questionClassPosted.id);
                System.out.println("Author: " + questionClassPosted.author);
                System.out.println("Title: " + questionClassPosted.title);
                System.out.println("Description: " + questionClassPosted.description);
                System.out.println("Previous Post ID: " + prevChildKey);
                if(prevChildKey != null){
                    next_id= questionClassPosted.id+1;
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"On Start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"On Resume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"On Pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"On Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "On Destroy");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Context context = getApplicationContext();
        CharSequence text = navbar_temp_array[pos];
        if (text.toString().equals("Profile")){
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Settings")){
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Logout")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Q&A")){
            Intent intent = new Intent(this, QAActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Tutors")){
            Intent intent = new Intent(this, FindTutorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cancelPost(View view) {
        Intent intent = new Intent(this, QAActivity.class);
        startActivity(intent);
    }

    public void submitPost(View view) {
        //How to add entry in db
        Map<String, Object> values = new HashMap<>();
        values.put("id", next_id);
        values.put("author", userString);
        values.put("title", title_input.getText().toString());
        values.put("description", description_input.getText().toString());
        questionRef.child("q"+next_id).setValue(values);
        submitNotifications();
        Intent intent = new Intent(this, QAActivity.class);
        startActivity(intent);
    }

    public void submitNotifications() {
        final DatabaseReference userRef = database.getReference("user");
        userRef.orderByChild("id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                UserClass user = dataSnapshot.getValue(UserClass.class);
                System.out.println("user.authid : " + user.authid);
                if(user.authid == null) {
                    return;
                }
                System.out.println("mauth.getauthid : " + mAuth.getCurrentUser().toString());
                if(!(user.id.equals(mAuth.getCurrentUser().getUid()))) {
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");
                    JSONObject json = new JSONObject();
                    JSONObject notification = new JSONObject();
                    try {
                        notification.put("body", "A new question has been posted");
                        notification.put("title", "Edeze Notification");
                        json.put("to", user.token);
                        json.put("notification", notification );
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
                    Request request = new Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .addHeader("Authorization", "key=AAAATMcQq2U:APA91bE_bLcpxZTEIUefTNYsUSNJ5Hz8EwXjaEX_jpYlGbVc-FNw-hMPonJrWdPryJfYI1VLS44h8lPYtzzJkXHtqCMKJO3eeWe4d9nsL9icco7W320eQPG0uLVBptwmUnWNuQeTa3HX")
                            .addHeader("Content-Type", "application/json")
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            // ...
        });
    }
}


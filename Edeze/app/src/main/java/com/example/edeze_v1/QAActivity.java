package com.example.edeze_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

// db
public class QAActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "QAActivity";
    private FirebaseAuth mAuth;

    String[] navbar_temp_array;
    LinearLayout linear_layout;
    FirebaseDatabase database;
    DatabaseReference questionRef;
    DatabaseReference userRef;
    String userString;
    //String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        //getToken();
        Resources res = getResources();
        navbar_temp_array = res.getStringArray(R.array.navbar_array);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove title from action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        Spinner dropdown = findViewById(R.id.navSpinner);
        dropdown.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navbar_array, android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);
        linear_layout = findViewById(R.id.question_linear_layout);

        database = FirebaseDatabase.getInstance();
        questionRef = database.getReference("question");
        userRef = database.getReference("user");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userString = currentUser.toString();
        System.out.println("---CURRENT USER---: " + userString);
        Map<String, Object> values = new HashMap<>();
        String next_user_id = mAuth.getCurrentUser().getUid();
        values.put("authid", userString);
        values.put("id", next_user_id);
        values.put("latitude", 0.0);
        values.put("longitude", 0.0);
        //System.out.println("token" + token);
        //values.put("token", token);
        values.put("tutor", false);
        //userRef.child("user_"+next_user_id).setValue(values);
        setToken(next_user_id, values);
        questionRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                QuestionClass questionClassPosted = dataSnapshot.getValue(QuestionClass.class);
                addQuestion(questionClassPosted);
                System.out.println("ID: " + questionClassPosted.id);
                System.out.println("Author: " + questionClassPosted.author);
                System.out.println("Title: " + questionClassPosted.title);
                System.out.println("Description: " + questionClassPosted.description);
                System.out.println("Previous Post ID: " + prevChildKey);
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
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                UserClass userCurrent = snapshot.getValue(UserClass.class);
                System.out.println("AUTHID: " + userCurrent.authid);
                System.out.println("ID: " + userCurrent.authid);
            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onCancelled(DatabaseError error) {}
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
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"On Destroy");
    }

    public void toolbarProfileClick(View view) {
        // go to Profile page
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Context context = getApplicationContext();
        CharSequence text = navbar_temp_array[pos];
        if(text.toString().equals("Profile")){
            Intent intent = new Intent(this, ProfileActivity.class);
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

    public void gotoPostQuestion(View view) {
        Intent intent = new Intent(this, PostQuestionActivity.class);
        startActivity(intent);
    }
    public void addQuestion(QuestionClass q){
        LinearLayout l1 = new LinearLayout(QAActivity.this);
        TextView tv1 = new TextView(QAActivity.this);
        TextView tv2 = new TextView(QAActivity.this);


        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setId(q.id);
        l1.setBackgroundResource(R.drawable.radius_light_grey);
        l1.setPadding(30,25,30,25);
        LinearLayout.LayoutParams paramsL1 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsL1.gravity = Gravity.CENTER;
        paramsL1.setMargins(80, 20, 80, 0);
        l1.setLayoutParams(paramsL1);
        linear_layout.addView(l1);

        tv1.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv1.setText(q.title);
        tv1.setTextSize(15);
        tv1.setTextColor(getColor(R.color.main_color_DARK));
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setGravity(Gravity.START);
        l1.addView(tv1);

        tv2.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv2.setText(q.description);
        tv2.setTextSize(15);
        l1.addView(tv2);

        //below testing
        tv1.setClickable(false);
        tv2.setClickable(false);
        l1.setClickable(true);
//        LinearLayout layout_for_click = (LinearLayout) findViewById(q.id);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent answer_intent = new Intent(QAActivity.this,RespondActivity.class);
                Gson gson = new Gson();
                String questionObjAsString = gson.toJson(q);
                answer_intent.putExtra("questionObj",questionObjAsString);
                startActivity(answer_intent);
            }
        });
    }

    public void setToken(String next_user_id, Map<String, Object> values){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        values.put("token", token);
                        System.out.println("token : " + token);
                        Log.d(TAG, msg);
                        userRef.child("user_"+mAuth.getCurrentUser().getUid()).setValue(values);
                    }
                });
    }


}
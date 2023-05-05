package com.example.edeze_v1;
//db
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RespondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "RespondActivity";
    private FirebaseAuth mAuth;

    String[] navbar_temp_array;
    TextView tv_question;
    private EditText description_input;
    FirebaseDatabase database;
    DatabaseReference answerRef;
    int next_id;
    String userString;
    LinearLayout linear_layout;
    List<AnswerClass> answerIDList = new ArrayList<>();
    QuestionClass question_Class_from_intent;
    QuestionClass question_Class_to_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);

        Resources res = getResources();
        navbar_temp_array = res.getStringArray(R.array.navbar_array);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //remove title from action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        tv_question = findViewById(R.id.tv_question_to_be_answered);
        description_input = findViewById(R.id.et_answer_description_input);
        linear_layout = findViewById(R.id.answer_linear_view);

        Gson gson = new Gson();
        String questionObjAsString = getIntent().getStringExtra("questionObj");
        question_Class_from_intent = gson.fromJson(questionObjAsString, QuestionClass.class);
        //question_to_show = (Question) getIntent().getSerializableExtra("questionObj");
        
        tv_question.setText(question_Class_from_intent.title+"\n"+ question_Class_from_intent.description);

        Spinner dropdown = findViewById(R.id.navSpinner);
        dropdown.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navbar_array, android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        answerRef = database.getReference("answer");
        answerIDList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userString = currentUser.toString();

        answerRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                AnswerClass answerPosted = dataSnapshot.getValue(AnswerClass.class);
                if(answerPosted.qid==question_Class_from_intent.id){
                    addAnswer(answerPosted);
                }
                answerIDList.add(answerPosted);
                System.out.println("Question ID: " + answerPosted.qid);
                System.out.println("Author: " + answerPosted.author);
                System.out.println("Description: " + answerPosted.description);
                if(prevChildKey != null){
                    next_id=answerPosted.id+1;
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
        Map<String, Object> values = new HashMap<>();
        values.put("author", userString);
        values.put("description", description_input.getText().toString());
        values.put("id", next_id);
        values.put("qid", question_Class_from_intent.id);
        answerRef.child("answer_obj"+next_id).setValue(values);
    }
    public void addAnswer(AnswerClass answer_obj){
        LinearLayout l1 = new LinearLayout(RespondActivity.this);
        TextView tv2 = new TextView(RespondActivity.this);

        l1.setOrientation(LinearLayout.VERTICAL);
        l1.setId(answer_obj.id);
        l1.setBackgroundResource(R.drawable.radius_light_grey);
        l1.setPadding(30,25,30,25);
        LinearLayout.LayoutParams paramsL1 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsL1.gravity = Gravity.CENTER;
        paramsL1.setMargins(35, 20, 35, 0);
        l1.setLayoutParams(paramsL1);
        linear_layout.addView(l1);

        tv2.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv2.setText(answer_obj.description);
        tv2.setTextSize(15);
        l1.addView(tv2);
    }
}


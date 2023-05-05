package com.example.edeze_v1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

public class SearchQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] navbar_temp_array;
    private static final String TAG = "SearchQuestionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_questions);

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
        // go to log in page
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

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

}
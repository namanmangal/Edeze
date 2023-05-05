package com.example.edeze_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PersonalInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "PersonalInformationActivity";
    String[] navbar_temp_array;
    TextView tv_ProfInfo_FirstLast,tv_study,tv_tutor;

    String result_from_edit_name,result_from_edit_SubjStudy,result_from_edit_SubjTutor;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG,"On Create");
        setContentView(R.layout.activity_personal_info);

        tv_ProfInfo_FirstLast = findViewById(R.id.tv_profile_name);
        tv_study = findViewById(R.id.tv_info_page_study);
        tv_tutor = findViewById(R.id.tv_info_page_tutor);

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //remove title from action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        Resources res = getResources();
        navbar_temp_array = res.getStringArray(R.array.navbar_array);
        setSupportActionBar(toolbar);
        Spinner dropdown = findViewById(R.id.navSpinner);
        dropdown.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.navbar_array, android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);

//        result_from_edit_name = EditProfileActivity.getActivityInstance().getNameStr();
//        result_from_edit_SubjStudy = EditProfileActivity.getActivityInstance().getSubjIStudyStr();
//        result_from_edit_SubjTutor = EditProfileActivity.getActivityInstance().getSubjITutorStr();

        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return;
            } else {
                result_from_edit_name = extras.getString("name");
                result_from_edit_SubjStudy = extras.getString("studySubj");
                result_from_edit_SubjTutor = extras.getString("tutorSubj");

                //update the tv with the new strings IF there was an update.
                tv_ProfInfo_FirstLast.setText(result_from_edit_name);
                tv_study.setText(result_from_edit_SubjStudy);
                tv_tutor.setText(result_from_edit_SubjTutor);
            }
        }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void toolbarProfileClick(View view) {
        // go to log in page
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }
}

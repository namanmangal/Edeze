package com.example.edeze_v1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// db
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "ProfilePageActivity";
    TextView tv_name;
    Button buttonInfo, buttonPostedQ, buttonFaveQ, buttonTutors;
    String[] navbar_temp_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");
        // set layout
        setContentView(R.layout.activity_profile_page);

        // set UI elements

        buttonInfo = findViewById(R.id.personalinfobutton);
        buttonPostedQ = findViewById(R.id.postedquestionsbutton);
//        buttonFaveQ = findViewById(R.id.favoritequestionsbutton);
        buttonTutors = findViewById(R.id.tutorsbutton);
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

        tv_name.setText(EditProfileActivity.getActivityInstance().resultName);
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

    public void tutorsClick(View view) {
        Intent intent = new Intent(this, FindTutorActivity.class);
        startActivity(intent);
    }

    public void postedQuestionsClick(View view) {
    }

    public void personalInfoClick(View view) {
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        startActivity(intent);
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
            //do nothing; we are at profile
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

    public void editProfileClick(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}

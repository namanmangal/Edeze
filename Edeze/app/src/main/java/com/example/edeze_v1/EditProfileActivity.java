package com.example.edeze_v1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.widget.Toolbar;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    static EditProfileActivity INSTANCE;
    private static final String TAG = "EditProfile";
    String[] navbar_temp_array;
    EditText et_editProfile_firstName,et_editProfilelastName,et_schoolName;
    String update_firstName_str,update_lastName_str;
    Spinner spinner_pronoun,spinner_learning,spinner_tutoring;
    String resultIStudy,resultITutor,resultName;

    TextView tv_dropdown_study,tv_dropdown_tutor;
    boolean[] selectedSubjStudy,selectedSubjTutor;
    ArrayList<Integer> subjIStudyList = new ArrayList<>();
    ArrayList<Integer> subjITutorList = new ArrayList<>();
    String[] subjIStudyArray = {"Math","English","Writing","Foreign Language","History","Sciences","Art","Music"};
    String[] subjITutorArray = {"Math","English","Writing","Foreign Language","History","Sciences","Art","Music"};



    public static EditProfileActivity getActivityInstance(){
        return INSTANCE;
    }

    public String getSubjIStudyStr(){
       return this.resultIStudy;
    }

    public String getSubjITutorStr(){
        return this.resultITutor;
    }

    public String getNameStr(){
        return this.resultName;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        INSTANCE = this;

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

        // edit profile spinners
        spinner_pronoun = findViewById(R.id.spinner_myPronoun);
//        spinner_learning = findViewById(R.id.spinner_studySubj);
//        spinner_tutoring = findViewById(R.id.spinner_tutorSubj);

        ArrayAdapter<CharSequence> adapter_spinnerPronoun = ArrayAdapter.createFromResource(this, R.array.spinner_pronoun_array, android.R.layout.simple_spinner_item);
        adapter_spinnerPronoun.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_pronoun.setAdapter(adapter_spinnerPronoun);

        //ui elements
        et_editProfile_firstName = findViewById(R.id.enter_firstname);
        et_editProfilelastName = findViewById(R.id.enter_lastname);
        et_schoolName = findViewById(R.id.school_name);

        tv_dropdown_study = findViewById(R.id.dropdown_studySubject);
        tv_dropdown_tutor = findViewById(R.id.dropdown_tutorSubject);

        selectedSubjStudy = new boolean[subjIStudyArray.length];
        selectedSubjTutor = new boolean[subjITutorArray.length];

        // onclick for multiple selection
        tv_dropdown_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Select Subjects You Study");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(subjIStudyArray, selectedSubjStudy, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            subjIStudyList.add(i);
                            Collections.sort(subjIStudyList);
                        } else {
                            subjIStudyList.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j<subjIStudyList.size();j++){
                            stringBuilder.append(subjIStudyArray[subjIStudyList.get(j)]);
                            if (j != subjIStudyList.size() - 1) {
                                stringBuilder.append(", ");

                            }
                        }
                        tv_dropdown_study.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j=0;j<selectedSubjStudy.length;j++){
                            selectedSubjStudy[j] = false;
                            subjIStudyList.clear();
                            tv_dropdown_study.setText("");
                        }
                    }
                });
                builder.show();
            }
        });

        tv_dropdown_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Select Subjects You Study");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(subjITutorArray, selectedSubjTutor, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            subjITutorList.add(i);
                            Collections.sort(subjITutorList);
                        } else {
                            subjITutorList.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j<subjITutorList.size();j++){
                            stringBuilder.append(subjITutorArray[subjITutorList.get(j)]);
                            if (j != subjITutorList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        tv_dropdown_tutor.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j=0;j<selectedSubjTutor.length;j++){
                            selectedSubjTutor[j] = false;
                            subjITutorList.clear();
                            tv_dropdown_tutor.setText("");
                        }
                    }
                });
                builder.show();
//                resultITutor = builder.toString();
            }
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Context context = getApplicationContext();
        CharSequence text = navbar_temp_array[pos];
        if (text.toString().equals("Profile")){
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }
        else if(text.toString().equals("Settings")){
            //do nothing; we are in profile settings aka edit profile
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

    public void toolbarProfileClick(View view) {
        // go to log in page
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    public void updateProfileClick(View view) {
        update_firstName_str = et_editProfile_firstName.getText().toString();
        update_lastName_str = et_editProfilelastName.getText().toString();

        String st1 = tv_dropdown_study.getText().toString();
        String st2 = tv_dropdown_tutor.getText().toString();
        resultIStudy = st1.replace(", ","\n");
        resultITutor = st2.replace(", ","\n");

        resultName = update_firstName_str.concat(" "+update_lastName_str);
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        intent.putExtra("name",resultName);
        intent.putExtra("studySubj",resultIStudy);
        intent.putExtra("tutorSubj",resultITutor);
        startActivity(intent);
        //updateProfileMethod(update_firstName_str,update_lastName_str,"","","");
    }

    void updateProfileMethod(String first, String last, String pronoun, String study, String tutor){

    }

}
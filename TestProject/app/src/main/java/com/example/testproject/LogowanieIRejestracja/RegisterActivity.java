package com.example.testproject.LogowanieIRejestracja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.testproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity  {
    private EditText name,email,password,height,age,weight;
    private Spinner activityType,gender;
    private String userName,userEmail,userPassword,userHeight,userGender,userAge,userActivityType,userWeight;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        weight=findViewById(R.id.weight);
        name=findViewById(R.id.editTextName);
        email=findViewById(R.id.editTextTextEmailAddressreg);
        password=findViewById(R.id.editTextTextPassword2);
        height=findViewById(R.id.editTextHeight);
        age=findViewById(R.id.editTextAge);
        gender=findViewById(R.id.spinner_gender);
        activityType=findViewById(R.id.spinner_sport);
        Button push = findViewById(R.id.registeruserbtn);

        setUpGenderSpinner();
        setUpActivityTypeSpinner();

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = name.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                userHeight = height.getText().toString().trim();
                userAge = age.getText().toString().trim();
                userWeight = weight.getText().toString().trim();
                userGender = gender.getSelectedItem().toString();
                userActivityType = activityType.getSelectedItem().toString();

                HashMap<String, String> validationMap = new HashMap<>();
                setUpValidationMap(validationMap);

                String error = validationMap.getOrDefault("", "");
                assert error != null;
                if (error.isEmpty()) {
                    authenticationCheck();
                } else {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setUpValidationMap(HashMap<String,String> validationMap){
        validationMap.put(userName, "Wypełnij nazwe");
        validationMap.put(userEmail, "Wypełnij e-mail");
        validationMap.put(userPassword, "Wypełnij hasło");
        validationMap.put("Gender", "Wybierz płeć");
        validationMap.put("Activity Level", "Wybierz poziom aktywności fizycznej");
        validationMap.put(userWeight, "Wypełnij wage");
        validationMap.put(userHeight, "Wypełnij wzrost");
    }
    private void authenticationCheck() {
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Created Successfully",Toast.LENGTH_SHORT).show();

                    String currentUserId=mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> userdataMap=new HashMap<>();

                    makeUserDataMap(userdataMap);
                    saveUser(currentUserId,userdataMap);
                    startLoginIntent();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void makeUserDataMap(HashMap<String,Object> userdataMap){
        userdataMap.put("name",userName);
        userdataMap.put("email",userEmail);
        userdataMap.put("password",userPassword);
        userdataMap.put("gender",userGender);
        userdataMap.put("height",userHeight);
        userdataMap.put("activityType",userActivityType);
        userdataMap.put("weight",userWeight);
        userdataMap.put("age",userAge);
    }
    private void saveUser(String currentUserId, HashMap<String,Object> userdataMap){
        databaseReference.child("users").child(currentUserId).updateChildren(userdataMap);
    }
    private void startLoginIntent(){
        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    private void setUpGenderSpinner(){
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapterGender);

    }
    private void setUpActivityTypeSpinner(){
        ArrayAdapter<CharSequence> adapterActivity = ArrayAdapter.createFromResource(this, R.array.activityType, android.R.layout.simple_spinner_item);
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityType.setAdapter(adapterActivity);
    }
}
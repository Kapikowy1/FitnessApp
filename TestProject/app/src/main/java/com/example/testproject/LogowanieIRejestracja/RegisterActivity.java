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

import com.example.testproject.LogowanieIRejestracja.LoginActivity;
import com.example.testproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity  {





    EditText name,email,password,height,age,weight;
    Spinner activityType,gender;

    String userName,userEmail,userPassword,userHeight,userGender,userAge,userActivityType,userWeight;
    Button push;
    DatabaseReference databaseReference;
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
        push=findViewById(R.id.registeruserbtn);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapterGender);


        ArrayAdapter<CharSequence> adapterActivity = ArrayAdapter.createFromResource(this, R.array.activityType, android.R.layout.simple_spinner_item);
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityType.setAdapter(adapterActivity);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=name.getText().toString().trim();
                userEmail=email.getText().toString().trim();
                userPassword=password.getText().toString().trim();
                userHeight=height.getText().toString().trim();
                userAge=age.getText().toString().trim();
                userWeight=weight.getText().toString().trim();

                userGender=gender.getSelectedItem().toString();
                userActivityType=activityType.getSelectedItem().toString();

                if(userName.equals("")){
                    name.setError("Fill username");
                }
                else if(userEmail.equals("")){
                    email.setError("Fill Email");
                }
                else if(userPassword.equals("")){
                    password.setError("Fill password");
                }
                else if(userGender.equals("Gender")){
                    Toast.makeText(getApplicationContext(), "Wybierz płeć", Toast.LENGTH_SHORT).show();
                }
                else if(userActivityType.equals("Activity Level")){
                    Toast.makeText(getApplicationContext(), "Wybierz poziom aktywnosci fizycznej", Toast.LENGTH_SHORT).show();
                }
                else if(userWeight.equals("")){
                    weight.setError("Fill Weight");
                }
                else if(userHeight.equals("")){
                    height.setError("Fill Height");
                }
                else {
                    authenticationCheck();
                }
            }
        });
    }
    private void authenticationCheck() {
        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Created Successfully",Toast.LENGTH_SHORT).show();
                    String currentUserId=mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("name",userName);
                    userdataMap.put("email",userEmail);
                    userdataMap.put("password",userPassword);
                    userdataMap.put("gender",userGender);
                    userdataMap.put("height",userHeight);
                    userdataMap.put("activityType",userActivityType);
                    userdataMap.put("weight",userWeight);
                    userdataMap.put("age",userAge);

                    databaseReference.child("users").child(currentUserId).updateChildren(userdataMap);

                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password,phone;
    String userName,userEmail,userPassword,userPhone;
    Button push;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.editTextName);
        email=findViewById(R.id.editTextTextEmailAddressreg);
        password=findViewById(R.id.editTextTextPassword2);
        phone=findViewById(R.id.editTextnumber);
        push=findViewById(R.id.registeruserbtn);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=name.getText().toString().trim();
                userEmail=email.getText().toString().trim();
                userPassword=password.getText().toString().trim();
                userPhone=phone.getText().toString().trim();

                if(userName.equals("")){
                    name.setError("Fill username");
                }
                else if(userEmail.equals("")){
                    email.setError("Fill Email");
                }
                else if(userPassword.equals("")){
                    password.setError("Fill password");
                }
                else if (userPhone.equals("")){
                    phone.setError("Fill phone");
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
                    userdataMap.put("phone",userPhone);
                    databaseReference.child("users").child(currentUserId).updateChildren(userdataMap);

                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.testproject.LogowanieIRejestracja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button Btn,tv;
    EditText Email,Password;
    String email,password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.u_email);
        Password=findViewById(R.id.u_password);
        Btn =findViewById(R.id.loginBtn);
        tv =findViewById(R.id.switch1);
        mAuth=FirebaseAuth.getInstance();

        if (isInternetConnected()) {
            // Wykonaj operacje, gdy jest dostępne połączenie internetowe
            // ...
        } else {
            // Obsłuż przypadek braku połączenia internetowego
            Toast.makeText(this, "Brak połączenia internetowego", Toast.LENGTH_SHORT).show();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Email.getText().toString().trim();
                password = Password.getText().toString();

                if(email.equals("")){
                    Email.setError("Username required");
                }
                else if(password.equals("")){
                    Password.setError("email required");
                }
                else {
                    authenticationUser(email,password);
                }
            }
        });
    }
    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private void authenticationUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Login Succesful",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(), ChooseModuleActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
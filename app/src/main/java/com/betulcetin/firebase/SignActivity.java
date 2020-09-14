package com.betulcetin.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        firebaseAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.EmailText);
        passwordText = findViewById(R.id.PasswordText);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(SignActivity.this,FeedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        if (email.matches("") || password.matches("")) {
            Toast.makeText(SignActivity.this, "Please enter password and email!", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(SignActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignActivity.this, "Wrong Password or email!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void signUp(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.matches("") || password.matches("")){
            Toast.makeText(SignActivity.this,"Please enter password and email!",Toast.LENGTH_LONG).show();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignActivity.this,"User created successfully!",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
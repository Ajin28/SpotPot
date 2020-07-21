package com.example.spotpot;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    EditText editText_email,editText_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_email=findViewById(R.id.editText5);
        editText_password=findViewById(R.id.editText6);

        progressBar=findViewById(R.id.progressBarlogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {
        Intent intent= new Intent(getApplicationContext(),Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
    }


    public void openAction(View view) {

        String sEmail=editText_email.getText().toString().trim();
        String sPassword=editText_password.getText().toString().trim();
        if(sEmail.isEmpty()){
            editText_email.setError("Email is required");
            editText_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches())
        {
            editText_email.setError("Email is invalid");
            editText_email.requestFocus();
            return;
        }
        if(sPassword.isEmpty()){
            editText_password.setError("Password is required");
            editText_password.requestFocus();
            return;
        }

        if(sPassword.length()<6){
            editText_password.setError("Minimum length of password is 6");
            editText_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(sEmail,sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful())
                        {
                            Intent intent= new Intent(getApplicationContext(),Action.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}

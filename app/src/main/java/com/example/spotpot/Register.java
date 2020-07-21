package com.example.spotpot;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    ProgressBar progressBar;
    EditText email,password;
     private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.editText5);
        password=findViewById(R.id.editText6);
        progressBar=findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }



    public void signIn(View view) {
        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void regiter(View view) {



        String sEmail=email.getText().toString().trim();
        String sPassword=password.getText().toString().trim();
        if(sEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches())
        {
            email.setError("Email is invalid");
            email.requestFocus();
            return;
        }
        if(sPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(sPassword.length()<6){
            password.setError("Minimum length of password is 6");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(sEmail,sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            DatabaseReference email= FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).child("Email");
                            email.setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());


                            Toast.makeText(getApplicationContext(),"Registeration complete",Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });








    }
}

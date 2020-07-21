package com.example.spotpot;

import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportSuccesfullySent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_succesfully_sent);





    }

    public void openAction(View view) {
        Intent intent= new Intent(getApplicationContext(),Action.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }





}

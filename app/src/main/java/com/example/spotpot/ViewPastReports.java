package com.example.spotpot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPastReports extends AppCompatActivity {




    DatabaseReference CurrentUser;
    DatabaseReference Report;
    MultipleReportsAdapter multipleReportsAdapter;
    RecyclerView recyclerView;
    ArrayList<MultipleReports> list;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_reports);


        recyclerView=findViewById(R.id.recyclerMultiple);
        layoutManager= new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);




        list= new ArrayList<MultipleReports>();

        multipleReportsAdapter= new MultipleReportsAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(multipleReportsAdapter);

        CurrentUser= FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("Reports");


        CurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot Reportid:dataSnapshot.getChildren()){
                    final  String  reportIDs=Reportid.getKey().toString();
                    if(reportIDs!=null)
                    {

                        Report=FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).child("Reports").child(reportIDs);
                        Report.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                                MultipleReports multipleReports=new MultipleReports("","","","https://firebasestorage.googleapis.com/v0/b/fluted-dynamo-262421.appspot.com/o/download.png?alt=media&token=92150359-756c-49c1-aff0-6c261c414168");
                                multipleReports.setReportID(reportIDs);
                                for(DataSnapshot ReportDetails:dataSnapshot1.getChildren())
                                {


                                    String key=ReportDetails.getKey();
                                    String value=ReportDetails.getValue().toString();
                                    if(key.equalsIgnoreCase("latitude")){
                                       multipleReports.setLatitude(value);
                                    }
                                    if(key.equalsIgnoreCase("longitude")){
                                        multipleReports.setLongitude(value);
                                    }
                                    if(key.equalsIgnoreCase("images")){
                                       multipleReports.setImageURL( ReportDetails.child("image_0").getValue().toString());
                                    }

                                }

                                list.add(multipleReports);
                                multipleReportsAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError1) {

                            }
                        });




                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onBackPressed() {

        list.clear();
        multipleReportsAdapter.notifyDataSetChanged();
        super.onBackPressed();

    }

    public void openmapview(View view) {

        Intent intent=new Intent(getApplicationContext(),MapView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

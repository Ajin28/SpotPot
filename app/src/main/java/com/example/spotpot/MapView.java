package com.example.spotpot;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Double lat ,lang;

    DatabaseReference CurrentUser;
    DatabaseReference Report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);



                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapv);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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


                                for(DataSnapshot ReportDetails:dataSnapshot1.getChildren())
                                {


                                    String key=ReportDetails.getKey();
                                    String value=ReportDetails.getValue().toString();
                                    if(key.equalsIgnoreCase("latitude")){
                                        lat=Double.parseDouble(value);
                                    }
                                    if(key.equalsIgnoreCase("longitude")){
                                        lang=Double.parseDouble(value);
                                    }


                                }

                                LatLng sydney = new LatLng(lat, lang);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(reportIDs));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,20));


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
}

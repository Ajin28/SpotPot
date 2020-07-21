package com.example.spotpot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DescribeReport extends AppCompatActivity {

    private  static  final  int request_image=101;
    Bitmap[] imageArray=new Bitmap[12];
    static   int i=0;
    private  RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String reportID;


    //DescribeReport()
  //  {
  //      i=0;
  //  }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_report);
        recyclerView=findViewById(R.id.recycler);
        layoutManager= new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        reportID=bundle.getString("reportID");

        i=0;
    }


    public void takePicture(View view) {

        Intent takeImage= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takeImage.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takeImage,request_image);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==request_image && resultCode==RESULT_OK){

            if(i>=12)
            {
                i=0;
            }
            Bundle extras=data.getExtras();
            Bitmap imageBitmap=(Bitmap) extras.get("data");
            imageArray[i]=imageBitmap;
            i++;
            recyclerAdapter =new RecyclerAdapter(imageArray);
            recyclerView.setAdapter(recyclerAdapter);

        }

    }


    public void deletePicture(View view) {

        if(imageArray[0]!=null)
        {

            imageArray[i-1]=null;
            recyclerAdapter =new RecyclerAdapter(imageArray);
            recyclerView.setAdapter(recyclerAdapter);
            i=i-1;

        }
        else{
            Toast.makeText(getApplicationContext(),"No Pictures Taken",Toast.LENGTH_SHORT).show();
        }
    }

    public void openMaps(View view) {
        if(reportID!=null)
        {
            Toast.makeText(getApplicationContext(),reportID,Toast.LENGTH_SHORT).show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance()
            .getCurrentUser().getUid()).child("Reports")
                    .child(reportID);
            if(i>0){
                for(int j=0;j<=i-1;j++)
                {

                    uploadImage(imageArray[j],ref,j);
                }
            }

            Intent intent= new Intent(getApplicationContext(),ReportSuccesfullySent.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        }
        else{
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }

    }

    public void uploadImage(Bitmap bitmap, final DatabaseReference reference,final int j) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String image_no=Integer.toString(j);
        String imageID="image_"+image_no;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://fluted-dynamo-262421.appspot.com");
        StorageReference imagesRef = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(reportID).child(imageID);

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url=uri.toString();
                        String image_no=Integer.toString(j);
                        String imageID="image_"+image_no;
                        DatabaseReference image=reference.child("Images").child(imageID);
                        image.setValue(url);
                    }
                });
                // Do what you want
            }
        });
    }

}

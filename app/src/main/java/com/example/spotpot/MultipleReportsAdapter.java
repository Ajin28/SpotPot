package com.example.spotpot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultipleReportsAdapter extends RecyclerView.Adapter<MultipleReportsAdapter.MyViewHolder> {


    Context context;
    ArrayList<MultipleReports> multipleReports;
    public MultipleReportsAdapter(Context c,ArrayList<MultipleReports> m)
    {
        context=c;
        multipleReports=m;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.multiple_reports,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.reportID.setText(multipleReports.get(position).getReportID());
        holder.Latitude.setText("Latitude:"+multipleReports.get(position).getLatitude());
        holder.Longitude.setText("Longitude:"+multipleReports.get(position).getLongitude());
        Picasso.get().load(multipleReports.get(position).getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return multipleReports.size();
    }

   public static   class MyViewHolder extends RecyclerView.ViewHolder{

        TextView reportID,Latitude,Longitude;
        ImageView imageView;
        public MyViewHolder(View itemView){
            super(itemView);
            reportID=itemView.findViewById(R.id.reportID);
            Latitude=itemView.findViewById(R.id.latitude);
            Longitude=itemView.findViewById(R.id.longitude);
            imageView=itemView.findViewById(R.id.reportImage);
        }
    }
}

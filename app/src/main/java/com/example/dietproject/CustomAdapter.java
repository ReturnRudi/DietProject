package com.example.dietproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;
    int totalkcal = 0;


    public CustomAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_food);
        holder.tv_name_list.setText(arrayList.get(position).getFood_name());
        holder.tv_num_list.setText(arrayList.get(position).getFood_num());
        holder.tv_review_list.setText(arrayList.get(position).getReview());
        holder.tv_time_list.setText(arrayList.get(position).getTime());
        holder.tv_date_list.setText(arrayList.get(position).getDate());
        holder.tv_kcal_list.setText(arrayList.get(position).getKcal());
        holder.tv_longitude_list.setText(arrayList.get(position).getLongitude());
        holder.tv_latitude_list.setText(arrayList.get(position).getLatitude());

        String Kcal = arrayList.get(position).getKcal().replaceAll("[^0-9]", "");
        int intkcal = Integer.parseInt(Kcal);
        totalkcal += intkcal;
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_food;
        TextView tv_name_list;
        TextView tv_num_list;
        TextView tv_review_list;
        TextView tv_time_list;
        TextView tv_date_list;
        TextView tv_kcal_list;
        TextView tv_longitude_list;
        TextView tv_latitude_list;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_food = itemView.findViewById(R.id.iv_food);
            this.tv_name_list = itemView.findViewById(R.id.tv_name_list);
            this.tv_num_list = itemView.findViewById(R.id.tv_num_list);
            this.tv_review_list = itemView.findViewById(R.id.tv_review_list);
            this.tv_time_list = itemView.findViewById(R.id.tv_time_list);
            this.tv_date_list = itemView.findViewById(R.id.tv_date_list);
            this.tv_kcal_list = itemView.findViewById(R.id.tv_kcal_list);
            this.tv_longitude_list = itemView.findViewById(R.id.tv_longitude_list);
            this.tv_latitude_list = itemView.findViewById(R.id.tv_latitude_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mposition = getAdapterPosition() ;
                    Context mcontext = v.getContext();

                    Intent detailActivity = new Intent(mcontext,View_food.class);
                    detailActivity.putExtra("image",arrayList.get(mposition).getImage());
                    detailActivity.putExtra("name", arrayList.get(mposition).getFood_name());
                    detailActivity.putExtra("num", arrayList.get(mposition).getFood_num());
                    detailActivity.putExtra("review", arrayList.get(mposition).getReview());
                    detailActivity.putExtra("date", arrayList.get(mposition).getDate());
                    detailActivity.putExtra("time", arrayList.get(mposition).getTime());
                    detailActivity.putExtra("kcal", arrayList.get(mposition).getKcal());
                    detailActivity.putExtra("latitude", arrayList.get(mposition).getLatitude());
                    detailActivity.putExtra("longitude", arrayList.get(mposition).getLongitude());
                    detailActivity.putExtra("totalkcal", totalkcal);


                    ((Show_list)context).startActivity(detailActivity);
                }
            });
        }
    }
}

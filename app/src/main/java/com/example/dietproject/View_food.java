package com.example.dietproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class View_food extends AppCompatActivity {

    final static int ACT_EDIT = 0;
    ImageView iv_food;
    TextView tv_name, tv_num, tv_review ,tv_kcal ,tv_date , tv_time, tv_location, tv_totalkcal;
    String food, name, num, review, kcal, date, time, latitude, longitude;
    int totalkcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);

        iv_food = findViewById(R.id.iv_food);
        tv_name = findViewById(R.id.tv_name_view);
        tv_num = findViewById(R.id.tv_num_view);
        tv_review = findViewById(R.id.tv_review_view);
        tv_date = findViewById(R.id.tv_date_view);
        tv_time = findViewById(R.id.tv_time_view);
        tv_kcal = findViewById(R.id.tv_kcal_view);
        tv_location = findViewById(R.id.tv_location_view);
        tv_totalkcal = findViewById(R.id.tv_totalkcal_view);

        Intent intent = getIntent();


        food = intent.getExtras().getString("image");
        name = intent.getExtras().getString("name");
        num = intent.getExtras().getString("num");
        review = intent.getExtras().getString("review");
        date = intent.getExtras().getString("date");
        time = intent.getExtras().getString("time");
        kcal = intent.getExtras().getString("kcal");
        latitude = intent.getExtras().getString("latitude");
        longitude = intent.getExtras().getString("longitude");
        totalkcal = intent.getExtras().getInt("totalkcal");


        //숫자만 추출후 string을 int로 변환
        String Kcal = kcal.replaceAll("[^0-9]", "");
        int intkcal = Integer.parseInt(Kcal);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_food.this, MapShow.class);
                intent.putExtra("위도", latitude);
                intent.putExtra("경도", longitude);
                startActivity(intent);
            }
        });



        //iv_food.setImageURI(Uri.parse(food));;
        Glide.with(this).load(food).into(iv_food);
        tv_name.setText(name);
        tv_num.setText(num);
        tv_time.setText(time);
        tv_date.setText(date);
        tv_review.setText(review);
        tv_kcal.setText(intkcal + "kcal");
        tv_totalkcal.setText(totalkcal +"kcal");

    }
}
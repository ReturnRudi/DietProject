package com.example.dietproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    int totalkcal = 0;
    String kcal;
    TextView tv_kcal;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        recyclerView = findViewById(R.id.recyclerView); //id 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Post 객체를 담을 arraylist

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Post"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출해냄
                    Post post = snapshot.getValue(Post.class); //만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(post); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    kcal = arrayList.get(count).getKcal();
                    totalkcal += Integer.parseInt(kcal);
                    count += 1;
                }
                tv_kcal = findViewById(R.id.tv_kcal);
                tv_kcal.setText("  최근 섭취한 Kcal 총량: " + String.valueOf(totalkcal));
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
                Log.e("Show_list", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        Button btn_del;
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = database.getReference("Post");
                databaseReference.removeValue();
                Toast.makeText(getApplicationContext(), "삭제되었습니다!", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
            }
        });





        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        //----------------------------------------------------
    }
}
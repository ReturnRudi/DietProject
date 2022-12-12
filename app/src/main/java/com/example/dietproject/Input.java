package com.example.dietproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Input extends AppCompatActivity {

    private Button btn_picture;
    private ImageView iv_food;
    Calendar myCalendar = Calendar.getInstance();
    private Button btn_done;
    public static final int REQUEST_CODE = 101;
    String latitude;
    String longitude;


    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;


    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);


        CropImage.activity()
                .setAspectRatio(1,1)
                .start(Input.this);

        iv_food = findViewById(R.id.iv_food);


        EditText et_date = (EditText) findViewById(R.id.et_date);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Input.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText et_time = (EditText) findViewById(R.id.et_time);
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Input.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        et_time.setText(state + " " + selectedHour + "시 " + selectedMinute + "분");
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final EditText et_location = (EditText) findViewById((R.id.et_location));
        et_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, MapsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
           }
        });


//        btn_picture = findViewById(R.id.btn_picture);
//        btn_picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
//            }
//        });

        //----------------------------------------------------추가 부분


        Button btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터 쓰기

                uploadImage();

                Intent intent = new Intent(Input.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.et_date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("게시중");
        progressDialog.show();

        if (imageUri != null){

            storageReference = FirebaseStorage.getInstance().getReference("Post");
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).
                    addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString(); //이미지에 넣으면 됨

                        EditText et_name = findViewById(R.id.et_name);
                        EditText et_num = findViewById(R.id.et_num);
                        EditText et_review = findViewById(R.id.et_review);

                        EditText et_date = findViewById(R.id.et_date);
                        EditText et_time = findViewById(R.id.et_time);
                        EditText et_kcal = findViewById(R.id.et_kcal);



                        FirebaseDatabase database = FirebaseDatabase.getInstance(); //연동

                        DatabaseReference databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("food_name");
                        databaseReference.setValue(et_name.getText().toString());
                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("food_num");
                        databaseReference.setValue(et_num.getText().toString());

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("review");
                        databaseReference.setValue(et_review.getText().toString());

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("date");
                        databaseReference.setValue(et_date.getText().toString());
                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("time");
                        databaseReference.setValue(et_time.getText().toString());

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("kcal");
                        databaseReference.setValue(et_kcal.getText().toString());

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("image");
                        databaseReference.setValue(myUrl);

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("latitude");
                        databaseReference.setValue(latitude);

                        databaseReference = database.getReference("Post").child(et_name.getText().toString()).child("longitude");
                        databaseReference.setValue(longitude);



                        progressDialog.dismiss();

                        startActivity(new Intent(Input.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Input.this, "오류!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Input.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "이미지가 없습니다.!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            iv_food.setImageURI(imageUri);
        }
        else if(requestCode == 101){
            if(data != null){
                latitude = data.getStringExtra("위도");
                longitude = data.getStringExtra("경도");
                if(latitude != null){
                    EditText et_location = (EditText) findViewById(R.id.et_location);
                    et_location.setText("위치 입력 완료");
                }
            }
        }
        else {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Input.this, MainActivity.class));
            finish();
        }



    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        iv_food = findViewById(R.id.iv_food);
//        btn_picture = findViewById(R.id.btn_picture);
//
//        switch(requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
//                    iv_food.setImageURI(uri);
//                }
//                break;
//        }
//        btn_picture.setVisibility(View.GONE);
//    }


}
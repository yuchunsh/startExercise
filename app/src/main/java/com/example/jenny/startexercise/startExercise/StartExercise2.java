package com.example.jenny.startexercise.startExercise;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Share.ShareActivity;

public class StartExercise2 extends AppCompatActivity {

    private TextView exerciseSummary;
    private TextView equipName_tv;
    private Button postBtn;
    private TextView excerciseSec;
    private ImageView equipImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise2);

        final Intent intent = this.getIntent();
        String exerciseDuration = intent.getStringExtra("exerciseDuration");
        String equipName = intent.getStringExtra("equipName");
        String minutes = exerciseDuration.split(":")[1];
        String seconds = exerciseDuration.split(":")[2];

        exerciseSummary = (TextView)findViewById(R.id.exerciseSummary);
        exerciseSummary.setText(minutes);

        excerciseSec = (TextView)findViewById(R.id.exerciseSec);
        excerciseSec.setText(seconds);

        equipName_tv = (TextView)findViewById(R.id.equip_name);
        equipName_tv.setText(equipName);

        equipImg = (ImageView)findViewById(R.id.equip_img);
        if (equipName.equals("直立式腳踏車")){
            Drawable myDrawable = getResources().getDrawable(R.mipmap.ic_bike);
            equipImg.setImageDrawable(myDrawable);
        }else{
            Drawable myDrawable = getResources().getDrawable(R.mipmap.ic_treadmill);
            equipImg.setImageDrawable(myDrawable);
        }


        postBtn = (Button)findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(StartExercise2.this, ShareActivity.class);
                startActivity(intent);
            }
        });
    }
}

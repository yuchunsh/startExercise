package com.example.jenny.startexercise.startExercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Share.ShareActivity;

public class StartExercise2 extends AppCompatActivity {

    private TextView exerciseSummary;
    private Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise2);

        final Intent intent = this.getIntent();
        String exerciseDuration = intent.getStringExtra("exerciseDuration");

        exerciseSummary = (TextView)findViewById(R.id.exerciseSummary);
        exerciseSummary.setText("本次運動時數為：" + exerciseDuration);

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

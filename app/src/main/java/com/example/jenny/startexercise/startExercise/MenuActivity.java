package com.example.jenny.startexercise.startExercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jenny.startexercise.R;

public class MenuActivity extends AppCompatActivity {

    private Button gymBtn;
    private Button othersBtn;
    private ImageView ic_gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        gymBtn = (Button)findViewById(R.id.gym);
        gymBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        ic_gym = (ImageView)findViewById(R.id.ic_white_dumbbell);

        othersBtn = (Button)findViewById(R.id.others);
    }
}

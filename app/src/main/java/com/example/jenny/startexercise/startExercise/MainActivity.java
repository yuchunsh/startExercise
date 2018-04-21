package com.example.jenny.startexercise.startExercise;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Rank.RankActivity;
import com.example.jenny.startexercise.Share.ShareActivity;
import com.example.jenny.startexercise.barcode.BarcodeCaptureActivity;
import com.example.jenny.startexercise.home.home;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    String showUri = "http://140.119.19.36:80/start.php";
    com.android.volley.RequestQueue requestQueue;

    private TextView mResultTextView;
    private String uid = "1";
    private String uname = "winnie";
    private String pname = "體育館重訓室";
    private String ename = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result_textview);

        Button scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        Button skipBtn = (Button) findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        Button homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, home.class);
                startActivity(intent);
            }
        });

        Button rankBtn = (Button) findViewById(R.id.rankBtn);
        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    String getData = barcode.displayValue;
                    ename = getData;
//                    mResultTextView.setText(barcode.displayValue);
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    Log.d("22222","hello");
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, showUri+"?getData="+getData, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("1111111","onResponse");
                                    try {
                                        //這邊要和上面json的名稱一樣
                                        JSONArray data = response.getJSONArray("data");
                                        Boolean canExercise = false;
                                        if (data.length()!=0){
                                            //get the smallest start_time
                                            Long minStartTime = Long.parseLong(data.getJSONObject(0).getString("start_time"));
                                            String minStartTimeUid = null;
                                            Long minEndTime = Long.parseLong(data.getJSONObject(0).getString("end_time"));
                                            for (int i = 0; i < data.length(); i++) {
                                                JSONObject jasonData = data.getJSONObject(i);
                                                Long start_time = Long.parseLong(jasonData.getString("start_time"));
                                                if (start_time<minStartTime){
                                                    minStartTime = start_time;
                                                    minStartTimeUid = jasonData.getString("uid");
                                                    minEndTime = jasonData.getLong("end_time");
                                                }
                                            }
                                            if (uid.equals(minStartTimeUid) ){
                                                canExercise = true;
                                            }else if(minStartTime-System.currentTimeMillis()<1800000 && minStartTime > System.currentTimeMillis()){
                                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("hh:mm");
                                                Date date = new Date();
                                                date.setTime(minStartTime);
                                                String nextReservation = simpleDateformat.format(date);
                                                Toast.makeText(MainActivity.this,"溫馨提醒：目前機台可以使用\n但下一位使用者的預約時間為" + nextReservation + "，請注意時間！",Toast.LENGTH_LONG).show();
                                                canExercise = true;
                                            }else if (minStartTime < System.currentTimeMillis() && minEndTime > System.currentTimeMillis()){
                                                minStartTime = minStartTime + 300000;
                                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("hh:mm");
                                                Date date = new Date();
                                                date.setTime(minStartTime);
                                                String availableTime = simpleDateformat.format(date);
                                                mResultTextView.setText("現在有人預約\n若到" + availableTime + "預約者還沒來即可使用");
                                                mResultTextView.setTextColor(Color.RED);
                                            }else{
                                                canExercise = true;
                                            }

                                        }else {
                                            canExercise = true;
                                        }
                                        if(canExercise){
                                            Intent intent = new Intent(MainActivity.this,StartExercise1.class);
                                            intent.putExtra("uid",uid );
                                            intent.putExtra("uname", uname);
                                            intent.putExtra("pname", pname);
                                            intent.putExtra("ename", ename);
                                            startActivity(intent);
                                            finish();

                                        }


                                    } catch (JSONException e) {


                                        e.printStackTrace();


                                    }


                                }


                            }, new Response.ErrorListener() {


                                @Override


                                public void onErrorResponse(VolleyError error) {
//                                if (error.getMessage() != null)
//                                    Log.d("11111111",error.getMessage());
                                    VolleyLog.e("error",error.getMessage());
//                                    try {
//                                        byte[] htmlBodyBytes = error.networkResponse.data;
//                                        Log.e(LOG_TAG, new String(htmlBodyBytes), error);
//                                    } catch (NullPointerException e) {
//                                        e.printStackTrace();
//                                    }
//



                                }

                            }) ;

                    requestQueue.add(jsonObjectRequest);


            } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}




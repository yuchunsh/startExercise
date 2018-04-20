package com.example.jenny.startexercise.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Utils.Home2ListAdapter;
import com.example.jenny.startexercise.models.Home2item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

//import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by jenny on 2018/3/1.
 */

public class Home2Fragment extends Fragment {
    private static final String TAG = "Home2Fragment";

    //vars
    private ListView mListView;
    private Button heart;
    private ArrayList<Home2item> mHome2items;
    private Home2ListAdapter mAdapter;
    private String HTTP_URL = "http://140.119.19.36:80/home2.php";
    private String FinalJSonObject;
    private FirebaseAuth mAuth;
    private Boolean isUser = Boolean.FALSE;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getContext());
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        View rl = inflater.inflate(R.layout.layout_home2_listitem, container, false);
        mListView = (ListView) view.findViewById(R.id.home2_lv);
        mHome2items = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Anonymous sign in success!");
                    isUser = Boolean.TRUE;
//                    Toast.makeText(getActivity(),"匿名登入成功 uid:\n" + mAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"匿名登入失敗",Toast.LENGTH_LONG).show();         }
            }
        });
        getHome2item();

//        animationView = view.findViewById(R.id.animation_view);


//        heart = (Button) rl.findViewById(R.id.send_heart_btn);
//        heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: heart onclick");
////                Drawable d = getResources().getDrawable(R.drawable.ic_fullheart);
////                heart.setBackground(d);
//                sendMessage(view);
//            }
//        });
//        heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: heart onclick");
//                Drawable d = getResources().getDrawable(R.drawable.ic_fullheart);
//                heart.setBackground(d);
//                sendMessage(view);
//            }
//        });


        return view;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void getHome2item(){
        Log.d(TAG, "getHome2item: getting home2item");
        StringRequest stringRequest = new StringRequest(HTTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new Home2Fragment.ParseJSonDataClass(getActivity()).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

        public Context context;


        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                // Checking whether FinalJSonObject is not equals to null.
                if (FinalJSonObject != null) {

                    // Creating and setting up JSON array as null.
                    JSONArray jsonArray = null;
                    try {

                        // Adding JSON response object into JSON array.
                        jsonArray = new JSONObject(FinalJSonObject).getJSONArray("data");

                        // Creating JSON Object.
                        JSONObject jsonObject;

                        // Creating Subject class object.
                        Home2item home2item;

                        // Defining CustomSubjectNamesList AS Array List.
                        mHome2items = new ArrayList<Home2item>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            home2item = new Home2item();

                            jsonObject = jsonArray.getJSONObject(i);

                            home2item.setContent(jsonObject.getString("content"));
                            home2item.setDate(convertTime(jsonObject.getString("date")));
                            home2item.setPic_path(jsonObject.getString("icon_url"));


                            // Adding subject list object into CustomSubjectNamesList.
                            mHome2items.add(home2item);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)

        {
            display();

        }
    }

    private void display(){
        if(mHome2items != null){
            Collections.sort(mHome2items, new Comparator<Home2item>() {
                @Override
                public int compare(Home2item o1, Home2item o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

            mAdapter = new Home2ListAdapter(getActivity(), R.layout.layout_home2_listitem, mHome2items);
            mListView.setAdapter(mAdapter);
        }
    }

    private String convertTime(String date){
        Long postDate = Long.parseLong(date);
        Long currentTime = System.currentTimeMillis();
        Long delta = currentTime - postDate;
        int secs = (int)(delta/1000);
        int mins = secs / 60;
        int hours = mins / 60;
        mins = mins % 60;
        int days = hours / 24;
        if (days > 7){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(postDate);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            return String.valueOf(mMonth) + "月" + String.valueOf(mDay) + "日";

        }else if (days > 0){
            return String.valueOf(days) + "天前";
        }else if (hours > 0){
            return String.valueOf(hours) + "小時前";
        }else if (mins > 0){
            return String.valueOf(mins) + "分鐘前";
        }else {
            return "剛剛";
        }

    }


}








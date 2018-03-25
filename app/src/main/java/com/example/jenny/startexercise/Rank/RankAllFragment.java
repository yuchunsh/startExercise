package com.example.jenny.startexercise.Rank;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Utils.RankListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankAllFragment extends Fragment {

    private static final String TAG = "RankALlFragment";

    //vars
    private ListView mListView;
    private RankListAdapter mAdapter;
    private ArrayList<String> mUsers;
    private String HTTP_URL = "http://140.119.19.36:80/rank.php";
    private String FinalJSonObject;
    private HashMap<String,Long> rankAllMap;
    private List<HashMap<String, Long>> rankList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rank_all, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mUsers = new ArrayList<>();

        getRankData();

        return view;
    }

    private void getRankData(){
        Log.d(TAG, "getRankData: getting rank data");
        StringRequest stringRequest = new StringRequest(HTTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new RankAllFragment.ParseJSonDataClass(getActivity()).execute();

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

    /**
     * Returns a sorted ArrayList of ranking data
     * @param map
     */
    private List<HashMap<String, Long>> sortRankData(HashMap<String, Long> map){
        Log.d(TAG, "sortRankData: sorting rank map");

        ArrayList<Map.Entry<String,Long>> entryList = new ArrayList<>(map.entrySet());

        Comparator<Map.Entry<String,Long>> sortByValue = (e1, e2) ->{return Long.compare(e1.getValue(), e2.getValue());};
        Collections.sort(entryList,Collections.reverseOrder(sortByValue));
        Log.d(TAG, "sortRankData: entryList: " + entryList);

        ArrayList<HashMap<String, Long>> sortedRankList = new ArrayList<>();

        for (HashMap.Entry e: entryList){
            HashMap<String, Long> hashMap = new HashMap<>();
            hashMap.put((String)e.getKey(), (Long)e.getValue());
            sortedRankList.add(hashMap);
        }

        Log.d(TAG, "sortRankData: sortedRankList: " + sortedRankList);

        return sortedRankList;

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
//                        Photo photo;
                        long exerciseTime ;
                        rankAllMap = new HashMap<>();

                        // Defining CustomSubjectNamesList AS Array List.
//                        mPhotos = new ArrayList<Photo>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonObject = jsonArray.getJSONObject(i);

//                            photo.setCaption(jsonObject.getString("content"));
//                            photo.setUser_name(jsonObject.getString("name"));
//                            photo.setDate_created(jsonObject.getString("date"));
//                            photo.setImage_path(jsonObject.getString("pic_url"));

                            exerciseTime = Long.parseLong(jsonObject.getString("end_time"))
                                    - Long.parseLong(jsonObject.getString("start_time"));
                            Long sumOfExercise = rankAllMap.get(jsonObject.getString("name"));
                            if (sumOfExercise == null){
                                rankAllMap.put(jsonObject.getString("name"), exerciseTime);
                            }else {
                                rankAllMap.put(jsonObject.getString("name"), exerciseTime + sumOfExercise);
                            }


                            // Adding subject list object into CustomSubjectNamesList.
//                            mPhotos.add(photo);
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
            rankList = sortRankData(rankAllMap);
            mAdapter = new RankListAdapter(getActivity(), R.layout.layout_ranklist_item, rankList);
            mListView.setAdapter(mAdapter);

        }
    }



}

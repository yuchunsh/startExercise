package com.example.jenny.startexercise.Rank;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jenny.startexercise.R;
import com.example.jenny.startexercise.Utils.SectionsPagerAdapter;
import com.example.jenny.startexercise.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RankActivity extends AppCompatActivity {

    private static final String TAG = "RankActivity";

    private ViewPager mViewPager;
    private Context mContext = RankActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Log.d(TAG, "onCreate: starting..");

        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        setupViewPager();
        initImageLoader();
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Responsible for adding two fragment: RankAll fragment and RankByType fragment
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RankAllFragment()); //index 0
        adapter.addFragment(new RankByExerciseFragment());//index 1
        mViewPager.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
//        mViewPager.invalidateBullets(adapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("總排名");
        tabLayout.getTabAt(1).setText("運動分類排名");

    }

}

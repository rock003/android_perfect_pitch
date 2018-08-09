package com.bunnybun.perfectpitch;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = MainActivity.class.getSimpleName();
    final static String DATA_FILE_NAME = "user_data";
    final static String[] SOUNDS = AppCore.getSOUNDS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loadUserData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(getResources().getIdentifier("tab_label_dashboard", "string", getPackageName()))));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(getResources().getIdentifier("tab_label_quiz", "string", getPackageName()))));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadUserData() {
        String jsonData;

        if (isDataFileExist()) {
            jsonData = readDataFile();
        } else {
            createDataFile();

            jsonData = readDataFile();
        }
    }

    private String readDataFile() {
        String jsonData = "";

        try {
            FileInputStream dataFile = openFileInput(DATA_FILE_NAME);

            jsonData = dataFile.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    private boolean isDataFileExist() {
        boolean result = false;

        String[] fileList = fileList();

        for (String s : fileList) {
            if (s.indexOf(DATA_FILE_NAME) >= 0) {
                result = true;
            }
        }

        return result;
    }

    private void createDataFile() {
        String data = defaultData(SOUNDS);

//        Log.v(LOG_TAG, data);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String defaultData(String[] notes) {
        String str;

        JSONObject stats = new JSONObject();
        JSONObject results = new JSONObject();
        JSONObject json = new JSONObject();

        try {
            stats.put("total", 0);
            stats.put("correct", 0);

            for (String s: notes) {
                results.put(s, stats);
            }

            json.put("results", results);

            str = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();

            str = "";
        }

        return str;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

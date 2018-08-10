package com.bunnybun.perfectpitch;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    final static String DATA_FILE_NAME = "user_data";

    private AppCore appCore;

//    Context mainContext;

//    String user_data;
    TextView statsTextView;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // this to fix new fragment on top of old fragment issue
        if (container != null) {
            container.removeAllViews();
        }

        appCore = AppCore.getInstance();

        statsTextView = (TextView) view.findViewById(R.id.stats);

        statsTextView.setText(appCore.generateStatText());
//
//        statsTextView.setText(user_data);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        mainContext = context;
//
//        readUserData();
    }

//    public void readUserData() {
//        try {
//            FileInputStream inputStream = mainContext.openFileInput(DATA_FILE_NAME);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                String receiveStr = "";
//
//                user_data = "";
//
//
//                while ((receiveStr = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(receiveStr);
//                }
//
//                inputStream.close();
//
//                user_data = stringBuilder.toString();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

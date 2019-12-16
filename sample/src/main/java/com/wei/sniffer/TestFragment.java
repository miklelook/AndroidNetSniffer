package com.wei.sniffer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("shuxin.wei", "onCreate: 1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(inflater.getContext());
        textView.setText("akasfdhaskj");
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        return textView;
    }
}

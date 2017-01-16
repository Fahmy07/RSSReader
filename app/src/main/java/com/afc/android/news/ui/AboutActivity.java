package com.afc.android.news.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.afc.android.news.R;

public class AboutActivity extends AppCompatActivity {

    private TextView mNameTextView;
    private TextView mEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About");

        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mEmailTextView = (TextView) findViewById(R.id.emailTextView);

        mNameTextView.setText("Developed by: AhmedFahmy\n");
        mEmailTextView.setText("Email: AhmedFahmy04@gmail.com");
    }
}

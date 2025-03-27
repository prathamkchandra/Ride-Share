package com.example.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BasicViewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_views); // Ensure activity_basic_views.xml exists in res/layout
    }
}

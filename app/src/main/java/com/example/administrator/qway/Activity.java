package com.example.administrator.qway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Activity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                boolean isGuide=sharedPreferences.getBoolean("first",false);
                if (isGuide){
                    intent=new Intent(Activity.this,MainActivity.class);
                }else {
                    intent=new Intent(Activity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000);
    }
}

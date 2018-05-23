package com.example.administrator.qway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton bt_Check;
    private ImageButton bt_Pandian;
    private ImageButton bt_data;
    private ImageButton bt_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bt_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CheckActivity.class);
                startActivity(intent);
            }
        });
        bt_Pandian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PandianActivity.class);
                startActivity(intent);
            }
        });
        bt_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Intent intent=new Intent(MainActivity.this,DataActivity.class);
                startActivity(intent);
            }
        });
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        bt_Check= (ImageButton) findViewById(R.id.check_bt);
        bt_Pandian= (ImageButton) findViewById(R.id.pandian_bt);
        bt_data= (ImageButton) findViewById(R.id.data_bt);
        bt_setting= (ImageButton) findViewById(R.id.setting_bt);
    }


}

package com.example.administrator.qway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    private Button mQuit;
    private TextView mUser;
    private TextView register;
    private View mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mQuit= (Button) findViewById(R.id.bt_quit);
        mUser= (TextView) findViewById(R.id.user);
        register= (TextView) findViewById(R.id.tv_register);
        mRegister=this.findViewById(R.id.bt_register);
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("first", false);
                editor.commit();
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        String username =sp.getString("username", "ltw");
        String department=sp.getString("department","财务部");
        mUser.setText("当前用户:"+username);
        register.setText(department);
    }
}

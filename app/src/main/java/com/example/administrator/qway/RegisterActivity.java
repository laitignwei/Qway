package com.example.administrator.qway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUser;
    private EditText mPassword;
    private Button mRegister;
    private Spinner sp;
    String department;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        List<String> list = new ArrayList<String>();
        list.add("财务部");
        list.add("外贸部");
        list.add("市场部");
        list.add("制造部");
        list.add("生产部");
        list.add("OEM事业部");
        list.add("开发部");
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        sp.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器

                department=adapter.getItem(position);
            }
            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = register();
                            //login()为向php服务器提交请求的函数，返回数据类型为int
                            if (result == 1) {
                                Log.e("log_tag", "注册成功！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                finish();
                                Looper.loop();
                            } else if (result == -2) {
                                Log.e("log_tag", "注册失败！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -1) {
                                Log.e("log_tag", "已存在该用户！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "已存在该用户！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
            }
        });
    }
    private int register() throws IOException {
        int returnResult=0;
     /*获取用户名和密码*/
        String username=mUser.getText().toString();
        String password=mPassword.getText().toString();
        if(username==null||username.length()<=0){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"请输入账号", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;

        }
        if(password==null||password.length()<=0){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"请输入密码", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        String urlstr="http://szqwayoa.dns0755.net:82/Qwayserver/Register.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http= (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        String params="username="+username+'&'+"password="+password+'&'+"department="+department;
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out=http.getOutputStream();
        out.write(params.getBytes());//post提交参数
        out.flush();
        out.close();
        //读取网页返回的数据
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        String line="";
        StringBuilder sb=new StringBuilder();//建立输入缓冲区
        while (null!=(line=bufferedReader.readLine())){//结束会读入一个null值
            sb.append(line);//写缓冲区
        }
        String result= sb.toString();//返回结果

        try {

        /*获取服务器返回的JSON数据*/
            Log.i("ddd",result);
            JSONObject jsonObject= new JSONObject(result);
            returnResult=jsonObject.getInt("status");//获取JSON数据中status字段值
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
        }
        return returnResult;
    }
    private void initView() {
        mUser= (EditText) findViewById(R.id.user_register);
        mPassword= (EditText) findViewById(R.id.password_register);
        mRegister= (Button) findViewById(R.id.registerBtn);
        sp=(Spinner) findViewById(R.id.spinner);
    }
}

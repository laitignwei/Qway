package com.example.administrator.qway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataActivity extends AppCompatActivity {
    private Button mUpload;
    private Button mScan;
    private EditText mNumber,mCode,mGoods_name,mDrawer,mAuditor,mWarehouse,mStock,mCurrent_stock,mNote;
    private TextClock mSingle_time;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(DataActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                //默认是横屏，加上下面这段代码转换为竖屏
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.setPrompt("扫描条形码");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = data();
                            //login()为向php服务器提交请求的函数，返回数据类型为int
                            if (result == 1) {
                                Log.e("log_tag", "数据保存成功！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(DataActivity.this, "数据保存成功！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -1) {
                                Log.e("log_tag", "数据保存失败！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(DataActivity.this, "数据保存失败！", Toast.LENGTH_SHORT).show();
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
    //条形码返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
                code=result.getContents();
                mCode.setText(code);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    /*
       *用户登录提交post请求
       * 向服务器提交数据1.username用户名，2.password密码
       * 返回JSON数据{"status":"1","info":"login success"}
       */
    private int data() throws IOException {
        int returnResult=0;
     /*获取表单所有信息*/
        String number=mNumber.getText().toString();
        String goods_name=mGoods_name.getText().toString();
        String drawer=mDrawer.getText().toString();
        String auditor=mAuditor.getText().toString();
        String warehouse=mWarehouse.getText().toString();
        String stock=mStock.getText().toString();
        String current_stock=mCurrent_stock.getText().toString();
        String note=mNote.getText().toString();
        //判断表单各项是否为空
        if(code==null||code.length()<=0){
            Looper.prepare();
            Toast.makeText(DataActivity.this,"未扫描条码", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return 0;

        }
        String urlstr="http://szqwayoa.dns0755.net:82/Qwayserver/Upload.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http= (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        String params="code="+code+'&'+"goods_name="+goods_name+'&'+"drawer="+drawer+'&'+"auditor="+auditor+'&'+"warehouse="+warehouse+'&'+"stock="+stock+'&'+"current_stock="+current_stock+'&'+"note="+note;
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
        String rs_upload= sb.toString();//返回结果

        try {

        /*获取服务器返回的JSON数据*/
            Log.i("lll",rs_upload);
            JSONObject jsonObject= new JSONObject(rs_upload);
            returnResult=jsonObject.getInt("status");//获取JSON数据中status字段值
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
        }
        return returnResult;
    }
    private void initView() {
        mUpload= (Button) findViewById(R.id.upload);
        mScan= (Button) findViewById(R.id.scan);
        mNumber= (EditText) findViewById(R.id.number);
        mCode= (EditText) findViewById(R.id.code);
        mGoods_name= (EditText) findViewById(R.id.goods_name);
        mDrawer= (EditText) findViewById(R.id.drawer);
        mAuditor= (EditText) findViewById(R.id.auditor);
        mWarehouse= (EditText) findViewById(R.id.warehouse);
        mStock= (EditText) findViewById(R.id.stock);
        mCurrent_stock= (EditText) findViewById(R.id.current_stock);
        mNote= (EditText) findViewById(R.id.note);
    }
}

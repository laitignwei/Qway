package com.example.administrator.qway;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import db.Goods;

public class CheckActivity extends AppCompatActivity {
    private  Button mScan1;
    private Button mUpdate;
    private Button mCheck;
    private TextView mId;
    private TextView mCode;
    private TextView mGood_Name;
    private TextView mStock;
    private TextView mDrawer;
    private TextView mAuditor;
    private TextView mWarehouse;
    private EditText mCurrentStock;
    int re_id;
    String re_code;
    String re_goods_name;
    String re_drawer;
    String re_auditor;
    String re_warehouse;
    int re_stock;
    int re_current_stock;
    int result = 0;
    private static final int JSON = 1;
    private Handler handler;
    //声明条形码
    String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initView();
        mScan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(CheckActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                //默认是横屏，加上下面这段代码转换为竖屏
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.setPrompt("扫描条形码");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();

            }
        });
        //盘点后向服务器发送数据进行数据库修改当前库存
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int result = update();
                        if (result == 1) {
                            Log.e("Update", "数据修改成功！");
                            //Toast toast=null;
                            Looper.prepare();
                            Toast.makeText(CheckActivity.this, "数据修改成功！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else if (result == -1) {
                            Log.e("Update", "数据修改失败！");
                            //Toast toast=null;
                            Looper.prepare();
                            Toast.makeText(CheckActivity.this, "数据修改失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    } catch (IOException e) {

                    }
                }
            }).start();
            }
        });
        //通过条码查询
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = check();
                            if (result == 1) {
                                Log.e("check", "数据查询成功！");
                                Looper.prepare();
                                Toast.makeText(CheckActivity.this, "数据查询成功！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        } catch (IOException e) {

                        }
                    }
                }).start();
            }
        });
        handler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int get_id = bundle.getInt("id");
                String get_code=bundle.getString("code");
                String get_goods_name=bundle.getString("goods_name");
                String get_drawer=bundle.getString("drawer");
                String get_auditor=bundle.getString("auditor");
                String get_warehouse=bundle.getString("warehouse");
                int get_stock=bundle.getInt("stock");
                int get_current_stock=bundle.getInt("current_stock");
                if(msg.what==JSON){
                    mId.setText(Integer.toString(get_id));
                    mCode.setText(get_code);
                    mGood_Name.setText(get_goods_name);
                    mDrawer.setText(get_drawer);
                    mAuditor.setText(get_auditor);
                    mWarehouse.setText(get_warehouse);
                    mStock.setText(Integer.toString(get_stock));
                    mCurrentStock.setText(Integer.toString(get_current_stock));
                }
            }
        };
    }
    //条形码返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_SHORT).show();
                code=result.getContents();
                mCode.setText(code);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    /*
  *用户登录提交post请求
  * 向服务器提交数据1.条码值
  * 返回JSON数据
  * 修改后再发送数据给服务器
  */
    private int check() throws IOException {
        int returnResult=0;
     /*获取表单所有信息*/
        //判断表单各项是否为空
        if(code==null||code.length()<=0){
            Looper.prepare();
            Toast.makeText(CheckActivity.this,"未扫描条码", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return 0;

        }
        String urlstr="http://szqwayoa.dns0755.net:82/Qwayserver/Check.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http= (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        String params="code="+code;
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
            Log.i("check",rs_upload);
            JSONObject jsonObject= new JSONObject(rs_upload);
            JSONArray jsonArray = jsonObject.getJSONArray("check");
            JSONObject content = null;
            content= jsonArray.getJSONObject(0);
            returnResult=content.getInt("status");//获取JSON数据中status字段值
            re_id=content.getInt("id");
            re_goods_name=content.getString("goods_name");
            re_drawer=content.getString("drawer");
            re_auditor=content.getString("auditor");
            re_warehouse=content.getString("warehouse");
            re_code=content.getString("code");
            re_stock=content.getInt("stock");
            re_current_stock=content.getInt("current_stock");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", re_id);
                    bundle.putString("code", re_code);
                    bundle.putString("goods_name", re_goods_name);
                    bundle.putString("drawer",re_drawer);
                    bundle.putString("auditor",re_auditor);
                    bundle.putString("warehouse",re_warehouse);
                    bundle.putInt("stock", re_stock);
                    bundle.putInt("current_stock", re_current_stock);
                    Message msg = new Message();
                    msg.setData(bundle);
                    msg.what=JSON;
                    handler.sendMessage(msg);
                }
            }).start();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("check", "the Error parsing data "+e.toString());
        }
        return returnResult;
    }
    private int update() throws IOException {
        int returnResult=0;
     /*获取表单所有信息*/
        String current_stock=mCurrentStock.getText().toString();
        String urlstr="http://szqwayoa.dns0755.net:82/Qwayserver/Update.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http= (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        String params="current_stock="+current_stock+'&'+"code="+code;
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
        String rs_update= sb.toString();//返回结果

        try {

        /*获取服务器返回的JSON数据*/
            Log.i("check",rs_update);
            JSONObject jsonObject= new JSONObject(rs_update);
            returnResult=jsonObject.getInt("status");//获取JSON数据中status字段值
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
        }
        return returnResult;
    }
    //初始化控件
    private void initView() {
        mScan1= (Button) findViewById(R.id.scan);
        mUpdate= (Button) findViewById(R.id.bt_update);
        mCheck= (Button) findViewById(R.id.bt_check);
        mId= (TextView) findViewById(R.id.id);
        mCode= (TextView) findViewById(R.id.code);
        mGood_Name=(TextView) findViewById(R.id.goods_name);
        mStock= (TextView) findViewById(R.id.stock);
        mDrawer= (TextView) findViewById(R.id.check_drawer);
        mAuditor= (TextView) findViewById(R.id.check_auditor);
        mWarehouse= (TextView) findViewById(R.id.check_warehouse);
        mCurrentStock= (EditText) findViewById(R.id.current_stock);
    }
}

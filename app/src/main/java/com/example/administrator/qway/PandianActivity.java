package com.example.administrator.qway;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import db.Goods;



public class PandianActivity extends Activity {

    private static final int JSON = 1;
    private ListView listView;
    private Button button;
    private Handler handler;
    private ArrayList<Goods> list;
    private TableAdapter adapter;
    private Button bt_search_condition;
    private SearchView mSearchView;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandian);
        initView();
        //设置表格标题的背景颜色
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResult();
            }
        });
        //搜索全部信息的
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String json = bundle.getString("json");
                list = new ArrayList<Goods>();
                if (msg.what == JSON) {
                    //列表数据
                    list = JSONUtil.parseJson(json);
                    Log.i("list", list.toString());
                    adapter = new TableAdapter(getBaseContext(), list);
                    listView.setAdapter(adapter);
                    listView.setTextFilterEnabled(true);
                }
            }
        };
        bt_search_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list=getSearchResult();
//                Log.i("choice",list.toString());
                adapter = new TableAdapter(getBaseContext(), list);
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                text=s;
                if (!TextUtils.isEmpty(s)){
                    listView.setFilterText(s);
                }else{
                    listView.clearTextFilter();
                }
                return false;
            }
        });

    }


    private void getResult() {
        final String uri = "http://szqwayoa.dns0755.net:82/Qwayserver/Pandian.php";
        Log.i("getResult：", "开始获取数据");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                String str = null;
                try {
                    str = getJsonContent(uri);
                } catch (IOException e) {

                }
                Log.i("result", str);
                bundle.putString("json", str);
                Message msg = new Message();
                msg.setData(bundle);
                msg.what = JSON;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public static String getJsonContent(String urlPath) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据

    }
    /**
     * 获取搜索条件,并对list进行搜索
     * */
    public ArrayList<Goods> getSearchResult() {
        ArrayList<Goods> resultList = new ArrayList<Goods>();
//        Log.i("ttttt", list.toString());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).optGoods_name().equals(text.toString())||list.get(i).optGoods_name().indexOf(text.toString()) != -1||list.get(i).optCode().equals(text.toString())||list.get(i).optCode().indexOf(text.toString()) != -1) {
                        resultList.add(list.get(i));

            }
        }
//        Log.i("resultList",resultList.toString());
        return resultList;

    }
//    public String getSort() {
//        PandianActivity m = this;
//        return m.getSort();
//    }
    private void initView() {
        button = (Button) findViewById(R.id.button);
        bt_search_condition = (Button) findViewById(R.id.bt_search_condition);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
    }


}

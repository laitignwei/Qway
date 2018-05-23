package com.example.administrator.qway;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import db.Goods;

public class JSONUtil {
    public static ArrayList<Goods> parseJson(String jsonString){
        ArrayList<Goods> list = new ArrayList<Goods>();
        try {
            JSONObject item = new JSONObject(jsonString);
            JSONArray jsonArray = item.getJSONArray("data");
            JSONObject content = null;
            for(int i=0;i<jsonArray.length();i++){
                content= jsonArray.getJSONObject(i);
                Goods knowledge = new Goods();
                int id=content.getInt("id");
                String goods_name = content.optString("goods_name");
                String code=content.optString("code");
                int stock=content.optInt("stock");
                int current_stock=content.optInt("current_stock");
//                String note=content.optString("note");
                knowledge.setId(id);
                knowledge.setGoods_name(goods_name);
                knowledge.setCode(code);
                knowledge.setStock(stock);
                knowledge.setCurrent_stock(current_stock);
//                knowledge.setNote(note);
                list.add(knowledge);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
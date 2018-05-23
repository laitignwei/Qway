package com.example.administrator.qway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import db.Goods;
import db.MyHScrollView;

public class TableAdapter extends BaseAdapter {

    private ArrayList<Goods> list;
    private LayoutInflater inflater;


    public TableAdapter(Context context, ArrayList<Goods> list){
        this.list = list;
        inflater = LayoutInflater.from(context);

    }


    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }


    public Object getItem(int position) {
        return list.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Goods goods = (Goods) this.getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.goodsId = (TextView) convertView.findViewById(R.id.text_id);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.text_goods_name);
            viewHolder.goodCode = (TextView) convertView.findViewById(R.id.text_codeBar);
            viewHolder.goodStock = (TextView) convertView.findViewById(R.id.text_stock);
            viewHolder.goodCurrStock = (TextView) convertView.findViewById(R.id.text_curStock);
//            viewHolder.goodNote = (TextView) convertView.findViewById(R.id.text_note);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.goodsId.setText(Integer.toString(goods.optId()));
        viewHolder.goodsId.setTextSize(13);
        viewHolder.goodsName.setText(goods.optGoods_name());
        viewHolder.goodsName.setTextSize(13);
        viewHolder.goodCode.setText(goods.optCode());
        viewHolder.goodCode.setTextSize(13);
        viewHolder.goodStock.setText(goods.optStock()+"");
        viewHolder.goodStock.setTextSize(13);
        viewHolder.goodCurrStock.setText(goods.optCurrent_stock()+"");
        viewHolder.goodCurrStock.setTextSize(13);
//        viewHolder.goodNote.setText(goods.optNote()+"");
//        viewHolder.goodNote.setTextSize(13);

        return convertView;
    }
    public static class ViewHolder{
        public TextView goodsId;
        public TextView goodsName;
        public TextView goodCode;
        public TextView goodStock;
        public TextView goodCurrStock;
//        public TextView goodNote;
    }

}

package com.cxh.android_city_select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CityTagSelectGridAdapter extends BaseAdapter {
    private Context context;
    private List<RegionModel> list;
    private OnDeleteTagListener onDeleteTagListener;

    public void setOnDeleteTagListener(OnDeleteTagListener onDeleteTagListener) {
        this.onDeleteTagListener = onDeleteTagListener;
    }

    public CityTagSelectGridAdapter(Context context, List<RegionModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Houlder houlder;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.item_grid_citys,null);
            houlder = new Houlder();
            houlder.textView = convertView.findViewById(R.id.tv_select_city_tag);
            houlder.delete = convertView.findViewById(R.id.iv_delete_tag);
            houlder.ll_deleteTag = convertView.findViewById(R.id.ll_delete_tag_up);
            convertView.setTag(houlder);
        }
        houlder = (Houlder) convertView.getTag();
        houlder.textView.setText(list.get(position).getName());
        final Houlder finalHoulder = houlder;
        houlder.ll_deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteTagListener.onDeleteTag(finalHoulder.delete,position);
            }
        });

        return convertView;
    }
    private static class Houlder {
        private TextView textView;
        private ImageView delete;
        private LinearLayout ll_deleteTag;
    }

    public interface OnDeleteTagListener{
        void onDeleteTag(View view, int position);
    }
}
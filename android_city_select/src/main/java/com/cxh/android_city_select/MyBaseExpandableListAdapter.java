package com.cxh.android_city_select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private List<RegionModel> mProvinceList;
    private List<RegionModel> mCityList;
    private OnShengClickListener onShengClickListener;
    private OnCityClickListener onCityClickListener;
    private Context context;
    private int grouPosition;
    private OnShengCheckListener onShengCheckListener;
    private OnCityCheckListener onCityCheckListener;
    void setOnCityCheckListener(OnCityCheckListener onCityCheckListener) {
        this.onCityCheckListener = onCityCheckListener;
    }

    void setOnShengCheckListener(OnShengCheckListener onShengCheckListener) {
        this.onShengCheckListener = onShengCheckListener;
    }


    MyBaseExpandableListAdapter(List<RegionModel> mProvinceList, List<RegionModel> mCityList, Context context) {
        this.mProvinceList = mProvinceList;
        this.mCityList = mCityList;
        this.context = context;
    }

    void setGrouPosition(int position) {
        this.grouPosition = position;
    }

    void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    void setOnShengClickListener(OnShengClickListener onShengClickListener) {
        this.onShengClickListener = onShengClickListener;
    }

    @Override
    public int getGroupCount() {
        return mProvinceList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCityList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mProvinceList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCityList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ProHodler tag ;
        if (convertView == null) {
            tag = new ProHodler();
            convertView = View.inflate(context, R.layout.item_sheng_picker, null);
            tag.textView = convertView.findViewById(R.id.tv_sheng);
            tag.checkBox = convertView.findViewById(R.id.area_sheng_cb);
            convertView.setTag(tag);
        }
        tag = (ProHodler) convertView.getTag();
        tag.checkBox.setTag(groupPosition);
        tag.textView.setText(mProvinceList.get(groupPosition).getName());
        if (grouPosition == groupPosition && groupPosition != 0) {
            tag.textView.setBackgroundColor(context.getResources().getColor(R.color.bg_f4));
        } else {
            tag.textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        final ProHodler finalTag = tag;
        tag.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShengClickListener.onShengClick(finalTag.textView, groupPosition);
            }
        });

        tag.checkBox.setChecked(mProvinceList.get(groupPosition).isChecked());
        tag.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShengCheckListener.onShengCheck(finalTag.checkBox.isChecked(), groupPosition);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CityHodler tag;
        if (convertView == null) {
            tag = new CityHodler();
            convertView = View.inflate(context, R.layout.item_city_picker, null);
            tag.textView = convertView.findViewById(R.id.tv_city_address);
            tag.checkBox = convertView.findViewById(R.id.cb_area_city);
            convertView.setTag(tag);
        }

        tag = (CityHodler) convertView.getTag();
        tag.textView.setText(mCityList.get(childPosition).getName());
        final CityHodler finalTag = tag;
        tag.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityClickListener.onCityClick(finalTag.textView, childPosition);
            }
        });

        tag.checkBox.setChecked(mCityList.get(childPosition).isChecked());
        tag.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityCheckListener.onCityCheck(finalTag.checkBox, childPosition, finalTag.checkBox.isChecked());
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

   private static class ProHodler {
        private TextView textView;
        private CheckBox checkBox;
    }

    private static class CityHodler {
        private TextView textView;
        private CheckBox checkBox;
    }

    public interface OnCityClickListener {
        void onCityClick(View view, int position);
    }

    public interface OnShengCheckListener {
        void onShengCheck(boolean isChecked, int position);
    }

    public interface OnCityCheckListener {
        void onCityCheck(View view, int position, boolean isChecked);
    }

    public interface OnShengClickListener {
        void onShengClick(View view, int position);
    }
}
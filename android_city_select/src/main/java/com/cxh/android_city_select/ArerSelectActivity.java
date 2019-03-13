package com.cxh.android_city_select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;



/**
 * @author chenxh
 */
public class ArerSelectActivity extends AppCompatActivity {

    private static final String TAG = "省市选择";
    private TagLayout cxhAreaSelectLayout;
    private DrawerLayout drawerLayout;
    private TextView tv_tag_ok;
    private ExpandableListView expandableListView;
    private GridView gridView;
    private TextView tv_finish;
    private TextView title;
    private RegionDao mRegionDao;
    private List<RegionModel> mProvinceList;
    private List<RegionModel> mCityList;
    private List<RegionModel> mAreaList;
    private Context context = this;
    private RegionModel regionModel;
    private LayoutInflater inflater;
    private MyBaseExpandableListAdapter baseExpandableListAdapter;
    private List<RegionModel> selectTags;
    private int sign = -1;//控制列表的展开
    private CityTagSelectGridAdapter cityTagSelectGridAdapter;
    private ArrayList<String> selectCitys = new ArrayList<>();
    public static final String ACTION_SELECT_CITY = "action_select_city";
    public static final int ACTION_SELECT_CITYS_CODE = 0x6F;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areaselect);
        DBCopyUtil.copyDataBaseFromAssets(this,"region.db");
        initView();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        title.setText("选择省市区");
        findViewById(R.id.title);
        initData();
    }

    private void initView() {
//        ListView shengGrid = this.findViewById(R.id.girdView);
//        ListView shi_gridview = this.findViewById(R.id.shi_gridview);
        this.findViewById(R.id.left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArerSelectActivity.this.finish();
            }
        });
        this.cxhAreaSelectLayout = this.findViewById(R.id.csl_select);
        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        this.tv_tag_ok = this.findViewById(R.id.close_tags);
        this.expandableListView = this.findViewById(R.id.expandablelistview);
        this.gridView = this.findViewById(R.id.gv_tags);
        this.tv_finish = this.findViewById(R.id.tv_finish);
        this.title = this.findViewById(R.id.title);

    }

    private void initData() {
        mRegionDao = new RegionDao(this);
        mProvinceList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mAreaList = new ArrayList<>();
        selectTags = new ArrayList<>();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        cityTagSelectGridAdapter = new CityTagSelectGridAdapter(context, selectTags);
        cityTagSelectGridAdapter.setOnDeleteTagListener(new CityTagSelectGridAdapter.OnDeleteTagListener() {
            @Override
            public void onDeleteTag(View view, int position) {
                selectTags.get(position).setChecked(false);
                selectTags.remove(position);
                for (int x = 0 ; x < mProvinceList.size() ; x ++)
                {
                    if (expandableListView.isGroupExpanded(x))
                    {
                        expandableListView.collapseGroup(x);
                        expandableListView.expandGroup(x);
                    }
                }

                baseExpandableListAdapter.notifyDataSetChanged();
                cityTagSelectGridAdapter.notifyDataSetChanged();
            }
        });
        gridView.setAdapter(cityTagSelectGridAdapter);
        mProvinceList = mRegionDao.loadProvinceList();
        tv_tag_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.END);
            }
        });
        this.baseExpandableListAdapter = new MyBaseExpandableListAdapter(mProvinceList, mCityList, context);
        this.expandableListView.setAdapter(baseExpandableListAdapter);
        this.baseExpandableListAdapter.setOnShengClickListener(new MyBaseExpandableListAdapter.OnShengClickListener() {
            @Override
            public void onShengClick(View view, int position) {
                regionModel = mProvinceList.get(position);
                mCityList.clear();
                mCityList.addAll(mRegionDao.loadCityList(regionModel.getId()));
                baseExpandableListAdapter.setGrouPosition(position);
                if (sign == -1) {
                    expandableListView.expandGroup(position);
                    expandableListView.setSelectedGroup(position);
                    sign = position;
                } else if (sign == position) {
                    expandableListView.collapseGroup(sign);
                    sign = -1;
                } else {
                    expandableListView.collapseGroup(sign);
                    expandableListView.expandGroup(position);
                    expandableListView.setSelectedGroup(position);
                    sign = position;
                }
            }
        });

        this.baseExpandableListAdapter.setOnShengCheckListener(new MyBaseExpandableListAdapter.OnShengCheckListener() {
            @Override
            public void onShengCheck(boolean isChecked, int position) {
                String str = mProvinceList.get(position).getName();
                if (isChecked) {
                    selectTags.add(mProvinceList.get(position));
                    mProvinceList.get(position).setChecked(true);
                } else {
                    mProvinceList.get(position).setChecked(false);
                    for (int x = 0; x < selectTags.size(); x++) {
                        if (selectTags.get(x).getName().equals(str)) {
                            selectTags.remove(x);
                        }
                    }
                }
                cityTagSelectGridAdapter.notifyDataSetChanged();
            }
        });
        this.baseExpandableListAdapter.setOnCityClickListener(new MyBaseExpandableListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(View view, int position) {
                mAreaList.clear();
                mAreaList = mRegionDao.loadCountyList(mCityList.get(position).getId());
                if (!mAreaList.isEmpty()) {
                    cxhAreaSelectLayout.setAdapter(new TagAdapter<RegionModel>(mAreaList) {
                        @Override
                        public View getView(CxhAreaSelectLayout parent, int position, RegionModel regionModel) {
                            View view1 =  inflater.inflate(R.layout.item_textview_tag, cxhAreaSelectLayout, false);
                            TextView textView = view1.findViewById(R.id.arer_select_tv);
                            textView.setText(regionModel.getName());
                            return view1;
                        }
                    });
                    drawerLayout.openDrawer(Gravity.END);
                }
            }
        });
        this.baseExpandableListAdapter.setOnCityCheckListener(new MyBaseExpandableListAdapter.OnCityCheckListener() {
            @Override
            public void onCityCheck(View view, int position, boolean isChecked) {
                if (isChecked) {
                    selectTags.add(mCityList.get(position));
                    mCityList.get(position).setChecked(true);
                } else {
                    mCityList.get(position).setChecked(false);
                    for (int x = 0; x < selectTags.size(); x++) {
                        if (selectTags.get(x).getName().equals(mCityList.get(position).getName())) {
                            selectTags.remove(x);
                        }
                    }
                }
                cityTagSelectGridAdapter.notifyDataSetChanged();
            }
        });
        this.expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int x = 0; x < mProvinceList.size(); x++) {
                    if (x != groupPosition) {
                        expandableListView.collapseGroup(x);
                    }
                }
            }
        });
        this.cxhAreaSelectLayout.setOnTagClickListener(new TagLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, CxhAreaSelectLayout parent) {
                Log.d(TAG, mAreaList.get(position).getName());
                if (selectTags.size() == 0) {
                    selectTags.add(mAreaList.get(position));
                } else {
                    selectTags.add(mAreaList.get(position));
                    LinkedHashSet<RegionModel> linkedHashSet = new LinkedHashSet<>(selectTags.size());
                    linkedHashSet.addAll(selectTags);
                    selectTags.clear();
                    selectTags.addAll(linkedHashSet);
                }
                cityTagSelectGridAdapter.notifyDataSetChanged();
                return true;
            }
        });
        tv_finish.setVisibility(View.VISIBLE);
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int x= 0 ; x < selectTags.size();x++)
                {
                   selectCitys.add(selectTags.get(x).getName()+",");
                }
                onSelectOk();
            }
        });
    }

    private void onSelectOk() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ACTION_SELECT_CITY,selectCitys);
        ArerSelectActivity.this.setResult(ACTION_SELECT_CITYS_CODE,intent);
        ArerSelectActivity.this.finish();
    }

}

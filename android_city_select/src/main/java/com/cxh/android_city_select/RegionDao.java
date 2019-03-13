package com.cxh.android_city_select;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * chenxh
 */
public class RegionDao {

    private Context mContext;
    private SQLiteDatabase db;

    public RegionDao(Context context) {
        this.mContext = context;
        this.db = RegionDBHelper.getInstance(context).getReadableDatabase();
    }

    public void closeDb()
    {
        db.close();
    }

    /**
     * 加载省份
     *
     */
    public List<RegionModel> loadProvinceList() {
        List<RegionModel> provinceList = new ArrayList<>();

        String sql = "SELECT ID,NAME FROM PROVINCE";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String name = cursor.getString(1);

            RegionModel regionModel = new RegionModel();
            regionModel.setId(id);
            regionModel.setName(name);

            provinceList.add(regionModel);
        }

        return provinceList;
    }


    public List<RegionModel> loadCityList(Long provinceId) {
        List<RegionModel> provinceList = new ArrayList<>();

        String sql = "SELECT ID,NAME FROM CITY WHERE PID = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(provinceId)});
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String name = cursor.getString(1);

            RegionModel regionModel = new RegionModel();
            regionModel.setId(id);
            regionModel.setName(name);
            provinceList.add(regionModel);
        }

        return provinceList;
    }


    public List<RegionModel> loadCountyList(Long cityId) {
        List<RegionModel> provinceList = new ArrayList<>();
        String sql = "SELECT ID,NAME FROM AREA WHERE PID = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(cityId)});
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String name = cursor.getString(1);

            RegionModel regionModel = new RegionModel();
            regionModel.setId(id);
            regionModel.setName(name);

            provinceList.add(regionModel);
        }
        return provinceList;
    }

}
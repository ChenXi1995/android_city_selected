package com.cxh.android_city_select;

/**
 * 地区
 * chenxh
 */
public class RegionModel {

    private Long id;
    private Long pid;
    private String name;
    private boolean isChecked = false;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId().equals(((RegionModel)obj).getId());
    }
}
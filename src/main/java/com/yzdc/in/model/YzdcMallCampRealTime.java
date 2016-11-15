package com.yzdc.in.model;

/**
 * Desc:    实时获取活动数据
 * Author: Iron
 * CreateDate: 2016-11-14
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class YzdcMallCampRealTime {


    private int report_date;
    private int org_id;
    private String org_name = "";
    private int active_number;
    private int org_type;
    private int data_type;
    private String product_date = "";

    public int getReport_date() {
        return report_date;
    }

    public void setReport_date(int report_date) {
        this.report_date = report_date;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public int getActive_number() {
        return active_number;
    }

    public void setActive_number(int active_number) {
        this.active_number = active_number;
    }

    public int getOrg_type() {
        return org_type;
    }

    public void setOrg_type(int org_type) {
        this.org_type = org_type;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("YzdcMallCampRealTime{");
        sb.append("report_date=").append(report_date);
        sb.append(", org_id=").append(org_id);
        sb.append(", org_name='").append(org_name).append('\'');
        sb.append(", active_number=").append(active_number);
        sb.append(", org_type=").append(org_type);
        sb.append(", data_type=").append(data_type);
        sb.append(", product_date='").append(product_date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

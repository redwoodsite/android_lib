package com.sjwlib.widget.cityselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjwlib.R;
import com.sjwlib.widget.cityselect.adapter.CityAdapter;
import com.sjwlib.widget.cityselect.adapter.DiscAdapter;
import com.sjwlib.widget.cityselect.adapter.ProvinceAdapter;
import com.sjwlib.widget.cityselect.bean.City;
import com.sjwlib.widget.cityselect.bean.Province;
import com.sjwlib.core.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;

public class CitySelectActivity extends Activity {
    private String selType;
    private String province = "", city = "";
    private JSONArray array;

    private List<Province> mAllProvinces;
    private ProvinceAdapter mProvinceAdapter;
    private List<City> mAllCitys;
    private CityAdapter mCityAdapter_city;
    private List<String> mAllDistrict;
    private DiscAdapter mDisctrictAdapter;

    TextView tvArea;
    GridView gvProvince;
    GridView gvCity;
    GridView gvDistrict;
    ImageView ivBack;
    ImageView ivClear;

//    @OnClick(R.id.lbl_OK) void ok() {
//        Intent intent = new Intent();
//        intent.putExtra("address", tvArea.getText().toString());
//        setResult(RESULT_OK, intent);
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityselect);
        //ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvArea = (TextView) findViewById(R.id.tvArea);
        gvProvince = (GridView) findViewById(R.id.gvProvince);
        gvCity = (GridView) findViewById(R.id.gvCity);
        gvDistrict = (GridView) findViewById(R.id.gvDistrict);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivClear = (ImageView) findViewById(R.id.ivClear);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvArea.getText().toString();
                int len = area.split("/").length;
                if (area.equals("城市")) {    // 省 返回：取消并退出
                    finish();
                } else if (len == 2) { // 区返回：到市
                    int index = area.lastIndexOf("/");
                    area = area.substring(0, index);
                    tvArea.setText(area);
                    gvCity.setVisibility(View.VISIBLE);
                    gvProvince.setVisibility(View.GONE);
                    gvDistrict.setVisibility(View.GONE);
                } else if (len == 1) {  // 市返回：到省
                    tvArea.setText("城市");
//                    int index = area.lastIndexOf("/");
//                    area = area.substring(0, index);
//                    tvArea.setText(area);
                    gvProvince.setVisibility(View.VISIBLE);
                    gvDistrict.setVisibility(View.GONE);
                    gvCity.setVisibility(View.GONE);
                }
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvArea.setText("");
                commit();
            }
        });

        String jsonFile = "city.json";
        selType = getIntent().getStringExtra("seltype");
        if (selType != null && !"".equals(selType)) {
            jsonFile = selType + ".json";
        }
        String jsonStr = CommUtil.getJson(getBaseContext(), jsonFile);
        JSONObject root = JSON.parseObject(jsonStr);
        array = root.getJSONArray("citylist");
        initListView_province();
    }

    private void initListView_province() {
        mAllProvinces = getProvice();
        mProvinceAdapter = new ProvinceAdapter(mAllProvinces, this);
        gvProvince.setAdapter(mProvinceAdapter);
        gvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province pro = (Province) parent.getAdapter().getItem(position);
                province = pro.getProvince();
                tvArea.setText(province);
                initListView_city(province);

                gvCity.setVisibility(View.VISIBLE);
                gvProvince.setVisibility(View.GONE);
                gvDistrict.setVisibility(View.GONE);
            }
        });
    }

    private void initListView_city(String iCode) {
        mAllCitys = getCity(iCode);
        if (mAllCitys.size() > 0) {
            if (selType.equals("txdz")) {
                //
            } else {
                City city = new City();
                city.setCityName("确定");
                mAllCitys.add(0, city);
            }
            mCityAdapter_city = new CityAdapter(mAllCitys, this);
            gvCity.setAdapter(mCityAdapter_city);
            gvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    City pro = (City) parent.getAdapter().getItem(position);
                    city = pro.getCityName();

                    if (city.equals("确定")) {
                        commit();
                    } else {
                        initListView_district(province, city);
                        tvArea.setText(province + "/" + city);

                        gvProvince.setVisibility(View.GONE);
                        //gvCity.setVisibility(View.GONE);
                        if (mAllDistrict.size() > 0) {
                            gvDistrict.setVisibility(View.VISIBLE);
                            gvCity.setVisibility(View.GONE);
                        } else {
//                            gvDistrict.setVisibility(View.GONE);
//                            gvCity.setVisibility(View.VISIBLE);
                            commit();
                        }
                    }
                }
            });
        }
    }

    private void initListView_district(String sCode0, String sCode1) {
        mAllDistrict = getDistrict(sCode0, sCode1);
        if (mAllDistrict.size() > 0) {
            if (selType.equals("txdz")) {
                //
            } else {
                mAllDistrict.add(0, "确定");
            }
            mDisctrictAdapter = new DiscAdapter(mAllDistrict, this);
            gvDistrict.setAdapter(mDisctrictAdapter);
            gvDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String disc = (String) parent.getAdapter().getItem(position);
                    if (!disc.equals("确定")) {
                        tvArea.setText(province + "/" + city + "/" + disc);
                    }
                    commit();
                }
            });
        }
    }

    private List<Province> getProvice() {
        List<Province> provinceList = new ArrayList<Province>();
        for(int i=0;i< array.size();i++){
            JSONObject privince = (JSONObject) array.get(i);
            String p = privince.getString("p");
            Province province = new Province();
            province.setProvince(p);
            provinceList.add(province);
        }
        return provinceList;
    }

    private List<City> getCity(String province) {
        List<City> cityList = new ArrayList<City>();
        for(int i=0;i< array.size();i++){
            JSONObject provinceNode = (JSONObject) array.get(i);
            String p = provinceNode.getString("p");
            if(p.equals(province)){
                JSONArray cityNodes = provinceNode.getJSONArray("c");
                for(int j=0;j<cityNodes.size();j++){
                    JSONObject cityNode = (JSONObject)cityNodes.get(j);
                    String c = cityNode.getString("n");
                    City city = new City();
                    city.setCityName(c);
                    cityList.add(city);
                }
            }
        }
        return cityList;
    }

    private List<String> getDistrict(String province, String city) {
        List<String> districtList = new ArrayList<String>();
        for(int i=0;i< array.size();i++){
            JSONObject provinceNode = (JSONObject) array.get(i);
            String p = provinceNode.getString("p");
            if(p.equals(province)){
                JSONArray cityNodes = provinceNode.getJSONArray("c");
                for(int j=0;j<cityNodes.size();j++){
                    JSONObject cityNode = (JSONObject)cityNodes.get(j);
                    String c = cityNode.getString("n");
                    if(c.equals(city)){
                        if(cityNode.containsKey("a")){
                            JSONArray aNodes = cityNode.getJSONArray("a");
                            for(int z=0;z<aNodes.size();z++){
                                JSONObject aNode = aNodes.getJSONObject(z);
                                String a = aNode.getString("s");
                                districtList.add(a);
                            }
                        }
                    }
                }
            }
        }
        return districtList;
    }

    private void commit() {
        Intent intent = new Intent();
        intent.putExtra("address", tvArea.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
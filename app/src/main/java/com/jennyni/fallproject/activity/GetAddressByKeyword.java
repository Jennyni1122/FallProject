package com.jennyni.fallproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.adapter.GetAddressAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2019/3/11.
 */

public class GetAddressByKeyword extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener, GetAddressAdapter.ItemClickListener {

    private RelativeLayout rl_title_bar;
    private TextView tv_main_title, tv_back, tv_switch;
    private GeocodeSearch geocoderSearch;
    private EditText et_address;
    private TextView tv_latlng;
    private RecyclerView recyclerView;
    private GetAddressAdapter adapter;
    private GeocodeAddress geocodeAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getaddressbykeyword);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        initView();

    }

    private void initView() {
        //标题栏
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("选择围栏地址");
        tv_back = findViewById(R.id.tv_back);
        tv_switch = findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("保存");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAddressByKeyword.this.finish();
            }
        });
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("geocenter", geocodeAddress.getLatLonPoint());
                setResult(RESULT_OK, data);
                GetAddressByKeyword.this.finish();
            }
        });

        et_address = (EditText) findViewById(R.id.et_address);
        tv_latlng = (TextView) findViewById(R.id.tv_latlng);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GetAddressAdapter(this, new ArrayList<GeocodeAddress>(), this);
        recyclerView.setAdapter(adapter);
    }


    public void search(View view) {
        // 第一个参数:地址，第二个参数:查询城市
        GeocodeQuery query = new GeocodeQuery(et_address.getText().toString(), "广州");
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求

    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    //地理编码查询回调
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if(geocodeResult!=null){
            Log.e("onGeocodeSearched: ", geocodeResult.toString());
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            Log.e("thread：", Thread.currentThread() + "");
            adapter.addAllData(geocodeAddressList);
        }else {
            Log.e("thread：", Thread.currentThread() + " 搜索异常！");
        }


    }

    @Override
    public void onItemClickListener(int position) {
        geocodeAddress = adapter.getGeocodeAddressList().get(position);
        double latitude = geocodeAddress.getLatLonPoint().getLatitude();
        double longitude = geocodeAddress.getLatLonPoint().getLongitude();
        LatLng latlng = new LatLng(latitude, longitude);
        Log.e("GetAddressByKeyword", "经纬度值:" + geocodeAddress.getLatLonPoint() + "\n位置描述:"
                + geocodeAddress.getFormatAddress());
        tv_latlng.setText(String.format("lat:%s lng:%s", String.valueOf(latlng.latitude), String.valueOf(latlng.longitude)));
    }
}

package com.shijinsz.shijin.ui.ad;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.utils.PopSetectPlace;
import com.hongchuang.ysblibrary.utils.SetText2;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by yrdan on 2018/8/17.
 */

public class FilterActivity extends BaseActivity {
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_male)
    TextView tvMale;
    @BindView(R.id.tv_female)
    TextView tvFemale;
    @BindView(R.id.tv_70s)
    TextView tv70s;
    @BindView(R.id.tv_80s)
    TextView tv80s;
    @BindView(R.id.tv_90s)
    TextView tv90s;
    @BindView(R.id.tv_00s)
    TextView tv00s;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_mom)
    TextView tvMom;
    @BindView(R.id.tv_white)
    TextView tvWhite;
    @BindView(R.id.tv_it)
    TextView tvIt;
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.tv_teacher)
    TextView tvTeacher;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_choice_city)
    TextView tvChoiceCity;
    @BindView(R.id.tv_loca_here)
    TextView tvLocaHere;
    @BindView(R.id.img_range)
    ImageView imgRange;
    @BindView(R.id.tv_range)
    TextView tvRange;
    @BindView(R.id.img_all_city)
    ImageView imgAllCity;
    @BindView(R.id.tv_all_city)
    TextView tvAllCity;
    @BindView(R.id.img_all_area)
    ImageView imgAllArea;
    @BindView(R.id.tv_all_area)
    TextView tvAllArea;
    @BindView(R.id.img_kilo_range)
    ImageView imgKiloRange;
    @BindView(R.id.tv_kilo_range)
    TextView tvKiloRange;
    @BindView(R.id.map)
    TextureMapView map;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_loca)
    TextView tvLoca;
    private BaiduMap baiduMap;
    private PopSetectPlace popSetectPlace;
    private LocationClient mLocationClient;
    private boolean isFirstLoc=true;
    private Marker circle;
    private MarkerOptions ooa;
    private LatLng ll;
    private boolean icChoice=false;
    private int kilo=1000;
    private GeoCoder mSearch;
    private String gender="other";
    private String age="other";
    private String income="other";
    private String job="other";
    private String range_mode="region_country";
    private String range="全国";
    private List<String> interests=new ArrayList<>();
    private String id;

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public int bindLayout() {
        return R.layout.filter_acitivity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.filter));
        id=getIntent().getStringExtra("id");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.pass));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvRight.getText().toString().equals(getString(R.string.pass))){
                    Intent intent = new Intent(mContext,SettingRedBagActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    tvRight.setEnabled(false);
                    mStateView.showLoading();
                    updata();
                }
            }
        });
        View v = map.getChildAt(0);
        popSetectPlace = new PopSetectPlace(mActivity);
        tvKiloRange.setText(String.format(getString(R.string.kilo_1), 1 + ""));
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        baiduMap = map.getMap();
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                ll = mapStatus.target;
                baiduMap.clear();
                ooa = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)).zIndex(9).draggable(true);
                mCurrentMarker = (Marker) baiduMap.addOverlay(ooa);
                if (icChoice) {
                    OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                            .center(ll).stroke(new Stroke(3, 0x784d73b3))
                            .radius(kilo);
                    baiduMap.addOverlay(ooCircle);
                }
//                mCurrentMarker.startAnimation();
            }
        });
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                ll = geoCodeResult.getLocation();
                baiduMap.clear();
                ooa = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)).zIndex(9).draggable(true);
                mCurrentMarker = (Marker) baiduMap.addOverlay(ooa);
                if (icChoice) {
                    OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                            .center(ll).stroke(new Stroke(3, 0x784d73b3))
                            .radius(kilo);
                    baiduMap.addOverlay(ooCircle);
                }
                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult
                        .getLocation()));
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
        getLocation();
    }
    private void updata(){
        Map map = new HashMap();
        map.put("gender",gender);
        map.put("mode","strategy");
        map.put("age",age);
        map.put("income",income);
        map.put("job",job);
        map.put("interests",interests);
        map.put("range_mode",range_mode);
        switch (range_mode){
            case "region_country":
                map.put("range",range);
                break;
            case "region_province":
                map.put("range",guojia+","+sheng);
                break;
            case "region_city":
                map.put("range",guojia+","+sheng+","+shi);
                break;
            case "region_district":
                map.put("range",guojia+","+sheng+","+shi+","+qu);
                break;
            case "region_gps":
                map.put("range",ll.longitude+","+ll.latitude+","+left);
                break;
            case "region_big":
                map.put("range",range);
                break;
        }
        YSBSdk.getService(OAuthService.class).updata_ads(id,map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                tvRight.setEnabled(true);
                Intent intent = new Intent(mContext,SettingRedBagActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                tvRight.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    ToastUtil.showToast("上传失败，请重新上传");
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){

                }

            }
        });
    }
    private String guojia,sheng,shi,qu;
    private boolean isArea=false,city=false,area=false,arange=false;
    @OnClick({R.id.tv_male, R.id.tv_female, R.id.tv_70s, R.id.tv_80s, R.id.tv_90s, R.id.tv_00s, R.id.tv_doctor, R.id.tv_mom, R.id.tv_white, R.id.tv_it, R.id.tv_free, R.id.tv_teacher, R.id.tv_other, R.id.tv_choice_city, R.id.tv_loca_here, R.id.img_range, R.id.tv_range, R.id.img_all_city, R.id.tv_all_city, R.id.img_all_area, R.id.tv_all_area, R.id.img_kilo_range, R.id.tv_kilo_range})
    public void onViewClicked(View view) {
        tvRight.setText(getString(R.string.next));
        switch (view.getId()) {
            case R.id.tv_male:
                gender = "male";
                tvMale.setTextColor(getResources().getColor(R.color.white));
                tvMale.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvFemale.setTextColor(getResources().getColor(R.color.text_33));
                tvFemale.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                break;
            case R.id.tv_female:
                gender = "female";
                tvFemale.setTextColor(getResources().getColor(R.color.white));
                tvFemale.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvMale.setTextColor(getResources().getColor(R.color.text_33));
                tvMale.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_70s:
                age = "70";
                tv70s.setTextColor(getResources().getColor(R.color.white));
                tv70s.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tv80s.setTextColor(getResources().getColor(R.color.text_33));
                tv80s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv90s.setTextColor(getResources().getColor(R.color.text_33));
                tv90s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv00s.setTextColor(getResources().getColor(R.color.text_33));
                tv00s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_80s:
                age = "80";
                tv80s.setTextColor(getResources().getColor(R.color.white));
                tv80s.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tv70s.setTextColor(getResources().getColor(R.color.text_33));
                tv70s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv90s.setTextColor(getResources().getColor(R.color.text_33));
                tv90s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv00s.setTextColor(getResources().getColor(R.color.text_33));
                tv00s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_90s:
                age = "90";
                tv90s.setTextColor(getResources().getColor(R.color.white));
                tv90s.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tv80s.setTextColor(getResources().getColor(R.color.text_33));
                tv80s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv70s.setTextColor(getResources().getColor(R.color.text_33));
                tv70s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv00s.setTextColor(getResources().getColor(R.color.text_33));
                tv00s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_00s:
                age = "00";
                tv00s.setTextColor(getResources().getColor(R.color.white));
                tv00s.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tv80s.setTextColor(getResources().getColor(R.color.text_33));
                tv80s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv90s.setTextColor(getResources().getColor(R.color.text_33));
                tv90s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tv70s.setTextColor(getResources().getColor(R.color.text_33));
                tv70s.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_doctor:
                job="doctor";
                tvDoctor.setTextColor(getResources().getColor(R.color.white));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_mom:
                job="mother";
                tvMom.setTextColor(getResources().getColor(R.color.white));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_white:
                job="white_collar";
                tvWhite.setTextColor(getResources().getColor(R.color.white));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_it:
                job="IT";
                tvIt.setTextColor(getResources().getColor(R.color.white));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_free:
                job="freelance";
                tvFree.setTextColor(getResources().getColor(R.color.white));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_teacher:
                job="teacher";
                tvTeacher.setTextColor(getResources().getColor(R.color.white));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvOther.setTextColor(getResources().getColor(R.color.text_33));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_other:
                job="other";
                tvOther.setTextColor(getResources().getColor(R.color.white));
                tvOther.setBackground(getResources().getDrawable(R.drawable.btn_checked));
                tvDoctor.setTextColor(getResources().getColor(R.color.text_33));
                tvDoctor.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvWhite.setTextColor(getResources().getColor(R.color.text_33));
                tvWhite.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvIt.setTextColor(getResources().getColor(R.color.text_33));
                tvIt.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvFree.setTextColor(getResources().getColor(R.color.text_33));
                tvFree.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvTeacher.setTextColor(getResources().getColor(R.color.text_33));
                tvTeacher.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));
                tvMom.setTextColor(getResources().getColor(R.color.text_33));
                tvMom.setBackground(getResources().getDrawable(R.drawable.btn_unchecked));

                break;
            case R.id.tv_choice_city:
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                popSetectPlace.showPopupWindow(tvChoiceCity, new SetText2() {
                    @Override
                    public void listenResult(String text, String province, String city, String area) {
                        sheng = province;
                        shi=city;
                        qu=area;
                        if (city.length() > 5) {
                            city = city.substring(0, 5) + "..";
                        }
                        if (area.length() > 5) {
                            area = area.substring(0, 5) + "..";
                        }
                        tvChoiceCity.setText(city + " " + area);
                        mSearch.geocode(new GeoCodeOption().city(city).address(area));
                    }
                }, 1);
                break;
            case R.id.tv_loca_here:
                getLocation();
                break;
            case R.id.img_range:
                if (isArea){
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    isArea=false;
                    imgRange.setImageResource(R.mipmap.icon_selected_3);
                    tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                showSouthPop();
                break;
            case R.id.tv_range:
                if (isArea){
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    isArea=false;
                    imgRange.setImageResource(R.mipmap.icon_selected_3);
                    tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                showSouthPop();
                break;
            case R.id.img_all_city:
                if (city){
                    city=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                    tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                city=true;
                isArea=false;
                area=false;
                arange=false;
                clearRange();
                range_mode="region_city";
                tvLoca.setText(getString(R.string.all_city));
                imgAllCity.setImageResource(R.mipmap.icon_selected_4);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_33));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                break;
            case R.id.tv_all_city:
                if (city){
                    city=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                    tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                city=true;
                isArea=false;
                area=false;
                arange=false;
                clearRange();
                range_mode="region_city";
                tvLoca.setText(getString(R.string.all_city));
                imgAllCity.setImageResource(R.mipmap.icon_selected_4);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_33));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                break;
            case R.id.img_all_area:
                if (area){
                    area=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                    tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                area=true;
                isArea=false;
                city=false;
                arange=false;
                clearRange();
                range_mode="region_district";
                tvLoca.setText(getString(R.string.all_area));
                imgAllArea.setImageResource(R.mipmap.icon_selected_4);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                break;
            case R.id.tv_all_area:
                if (area){
                    area=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                    tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                area=true;
                isArea=false;
                city=false;
                arange=false;
                clearRange();
                tvLoca.setText(getString(R.string.all_area));
                range_mode="region_district";
                imgAllArea.setImageResource(R.mipmap.icon_selected_4);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));

                break;
            case R.id.img_kilo_range:
                if (arange){
                    clearRange();
                    arange=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                    tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                arange=true;
                isArea=false;
                city=false;
                area=false;
                imgKiloRange.setImageResource(R.mipmap.icon_selected_4);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                showRangePop();
                break;
            case R.id.tv_kilo_range:
                if (arange){
                    clearRange();
                    arange=false;
                    range_mode="region_country";
                    tvLoca.setText("全国");
                    imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                    tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                    return;
                }
                arange=true;
                isArea=false;
                city=false;
                area=false;
                imgKiloRange.setImageResource(R.mipmap.icon_selected_4);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgRange.setImageResource(R.mipmap.icon_selected_3);
                tvRange.setTextColor(getResources().getColor(R.color.text_999999));
                showRangePop();
                break;
        }
    }
    Marker mCurrentMarker;
    private void getLocation() {
        //开启定位
        mLocationClient = new LocationClient(mContext);
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String city = bdLocation.getCity();
                String area = bdLocation.getDistrict();
                sheng = bdLocation.getProvince();
                shi=bdLocation.getCity();
                qu=bdLocation.getDistrict();
                guojia=bdLocation.getCountry();
                if (null != city && !city.isEmpty()) {
                    if (city.length() > 5) {
                        city = city.substring(0, 5) + "..";
                    }
                    tvChoiceCity.setText(city + "");
                } else {
                    tvChoiceCity.setText("未定位");
                }
                if (null != area && !area.isEmpty()) {
                    if (area.length() > 5) {
                        area = area.substring(0, 5) + "..";
                    }
                    tvChoiceCity.setText(tvChoiceCity.getText().toString() + " " + area);
                    mLocationClient.stop();
//                    baiduMap.setMyLocationEnabled(true);
//
//                    // 构造定位数据
//                    MyLocationData locData = new MyLocationData.Builder()
//                            .accuracy(bdLocation.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                            .direction(100).latitude(bdLocation.getLatitude())
//                            .longitude(bdLocation.getLongitude()).build();
//
//                    // 设置定位数据
//                    baiduMap.setMyLocationData(locData);
                    ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    baiduMap.clear();
                    ooa = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)).zIndex(9).draggable(true);
                    mCurrentMarker = (Marker) baiduMap.addOverlay(ooa);
                    if (icChoice) {
                        OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                                .center(ll).stroke(new Stroke(3, 0x784d73b3))
                                .radius(kilo);
                        baiduMap.addOverlay(ooCircle);
                    }
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    //画圆，主要是这里

                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                } else {
                }
            }
        });
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            mLocationClient.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            mLocationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.start();
                } else {
//                    ToastUtil.showToas;
                    //没有获取到权限，做特殊处理
                }
                break;
            default:
                break;
        }
    }

    private PopupWindow SouthPop;
    private View SouthPopView;
    private TextView northwest;
    private TextView southwest;
    private TextView northeast;
    private TextView southeast;
    private TextView center_china;
    private TextView north_china;
    private TextView south_china;

    public void showSouthPop() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        if (SouthPopView == null)
            SouthPopView = mInflater.inflate(R.layout.south_pop, null);
        if (SouthPop == null) {
            SouthPop = new PopupWindow(SouthPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        northwest = SouthPopView.findViewById(R.id.northwest);
        southwest = SouthPopView.findViewById(R.id.southwest);
        northeast = SouthPopView.findViewById(R.id.northeast);
        southeast = SouthPopView.findViewById(R.id.southeast);
        center_china = SouthPopView.findViewById(R.id.center_china);
        north_china = SouthPopView.findViewById(R.id.north_china);
        south_china = SouthPopView.findViewById(R.id.south_china);
        //设置背景颜色
        SouthPop.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        SouthPop.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        SouthPop.setOutsideTouchable(true);
        SouthPop.showAsDropDown(imgRange);
        northwest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.northwest);
                tvLoca.setText(getString(R.string.northwest));
                tvRange.setText(getString(R.string.northwest));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        southwest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.southwest);
                tvLoca.setText(getString(R.string.southwest));
                tvRange.setText(getString(R.string.southwest));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        northeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.northeast);
                tvLoca.setText(getString(R.string.northeast));
                tvRange.setText(getString(R.string.northeast));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        southeast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.southeast);
                tvLoca.setText(getString(R.string.southeast));
                tvRange.setText(getString(R.string.southeast));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        center_china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.center_china);
                tvLoca.setText(getString(R.string.center_china));
                tvRange.setText(getString(R.string.center_china));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        north_china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.north_china);
                tvLoca.setText(getString(R.string.north_china));
                tvRange.setText(getString(R.string.north_china));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
        south_china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isArea=true;
                arange=false;
                city=false;
                area=false;
                clearRange();
                range_mode="region_big";
                range=getString(R.string.south_china);
                tvLoca.setText(getString(R.string.south_china));
                tvRange.setText(getString(R.string.south_china));
                imgRange.setImageResource(R.mipmap.icon_selected_4);
                tvRange.setTextColor(getResources().getColor(R.color.text_33));
                imgAllCity.setImageResource(R.mipmap.icon_selected_3);
                tvAllCity.setTextColor(getResources().getColor(R.color.text_999999));
                imgKiloRange.setImageResource(R.mipmap.icon_selected_3);
                tvKiloRange.setTextColor(getResources().getColor(R.color.text_999999));
                imgAllArea.setImageResource(R.mipmap.icon_selected_3);
                tvAllArea.setTextColor(getResources().getColor(R.color.text_999999));
                SouthPop.dismiss();
            }
        });
    }

    private PopupWindow RangePop;
    private View RangePopView;
    private int left = 0;
    private RangeSeekBar rangeseekbar;

    public void showRangePop() {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        if (RangePopView == null)
            RangePopView = mInflater.inflate(R.layout.range_pop, null);
        if (RangePop == null) {
            RangePop = new PopupWindow(RangePopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        rangeseekbar = RangePopView.findViewById(R.id.rangeseekbar);
        rangeseekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                left = (int) leftValue / 25 + 1;
//                right=(int)rightValue/20+"";
                tvKiloRange.setText(String.format(getString(R.string.kilo_1), left+""));
                kilo=left*1000;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        //设置背景颜色
        RangePop.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        RangePop.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        RangePop.setOutsideTouchable(true);
        RangePop.showAsDropDown(imgKiloRange);
        RangePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvLoca.setText(tvKiloRange.getText());
                icChoice=true;
                baiduMap.clear();
                ooa = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)).zIndex(9).draggable(true);
                mCurrentMarker = (Marker) baiduMap.addOverlay(ooa);
                OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                        .center(ll).stroke(new Stroke(3, 0x784d73b3))
                        .radius(kilo);
                if (left>2) {
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(14));
                }else {
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(15));
                }
                baiduMap.addOverlay(ooCircle);
                range_mode="region_gps";
            }
        });
    }

    private void clearRange(){
        icChoice=false;
        baiduMap.clear();
        ooa = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)).zIndex(9).draggable(true);
        mCurrentMarker = (Marker) baiduMap.addOverlay(ooa);
    }

}

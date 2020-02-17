package com.shijinsz.shijin.ui.store;

import android.app.AlertDialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MarkerOptions;
import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.UserSiteBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.ywp.addresspickerlib.AddressPickerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/** 新增收货地址
 * 修改收货地址 */
public class NewLocationActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_right)
    TextView mAddSite;
    @BindView(R.id.title_layout)
    RelativeLayout mLayout;
    @BindView(R.id.top_view)
    TextView mTopView;

    @BindView(R.id.sub_delete)
    Button mDelete;
    @BindView(R.id.user_name)
    EditText mName;
    @BindView(R.id.user_phone)
    EditText mPhone;
    @BindView(R.id.user_city)
    TextView mCity;
    @BindView(R.id.detailed_address)
    EditText mAddredss;
    @BindView(R.id.user_site_label)
    TextView mSiteLabel;
    @BindView(R.id.switch_state)
    Switch mSwState;

    UserSiteBean site = null;

    private String userId;


    //0 表示新增 有id表示修改
    int into_style = 0;

    @Override
    public int bindLayout() {

        return R.layout.new_location_site;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);

        showTitleBackButton();
        mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_f1));

        userId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME);

        mAddSite.setText("保存");
        mAddSite.setTextColor(mContext.getResources().getColor(R.color.color_fdb));
        mAddSite.setLetterSpacing(0);
        mAddSite.setVisibility(View.VISIBLE);
        mAddSite.setOnClickListener(this);
        mTopView.setBackgroundColor(mContext.getResources().getColor(R.color.color_f1));

        into_style = getIntent().getIntExtra("id",0);
        site = new UserSiteBean();
        if(into_style == -1){
            setTitle("添加收货地址");
            mDelete.setVisibility(View.GONE);
        }else{
            setTitle("编辑收货地址");
            mDelete.setVisibility(View.VISIBLE);
            site = new Gson().fromJson(getIntent().getStringExtra("bean"),UserSiteBean.class);
            if(site != null){
                mName.setText(site.getName());
                mPhone.setText(site.getPhone());
                int index = site.getAddress().indexOf("-");
                mCity.setText(site.getAddress().subSequence(0,index));
                mAddredss.setText(site.getAddress().subSequence(index+1,site.getAddress().length()));
                setLabelType(site.getSiteType());
                mSwState.setChecked(site.isUse());

            }
        }

        mSwState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                site.setUse(b);
            }
        });
    }

    //设置标签类型
    private void setLabelType(int type){
        Drawable type_img;
        String hint;
        switch (type){
            case 1:
                type_img = mContext.getResources().getDrawable(R.mipmap.site_home_ic);
                hint = "家";
                break;
            case 2:
                type_img = mContext.getResources().getDrawable(R.mipmap.site_company_ic);
                hint = "公司";
                break;
            case 3:
                type_img = mContext.getResources().getDrawable(R.mipmap.site_school_ic);
                hint = "学校";
                break;
            default:
                type_img = mContext.getResources().getDrawable(R.mipmap.icon_dropdown_2);
                hint = "请选择";
                break;
        }
        mSiteLabel.setText(hint);
        mSiteLabel.setCompoundDrawablesWithIntrinsicBounds(null,null,type_img,null);
    }

    @OnClick({R.id.user_city,R.id.sub_delete,R.id.lable_layout})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.user_city:
                //显示选择城市
                showAddressPickerPop();
                break;
            case R.id.sub_delete:
                //删除当前地址
                showDeletePop();
                break;
            case R.id.lable_layout:
                //选择标签
                Intent label = new Intent(mContext,LabelPopActivity.class);
                label.putExtra("id",1);
                startActivityForResult(label,101);
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);
                break;

        }
    }

    //保存收货地址
    private void putSiteData() {

        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();
        String address = mCity.getText().toString() +"-"+ mAddredss.getText().toString();
        if(name.equals("") || phone.equals("") || address.equals("")){
            Toast.makeText(mContext,"地址信息不能为空",Toast.LENGTH_LONG).show();
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("_id",site.get_id());
        map.put("name",name);
        map.put("phone",phone);
        map.put("address",address);
        map.put("use",site.isUse());
        map.put("siteType",site.getSiteType());
        YSBSdk.getService(OAuthService.class).putAddress(userId, map,new YRequestCallback<UserSiteBean>() {
            @Override
            public void onSuccess(UserSiteBean var1) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("保存");
                dialog.setMessage("保存成功");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {


            }
        });

    }

    //显示删除弹框
    private void showDeletePop() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("确认删除");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delSite();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.setMessage("删除当前地址");
        dialog.show();
    }

    //删除收货地址
    private void delSite() {
        Map<String,Object> map = new HashMap<>();
        map.put("_id",site.get_id());
        YSBSdk.getService(OAuthService.class).delAddress(userId,map, new YRequestCallback<UserSiteBean>() {
            @Override
            public void onSuccess(UserSiteBean var1) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("确认删除");
                dialog.setMessage("删除成功");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {


            }
        });
    }

    /**
     * 显示地址选择的pop
     */
    private void showAddressPickerPop() {
        final PopupWindow popupWindow = new PopupWindow(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.address_picker_pop, null, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AddressPickerView addressView = rootView.findViewById(R.id.apvAddress);
        rootView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                mCity.setText(address);
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(rootView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAsDropDown(mCity);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            site.setSiteType(data.getIntExtra("type",0));
            setLabelType(site.getSiteType());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_right:
                //保存收货地址
                putSiteData();
                break;
        }
    }
}

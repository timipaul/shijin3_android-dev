package com.shijinsz.shijin.ui.store;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.view.FlowTag;
import com.hongchuang.hclibrary.view.FlowTagList;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.SeekGoodsAdapter;
import com.shijinsz.shijin.ui.store.adapter.SeekShopAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * 商城搜索
 */
public class StoreSeekActivity extends BaseActivity {

    //历史记录
    @BindView(R.id.store_seek)
    FlowTagList gridview1;
    //热门搜索
    @BindView(R.id.store_seek_hot)
    FlowTagList mFlowHot;
    @BindView(R.id.et_seek)
    EditText mEdSeek;
    @BindView(R.id.tv_delete)
    Button mButton;
    @BindView(R.id.ln_nofind)
    LinearLayout mNofind;
    @BindView(R.id.radioGroup)
    RadioGroup mRadio;
    @BindView(R.id.rg_goods)
    RadioGroup mRadio_Goods;
    @BindView(R.id.ln_history)
    LinearLayout layout_history;
    @BindView(R.id.list_view)
    //店铺搜索
    PowerfulRecyclerView mSeek_view;
    //商品搜索
    @BindView(R.id.gl_list)
    GridView mGrid_layout;
    @BindView(R.id.rb_goods_price)
    RadioButton mRb_price;

    /** 浏览历史 */
    private List<FlowTag> historyList;
    /** 推荐数据 */
    List<FlowTag> tagList = new ArrayList<>();
    //数据库
    SQLiteDatabase db;
    private Handler handler = new Handler();
    //查询数据状态 1商品 2店铺
    private String rg_statue = "1";
    //筛选状态 1销量 2价格
    private String goods_statue = "1";
    private boolean price_top = true;
    private final static String TAG = "storeSeekActivity";

    //商品结果适配器
    private SeekGoodsAdapter goodsAdapter;
    private List<StoreGoodsBean> goodsData = new ArrayList<>();
    //店铺结果适配器
    private SeekShopAdapter shopAdapter;
    private List<ShoppingShopBean> shopData = new ArrayList<>();


    @Override
    public int bindLayout() {
        return R.layout.store_seek_view;
    }



    @Override
    public void initView(View view) {

        mEdSeek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 0){
                    mButton.setVisibility(View.VISIBLE);
                    mNofind.setVisibility(View.GONE);
                }else{
                    mButton.setVisibility(View.GONE);
                    mSeek_view.setVisibility(View.GONE);
                    setShowView(false);


                }
            }
        });

        mEdSeek.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    if(!mEdSeek.getText().toString().equals("")){
                        getSeekData();
                    }


                }
                return false;
            }

        });

        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){
                    case R.id.rb_goods:
                        rg_statue = "1";
                        break;
                    case R.id.rb_shop:
                        rg_statue = "2";
                        break;
                }
                if(mEdSeek.getText().toString().trim().length() > 0){
                    //设置显示状态
                    getSeekData();
                    setScreenView(rg_statue);
                }



            }
        });

        mRadio_Goods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_goods_sales:
                        goods_statue = "1";
                        mRb_price.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.mipmap.car_num_default_ic),null);
                        break;
                    case R.id.rb_goods_price:
                        goods_statue = "2";
                        break;
                }
            }
        });

        mFlowHot.setOnItemClickListener(new FlowTagList.onItemClickListener() {
            @Override
            public void onClick(View v, FlowTag tag) {
                mEdSeek.setText(tag.getTitle());
                //点击搜索
                getSeekData();

            }
        });

        initData();
    }

    //设置筛选状态
    private void setScreenView(String goods_statue) {
        if(goods_statue.equals("1")){
            mGrid_layout.setVisibility(View.VISIBLE);
            mSeek_view.setVisibility(View.GONE);
        }else{
            mSeek_view.setVisibility(View.VISIBLE);
            mGrid_layout.setVisibility(View.GONE);
        }
    }

    //获取搜索数据
    private void getSeekData() {
        //保存本地
        insertData(mEdSeek.getText().toString());
        //设置视图
        setShowView(true);


        //判断选择适配器
        if(rg_statue.equals("1")){
            goodsAdapter = new SeekGoodsAdapter(mContext,goodsData,R.layout.store_seek_goods_item);
            mGrid_layout.setAdapter(goodsAdapter);
            mGrid_layout.setVisibility(View.VISIBLE);
            goodsAdapter.setOnItemClickListener(new SeekGoodsAdapter.ItemOnclick() {
                @Override
                public void addClick(String goodsId, TextView tv_num, Button bt) {
                    try {
                        int num = Integer.valueOf(tv_num.getText().toString())+1;
                        if(num > 0){
                            tv_num.setVisibility(View.VISIBLE);
                            bt.setVisibility(View.VISIBLE);
                            tv_num.setText(String.valueOf(num));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void minusClick(String goodsId, TextView tv_num, Button bt) {
                    try {
                        int num = Integer.valueOf(tv_num.getText().toString()) - 1;
                        if(num <= 0){
                            tv_num.setVisibility(View.GONE);
                            bt.setVisibility(View.GONE);
                        }
                        tv_num.setText(String.valueOf(num));
                    } catch (Exception e) {

                    }

                }

                @Override
                public void itemClickInto(String goodsId) {
                    Intent intent = new Intent(mContext, StoreGoodsDetails.class);
                    intent.putExtra("goodsId",goodsId);
                    startActivityForResult(intent,102);
                }
            });

            getGoodsData();

        }else{
            //店铺
            shopAdapter = new SeekShopAdapter(R.layout.store_seek_shop_item,shopData);
            mSeek_view.setAdapter(shopAdapter);
            mSeek_view.setVisibility(View.VISIBLE);
            getShopGoodsData();


        }
    }

    //获取店铺数据显示
    private void getShopGoodsData() {
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex",1);
        map.put("pageSize","30");
        map.put("type","SHOP");
        map.put("name",mEdSeek.getText().toString());
        YSBSdk.getService(OAuthService.class).getShopSeek(map, new YRequestCallback<BaseBean<ShoppingShopBean>>() {
            @Override
            public void onSuccess(BaseBean<ShoppingShopBean> var1) {
                shopData.clear();
                shopData.addAll(var1.getResult());
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    //获取商品显示数据
    public void getGoodsData(){
        Map<String,Object> map = new HashMap<>();
        map.put("pageIndex",1);
        map.put("pageSize","30");
        map.put("name",mEdSeek.getText().toString());
        YSBSdk.getService(OAuthService.class).getShopGoods(map, new YRequestCallback<BaseBean<StoreGoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<StoreGoodsBean> var1) {
                goodsData.clear();
                goodsData.addAll(var1.getGoods());
                goodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    //获取店铺搜索信息


    //判断搜索结果显示
    public void setShowView(boolean statue){
        if(statue){
            layout_history.setVisibility(View.GONE);
            mRadio_Goods.setVisibility(View.VISIBLE);
        }else{
            layout_history.setVisibility(View.VISIBLE);
            mRadio_Goods.setVisibility(View.GONE);
            mGrid_layout.setVisibility(View.GONE);

        }
    }

    //获取数据
    private void initData() {

        // 创建或打开数据库（此处需要使用绝对路径）
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/my.db3", null);

        //创建数据库
        createData();
        seekHistory();
        //获取热门数据
        getHotData();
    }

    //获取热门数据
    private void getHotData() {
        YSBSdk.getService(OAuthService.class).getStoreSeekHot(new YRequestCallback<BaseBean<FlowTag>>() {
            @Override
            public void onSuccess(BaseBean<FlowTag> var1) {

                if(var1.getResult().size() == 0){
                    mNofind.setVisibility(View.VISIBLE);
                    return;
                }else{
                    mNofind.setVisibility(View.GONE);
                }
                tagList.addAll(var1.getResult());
                mFlowHot.setData(tagList);

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);

            }

            @Override
            public void onException(Throwable var1) {
                Log.i(TAG,"处理异常");
            }
        });

    }

    @OnClick({R.id.tv_delete,R.id.tv_cancel,R.id.img_delete,R.id.rb_goods_price})
    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.tv_delete:
                //删除
                mEdSeek.setText("");
                mButton.setVisibility(View.GONE);
                break;
            case R.id.tv_cancel:
                //取消
                finish();
                break;
            case R.id.img_delete:
                //删除本地缓存
                deleteHistoryData();
                break;
            case R.id.rb_goods_price:
                //价格升价
                if(price_top){
                    mRb_price.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.mipmap.car_num_top_ic),null);
                }else{
                    mRb_price.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.mipmap.car_num_down_ic),null);
                }
                getSeekData();
                price_top = !price_top;
                break;
        }
    }



    //创建表
    private void createData(){
        db.execSQL("create table if not exists seek_history(_id integer"
                + " primary key autoincrement,"
                + " commodityName varchar(20))");
    }

    /**数据库插入信息*/
    private void insertData(String commodityName) {
        //插入之前删除同样的记录，保证只有最后一条唯一数据
        deleteSameData(commodityName);
        ContentValues cValue = new ContentValues();
        cValue.put("commodityName",commodityName);
        db.insert("seek_history",null,cValue);

        try {
            Cursor cursor = db.rawQuery("select * from seek_history", null);
            /*if(cursor != null){
                while(cursor.moveToNext()){
                    Log.i(TAG,"seek_history : _id "
                            + cursor.getString(0)
                            +" commodityName " + cursor.getInt(1));
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSameData(String seekId){
        try {
            db.delete("seek_history","commodityName = ?",new String[]{seekId});
        } catch (Exception e) {
            Log.i(TAG,"之前没有此内容的搜索记录");
        }
    }

    //删除历史记录
    private void deleteHistoryData(){
        try {
            db.delete("seek_history",null,null);
        } catch (Exception e) {
            Log.i(TAG,"删除历史记录异常");
        }

        //刷新搜索历史
        seekHistory();
    }

    //搜索历史
    private void seekHistory() {
        getDatabase();
        //从本地数据库里查询
        handler.post(runnableHistory);
    }

    //获取数据库浏览信息
    private void getDatabase() {
        historyList = new ArrayList<>();
        try {
            String[] columns = new String[] {"commodityName"};
            //Cursor cursor = db.query("seek_history",columns,null,null,null,null,"_id desc");
            Cursor cursor = db.rawQuery("select _id,commodityName from seek_history order by _id desc limit 9", null);
            //获取数据库阅读历史
            if(cursor != null){
                while(cursor.moveToNext()){
                    /*Log.i(TAG,"seek_history : _id"
                            + cursor.getString(0)
                            +" commodityName " + cursor.getInt(1));*/
                    historyList.add(new FlowTag(Integer.valueOf(cursor.getString(0)),cursor.getString(1)));

                }
            }
        } catch (Exception e) {
            Log.i("详情获取浏览记录","查询本地数据库异常");

        }
    }

    //更新浏览历史
    Runnable runnableHistory = new Runnable(){
        @Override
        public void run() {
            gridview1.removeAllViews();
            gridview1.setData(historyList);
            gridview1.setOnItemClickListener(new FlowTagList.onItemClickListener() {
                @Override
                public void onClick(View v, FlowTag tag) {
                    mEdSeek.setText(tag.getTitle());
                    //跳转
                    getSeekData();
                }
            });
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102 && resultCode == 102){
            setResult(102);
            finish();
        }
    }
}

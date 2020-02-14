package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车数据适配器
 */
public class ShopcartAdapter extends BaseExpandableListAdapter {

    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    public int flag = 0;
    private GroupEdtorListener mListener;

    //判断会员状态
    private String state;

    public GroupEdtorListener getmListener() {
        return mListener;
    }

    private List<ShoppingShopBean> shopData = new ArrayList<>();

    public void setmListener(GroupEdtorListener mListener) {
        this.mListener = mListener;
    }
    int count=0;
    /**
     * 构造函数
     *
     * @param shopData   组元素列表
     * @param context
     */
    public ShopcartAdapter(List<ShoppingShopBean> shopData, Context context) {
        this.shopData = shopData;
        this.context = context;
        state = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip);
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return shopData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return shopData.get(groupPosition).getGoodsList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shopData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  shopData.get(groupPosition).getGoodsList().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder gholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder = new GroupViewHolder(convertView);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingShopBean group = (ShoppingShopBean) getGroup(groupPosition);

        if(groupPosition == 0){
            gholder.TopView.setVisibility(View.GONE);
        }else{
            gholder.TopView.setVisibility(View.VISIBLE);
        }

        gholder.tvSourceName.setText(group.getName());
        gholder.determineChekbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)

            {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        gholder.determineChekbox.setChecked(group.isChoosed());
        /*if (group.isEdtor()) {
            gholder.tvStoreEdtor.setText("完成");
        } else {
            gholder.tvStoreEdtor.setText("编辑");
        }
        gholder.tvStoreEdtor.setOnClickListener(new GroupViewClick(groupPosition, gholder.tvStoreEdtor, group));*/
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {

        final ChildViewHolder cholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
//            if(isLastChild&&getChild(groupPosition,childPosition)!=null)
//            {
//                View    v = View.inflate(context, R.layout.child_footer,null);
//                TextView txtFooter = (TextView)v.findViewById(R.id.txtFooter);
//                txtFooter.setText("店铺满99元包邮");
//                if(convertView instanceof ViewGroup){
//                    ((ViewGroup) convertView).addView(v);
//                }
//            }

            cholder = new ChildViewHolder(convertView);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }

        final StoreGoodsBean goodsInfo = (StoreGoodsBean) getChild(groupPosition, childPosition);


        if(isLastChild&&getChild(groupPosition,childPosition)!=null){
            //cholder.stub.setVisibility(View.VISIBLE);
            //  TextView tv= (TextView) cholder.stub.findViewById(R.id.txtFooter);//这里用来动态显示店铺满99元包邮文字内容
        }else{
            //cholder.stub.setVisibility(View.GONE);
        }
        if (goodsInfo != null) {

            cholder.tvIntro.setText(goodsInfo.getName());
            if(state.equals("true") && goodsInfo.getDiscount() > 0){
                cholder.tvDis_price.setText("¥" + goodsInfo.getDiscount());
            }else{
                cholder.tvDis_price.setText("¥" + goodsInfo.getPrice());
            }
            cholder.tvPrice.setText("");

            /*if(goodsInfo.getDiscount() == 0){
                cholder.tvDis_price.setText("¥" + goodsInfo.getPrice());
            }else{
                cholder.tvPrice.setText("¥" + goodsInfo.getPrice() + "");
                cholder.tvDis_price.setText(""+goodsInfo.getDiscount());
                cholder.tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线（删除线）
            }*/


            cholder.etNum.setText(goodsInfo.getNum() + "");
            cholder.tvColorSize.setText(goodsInfo.getAttriValStr());
            if(goodsInfo.getGoodsImg() != null){
                Glide.with(context).load(goodsInfo.getGoodsImg()).into(cholder.ivAdapterListPic);
            }else if(goodsInfo.getCoverImg().length > 0){
                Glide.with(context).load(goodsInfo.getCoverImg()[0]).into(cholder.ivAdapterListPic);
            }


            cholder.checkBox.setChecked(goodsInfo.isChoosed());
            cholder.checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.checkBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.btAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.etNum, cholder.checkBox.isChecked());// 暴露增加接口
                }
            });
            cholder.btReduce.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.etNum, cholder.checkBox.isChecked());// 暴露删减接口
                }
            });
            /********************方案一：弹出软键盘修改数量，应为又不知名的bug会使然键盘强行关闭***********************/
            /****在清单文件的activity下设置键盘：
            android:windowSoftInputMode="adjustUnspecified|stateHidden|adjustPan"
            android:configChanges="orientation|keyboardHidden"****/
            cholder.etNum.addTextChangedListener(new GoodsNumWatcher(goodsInfo));//监听文本输入框的文字变化，并且刷新数据
            notifyDataSetChanged();
            /********************方案一***************************************************************************/
            /********************方案二：让软键盘不能弹出，文本框不可编辑弹出dialog修改***********************/
//            cholder.etNum.setOnFocusChangeListener(new android.view.View.
//                    OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {//监听焦点的变化
//                    if (hasFocus) {//获取到焦点也就是文本框被点击修改了
//                        // 1，先强制键盘不弹出
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
//                        // 2.显示弹出dialog进行修改
//                        showDialog(goodsInfo,cholder.etNum);
                          //3.清除焦点防止不断弹出dialog和软键盘
//                        cholder.etNum.clearFocus();
                          // 4. 数据刷型
//                        ShopcartAdapter.this.notifyDataSetChanged();
//                    }
//                }
//            });
            /********************方案二***********************/
            //删除 购物车
            /*cholder.tvGoodsDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("操作提示");
                    alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(groupPosition, childPosition);

                                }
                            });
                    alert.show();

                }
            });*/

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;

    }





    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener {
        void groupEdit(int groupPosition);
    }

    /**
     * 使某个组处于编辑状态
     * <p/>
     * groupPosition组的位置
     */
    class GroupViewClick implements OnClickListener {
        private int groupPosition;
        private Button edtor;
        private ShoppingShopBean group;

        public GroupViewClick(int groupPosition, Button edtor, ShoppingShopBean group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setIsEdtor(false);
                } else {
                    group.setIsEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 组元素绑定器
     */
    static class GroupViewHolder {
        @BindView(R.id.determine_chekbox)
        CheckBox determineChekbox;
        @BindView(R.id.tv_source_name)
        TextView tvSourceName;
        @BindView(R.id.top_view)
        LinearLayout TopView;
        /*@BindView(R.id.tv_store_edtor)
        Button tvStoreEdtor;*/

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 子元素绑定器
     */
    static class ChildViewHolder {
        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.iv_adapter_list_pic)
        ImageView ivAdapterListPic;
        @BindView(R.id.tv_intro)
        TextView tvIntro;
        @BindView(R.id.tv_color_size)
        TextView tvColorSize;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.discount_price)
        TextView tvDis_price;
        @BindView(R.id.rl_no_edtor)
        RelativeLayout rlNoEdtor;
        @BindView(R.id.bt_reduce)
        Button btReduce;
        @BindView(R.id.et_num)
        TextView etNum;
        @BindView(R.id.bt_add)
        Button btAdd;
        @BindView(R.id.ll_change_num)
        RelativeLayout llChangeNum;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    /**
     * 购物车的数量修改编辑框的内容监听
     */
    class GoodsNumWatcher implements TextWatcher{
        StoreGoodsBean   goodsInfo;
        public GoodsNumWatcher(StoreGoodsBean goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s.toString())){//当输入的数字不为空时，更新数字
                goodsInfo.setNum(Integer.valueOf(s.toString().trim()));
            }
       }

    }

    /**
     * 显示修改购物车商品数量的dialog
     * @param goodinfo  item的商品信息实体
     * @param edittext   购物车item的数量文本框
     */
    private void showDialog(final StoreGoodsBean goodinfo,final EditText edittext){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View alertDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_change_num, null,false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(alertDialogView);
        count = goodinfo.getNum();
       final EditText editText = (EditText) alertDialogView.findViewById(R.id.et_num);
        editText.setText(""+goodinfo.getNum());//设置dialog的数量初始值
        //自动弹出软键盘
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        final   Button btadd= (Button) alertDialogView.findViewById(R.id.bt_add);
        final   Button btreduce= (Button) alertDialogView.findViewById(R.id.bt_reduce);
        final   TextView cancle= (TextView) alertDialogView.findViewById(R.id.tv_cancle);
        final   TextView sure= (TextView) alertDialogView.findViewById(R.id.tv_sure);
        cancle.setOnClickListener(new OnClickListener() { //取消按钮
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new OnClickListener() {//确定按钮
            @Override
            public void onClick(View v) {
                goodinfo.setNum(count);//重新设置数量
                edittext.setText(count+"");//购物车界面的文本框显示同步
                alertDialog.dismiss();
            }
        });
        btadd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               count ++;   //点一下量加1
               editText.setText(""+count);//动态显示dialog的文本框的数据

            }
        });
        btreduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1) {//数量大雨1时操作
                    count--; //点一下减1
                    editText.setText("" + count);
                }
            }
        });
        alertDialog.show();
    }
}
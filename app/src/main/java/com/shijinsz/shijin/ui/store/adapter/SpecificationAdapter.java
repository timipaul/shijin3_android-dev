package com.shijinsz.shijin.ui.store.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.MyRadioGroup;

import java.util.List;

/**商品规格适配器*/
public class SpecificationAdapter extends BaseQuickAdapter<StoreGoodsBean.Specification, BaseViewHolder> {

    public onListen onListen;
    public MyRadioGroup radioGroup;


    public SpecificationAdapter(int layoutResId, @Nullable List<StoreGoodsBean.Specification> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, StoreGoodsBean.Specification item) {

        helper.setText(R.id.item_key,item.getKey());
        radioGroup = helper.getView(R.id.radioGroup);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        for (int i = 0; i < item.getVal().size(); i++){
            RadioButton rdbtn = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.common_radio_button, null);
            rdbtn.setText(item.getVal().get(i).getText());
            rdbtn.setTag(item.getKey());
            rdbtn.setTag(R.id.tag_rbutton_index,i);
            rdbtn.setLayoutParams(params);
            radioGroup.addView(rdbtn);
            if(i == 0){
                //rdbtn.setChecked(true);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = helper.getView(radioGroup.getCheckedRadioButtonId());
                String str = rb.getText().toString();
                Integer index = Integer.valueOf(rb.getTag(R.id.tag_rbutton_index).toString());
                onListen.callback(String.valueOf(rb.getTag()),str, index);
            }
        });

    }


    public interface onListen{
        void callback(String key,String val,int index);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }


}

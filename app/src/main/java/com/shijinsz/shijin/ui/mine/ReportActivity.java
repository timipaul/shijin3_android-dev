package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ReportBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.CommonAdapter;
import com.shijinsz.shijin.base.ViewHolder;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/18.
 */

public class ReportActivity extends BaseActivity {
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.commit)
    Button commit;
    private String id;
    @Override
    public int bindLayout() {
        return R.layout.repore_activity;
    }
    private CommonAdapter<ReportBean> adapter;
    private List<ReportBean> list=new ArrayList<>();
    private String content="";
    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.report));
        showTitleBackButton();
        id=getIntent().getStringExtra("id");
        list.add(new ReportBean("广告",false));
        list.add(new ReportBean("过时",false));
        list.add(new ReportBean("重复",false));
        list.add(new ReportBean("错别字",false));
        list.add(new ReportBean("色情低俗",false));
        list.add(new ReportBean("标题夸张",false));
        list.add(new ReportBean("观点错误",false));
        list.add(new ReportBean("事实不符",false));
        list.add(new ReportBean("版权反馈",false));
        list.add(new ReportBean("格式错误",false));
        list.add(new ReportBean("网络诈骗",false));
        list.add(new ReportBean("其他问题",false));
        adapter=new CommonAdapter<ReportBean>(mContext,list,R.layout.report_item) {
            @Override
            public void convert(ViewHolder helper, ReportBean item, int position) {
                helper.setText(R.id.tv,item.getName());
                if (item.isSelect()){
                    helper.setTextColor(R.id.tv,R.color.colorPrimary);
                    helper.setBackgroundResource(R.id.tv,R.drawable.report_choice);
                }else {
                    helper.setTextColor(R.id.tv,R.color.text_33);
                    helper.setBackgroundResource(R.id.tv,R.drawable.report_unchoice);
                }
            }
        };
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==list.size()-1){
                    Intent intent=new Intent(mContext,ReportOtherActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    return;
                }
                for (int i1 = 0; i1 < list.size(); i1++) {
                    if (i1==i){
                        content=list.get(i1).getName();
                        list.get(i1).setSelect(true);
                    }else {
                        list.get(i1).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (!content.isEmpty())
        report();
    }

    public void report(){
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("complaint_content",content);
        YSBSdk.getService(OAuthService.class).complaints(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("举报成功");
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}

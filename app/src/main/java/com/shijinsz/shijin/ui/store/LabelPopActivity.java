package com.shijinsz.shijin.ui.store;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.mine.adapter.DataAdAdapter;

import java.lang.reflect.Method;

import butterknife.OnClick;
import freemarker.ext.beans.BeansWrapper;

/** 地址标签页 */
public class LabelPopActivity extends BaseActivity {


    private int type = 0;

    @Override
    public int bindLayout() {
        return R.layout.site_label_pop;
    }

    @Override
    public void initView(View view) {

        //设置视图背景透明
        translucentActivity(LabelPopActivity.this);
    }

    //设置视图透明
    private void translucentActivity(Activity activity) {

        try {
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            activity.getWindow().getDecorView().setBackground(null);
            Method activityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            activityOptions.setAccessible(true);
            Object options = activityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> aClass = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    aClass = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent", aClass, ActivityOptions.class);
            method.setAccessible(true);
            method.invoke(activity, null, options);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @OnClick({R.id.label_home,R.id.label_company,R.id.label_school,R.id.tv_cancel})
    public void onClickView(View view){

        switch (view.getId()){
            case R.id.label_home:
                //家
                type = 1;
                break;
            case R.id.label_company:
                //公司
                type = 2;
                break;
            case R.id.label_school:
                //学校
                type = 3;
                break;
            case R.id.tv_cancel:

                break;
                default:
                    type = 0;
                    break;
        }

        Intent intent = new Intent();
        intent.putExtra("type",type);
        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }
}

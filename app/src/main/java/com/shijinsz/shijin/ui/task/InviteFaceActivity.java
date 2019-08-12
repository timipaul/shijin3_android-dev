package com.shijinsz.shijin.ui.task;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.utils.QRCodeUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InviteFaceActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;

    @Override
    public int bindLayout() {
        return R.layout.invite_face_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.invite_face));
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(Comment.url+"invitation_registration?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)+"&channel=face_to_face", 390);
        img.setImageBitmap(bitmap);
    }
}

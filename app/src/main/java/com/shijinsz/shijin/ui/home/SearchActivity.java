package com.shijinsz.shijin.ui.home;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.AdsBean;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.RecomWordsBean;
import com.hongchuang.ysblibrary.model.model.bean.SearchBean;
import com.hongchuang.ysblibrary.model.model.bean.SearchedBean;
import com.hongchuang.ysblibrary.model.model.bean.ShieidsBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.mrgao.luckly_popupwindow.LucklyPopopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.CommonAdapter;
import com.shijinsz.shijin.base.MyPagerAdapter;
import com.shijinsz.shijin.base.ViewHolder;
import com.shijinsz.shijin.ui.ad.VideoDetailActivity;
import com.shijinsz.shijin.ui.home.adapter.NewsAdapter;
import com.shijinsz.shijin.ui.home.adapter.UsersAdapter;
import com.shijinsz.shijin.ui.mine.UserDetailActivity;
import com.shijinsz.shijin.ui.mine.adapter.FollowListAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/9/4.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt)
    EditText edt;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.gridview1)
    MyGridView gridview1;
    @BindView(R.id.gridview2)
    MyGridView gridview2;
    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ln_nofind)
    LinearLayout lnNofind;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.ln_history)
    LinearLayout lnHistory;
    @BindView(R.id.ln_hot)
    LinearLayout lnHot;
    @BindView(R.id.ln_tab)
    LinearLayout lnTab;
    @BindView(R.id.tv_nofind)
    TextView tvNofind;

    private List<SearchBean> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private CommonAdapter<SearchBean> adapter;
    private CommonAdapter<String> adapter2;
    private DialogUtils dialogUtils;

    @Override
    public int bindLayout() {
        return R.layout.search_activity;
    }

    @Override
    public void initView(View view) {
        dialogUtils = new DialogUtils(mContext);
        adapter = new CommonAdapter<SearchBean>(mContext, list1, R.layout.search_iem) {
            @Override
            public void convert(ViewHolder helper, SearchBean item, int position) {
                if (item.getSearch_keyword() != null) {
                    helper.setText(R.id.text, item.getSearch_keyword() + "");
                }else {
                    helper.setText(R.id.text, "");
                }

            }
        };
        gridview1.setAdapter(adapter);
        adapter2 = new CommonAdapter<String>(mContext, list2, R.layout.search_iem) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.text, item);
            }
        };
        gridview2.setAdapter(adapter2);
        gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list1.get(i).getSearch_keyword()!=null&&!list1.get(i).getSearch_keyword().isEmpty()) {
                    edt.setText(list1.get(i).getSearch_keyword()+"");
                    getSearch(edt.getText().toString());
                }
            }
        });
        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!list2.get(i).isEmpty()) {
                    edt.setText(list2.get(i));
                    getSearch(edt.getText().toString());
                }
            }
        });
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt.getText().toString().isEmpty()) {
                    clear.setVisibility(View.GONE);
                } else {
                    clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initTab();
        getHistory();
        getHot();
    }

    private List<Ads> answerList = new ArrayList<>();
    private List<SearchedBean.Users> userList = new ArrayList<>();
    private NewsAdapter answerAdapter;
    private UsersAdapter usersAdapter;
    private PowerfulRecyclerView answerRecycerview, userRecycerview;

    private void initTab() {
        List<View> mFragments = new ArrayList<>();
        View answerView = mInflater.inflate(R.layout.get_comment_fragment, null);
        View userView = mInflater.inflate(R.layout.get_comment_fragment, null);
        answerRecycerview = answerView.findViewById(R.id.recyclerView);
        userRecycerview = userView.findViewById(R.id.recyclerView);
        mFragments.add(answerView);
        mFragments.add(userView);
        SmartRefreshLayout answerRefresh = answerView.findViewById(R.id.refresh);
        answerRefresh.setEnableRefresh(false);
        answerRefresh.setEnableLoadMore(false);
        SmartRefreshLayout userRefresh = userView.findViewById(R.id.refresh);
        userRefresh.setEnableRefresh(false);
        userRefresh.setEnableLoadMore(false);
        answerAdapter = new NewsAdapter(R.layout.home_big_pic_item, answerList);
        answerRecycerview.setAdapter(answerAdapter);
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("id",answerList.get(position).getId());
                startActivity(intent);
            }
        });
        usersAdapter = new UsersAdapter(R.layout.search_user_item, userList);
        userRecycerview.setAdapter(usersAdapter);
        usersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("id",userList.get(position).getId());
                startActivity(intent);
            }
        });
        MyPagerAdapter mTabAdapter = new MyPagerAdapter(mFragments);
        viewpager.setAdapter(mTabAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setText(getString(R.string.answer));
        tabLayout.getTabAt(1).setText(getString(R.string.user));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (answerList.size() == 0) {
                        lnNofind.setVisibility(View.VISIBLE);
                        lnHot.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.GONE);
                    } else {
                        lnNofind.setVisibility(View.GONE);
                        lnHot.setVisibility(View.GONE);
                        viewpager.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (userList.size() == 0) {
                        lnNofind.setVisibility(View.VISIBLE);
                        lnHot.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.GONE);
                    } else {
                        lnNofind.setVisibility(View.GONE);
                        lnHot.setVisibility(View.GONE);
                        viewpager.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initAnswer();
        initUser();
    }

    private void initUser() {
        usersAdapter.setOnFollow(new FollowListAdapter.OnFollow() {
            @Override
            public void call(int pos) {
                if (userList.get(pos).getIs_follow().equals("on")) {
                    unfollow(pos);
                } else {
                    follow(pos);
                }
            }
        });
    }

    private void follow(final int pos) {
        Map map = new HashMap();

        YSBSdk.getService(OAuthService.class).fans(userList.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                userList.get(pos).setIs_follow("on");
                usersAdapter.notifyDataSetChanged();
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

    private void unfollow(final int pos) {
        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.content)).setText(getString(R.string.unfollow_dialog));
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();

                YSBSdk.getService(OAuthService.class).unfans(userList.get(pos).getId(), map, new YRequestCallback<PicCodeBean>() {
                    @Override
                    public void onSuccess(PicCodeBean var1) {
                        userList.get(pos).setIs_follow("off");
                        mailDialog.dismiss();
                        usersAdapter.notifyDataSetChanged();
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
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });

    }

    LucklyPopopWindow mLucklyPopopWindow;
    private ShieidsBean shieidsBean;

    private void initAnswer() {
        answerAdapter.setCloseClickListen(new NewsAdapter.OnCloseClickListen() {
            @Override
            public void onClick(View view, int pos) {
                mLucklyPopopWindow = new LucklyPopopWindow(mContext, answerList.get(pos + 1).getRelease_record().getNickname(), answerList.get(pos+1).getInterests());
                DisplayMetrics dm = getResources().getDisplayMetrics();
                mLucklyPopopWindow.setWidth(dm.widthPixels);
                //监听事件
                mLucklyPopopWindow.setOnItemClickListener(new LucklyPopopWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(boolean see, boolean interested, boolean content_level, boolean black_user, boolean black_label1, boolean black_label2, boolean black_label3) {
                        mLucklyPopopWindow.dismiss();
                        String user_id = "";
                        List<String> interests = new ArrayList<>();
                        if (black_user) {
                            user_id = answerList.get(pos + 1).getUser_id();
                        }
                        if (black_label1) {
                            interests.add(answerList.get(pos + 1).getInterests().get(0));
                        }
                        if (black_label2) {
                            interests.add(answerList.get(pos + 1).getInterests().get(1));
                        }
                        if (black_label3) {
                            interests.add(answerList.get(pos + 1).getInterests().get(2));
                        }
                        shieidsBean = new ShieidsBean(see, interested, content_level, user_id, interests);
                        black_ad(pos + 1);

                    }
                });

                mLucklyPopopWindow.showAtLocation(getWindow().getDecorView(), view);
            }
        });
    }

    private void black_ad(final int pos) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("shields", shieidsBean);
        YSBSdk.getService(OAuthService.class).shield(answerList.get(pos).getId(),map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                answerAdapter.remove(pos);
                answerAdapter.notifyDataSetChanged();
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

    private void getSearch(String key) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("keyword", key);
        YSBSdk.getService(OAuthService.class).getsearch(map, new YRequestCallback<SearchedBean>() {
            @Override
            public void onSuccess(SearchedBean var1) {
                mStateView.showContent();
                String src= String.format(getString(R.string.sorry_nofind),key);
                int index=src.indexOf(key);
                SpannableStringBuilder style = new SpannableStringBuilder(src);
                style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)),index,index+key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvNofind.setText(style);
                viewpager.setVisibility(View.VISIBLE);
                lnTab.setVisibility(View.VISIBLE);
                lnHistory.setVisibility(View.GONE);
                lnHot.setVisibility(View.GONE);
                answerList.clear();
                userList.clear();
                answerList.addAll(var1.getAds());
                userList.addAll(var1.getUsers());
                answerAdapter.setKey(key);
                usersAdapter.setKey(key);
                answerAdapter.notifyDataSetChanged();
                usersAdapter.notifyDataSetChanged();
                if (tabLayout.getSelectedTabPosition() == 0) {
                    if (answerList.size() == 0) {
                        lnNofind.setVisibility(View.VISIBLE);
                        lnHot.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.GONE);
                    } else {
                        lnNofind.setVisibility(View.GONE);
                        lnHot.setVisibility(View.GONE);
                        viewpager.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (userList.size() == 0) {
                        lnNofind.setVisibility(View.VISIBLE);
                        lnHot.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.GONE);
                    } else {
                        lnNofind.setVisibility(View.GONE);
                        lnHot.setVisibility(View.GONE);
                        viewpager.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    private void getHot() {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).recom_words(map, new YRequestCallback<RecomWordsBean>() {
            @Override
            public void onSuccess(RecomWordsBean var1) {
                list2.addAll(var1.getRecom_words());
                if (var1.getRecom_words().size()%2==1){
                    list2.add("");
                }
                adapter2.notifyDataSetChanged();
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

    private void getHistory() {
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("cursor", System.currentTimeMillis()/1000);
        map.put("size", "6");
        YSBSdk.getService(OAuthService.class).search(map, new YRequestCallback<BaseBean<SearchBean>>() {
            @Override
            public void onSuccess(BaseBean<SearchBean> var1) {
                list1.addAll(var1.getRecords());
                if (var1.getRecords().size()%2==1){
                    list1.add(new SearchBean());
                }
                adapter.notifyDataSetChanged();
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


    @OnClick({R.id.iv_back, R.id.tv_search, R.id.img_delete, R.id.clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                if (edt.getText().toString().isEmpty()) {
                    return;
                }
                getSearch(edt.getText().toString());
                break;
            case R.id.clear:
                edt.setText("");
                break;
            case R.id.img_delete:
                dialogUtils.showCommentDialog(getString(R.string.delete_history), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete_search();
                        dialogUtils.dismissCommentDialog();
                    }
                });
                break;
        }


    }

    private void delete_search() {
        YSBSdk.getService(OAuthService.class).delete_search("person", new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                list1.clear();
                adapter.notifyDataSetChanged();
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

}

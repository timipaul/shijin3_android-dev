package com.hongchuang.ysblibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hongchuang.ysblibrary.R;

import java.util.ArrayList;
import java.util.List;


public class PopSetectSingle {

	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private Button bt_cancel;// 取消
	private Button bt_ok;// 确定
	private TextView tv_pop_title;
	int mHour ;
	int mMinute ;
	String timer;
	private int pos=1;
	private String [] times=new String[2];
	private List<String> list= new ArrayList<>();
	private Activity mContext;
	public PopSetectSingle(Activity context, List<String> list){
		this.mContext=context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list=list;
	}

	public PopSetectSingle(Activity context,List<String> list,int pos){
		this.mContext=context;
		this.pos=pos;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list=list;
	}
	public void showPopupWindow(View parent, SetText listener) {
		
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_single, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			initPop(view,listener);
		}
		WindowManager.LayoutParams lp = mContext.getWindow()
				.getAttributes();
		lp.alpha = 0.5f;
		mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		mContext.getWindow().setAttributes(lp);
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = mContext.getWindow()
						.getAttributes();
				lp.alpha = 1f;
				mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				mContext.getWindow().setAttributes(lp);
			}
		});
	}

	private void initPop(View view,final SetText listener) {
		bt_ok = (Button) view.findViewById(R.id.bt_ok);//确定
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);// 取消

		PickerView pv_year = (PickerView) view
				.findViewById(R.id.pv_check);



//		List<String> days = new ArrayList<String>();

//		初始化
		// 准备数据
//		Calendar c = Calendar.getInstance();
//		mHour = c.get(Calendar.HOUR_OF_DAY);
//		mMinute = c.get(Calendar.MINUTE);
//
//
//
//		//获取时间戳
//		long time = System.currentTimeMillis();
//		timer = DateUtil.longTimetoStr(time);
//		for (int i = 0; i <30 ; i++) {
//			years.add("" + DateUtil.longTimetoStr(time+24*3600*1000L*i));
//		}
//		if(mMinute <= 30){
//			times[1] = mHour+":30";
//		}else {
//			mHour=mHour+1;
//			times[1]=mHour+":00";
//		}
		pv_year.setData(list,pos);
		times[0] = list.get(pos);

		pv_year.setOnSelectListener(new PickerView.onSelectListener() {


			@Override
			public void onSelect(String text, int point) {
				times[0]=text;
//				if(timer.equals(text)){
//					months.clear();
//					for (int i = mHour; i < 24; i++) {
//                        String hour;
//                        if(i<10) {
//                            hour ="0"+i;
//                        }else {
//                            hour=""+i;
//                        }
//                        if(i==mHour) {
//                            if (mMinute > 30) {
//                                continue;
//                            }else {
//                                months.add(hour+":30");
//                                continue;
//                            }
//                        }
//						months.add(hour+":00");
//						months.add(hour+":30");
//					}
//					pv_month.setData(months,1);
//				}else {
//					months.clear();
//					for (int i = 0; i < 24; i++)
//					{
//						String hour;
//						if(i<10) {
//							hour ="0"+i;
//						}else {
//							hour=""+i;
//						}
//						months.add(hour+":00");
//						months.add(hour+":30");
//					}
//					pv_month.setData(months,1);
//				}
//				tv_pop_title .setText(times[0]+"   "+times[1]);
			}
		});



		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				listener.listenResult(times[0]);
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();

			}
		});
	}
	
	
}

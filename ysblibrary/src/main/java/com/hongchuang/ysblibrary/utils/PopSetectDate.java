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


public class PopSetectDate {

	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private Button bt_cancel;// 取消
	private Button bt_ok;// 确定
	private TextView tv_pop_title;
	int mHour ;
	int mMinute ;
	String timer;
	private Activity activity;
	private String [] times=new String[3];

	public PopSetectDate(Activity context){
		this.activity=context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void showPopupWindow(View parent, SetText listener) {
		
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_date, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			initPop(view,listener);
		}
		WindowManager.LayoutParams lp = activity.getWindow()
				.getAttributes();
		lp.alpha = 0.5f;
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		activity.getWindow().setAttributes(lp);
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = activity.getWindow()
						.getAttributes();
				lp.alpha = 1f;
				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				activity.getWindow().setAttributes(lp);
			}
		});
	}

	private void initPop(View view,final SetText listener) {
		bt_ok = (Button) view.findViewById(R.id.bt_ok);//确定
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);// 取消

		PickerView pv_year = (PickerView) view
				.findViewById(R.id.pv_year);

		final PickerView pv_month = (PickerView) view
				.findViewById(R.id.pv_month);
		final PickerView pv_day = (PickerView) view
				.findViewById(R.id.pv_day);



		List<String> years = new ArrayList<String>();
		final List<String> months = new ArrayList<String>();
		for (int i = 1900; i < 2019; i++) {
			years.add(""+i);
		}

		final List<String> days = new ArrayList<>();

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
		pv_year.setData(years,years.size()-20);
		times[0] = years.get(years.size()-20);
		//准备月数据
		for (int i = 1; i < 13; i++)
		{
			months.add(i+"");
		}
		pv_month.setData(months,1);
		times[1] = "0"+months.get(1);
		for (int i = 1;i<32;i++)
		{
			days.add(i+"");
		}
		pv_day.setData(days,2);
		times[2] = "0"+days.get(2);
		pv_year.setOnSelectListener(new PickerView.onSelectListener() {


			@Override
			public void onSelect(String text, int point) {
				times[0]=text;
				if (Integer.parseInt(times[1])==2){
					days.clear();
					if (isLeapYear(Integer.parseInt(times[0]))){
						for (int i=1;i<30;i++){
							days.add(i+"");
						}
					}else {
						for (int i=1;i<29;i++){
							days.add(i+"");
						}
					}
					pv_day.setData(days,2);
				}

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
//			     	{
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


		pv_month.setOnSelectListener(new PickerView.onSelectListener() {

			@Override
			public void onSelect(String text, int point) {
				days.clear();
				// 选中的开始时间
				int month = Integer.parseInt(text);
				if (month<10) {
					times[1] = "0"+text;
				}else {
					times[1] = text;
				}
				if (month==2){
					if (isLeapYear(Integer.parseInt(times[0]))){
						for (int i=1;i<30;i++){
							days.add(i+"");
						}
					}else {
						for (int i=1;i<29;i++){
							days.add(i+"");
						}
					}
				} else if (month == 1 || month ==3 || month ==5 || month ==7 || month ==8
						|| month ==10 || month ==12) {
					for (int i=1;i<32;i++){
						days.add(i+"");
					}
				}else {
					for (int i=1;i<31;i++){
						days.add(i+"");
					}
				}
				pv_day.setData(days,2);

//				tv_pop_title .setText(times[0]+"   "+times[1]);
			}
		});

		pv_day.setOnSelectListener(new PickerView.onSelectListener() {
			@Override
			public void onSelect(String text, int point) {
				int day = Integer.parseInt(text);
				if (day<10) {
					times[2] = "0"+text;
				}else {
					times[2] = text;
				}
			}
		});

		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				listener.listenResult(times[0]+"."+times[1]+"."+times[2]);
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();

			}
		});
	}

	public boolean isLeapYear(int year){
		if (year%100 == 0){
			if (year%400 == 0){
				return true;
			}else {
				return false;
			}
		}else if (year%4 == 0){
			return true;
		}else {
			return false;
		}
	}
	
	
}

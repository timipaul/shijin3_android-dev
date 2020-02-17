package com.hongchuang.ysblibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hongchuang.ysblibrary.R;
import com.hongchuang.ysblibrary.dao.AreaBean;
import com.hongchuang.ysblibrary.dao.AreaBeanDao;
import com.hongchuang.ysblibrary.dao.CityBean;
import com.hongchuang.ysblibrary.dao.CityBeanDao;
import com.hongchuang.ysblibrary.dao.CityManager;
import com.hongchuang.ysblibrary.dao.ProvinceBean;

import java.util.ArrayList;
import java.util.List;


public class PopSetectPlace {

	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private Button bt_cancel;// 取消
	private Button bt_ok;// 确定
	private String provinceid,cityid,areaid;
	int mHour ;
	int mMinute ;
	String timer;
	Activity mContext;
	List<CityBean> cityList;
	List<AreaBean> areaList;
	private String [] times=new String[3];

	public PopSetectPlace(Activity context){
		mContext=context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void showPopupWindow(View parent, SetText2 listener) {
		
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_place, null);
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
	public void showPopupWindow(View parent, SetText2 listener,int type) {

		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_select_place, null);
			(view.findViewById(R.id.bg)).setBackgroundResource(R.mipmap.icon_block_4);
			popWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			initPop(view,listener);
		}
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.showAsDropDown(parent);
//		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	private void initPop(View view,final SetText2 listener) {
		bt_ok = (Button) view.findViewById(R.id.bt_ok);//确定
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);// 取消


		PickerView pv_province =(PickerView) view.findViewById(R.id.pv_province);
		final PickerView pv_city =(PickerView) view.findViewById(R.id.pv_city);
		final PickerView pv_area =(PickerView) view.findViewById(R.id.pv_area);
		List<ProvinceBean> provinceList= new ArrayList<ProvinceBean>();
		cityList= new ArrayList<CityBean>();
		areaList= new ArrayList<AreaBean>();
		provinceList = CityManager.getInstance(mContext).getSession().getProvinceBeanDao().loadAll();
		cityList = CityManager.getInstance(mContext).getSession().getCityBeanDao().loadAll();
		areaList = CityManager.getInstance(mContext).getSession().getAreaBeanDao().loadAll();

		times[0]=provinceList.get(1).getFNAME();
		provinceid = provinceList.get(1).getFID();
		for (int i = 0; i < provinceList.size() ; i++) {
			if("SINGAPORE".equals(provinceList.get(i).getFNAME())){
				provinceList.remove(i);
			}
		}
		pv_province.setPData(provinceList,1);
		List<CityBean> citylist1=new ArrayList<CityBean>();
		for (CityBean list : cityList
				) {
			if (provinceList.get(1).getFID().equals(list.getCFPROVINCEIDID())) {
				citylist1.add(list);
			}
		}
		pv_city.setCData(citylist1, 1);
		times[1]=citylist1.get(1).getFNAME();
		cityid = citylist1.get(1).getFID();
		List<AreaBean> arealist1=new ArrayList<AreaBean>();
		for (AreaBean list : areaList
				) {
			if (citylist1.get(1).getFID().equals(list.getCFCITYIDID())) {
				arealist1.add(list);
			}
		}
		pv_area.setAData(arealist1, 1);
		times[2]=arealist1.get(1).getFNAME();
		areaid = arealist1.get(1).getFID();
		pv_province.setOnSelectListener1(new PickerView.onSelectListener1() {
			@Override
			public void onSelect(ProvinceBean pbean) {
				List<CityBean> citylist=new ArrayList<CityBean>();
				if(!"".equals(pbean.getFNAME()) && pbean.getFNAME()!=null) {
					times[0] = pbean.getFNAME();
					provinceid = pbean.getFID();
					citylist = CityManager.getInstance(mContext).getSession().getCityBeanDao().queryBuilder().where(CityBeanDao.Properties.CFPROVINCEIDID.eq(provinceid)).list();
//					for (CityBean list : cityList
//							) {
//						if (pbean.getFID().equals(list.getCFPROVINCEIDID())) {
//							citylist.add(list);
//						}
//					}
					if(citylist.size()==1) {
						pv_city.setCData(citylist, 0);
					}else if(citylist.size()>1){
						pv_city.setCData(citylist, citylist.size()-2);
					}else {
						pv_city.setCData(null,0);
					}
				}
			}
		});

		pv_city.setOnSelectListener2(new PickerView.onSelectListener2() {
			@Override
			public void onSelect( CityBean cbean) {
				if(null==cbean){
					times[1]="";
					pv_area.setAData(null,0);
					return;
				}
				times[1] = cbean.getFNAME();
				cityid = cbean.getFID();
				List<AreaBean> arealist=new ArrayList<AreaBean>();

				if(!"".equals(cbean.getFNAME())&&cbean.getFNAME()!=null) {
//					for (AreaBean list : areaList
//							) {
//						if (cbean.getFID().equals(list.getCFCITYIDID())) {
//							arealist.add(list);
//						}
////					}
					arealist = CityManager.getInstance(mContext).getSession().getAreaBeanDao().queryBuilder().where(AreaBeanDao.Properties.CFCITYIDID.eq(cityid)).list();
					if(arealist.size()==1) {
						pv_area.setAData(arealist, 0);
					}else if(arealist.size()>1){
						pv_area.setAData(arealist, arealist.size()-2);
					}else {
						pv_area.setAData(null,0);
					}
				}
			}
		});

		pv_area.setOnSelectListener3(new PickerView.onSelectListener3() {
			@Override
			public void onSelect(AreaBean Abean) {
				areaid = Abean.getFID();
				if(null==Abean){
					times[2]="";
					return;
				}
				if(!"".equals(Abean.getFNAME()) && Abean.getFNAME() != null ) {
					times[2] = Abean.getFNAME();
				}
			}

		});


		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				listener.listenResult(times[0]+" "+times[1]+" "+times[2],times[0],times[1],times[2]);
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

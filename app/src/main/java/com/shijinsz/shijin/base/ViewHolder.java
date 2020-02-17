package com.shijinsz.shijin.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/** 
 * @Description: 通用ViewHolder
*/
public class ViewHolder{
	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mPosition;
	private Context context;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mViews = new SparseArray<View>();
		this.mPosition = position;
		this.context = context;
			mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
			// setTag
			mConvertView.setTag(this);


	}

	/**
	 * 拿到一个ViewHolder对象
	 *
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {

		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			return (ViewHolder) convertView.getTag();
		}
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 *
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 *
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(View parentView,int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = parentView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}
	/**
	 * @Title: setVisibility
	 * @Description: View显示和隐藏
	 * @param @param viewId
	 * @param @param isShow    是否显示
	 * @return void    返回类型
	 */
	public void setVisibility(int viewId, boolean isShow){
		View view = getView(viewId);
		if (isShow) {
			view.setVisibility(View.VISIBLE);
		}else {
			view.setVisibility(View.GONE);
		}
	}
	/**
	 * @Title: setVisibility
	 * @Description: View显示和隐藏
	 * @param @param viewId
	 * @param @param isShow    是否显示
	 * @return void    返回类型
	 */
	public void setInVisibility(int viewId, boolean isShow){
		View view = getView(viewId);
		if (isShow) {
			view.setVisibility(View.VISIBLE);
		}else {
			view.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * @Title: setBackgroundResource
	 * @Description: View显示和隐藏
	 * @param @param viewId
	 * @param @param isShow    是否显示
	 * @return void    返回类型
	 */
	public void setBackgroundResource(int viewId, int isShow){
		View view = getView(viewId);
		view.setBackgroundResource(isShow);
	}
	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}
	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBackgroundResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setBackgroundResource(drawableId);
		return this;
	}
	/**
	 * 为TextView设置背景
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setTextViewResource(int viewId, int drawableId) {
		TextView view = getView(viewId);
		view.setBackgroundResource(drawableId);

		return this;
	}
	/**
	 *
	 * @Title: setTextColor
	 * @Description: 设置字体颜色
	 * @param @param viewId
	 * @param @param colorId
	 * @param @return    入参
	 * @return ViewHolder    返回类型
	 */
	public ViewHolder setTextColor(int viewId, int colorId) {
		TextView view = getView(viewId);
		view.setTextColor(context.getResources().getColor(colorId));
		return this;
	}


	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	public ViewHolder setOnClickListener(int viewId,
										 View.OnClickListener listener)
	{
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}


	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @return
	 */
//	public ViewHolder setImageByUrl(int viewId, String url) {
//		ImageLoaderHelper.displayImage((ImageView)getView(viewId), url);
//		return this;
//	}
	/**
	 * @Title: hideView
	 * @Description: 隐藏控件
	 * @param @param view
	 */
	public void hideView(View view) {
		view.setVisibility(View.GONE);
	}
	/**
	 * @Title: hideView
	 * @Description: 隐藏控件
	 * @param id
	 */
	public void hideView(int id) {
		getView(id).setVisibility(View.GONE);
	}
	/**
	 * @Title: hideView
	 * @Description: 显示控件
	 * @param @param view
	 */
	public void showView(View view) {
		view.setVisibility(View.VISIBLE);
	}
	/**
	 * @Title: hideView
	 * @Description: 显示控件
	 * @param id
	 */
	public void showView(int id) {
		getView(id).setVisibility(View.VISIBLE);
	}

	public View getmConvertView() {
		return mConvertView;
	}

}

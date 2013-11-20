package com.adpters;

import java.util.List;

import com.bean.UserWeiboBean;
import com.njust.testapi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserWeiBoInfoAdpter extends BaseAdapter {
	final private String TAG ="UserWeiBoInfoAdpter";
	private List<UserWeiboBean> mWeiBoInfo;
	private Context mContext;
	private LayoutInflater mInflater;

	static class WeiBoInfoViewHolder {
		TextView mWeiboInfomation;
		TextView mName;
		TextView mCreateTime;
	}

	public UserWeiBoInfoAdpter(Context ctx) {
		mContext = ctx;
		mInflater = LayoutInflater.from(ctx);
	}
	
	public void setWeiBoList(List<UserWeiboBean> mWeiboList) {
		mWeiBoInfo = mWeiboList;
	}
	@Override
	public int getCount() {
		if (null == mWeiBoInfo) {
			return 0;
		}
		return mWeiBoInfo.size();
	}

	@Override
	public Object getItem(int position) {
		if (null == mWeiBoInfo) {
			return null;
		}
		if (position < 0 || position >= mWeiBoInfo.size()) {
			return null;
		}
		return mWeiBoInfo.get(position);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == mWeiBoInfo) {
			return convertView;
		}
		if (position < 0 || position >= mWeiBoInfo.size()) {
			return convertView;
		}
		WeiBoInfoViewHolder viewHolder = null;
		final UserWeiboBean wb = mWeiBoInfo.get(position);

		convertView = mInflater.inflate(R.layout.adpter_showweibo,
				null);
		viewHolder = new WeiBoInfoViewHolder();
		viewHolder.mWeiboInfomation = (TextView) convertView
				.findViewById(R.id.getWeiboInfo);
		viewHolder.mName = (TextView) convertView
				.findViewById(R.id.getWeiboName);
		viewHolder.mCreateTime = (TextView) convertView
				.findViewById(R.id.getCreatTime);

		viewHolder.mWeiboInfomation.setText(wb.getText());
		viewHolder.mName.setText(wb.getName());
		viewHolder.mCreateTime.setText(wb.getTime());
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package com.adpters;

import java.util.List;



import com.bean.UserFriendsBean;
import com.njust.testapi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserFriends extends BaseAdapter {
	final private String TAG ="UserFriend";
	private List<UserFriendsBean> mFriendsInfo;
	private Context mContext;
	private LayoutInflater mInflater;

	static class FriendInfoViewHolder {
		TextView mName;
		TextView mUid;
	}

	public UserFriends(Context ctx) {
		mContext = ctx;
		mInflater = LayoutInflater.from(ctx);
	}
	
	public void setWeiBoList(List<UserFriendsBean> mFriendList) {
		mFriendsInfo = mFriendList;
	}
	@Override
	public int getCount() {
		if (null == mFriendsInfo) {
			return 0;
		}
		return mFriendsInfo.size();
	}

	@Override
	public Object getItem(int position) {
		if (null == mFriendsInfo) {
			return null;
		}
		if (position < 0 || position >= mFriendsInfo.size()) {
			return null;
		}
		return mFriendsInfo.get(position);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == mFriendsInfo) {
			return convertView;
		}
		if (position < 0 || position >= mFriendsInfo.size()) {
			return convertView;
		}
		FriendInfoViewHolder viewHolder = null;
		final UserFriendsBean wb = mFriendsInfo.get(position);

		convertView = mInflater.inflate(R.layout.adpter_friend,
				null);
		viewHolder = new FriendInfoViewHolder();
		viewHolder.mName = (TextView) convertView
				.findViewById(R.id.screen_name);
		viewHolder.mUid = (TextView) convertView
				.findViewById(R.id.uid);
		
		viewHolder.mName.setText(wb.getName());
		viewHolder.mUid.setText(wb.getId());
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package com.sjwlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sjwlib.R;
import com.sjwlib.bean.Province;

import java.util.List;

public class ProvinceAdapter extends BaseAdapter {

	private List<Province> mListDatas;
	private Context mContext;
	private LayoutInflater layoutInflater;

	public ProvinceAdapter(List<Province> mListDatas, Context mContext) {
		super();
		this.mListDatas = mListDatas;
		this.mContext = mContext;
		layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mListDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mListDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			convertView = new TextView(mContext);
//		}
//		((TextView) convertView)
//				.setText(mListDatas.get(position).getProvince());
//		return convertView;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_cityselect_item, parent, false);
		}
		TextView lbl_item = (TextView) convertView.findViewById(R.id.lbl_item);
		lbl_item.setText(mListDatas.get(position).getProvince());
		return convertView;
	}
}

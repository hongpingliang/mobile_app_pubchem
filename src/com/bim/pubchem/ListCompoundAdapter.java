package com.bim.pubchem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bim.pubchem.base.ECompound;

public class ListCompoundAdapter extends ListAdapter {
	final private ActivityListCompound activity;
	protected LayoutInflater inflater;
	private List<ECompound> compoundList;

	public ListCompoundAdapter(ActivityListCompound activity) {
		this.activity = activity;
		this.compoundList = new ArrayList<ECompound>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addArticleList(List<ECompound> list) {
		if (list != null) {
			compoundList.addAll(list);
		}
	}

	public int getCount() {
		if (compoundList.size() >= activity.getAvailabeArticleCount()) {
			return compoundList.size();
		} else {
			return compoundList.size() + 1;
		}
	}

	public ECompound getCompound(int position) {
		if (position < compoundList.size()) {
			return compoundList.get(position);
		} else {
			return null;
		}
	}

	public Object getItem(int position) {
		return getCompound(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_compound_row, parent,
					false);
		}

		View loadingView = convertView.findViewById(R.id.list_row_loading_view);
		View dataView = convertView.findViewById(R.id.list_row_data_view);
		final ECompound compound = getCompound(position);
		if (compound == null) {
			loadingView.setVisibility(View.VISIBLE);
			dataView.setVisibility(View.GONE);
			activity.chekAndLoadMore();
			return convertView;
		}

		loadingView.setVisibility(View.GONE);
		dataView.setVisibility(View.VISIBLE);

		updateView(activity, convertView, compound, position);
		return convertView;
	}

	public List<ECompound> getCompoundList() {
		return compoundList;
	}

	public void setCompoundList(List<ECompound> compoundList) {
		this.compoundList = compoundList;
	}
}

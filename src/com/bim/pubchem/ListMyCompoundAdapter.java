package com.bim.pubchem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bim.pubchem.base.ECompound;

public class ListMyCompoundAdapter extends ListAdapter {
	private ActivityMyCompound activity;
	protected LayoutInflater inflater;
	private List<ECompound> compoundList;

	public ListMyCompoundAdapter(ActivityMyCompound activity) {
		this.activity = activity;
		this.compoundList = new ArrayList<ECompound>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		if ( compoundList == null ) {
			return 0;
		}
		return compoundList.size();
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

		final ECompound compound = getCompound(position);

		updateView(activity, convertView, compound, position);
		return convertView;
	}

	public List<ECompound> getCompoundList() {
		return compoundList;
	}

	public void setCompoundList(List<ECompound> compoundList) {
		if (compoundList == null) {
			this.compoundList = new ArrayList<ECompound>();
		} else {
			this.compoundList = compoundList;
		}
	}

}

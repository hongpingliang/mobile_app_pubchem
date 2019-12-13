package com.bim.pubchem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.bim.core.Util;
import com.bim.pubchem.base.ESearchPubChem;

public class ListMySearchAdapter extends BaseAdapter {
	private ActivityMySearch activity;
	protected LayoutInflater inflater;
	private List<ESearchPubChem> searchList;

	public ListMySearchAdapter(ActivityMySearch activity) {
		this.activity = activity;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		if ( searchList == null ) {
			return 0;
		}
		return searchList.size();
	}

	public ESearchPubChem getSearch(int position) {
		return searchList.get(position);
	}

	public Object getItem(int position) {
		return getSearch(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View rowView, ViewGroup parent) {
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.my_search_row, parent,
					false);
		}

		final ESearchPubChem search = getSearch(position);

		CheckBox mCheckbox = (CheckBox) rowView
				.findViewById(R.id.my_search_row_checkbox);
		mCheckbox.setOnCheckedChangeListener(null);
		mCheckbox.setChecked(search.isChecked());
		mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				search.setChecked(isChecked);
			}
		});

		TextView mTerm = (TextView) rowView
				.findViewById(R.id.my_search_row_term);
		mTerm.setText(search.getTerm());

		
		set(rowView, R.id.my_search_row_create_date, search
				.getCreateDateLabel(), "Create Date: ");
		
		set(rowView, R.id.my_search_row_stereo_chirality, search
				.getStereoChiralityLabel(), "");
		set(rowView, R.id.my_search_row_stereo_ez, search
				.getStereoEZLabel(), "");
		set(rowView, R.id.my_search_row_bioassay,
				search.getBioAssayLabel(), "BioAssay: ");
		set(rowView, R.id.my_search_row_element, search.getElementsLabel(),
				"Element: ");

		setChemicalProperties(rowView, search);
		
		TextView mTime = (TextView) rowView
				.findViewById(R.id.my_search_row_time);
		mTime.setText(search.getTime());

		TextView mResult = (TextView) rowView
				.findViewById(R.id.my_search_row_result);
		mResult.setText(Util.getResourceString(activity,
				R.string.list_compound_result)
				+ ": " + search.getResult());

		ImageButton runButton = (ImageButton) rowView
				.findViewById(R.id.my_search_row_button_run);
		runButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				activity.runSearch(search);
			}
		});
		
		return rowView;
	}
	
	private void setChemicalProperties(View view, ESearchPubChem search) {
		List<String> properties = search.getChemicalPropertyList();
		String t = "";
		int cnt = 0;
		for (String s : properties) {
			if (Util.isNull(s)) {
				continue;
			}
			if (cnt > 0) {
				t += "\n";
			}
			t += s;
			cnt++;
		}
		set(view, R.id.my_search_row_chem_property, t, "");
		TextView mChemProp = (TextView) view
				.findViewById(R.id.my_search_row_chem_property);
		mChemProp.setLines(cnt);		
	}

	private void set(View view, int resId, String value, String name) {
		TextView mText = (TextView) view.findViewById(resId);
		if (Util.isNull(value)) {
			mText.setVisibility(View.GONE);
		} else {
			mText.setText(name + value);
			mText.setVisibility(View.VISIBLE);
		}
	}

	public List<ESearchPubChem> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<ESearchPubChem> searchList) {
		if (searchList == null) {
			searchList = new ArrayList<ESearchPubChem>();
		} else {
			this.searchList = searchList;
		}
	}
}

package com.bim.pubchem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.bim.core.Device;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSummary;
import com.bim.ncbi.EDatabase;
import com.bim.ncbi.ERequest;
import com.bim.ncbi.EResponse;
import com.bim.ncbi.EResponseSearch;
import com.bim.pubchem.base.DevicePubChem;
import com.bim.pubchem.base.ECallSummaryPubChem;
import com.bim.pubchem.base.ECompound;
import com.bim.pubchem.base.EResponseSummaryPubChem;
import com.bim.pubchem.base.ESearchPubChem;

public class ActivityListCompound extends ActivityPub {

	public final static int ACTIVITY_SORT = 2;
	private ListCompoundAdapter mListAdapter;
	private ERequest request;
	private ESearchPubChem search;
	private EResponseSearch searchResponse;
	private int sortResId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.list_compound);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		Intent intent = getIntent();
		if (intent == null) {
			Log.d("Error in ActivityListArticle:onCreate");
			return;
		}

		searchResponse = intent.getParcelableExtra("response");
		search = intent.getParcelableExtra("search");

		// searchResponse = new EResponseSearch();
		// searchResponse.setCount(10);

		if (searchResponse == null || searchResponse.getCount() < 1) {
			displayError("no search searchRequest/response");
			return;
		}

		ECallSummaryPubChem eSummary = new ECallSummaryPubChem(this);
		request = eSummary.getRequest();
		request.setDb(EDatabase.PUBCHEM_Compound);
		request.setRetstart(1);
		request.setRetmax(5);
		request.setQuery_key(searchResponse.getQueryKey());
		request.setWebEnv(searchResponse.getWebEnv());

		mListAdapter = new ListCompoundAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.list_compound_list);
		mListView.setAdapter(mListAdapter);

		setTitle(Util.getResourceString(this, R.string.list_compound_result)
				+ ": " + getAvailabeArticleCount());

		closeDialog();

		logDevice();
	}
	
	private void logDevice() {
		if (isFirstCreated) {
			if (search != null) {
				DevicePubChem.save(this, Device.ACTION_SEARCH, search
						.toJsonString());
			}
		}
		isFirstCreated = false;		
	}

	public void onESummaryOkay(EResponse response, ECallSummary eSummary) {
		EResponseSummaryPubChem responseChem = (EResponseSummaryPubChem) response;

		List<ECompound> list = responseChem.getCompoundList();
		if (list != null && list.size() > 0) {
			// Log.d("load: " + list.size());
			mListAdapter.addArticleList(list);
			mListAdapter.notifyDataSetChanged();
			int newStart = request.getRetstart() + request.getRetmax();
			request.setRetstart(newStart);
		}
	}

	public int getAvailabeArticleCount() {
		return searchResponse.getCount();
	}

	public void startSortActivity() {
		Intent intent = new Intent(this, ActivityCompoundSort.class);
		intent.putExtra("sortBy", sortResId);
		this.startActivityForResult(intent, ACTIVITY_SORT);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null) {
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		if (requestCode == ACTIVITY_SORT) {
			sort(intent);
		}
	}

	private void sort(Intent intent) {
		sortResId = intent.getIntExtra("sortBy", 0);
		if (sortResId <= 0) {
			return;
		}

		if (mListAdapter.getCompoundList().size() < 2) {
			return;
		}

		CompoundComparator comparator = new CompoundComparator(sortResId, false);
		Collections.sort(mListAdapter.getCompoundList(), comparator);
		mListAdapter.notifyDataSetChanged();
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.list_compound, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_list_compound_save_compound:
			save();
			return true;
		case R.id.menu_list_compound_email_compound:
			email();
			return true;
		case R.id.menu_list_compound_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		case R.id.menu_list_compound_save_search:
			saveSearch();
			return true;
		case R.id.menu_list_compound_sort:
			startSortActivity();
			return true;
		}

		return true;
	}

	private void saveSearch() {
		if (search == null) {
			return;
		}
		ArrayList<ESearchPubChem> list = new ArrayList<ESearchPubChem>();
		list.add(search);
		ActivityMySearch.saveSearch(this, list, MODE_APPEND);
	}

	private List<ECompound> getCheckedCompoundList() {
		List<ECompound> list = new ArrayList<ECompound>();
		for (ECompound a : mListAdapter.getCompoundList()) {
			if (a.isChecked()) {
				list.add(a);
			}
		}
		return list;
	}

	private void email() {
		List<ECompound> list = getCheckedCompoundList();
		if (list.size() < 1) {
			displayError("Please check a compound");
			return;
		}
		emailCompounds(this, list);
	}

	public static void emailCompounds(Activity activity, List<ECompound> list) {

		String subject = "PubChem Compounds";
		String content = "";
		int cnt = 1;
		for (ECompound a : list) {
			content += cnt + ". ";
			content += "\n";
			content += "  CID: " + a.getId() + "\n";
			if (!Util.isNull(a.getIupac())) {
				// content += " IUPAC: " + a.getIupac() + "\n";
			}
			content += "  ";
			content += ActivityCompoundDetail.URL + a.getCid();
			content += "\n\n";
			cnt++;
		}

		Util.doEmail(activity, subject, content);
	}

	public void chekAndLoadMore() {
		if (mListAdapter.getCompoundList().size() >= getAvailabeArticleCount()) {
			return;
		}

		if (httpThread != null && httpThread.isAlive()) {
			return;
		}

		showLoadingDialog();
		ECallSummaryPubChem eSummary = new ECallSummaryPubChem(this);
		eSummary.setRequest(request);
		httpThread = new Thread(eSummary);
		httpThread.start();
	}

	private void save() {
		List<ECompound> list = getCheckedCompoundList();
		if (list.size() < 1) {
			displayError("Please check a compound");
			return;
		}

		ActivityMyCompound.saveCompounds(this, list, MODE_APPEND);

		showMessage("Compound saved");
	}

	public void onImageLoaderCallback(Bitmap bitmap, Object object) {
		if (bitmap != null) {
			ECompound compound = (ECompound) object;
			compound.setIconBitmap(bitmap);
			mListAdapter.notifyDataSetChanged();
		}
	}

	public void startCompoundDetailActivity(ECompound compound) {
		ActivityCompoundDetail.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityCompoundDetail.class);
		intent.putExtra("compound", compound);
		this.startActivityForResult(intent, 1);
	}

}

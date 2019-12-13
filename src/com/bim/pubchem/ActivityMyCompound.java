package com.bim.pubchem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.pubchem.base.DevicePubChem;
import com.bim.pubchem.base.ECompound;

public class ActivityMyCompound extends ActivityPub {
	private ListMyCompoundAdapter mListAdapter;
	public static final String MY_COMPOUND_FILE_NAME = "pubchem_my_compound.txt";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.my_compound);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		mListAdapter = new ListMyCompoundAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.my_compound_list);
		mListView.setAdapter(mListAdapter);

		refreshDisplay();

		if (mListAdapter.getCount() < 1) {
			displayError("No saved compound");
		}
		logDevice();
	}

	private void logDevice() {
		if (isFirstCreated) {
			DevicePubChem.save(this, Device.ACTION_MY_ARTICLE, mListAdapter
					.getCount()
					+ "");
		}
		isFirstCreated = false;
	}

	private void refreshDisplay() {
		List<ECompound> compoundList = getCompoundList();
		mListAdapter.setCompoundList(compoundList);
		mListAdapter.notifyDataSetChanged();
	}

	public static void saveCompounds(Activity activity, List<ECompound> list,
			int mode) {
		try {
			FileOutputStream fOut = activity.openFileOutput(
					MY_COMPOUND_FILE_NAME, mode);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			for (ECompound a : list) {
				// Log.d(a.toJsonString());
				osw.write(a.toJsonString() + "\n");
			}
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ECompound> getCompoundList() {
		ArrayList<ECompound> compoundList = new ArrayList<ECompound>();
		try {
			FileInputStream fIn = openFileInput(MY_COMPOUND_FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(fIn));
			String line;
			while ((line = br.readLine()) != null) {
				if (Util.isNull(line)) {
					continue;
				}
				// Log.d(line);
				ECompound a = new ECompound();
				if (a.parse(line)) {
					compoundList.add(a);
				}
			}
			fIn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compoundList;
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.my_compound, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_my_compound_select_all:
			selectAll();
			return true;
		case R.id.menu_my_compound_email_compound:
			email();
			return true;
		case R.id.menu_my_compound_delete:
			delete();
			return true;
		case R.id.menu_my_compound_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}

		return true;
	}

	private void selectAll() {
		for (ECompound a : mListAdapter.getCompoundList()) {
			a.setChecked(true);
		}
		mListAdapter.notifyDataSetChanged();
	}

	private void email() {
		List<ECompound> list = getSelected();
		if (list.size() < 1) {
			displayError("Please check a compound");
			return;
		}
		ActivityListCompound.emailCompounds(this, list);
	}

	private List<ECompound> getSelected() {
		ArrayList<ECompound> list = new ArrayList<ECompound>();
		for (ECompound a : mListAdapter.getCompoundList()) {
			if (a.isChecked()) {
				list.add(a);
			}
		}
		return list;
	}

	private void delete() {
		ArrayList<ECompound> list = new ArrayList<ECompound>();
		ArrayList<ECompound> saveList = new ArrayList<ECompound>();
		for (ECompound a : mListAdapter.getCompoundList()) {
			if (a.isChecked()) {
				list.add(a);
			} else {
				saveList.add(a);
			}
		}
		if (list.size() < 1) {
			displayError("Please check a compound");
			return;
		}
		saveCompounds(this, saveList, MODE_PRIVATE);
		refreshDisplay();
	}

	public void startCompoundDetailActivity(ECompound compound) {
		ActivityCompoundDetail.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityCompoundDetail.class);
		intent.putExtra("compound", compound);
		this.startActivityForResult(intent, 1);
	}

	public void onImageLoaderCallback(Bitmap bitmap, Object object) {
		if (bitmap != null) {
			ECompound compound = (ECompound) object;
			compound.setIconBitmap(bitmap);
			mListAdapter.notifyDataSetChanged();
		}
	}
}

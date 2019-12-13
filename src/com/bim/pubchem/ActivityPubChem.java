package com.bim.pubchem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bim.core.Device;
import com.bim.core.EDate;
import com.bim.core.Log;
import com.bim.core.PubSetting;
import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.ncbi.ECallSearch;
import com.bim.ncbi.EDatabase;
import com.bim.ncbi.ERequest;
import com.bim.ncbi.EResponseSearch;
import com.bim.ncbi.ESearch;
import com.bim.pubchem.base.BioAssay;
import com.bim.pubchem.base.ChemicalElement;
import com.bim.pubchem.base.DevicePubChem;
import com.bim.pubchem.base.ESearchPubChem;
import com.bim.pubchem.base.StereoChirality;
import com.bim.pubchem.base.StereoEZ;

public class ActivityPubChem extends ActivityPub {
	public static final int ACTIVITY_MY_SEARCH = 1;

	private static final int DATE_ID_FROM = 1;
	private static final int DATE_ID_TO = 2;
	
	private EditText mSearchTerm;

	private EDate fromDate;
	private EDate toDate;

	private String stereoChirality;
	private String stereoEZ;
	private String bioAssay;

	private List<String> elementList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ERequest.NCBI_TOOL = "PubChemMobile";
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.pubchem);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		mSearchTerm = (EditText) findViewById(R.id.pubchem_search_term_text);

		fromDate = new EDate();
		fromDate.setToToday();
		fromDate.setId(DATE_ID_FROM);
		Button createFromDate = (Button) findViewById(R.id.pubchem_create_date_from_button);
		createFromDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDatePicker(fromDate);
			}
		});
		
		toDate = new EDate();
		toDate.setToToday();
		toDate.setId(DATE_ID_TO);
		Button createToDate = (Button) findViewById(R.id.pubchem_create_date_to_button);
		createToDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDatePicker(toDate);
			}
		});

		initSpinner();

		Button searchButton = (Button) findViewById(R.id.pubchem_button_search);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doSearch();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.pubchem_button_clear);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				clear();
			}
		});
		
		PubSetting setting = getSetting(this);		
		
		
		boolean isFirst = setting.isFirstTime();
		if (isFirst) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Intent intent = new Intent(ActivityPubChem.this,
							ActivityWelcome.class);
					ActivityPubChem.this.startActivityForResult(intent, 0);
				}
			}, 3000);
			setting.setFirstTime(false);
		}

		closeDialog();		
		
		if ( isFirstCreated ) {
			DevicePubChem.save(this, Device.ACTION_START, isFirst + "");
		}
		isFirstCreated = false;
	}

	protected void onDatePickerDateSet(EDate date) {
		Button button = null;
		Button toButton = (Button) findViewById(R.id.pubchem_create_date_to_button);
		switch (date.getId()) {
		case DATE_ID_FROM:
			button = (Button) findViewById(R.id.pubchem_create_date_from_button);
			break;
		case DATE_ID_TO:
			button = toButton;
			break;
		}
		button.setText(date.getLabel());
		if (!toDate.isUserChanged()) {
			toDate.setUserChanged(true);
			toButton.setText(toDate.getLabel());
		}
	}

	private String addField(String name, int fromResId, int toResId) {
		EditText mFromText = (EditText) findViewById(fromResId);
		String from = mFromText.getText().toString();

		EditText mToText = (EditText) findViewById(toResId);
		String to = mToText.getText().toString();
		if (Util.isNull(from) && Util.isNull(to)) {
			return null;
		}

		if (Util.isNull(from)) {
			from = "0";
		}
		if (Util.isNull(to)) {
			to = "2147483647";
		}
		return ESearch.range(name, from, to);

	}

	private String getCreateDate(String op) {
		String from = null;
		if (fromDate.isUserChanged()) {
			from = fromDate.getLabel();
		}
		String to = null;
		if (toDate.isUserChanged()) {
			to = toDate.getLabel();
		}
		if (Util.isNull(from) && Util.isNull(to)) {
			return null;
		}

		return ESearch.range("CreateDate", from, to);
	}

	private void doSearch() {
		String op = "AND";
		String field = null;

		String t;

		field = getCreateDate(op);

		field = ESearch.join(field, stereoChirality, op);
		field = ESearch.join(field, stereoEZ, op);
		field = ESearch.join(field, bioAssay, op);

		t = addField("MolecularWeight",
				R.id.pubchem_field_MolecularWeight_from,
				R.id.pubchem_field_MolecularWeight_to);
		field = ESearch.join(field, t, op);

		t = addField("XLogP", R.id.pubchem_field_XLogP_from,
				R.id.pubchem_field_XLogP_to);
		field = ESearch.join(field, t, op);

		t = addField("HydrogenBondDonorCount",
				R.id.pubchem_field_HydrogenBondDonorCount_from,
				R.id.pubchem_field_HydrogenBondDonorCount_to);
		field = ESearch.join(field, t, op);

		t = addField("HydrogenBondAcceptorCount",
				R.id.pubchem_field_HydrogenBondAcceptorCount_from,
				R.id.pubchem_field_HydrogenBondAcceptorCount_to);
		field = ESearch.join(field, t, op);

		t = addField("RotatableBondCount",
				R.id.pubchem_field_RotatableBondCount_from,
				R.id.pubchem_field_RotatableBondCount_to);
		field = ESearch.join(field, t, op);

		t = addField("TPSA", R.id.pubchem_field_TPSA_from,
				R.id.pubchem_field_TPSA_to);
		field = ESearch.join(field, t, op);

		t = addField("HeavyAtomCount", R.id.pubchem_field_HeavyAtomCount_from,
				R.id.pubchem_field_HeavyAtomCount_to);
		field = ESearch.join(field, t, op);

		t = addField("IsotopeAtomCount",
				R.id.pubchem_field_IsotopeAtomCount_from,
				R.id.pubchem_field_IsotopeAtomCount_to);
		field = ESearch.join(field, t, op);

		t = addField("TautomerCount", R.id.pubchem_field_TautomerCount_from,
				R.id.pubchem_field_TautomerCount_to);
		field = ESearch.join(field, t, op);

		t = addField("CovalentUnitCount",
				R.id.pubchem_field_CovalentUnitCount_from,
				R.id.pubchem_field_CovalentUnitCount_to);
		field = ESearch.join(field, t, op);

		t = addField("Complexity", R.id.pubchem_field_Complexity_from,
				R.id.pubchem_field_Complexity_to);
		field = ESearch.join(field, t, op);

		t = addField("TotalFormalCharge",
				R.id.pubchem_field_TotalFormalCharge_from,
				R.id.pubchem_field_TotalFormalCharge_to);
		field = ESearch.join(field, t, op);

		if (elementList != null && elementList.size() > 0) {
			for (String e : elementList) {
				if (Util.isNull(e)) {
					continue;
				}
				String v = ESearch.value("Element", e);
				field = ESearch.join(field, v, op);
			}
		}

		String term = mSearchTerm.getText().toString();
		if (!Util.isNull(term)) {
			term = ESearch.value("All Fields", term);
		}
		if (Util.isNull(term) && Util.isNull(field)) {
			displayError("Please enter a term or tap a field");
			return;
		}

		String search = ESearch.join(term, field, op);
		search = search.replaceAll(" ", "+");

		showLoadingDialog();
		ECallSearch eSearch = new ECallSearch(this);
		eSearch.getRequest().setDb(EDatabase.PUBCHEM_Compound);
		eSearch.getRequest().setTerm(search);
		eSearch.getRequest().setRetmax(10);

		httpThread = new Thread(eSearch);
		httpThread.start();
	}

	private void onMySearchReturn(Intent intent) {
		ESearchPubChem search = intent.getParcelableExtra("search");
		if (search == null) {
			return;
		}
		mSearchTerm.setText(search.getTerm());

		Button button = (Button) findViewById(R.id.pubchem_create_date_from_button);
		fromDate.setValue(search.getFromDate(), button);
		button = (Button) findViewById(R.id.pubchem_create_date_to_button);
		toDate.setValue(search.getToDate(), button);

		stereoChirality = search.getStereoChirality();
		stereoEZ = search.getStereoEZ();
		bioAssay = search.getBioAssay();

		Util.selectSpinner(this, R.id.pubchem_stere_chirality_spinner,
				StereoChirality.findPositionById(stereoChirality));
		Util.selectSpinner(this, R.id.pubchem_stere_ez_spinner, StereoEZ
				.findPositionById(stereoEZ));
		Util.selectSpinner(this, R.id.pubchem_bioassay_spinner, StereoChirality
				.findPositionById(bioAssay));

		elementList = search.getElementList();
		if ( elementList != null && elementList.size() > 0 ) {
			int pos = ChemicalElement.findPositionById(elementList.get(0));
			Util.selectSpinner(this, R.id.pubchem_element_spinner, pos);
		}

		setValue(search.getMolecularWeight(),
				R.id.pubchem_field_MolecularWeight_from,
				R.id.pubchem_field_MolecularWeight_to);

		setValue(search.getXLogP(), R.id.pubchem_field_XLogP_from,
				R.id.pubchem_field_XLogP_to);
		setValue(search.getHydrogenBondDonorCount(),
				R.id.pubchem_field_HydrogenBondDonorCount_from,
				R.id.pubchem_field_HydrogenBondDonorCount_to);
		setValue(search.getHydrogenBondAcceptorCount(),
				R.id.pubchem_field_HydrogenBondAcceptorCount_from,
				R.id.pubchem_field_HydrogenBondAcceptorCount_to);
		setValue(search.getRotatableBondCount(),
				R.id.pubchem_field_RotatableBondCount_from,
				R.id.pubchem_field_RotatableBondCount_to);
		setValue(search.getTPSA(), R.id.pubchem_field_TPSA_from,
				R.id.pubchem_field_TPSA_to);
		setValue(search.getHeavyAtomCount(),
				R.id.pubchem_field_HeavyAtomCount_from,
				R.id.pubchem_field_HeavyAtomCount_to);
		setValue(search.getIsotopeAtomCount(),
				R.id.pubchem_field_IsotopeAtomCount_from,
				R.id.pubchem_field_IsotopeAtomCount_to);
		setValue(search.getTautomerCount(),
				R.id.pubchem_field_TautomerCount_from,
				R.id.pubchem_field_TautomerCount_to);
		setValue(search.getCovalentUnitCount(),
				R.id.pubchem_field_CovalentUnitCount_from,
				R.id.pubchem_field_CovalentUnitCount_to);
		setValue(search.getComplexity(), R.id.pubchem_field_Complexity_from,
				R.id.pubchem_field_Complexity_to);
		setValue(search.getTotalFormalCharge(),
				R.id.pubchem_field_TotalFormalCharge_from,
				R.id.pubchem_field_TotalFormalCharge_to);
		
		doSearch();
	}

	public void onESearchOkay(EResponseSearch response, ECallSearch eSearch) {
		if (response == null) {
			return;
		}
		Log.d("found: " + response.getIdList().size());
		if (response.getIdList().size() < 1) {
			displayError("No result found");
			return;
		}

		if (response.getQueryKey() <= 0 || Util.isNull(response.getWebEnv())) {
			displayError("Failed to get query key and webenv");
			return;
		}

		ESearchPubChem search = new ESearchPubChem();
		search.setTerm(mSearchTerm.getText().toString());
		if (fromDate != null && fromDate.isUserChanged()) {
			search.setFromDate(fromDate.getLabel());
		}
		if (toDate != null && toDate.isUserChanged()) {
			search.setToDate(toDate.getLabel());
		}
		search.setStereoChirality(stereoChirality);
		search.setStereoEZ(stereoEZ);
		search.setBioAssay(bioAssay);
		search.setElementList(elementList);
		search.setMolecularWeight(getValue(
				R.id.pubchem_field_MolecularWeight_from,
				R.id.pubchem_field_MolecularWeight_to));
		search.setXLogP(getValue(R.id.pubchem_field_XLogP_from,
				R.id.pubchem_field_XLogP_to));
		search.setHydrogenBondDonorCount(getValue(
				R.id.pubchem_field_HydrogenBondDonorCount_from,
				R.id.pubchem_field_HydrogenBondDonorCount_to));
		search.setHydrogenBondAcceptorCount(getValue(
				R.id.pubchem_field_HydrogenBondAcceptorCount_from,
				R.id.pubchem_field_HydrogenBondAcceptorCount_to));
		search.setRotatableBondCount(getValue(
				R.id.pubchem_field_RotatableBondCount_from,
				R.id.pubchem_field_RotatableBondCount_to));
		search.setTPSA(getValue(R.id.pubchem_field_TPSA_from,
				R.id.pubchem_field_TPSA_to));
		search.setHeavyAtomCount(getValue(
				R.id.pubchem_field_HeavyAtomCount_from,
				R.id.pubchem_field_HeavyAtomCount_to));
		search.setIsotopeAtomCount(getValue(
				R.id.pubchem_field_IsotopeAtomCount_from,
				R.id.pubchem_field_IsotopeAtomCount_to));
		search.setTautomerCount(getValue(R.id.pubchem_field_TautomerCount_from,
				R.id.pubchem_field_TautomerCount_to));
		search.setCovalentUnitCount(getValue(
				R.id.pubchem_field_CovalentUnitCount_from,
				R.id.pubchem_field_CovalentUnitCount_to));
		search.setComplexity(getValue(R.id.pubchem_field_Complexity_from,
				R.id.pubchem_field_Complexity_to));
		search.setTotalFormalCharge(getValue(
				R.id.pubchem_field_TotalFormalCharge_from,
				R.id.pubchem_field_TotalFormalCharge_to));

		search.setSort(eSearch.getRequest().getSort());
		search.setResult(response.getCount());

		ActivityListCompound.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityListCompound.class);
		intent.putExtra("search", search);
		intent.putExtra("response", response);
		this.startActivityForResult(intent, 1);
	}

	private String getValue(int fromResId, int toResId) {
		EditText mFromText = (EditText) findViewById(fromResId);
		String from = mFromText.getText().toString();

		EditText mToText = (EditText) findViewById(toResId);
		String to = mToText.getText().toString();
		if (Util.isNull(from) && Util.isNull(to)) {
			return null;
		}

		return Util.nullToNone(from) + ESearchPubChem.SEPARATOR
				+ Util.nullToNone(to);
	}

	private void setValue(String val, int fromResId, int toResId) {
		if (Util.isNull(val)) {
			return;
		}

		String[] pairs = val.split(ESearchPubChem.SEPARATOR);
		if (pairs == null) {
			return;
		}
		int len = pairs.length;
		if (len > 0) {
			EditText mFromText = (EditText) findViewById(fromResId);
			mFromText.setText(pairs[0]);
			if (len > 1) {
				EditText mToText = (EditText) findViewById(toResId);
				mToText.setText(pairs[1]);
			}
		}
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.pubchem, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_pubchem_my_search:
			startMySearchActivity();
			return true;
		case R.id.menu_pubchem_my_compound:
			startMyArticleActivity();
			return true;
		}

		return true;
	}

	private void startMySearchActivity() {
		ActivityMySearch.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityMySearch.class);
		this.startActivityForResult(intent, ACTIVITY_MY_SEARCH);
	}

	private void startMyArticleActivity() {
		ActivityMyCompound.isFirstCreated = true;
		Intent intent = new Intent(this, ActivityMyCompound.class);
		this.startActivityForResult(intent, 2);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null) {
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		if (requestCode == ACTIVITY_MY_SEARCH) {
			onMySearchReturn(intent);
		}
	}

	private void initSpinner() {
		ArrayAdapter adapter;
		int aSpinItem = android.R.layout.simple_spinner_item;
		int androidDropItem = android.R.layout.simple_spinner_dropdown_item;

		Spinner mSteChirSpinner = (Spinner) findViewById(R.id.pubchem_stere_chirality_spinner);
		adapter = new ArrayAdapter(this, aSpinItem, StereoChirality.getLabels());
		adapter.setDropDownViewResource(androidDropItem);
		mSteChirSpinner.setAdapter(adapter);
		mSteChirSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				stereoChirality = StereoChirality.find(position);
			}

			public void onNothingSelected(AdapterView arg0) {
			}
		});

		Spinner mStereoEZSpinner = (Spinner) findViewById(R.id.pubchem_stere_ez_spinner);
		adapter = new ArrayAdapter(this, aSpinItem, StereoEZ.getLabels());
		adapter.setDropDownViewResource(androidDropItem);
		mStereoEZSpinner.setAdapter(adapter);
		mStereoEZSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView parent, View v,
							int position, long id) {
						stereoEZ = StereoEZ.find(position);
					}

					public void onNothingSelected(AdapterView arg0) {
					}
				});

		Spinner mBioSpinner = (Spinner) findViewById(R.id.pubchem_bioassay_spinner);
		adapter = new ArrayAdapter(this, aSpinItem, BioAssay.getLabels());
		adapter.setDropDownViewResource(androidDropItem);
		mBioSpinner.setAdapter(adapter);
		mBioSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				bioAssay = BioAssay.find(position);
			}

			public void onNothingSelected(AdapterView arg0) {
			}
		});

		Spinner mElementSpinner = (Spinner) findViewById(R.id.pubchem_element_spinner);
		adapter = new ArrayAdapter(this, aSpinItem, ChemicalElement.getLabels());
		adapter.setDropDownViewResource(androidDropItem);
		mElementSpinner.setAdapter(adapter);
		mElementSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				elementList = new ArrayList<String>();
				String e = ChemicalElement.find(position);
				if (!Util.isNull(e)) {
					elementList.add(e);
				}
			}

			public void onNothingSelected(AdapterView arg0) {
			}
		});
	}

	private void clear() {
		mSearchTerm.setText(null);
		fromDate.setUserChanged(false);
		toDate.setUserChanged(false);
		Button fromButton = (Button) findViewById(R.id.pubchem_create_date_from_button);
		fromButton.setText(null);
		Button toButton = (Button) findViewById(R.id.pubchem_create_date_to_button);
		toButton.setText(null);

		clearSpinner(R.id.pubchem_stere_chirality_spinner);
		clearSpinner(R.id.pubchem_stere_ez_spinner);
		clearSpinner(R.id.pubchem_bioassay_spinner);
		clearSpinner(R.id.pubchem_element_spinner);

		clear(R.id.pubchem_field_MolecularWeight_from);
		clear(R.id.pubchem_field_MolecularWeight_to);
		clear(R.id.pubchem_field_XLogP_from);
		clear(R.id.pubchem_field_XLogP_to);
		clear(R.id.pubchem_field_HydrogenBondDonorCount_from);
		clear(R.id.pubchem_field_HydrogenBondDonorCount_to);
		clear(R.id.pubchem_field_HydrogenBondAcceptorCount_from);
		clear(R.id.pubchem_field_HydrogenBondAcceptorCount_to);
		clear(R.id.pubchem_field_RotatableBondCount_from);
		clear(R.id.pubchem_field_RotatableBondCount_to);
		clear(R.id.pubchem_field_TPSA_from);
		clear(R.id.pubchem_field_TPSA_to);
		clear(R.id.pubchem_field_HeavyAtomCount_from);
		clear(R.id.pubchem_field_HeavyAtomCount_to);
		clear(R.id.pubchem_field_IsotopeAtomCount_from);
		clear(R.id.pubchem_field_IsotopeAtomCount_to);
		clear(R.id.pubchem_field_TautomerCount_from);
		clear(R.id.pubchem_field_TautomerCount_to);
		clear(R.id.pubchem_field_CovalentUnitCount_from);
		clear(R.id.pubchem_field_CovalentUnitCount_to);
		clear(R.id.pubchem_field_Complexity_from);
		clear(R.id.pubchem_field_Complexity_to);
		clear(R.id.pubchem_field_TotalFormalCharge_from);
		clear(R.id.pubchem_field_TotalFormalCharge_to);
	}
	
	private static PubSetting _appSetting;

	public static PubSetting getSetting(Activity activity) {
		if (_appSetting != null) {
			return _appSetting;
		}
		_appSetting = new PubSetting();
		_appSetting.load(activity);
		if (_appSetting.isFirstTime()) {
			_appSetting.save(activity);
		}

		return _appSetting;
	}	
}
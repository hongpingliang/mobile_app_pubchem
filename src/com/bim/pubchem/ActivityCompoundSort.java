package com.bim.pubchem;

import com.bim.ncbi.ActivityPub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ActivityCompoundSort extends ActivityPub implements View.OnClickListener {
	private RadioGroup mRadioGroup;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.sort_compound);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		mRadioGroup = (RadioGroup) findViewById(R.id.sort_radio_group);

		Intent intent = getIntent();
		if (intent != null) {
			int mapMode = intent.getIntExtra("sortBy", 0);
			if (mapMode > 0) {
				mRadioGroup.check(mapMode);
			}
		}
		RadioButton mRadio = (RadioButton) findViewById(R.id.sort_by_cid);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_iupac);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_date_created);
		mRadio.setOnClickListener(this);
		mRadio = (RadioButton) findViewById(R.id.sort_by_mol_weight);
		mRadio.setOnClickListener(this);
		
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Intent intent = new Intent();
		intent.putExtra("sortBy", checkedId);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		intent.putExtra("sortBy", mRadioGroup.getCheckedRadioButtonId());
		setResult(RESULT_OK, intent);
		finish();
	}
}

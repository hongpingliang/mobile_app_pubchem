package com.bim.pubchem;

import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.pubchem.base.ECompound;

import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public abstract class ListAdapter extends BaseAdapter {

	protected void startCompoundDetailActivity(final ActivityPub activityBase,
			ECompound compound) {
		if (activityBase instanceof ActivityListCompound) {
			ActivityListCompound activity = (ActivityListCompound) activityBase;
			activity.startCompoundDetailActivity(compound);
			return;
		}
		if (activityBase instanceof ActivityMyCompound) {
			ActivityMyCompound activity = (ActivityMyCompound) activityBase;
			activity.startCompoundDetailActivity(compound);
			return;
		}
	}

	protected void updateView(final ActivityPub activityBase, View rowView,
			final ECompound compound, int position) {
		CheckBox mCheckbox = (CheckBox) rowView
				.findViewById(R.id.list_article_checkbox);
		mCheckbox.setOnCheckedChangeListener(null);
		mCheckbox.setChecked(compound.isChecked());
		mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				compound.setChecked(isChecked);
			}
		});

		TextView mTitle = (TextView) rowView
				.findViewById(R.id.list_row_compound_cid);
		mTitle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startCompoundDetailActivity(activityBase, compound);
			}
		});
		String num = (position + 1) + ".  ";
		String title = num + "CID " + compound.getCid();
		mTitle.setText(title, TextView.BufferType.SPANNABLE);

		Spannable str = (Spannable) mTitle.getText();
		// new StyleSpan(android.graphics.Typeface.ITALIC);
		str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, num.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new RelativeSizeSpan(0.75f), 0, num.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new UnderlineSpan(), num.length(), title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		TextView mCreateDate = (TextView) rowView
				.findViewById(R.id.list_row_compound_created_date);
		String created = "created: ";
		mCreateDate.setText(created + compound.getCreateDate());

		set(rowView, R.id.list_row_compound_synonym, compound.getSynonym());
		set(rowView, R.id.list_row_compound_iupac, compound.getIupac(),
				"IUPAC: ");

		TextView mMolWeight = (TextView) rowView
				.findViewById(R.id.list_row_compound_molWeight);
		String mw = "MW: ";
		mMolWeight.setText(mw + Util.nullToNone(compound.getMolWeight()));

		TextView mFormula = (TextView) rowView
				.findViewById(R.id.list_row_compound_formula);
		String mf = "MF: ";
		mFormula.setText(mf + Util.nullToNone(compound.getFormula()),
				TextView.BufferType.SPANNABLE);
		Spannable mfSpan = (Spannable) mFormula.getText();
		String mfStr = mFormula.getText().toString();
		for (int i = 0; i < mfStr.length(); i++) {
			if (Character.isDigit(mfStr.charAt(i))) {
				mfSpan.setSpan(new RelativeSizeSpan(0.65f), i, i + 1,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

		LinearLayout mAssayLayout = (LinearLayout) rowView
				.findViewById(R.id.list_row_compound_assay_layout);
		if (compound.getTotalAssay() > 0 || compound.getActiveAssay() > 0) {
			TextView mAssayAll = (TextView) rowView
					.findViewById(R.id.list_row_compound_assay_all);
			String all = "All ";
			mAssayAll.setText(all + compound.getTotalAssay());

			TextView mAssayActive = (TextView) rowView
					.findViewById(R.id.list_row_compound_assay_active);
			String active = "Active ";
			mAssayActive.setText(active + compound.getActiveAssay());
			mAssayLayout.setVisibility(View.VISIBLE);
		} else {
			mAssayLayout.setVisibility(View.GONE);
		}
		set(rowView, R.id.list_row_compound_pharmAction, compound
				.getPharmAction());

		ImageButton mImage = (ImageButton) rowView
				.findViewById(R.id.list_row_compound_image);
		mImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startCompoundDetailActivity(activityBase, compound);
			}
		});
		compound.updateOrLoadImage(activityBase, mImage);
	}

	protected void set(View view, int resId, String value) {
		set(view, resId, value, "");
	}

	protected void set(View view, int resId, String value, String name) {
		TextView mText = (TextView) view.findViewById(resId);
		if (Util.isNull(value)) {
			mText.setVisibility(View.GONE);
		} else {
			mText.setText(name + value);
			mText.setVisibility(View.VISIBLE);
		}
	}

}

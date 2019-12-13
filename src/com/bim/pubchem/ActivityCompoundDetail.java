package com.bim.pubchem;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.bim.core.Device;
import com.bim.core.Log;
import com.bim.core.Util;
import com.bim.ncbi.ActivityPub;
import com.bim.pubchem.base.DevicePubChem;
import com.bim.pubchem.base.ECompound;

public class ActivityCompoundDetail extends ActivityPub {
	public static final String URL = "https://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?cid=";

	private ECompound compound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.compound_detail);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon_title);

		Intent intent = getIntent();
		if (intent == null) {
			Log.d("Error in ActivityCompoundDetail:onCreate");
			return;
		}
		compound = intent.getParcelableExtra("compound");

		// compound = new ECompound();
		// compound.setCid(1983);

		if (compound == null) {
			displayError("No compound");
			return;
		}

		setTitle(Util.getResourceString(this, R.string.list_compound_row_CID)
				+ " " + compound.getCid());

		WebView mWebView = (WebView) findViewById(R.id.compound_detail_webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		FrameLayout mContentView = (FrameLayout) getWindow().getDecorView()
				.findViewById(android.R.id.content);
		final View zoom = mWebView.getZoomControls();
		mContentView.addView(zoom, ZOOM_PARAMS);
		zoom.setVisibility(View.GONE);

		mWebView.setWebViewClient(new WebViewClient() {

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setProgressBarIndeterminateVisibility(true);
			}

			public void onPageFinished(WebView view, String url) {
				setProgressBarIndeterminateVisibility(false);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				setProgressBarIndeterminateVisibility(false);
			}
		});
		mWebView.loadUrl(URL + compound.getCid());

		if (isFirstCreated) {
			DevicePubChem.save(this, Device.ACTION_SHOW_ABSTRACT, compound
					.getCid()
					+ "");
		}
		isFirstCreated = false;
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.compound_detail, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item == null) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_compound_detail_save_compound:
			save();
			return true;
		case R.id.menu_compound_detail_email_compound:
			email();
			return true;
		case R.id.menu_compound_detail_back:
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}

		return true;
	}

	private void email() {
		String subject = "PubChem: CID " + compound.getCid();
		String content = "\n";
		content += "  CID: " + compound.getCid() + "\n";
		content += "  ";
		content += ActivityCompoundDetail.URL + compound.getCid();
		content += "\n\n";
		Util.doEmail(this, subject, content);
	}

	public void save() {
		ArrayList<ECompound> list = new ArrayList<ECompound>();
		list.add(compound);
		ActivityMyCompound.saveCompounds(this, list, MODE_APPEND);
		showMessage("Compound saved");
	}

	private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

}
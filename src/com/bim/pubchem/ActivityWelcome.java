package com.bim.pubchem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ActivityWelcome extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.welcome);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.app_icon);

		Button closeButton = (Button) findViewById(R.id.welcome_close);
		closeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		TextView content = (TextView) findViewById(R.id.welcome_content);

		String t = "";
		t += "Thank you for using PubChem Mobile.\n\n";
		t += "To search, enter keywords or select other options, and hit the \"Search\" button.\n\n";
//		t += "To sort the search result, tap \"Sort by\".\n\n";
		t += "To view the saved query, press menu, then \"My Searches\"\n\n";
		t += "To view the saved compounds, press menu, then \"My Compounds\"\n\n";
		t += "To save the search query, on the list of compounds screen, press menu,  \"Save Search\".\n\n";
		t += "To email compounds, press menu, then \"Email\".\n\n";		
		t += "Enjoy!\n\n";
		t += "Your feedback is highly appreciated.\n\n";
		content.setText(t);

	}
}

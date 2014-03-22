package net.schuemie.GroceryList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SmsRead extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smsreader);
		
		
		
		
		TextView smsTxt = (TextView) findViewById(R.id.smsTxt);
	String  text = getIntent().getStringExtra("smsfrom");
		smsTxt.setText(text);
		
	TextView frmTxt = (TextView) findViewById(R.id.fromtxt);
	String  fromtext = getIntent().getStringExtra("smsbody");
	frmTxt.setText(fromtext);
		
		
	}

}

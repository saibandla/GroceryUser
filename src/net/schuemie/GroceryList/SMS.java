package net.schuemie.GroceryList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS extends Activity {
	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;
	public final static String EXTRA_MESSAGE = "net.schuemie.GroceryList.action.SmsAlertDialogActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);
		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);

		/*
		 * Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		 * sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
		 * sendIntent.setType("vnd.android-dir/mms-sms");
		 * startActivity(sendIntent);
		 */

		StringBuffer sb = new StringBuffer();
		final StringBuffer msg1 = new StringBuffer();
		for (int i = 0; i < GroceryListCursorAdapter.vector.size(); i++) {

			System.err.println(" from == "
					+ GroceryListCursorAdapter.vector.get(i));
			sb.append(GroceryListCursorAdapter.vector.get(i));
			sb.append("\n");

			msg1.append(GroceryListCursorAdapter.vector.get(i));
			if (i < GroceryListCursorAdapter.vector.size() - 1)
				msg1.append(",");
			// Put additional info (price, tags) in brackets
			txtMessage.setText(sb.toString());

		}
		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNo = txtPhoneNo.getText().toString();
				String message = txtMessage.getText().toString();

				if (phoneNo.length() > 0 && message.length() > 0)
					sendSMS(phoneNo, msg1.toString());
				else
					Toast.makeText(getBaseContext(),
							"Please enter both phone number and message.",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	// ---sends a SMS message to another device---
	private void sendSMS(String phoneNumber, String message) {
		/*
		 * PendingIntent pi = PendingIntent.getActivity(this, 0, new
		 * Intent(this, test.class), 0); SmsManager sms =
		 * SmsManager.getDefault(); sms.sendTextMessage(phoneNumber, null,
		 * message, pi, null);
		 */

		 String SENT = "SMS_SENT";
		// String DELIVERED = "SMS_DELIVERED";
		
		Intent sendintent = new Intent(getBaseContext(),
				SmsAlertDialogActivity.class);
		sendintent.putExtra(EXTRA_MESSAGE, message);
		PendingIntent sentPI = PendingIntent.getActivity(getBaseContext(), 0,
				sendintent, 0);

		// sentPI.putExtra(EXTRA_MESSAGE, message);
		// PendingIntent deliveredPI =
		// PendingIntent.getActivity(getBaseContext(), 0,
		// new Intent(getBaseContext(), SmsAlertDialogActivity.class),
		// 0);

		// ---when the SMS has been sent---
		IntentFilter nop=new IntentFilter();
		nop.addDataScheme("scheme1");
		 registerReceiver(new BroadcastReceiver(){
		 @Override
		 public void onReceive(Context arg0, Intent arg1) {
		 switch (getResultCode())
		 {
		 case Activity.RESULT_OK:
			 
		 Toast.makeText(getBaseContext(), "SMS sent",
				 
		 Toast.LENGTH_SHORT).show();
		 break;
		 case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		 Toast.makeText(getBaseContext(), "Generic failure",
		 Toast.LENGTH_SHORT).show();
		 break;
		 case SmsManager.RESULT_ERROR_NO_SERVICE:
		 Toast.makeText(getBaseContext(), "No service",
		 Toast.LENGTH_SHORT).show();
		 break;
		 case SmsManager.RESULT_ERROR_NULL_PDU:
		 Toast.makeText(getBaseContext(), "Null PDU",
		 Toast.LENGTH_SHORT).show();
		 break;
		 case SmsManager.RESULT_ERROR_RADIO_OFF:
		 Toast.makeText(getBaseContext(), "Radio off",
		 Toast.LENGTH_SHORT).show();
		 break;
		 }
		 }
		 }, new IntentFilter(SENT));
		//
		// //---when the SMS has been delivered---
		// registerReceiver(new BroadcastReceiver(){
		// @Override
		// public void onReceive(Context arg0, Intent arg1) {
		// switch (getResultCode())
		// {
		// case Activity.RESULT_OK:
		// Toast.makeText(getBaseContext(), "SMS delivered",
		// Toast.LENGTH_SHORT).show();
		// break;
		// case Activity.RESULT_CANCELED:
		// Toast.makeText(getBaseContext(), "SMS not delivered",
		// Toast.LENGTH_SHORT).show();
		// break;
		// }
		// }
		// }, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		
		sms.sendTextMessage(phoneNumber, null, "Grocery12F3"+message, sentPI, null);
		
		try {
			Thread.sleep(10000);
			new deleteSms(getBaseContext(), "Grocery12F3"+message, phoneNumber);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
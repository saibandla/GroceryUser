package net.schuemie.GroceryList;

import net.schuemie.GroceryList.GroceryListContentProvider.Categories;
import net.schuemie.GroceryList.GroceryListContentProvider.Items;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	String fromaddress;
	String messagebody;

	@Override
	public void onReceive(Context context, Intent intent) {
		fromaddress = "";
		messagebody = "";
		System.err.println("onReceive is triggereeeeeeeeeeeeeeeeeeee");
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				if (msgs[i].getOriginatingAddress() != null)
					fromaddress += msgs[i].getOriginatingAddress().toString();
				str += "SMS from mmm" + msgs[i].getOriginatingAddress();
				str += " :";
				if (msgs[i].getMessageBody() != null)
					messagebody += msgs[i].getMessageBody().toString();
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}
			// ---display the new SMS message---
			Log.d("smsrecevr", "msgggggggggggggggggggg");
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			try {
				// for(int i=0;i<fromaddress.length;i++)
				// {
				if (messagebody.contains("NewCategory")) {
					
					ContentValues values = new ContentValues();
				String cat=messagebody.replace("NewCategory", "");
					values.put(Categories.NAME, cat);
					context.getContentResolver().insert(GroceryListContentProvider.CATEGORIES_URI, values);
					// new deleteSms(context, delemsg, fromaddress);
				} else if (messagebody.contains("NewItem")) {
					ContentValues values = new ContentValues();
					String cat=messagebody.replace("NewItem", "");
					String[] item_msg=cat.split(",");
						values.put(Items.NAME, item_msg[0]);
						values.put(Items.CATEGORY, item_msg[1]);
						context.getContentResolver().insert(GroceryListContentProvider.ITEMS_URI, values);
					Toast.makeText(context, "new item Recieved",
							Toast.LENGTH_LONG).show();
				} else {
					// new deleteSms(context, messagebody, fromaddress);
					System.exit(0);
				}
				// }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
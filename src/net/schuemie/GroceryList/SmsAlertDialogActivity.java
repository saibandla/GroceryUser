package net.schuemie.GroceryList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class SmsAlertDialogActivity extends Activity 
{
	 String text;
 String fromtext;
 public static final String SMSALERT = "net.schuemie.GroceryList.action.SmsAlertDialogActivity";
@Override
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.grocery_home);
     text = getIntent().getStringExtra("smsbody");
	System.err.println("SmsAlertDialogActivity read ======================= "+ text);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder
        .setTitle("New Message")
        .setMessage("Are you sure you want to Save Data ?")
        .setCancelable(false)
        .setPositiveButton("Read", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	Intent i = new Intent(SmsAlertDialogActivity.this, SmsRead.class); 
			    i.putExtra("smsbody", text);
			    
		   i.putExtra("smsfrom", fromtext);
			    
			    
			    startActivity(i);
			    System.err.println("SmsAlertDialogActivity read =============2222========== "+ text);
            	
			    
			    
            	/*TextView view = new TextView(SmsAlertDialogActivity.this);
                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);
                String sms = "";
                while (cur.moveToNext()) {
                    sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";         
                }
                view.setText(sms);
                setContentView(view);*/
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
                dialog.cancel();
            }
        });
    AlertDialog alert = builder.create();
    alert.show();
}
}
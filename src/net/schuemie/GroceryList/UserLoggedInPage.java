/**
 * 
 */
package net.schuemie.GroceryList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author saket
 *
 */
public class UserLoggedInPage extends Activity {

	AlertDialog dialog1;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdashboard);
		
	}

	public void startShoping(View v)
	{
		Intent i = new Intent(UserLoggedInPage.this, GroceryList.class);
		startActivity(i);
		
	}
	public void history(View v)
	{
		Toast.makeText(UserLoggedInPage.this, "No history Found", Toast.LENGTH_LONG).show();
	}
	public void updateInfo(View v)
	{
		Intent i = new Intent(UserLoggedInPage.this, NewUserActivity.class);
		i.putExtra("ActionType", "Update");
		startActivity(i);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */

	@Override
	public void onBackPressed() {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(UserLoggedInPage.this);
		builder.setTitle("LogOut Confirm");
		builder.setMessage("Do you want to LogOut?");
		builder.setPositiveButton("LogOut", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserLoggedInPage.this, DatabaseActivity.class);
				startActivity(i);
				finish();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog1.dismiss();
			}
		});
		dialog1=builder.create();
		dialog1.show();
		
		
	}
	
	
	
}

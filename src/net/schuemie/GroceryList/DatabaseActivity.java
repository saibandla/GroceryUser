package net.schuemie.GroceryList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatabaseActivity extends Activity implements OnClickListener {
    
	Button mLogin;
	Button mNewUser;
	Button mShowAll;
	EditText mUsername;
	EditText mPassword;
	DbHelper mydb = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlogin);
        
        mNewUser = (Button)findViewById(R.id.buttonNewUser);
    	mNewUser.setOnClickListener(this);
      // NewUserActivity.this.getLoginCnfm();
        
        mLogin = (Button)findViewById(R.id.buttonLogin);
        mLogin.setOnClickListener(this);
        
        mShowAll = (Button)findViewById(R.id.buttonShowAll);
        mShowAll.setOnClickListener(this);
        if(getLoginCnfm())
        {
        	mNewUser.setText("Change Password");
        
        
        }
        
    }
    public  boolean getLoginCnfm()
	{
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		 boolean cbValue = sp.getBoolean("CHECKBOX", false);
		 return cbValue;
		 
	}
	public void onClick(View v) {
	
		switch(v.getId()){
		
		case R.id.buttonLogin:
			mUsername = (EditText)findViewById(R.id.editUsername);
			mPassword = (EditText)findViewById(R.id.editPassword);
			
			String uname = mUsername.getText().toString();
			String pass = mPassword.getText().toString();
			
			if(uname.equals("") || uname == null){
				Toast.makeText(getApplicationContext(), "Username Empty", Toast.LENGTH_SHORT).show();
			}else if(pass.equals("") || pass == null){
				Toast.makeText(getApplicationContext(), "Password Empty", Toast.LENGTH_SHORT).show();
			}else{
				boolean validLogin = validateLogin(uname, pass, DatabaseActivity.this);
				if(validLogin){
					System.out.println("In Valid");
					Intent i = new Intent(DatabaseActivity.this, UserLoggedInPage.class);
					startActivity(i);
					finish();
				}
			}
			break;
			
		case R.id.buttonNewUser:
			if(getLoginCnfm())
			{
				changePassword();
			}
			else
			{
			Intent i = new Intent(DatabaseActivity.this, NewUserActivity.class);
			i.putExtra("ActionType", "Insert");
			startActivity(i);
			finish();
			}
			break;
			
		case R.id.buttonShowAll:
			Intent i_admin = new Intent(DatabaseActivity.this, AdminPage.class);
			startActivity(i_admin);
			finish();
			break;
		}
	}

	public boolean validateLogin(String uname, String pass, Context context) {
		
		mydb = new DbHelper(context);
		SQLiteDatabase db = mydb.getReadableDatabase();
		//SELECT
		String[] columns = {"_id"};
		
		//WHERE clause
		String selection = "username=? AND password=?";
		
		//WHERE clause arguments
		String[] selectionArgs = {uname,pass};
		
		Cursor cursor = null;
		try{
		//SELECT _id FROM login WHERE username=uname AND password=pass
		cursor = db.query(DbHelper.SAKET_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
		startManagingCursor(cursor);
		}catch(Exception e){
			e.printStackTrace();
		}
		int numberOfRows = cursor.getCount();
		
		if(numberOfRows <= 0){
		
			Toast.makeText(getApplicationContext(), "Login Failed..\nTry Again", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		return true;
	}
	public void changePassword()
	{
		final Dialog dialog = new Dialog(DatabaseActivity.this);
		dialog.setContentView(R.layout.changepassword);
	    dialog.setTitle("Login");

	    // get the Refferences of views
	    final  EditText Oldpassword=(EditText)dialog.findViewById(R.id.oldpass);
	    final  EditText Newpassowrd=(EditText)dialog.findViewById(R.id.newpass);
	    final  EditText CNFNewpassowrd=(EditText)dialog.findViewById(R.id.cnfnewpass);
	    
		Button btnChange=(Button)dialog.findViewById(R.id.changePass);
			
		// Set On ClickListener
		btnChange.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// get The User name and Password
				String Oldpassw=Oldpassword.getText().toString();
				String Newpassw=Newpassowrd.getText().toString();
				String cnfNewpassw=CNFNewpassowrd.getText().toString();
				mydb = new DbHelper(DatabaseActivity.this);
				// fetch the Password form database for respective user name
				String username=mydb.isPasswordExists(Oldpassw);
				
				// check if the Stored password matches with  Password entered by user
				if(username!="Not Exist")
				{
					if(Newpassw.equals(cnfNewpassw))
					{
						SQLiteDatabase db=mydb.getWritableDatabase();
						db.execSQL("UPDATE "+ mydb.SAKET_TABLE_NAME + " SET password=\""+Newpassw+"\"  WHERE username = \""+username+"\"");
						Toast.makeText(DatabaseActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
					else
					{
						Toast.makeText(DatabaseActivity.this, "New password and Confirm Password does not match", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(DatabaseActivity.this, "The Old password does not Exist", Toast.LENGTH_LONG).show();
					
					
				}
			}
		});
		
		dialog.show();
	}
	
//	public void onDestroy(){
//		super.onDestroy();
//		mydb.close();
//	}
}
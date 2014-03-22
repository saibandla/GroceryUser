/**
 * 
 */
package net.schuemie.GroceryList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author saket
 *
 */
public class NewUserActivity extends Activity implements OnClickListener {

	private Button mRegister;
	private Button mCancel;
	private EditText mUsername;
	private EditText mPassword;
	private EditText mAddress;
	private EditText mEmail;
	public boolean cbValue;
	public String Actionl;
	String username1;
	private DbHelper myDb = new DbHelper(NewUserActivity.this);
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		Actionl=getIntent().getExtras().getString("ActionType");
		Toast.makeText(NewUserActivity.this, Actionl, Toast.LENGTH_LONG).show();
		mRegister = (Button)findViewById(R.id.buttonRegister);
		if(Actionl.equals("Update"))
		{
			TextView title=(TextView)findViewById(R.id.singupTitle);
			title.setText("Update Details");
			TableRow userNamerow=(TableRow)findViewById(R.id.row1);
			userNamerow.setVisibility(TableRow.INVISIBLE);
			mRegister.setText("Update");
			getUserData();
			
		}
		mRegister.setOnClickListener(this);
		
		mCancel = (Button)findViewById(R.id.buttonCancel);
		mCancel.setOnClickListener(this);
	}
	
	public void onClick(View v) {
	
		switch(v.getId()){
			
			case R.id.buttonCancel:
				if(Actionl.equals("Update"))
				{
					finish();
				}
				else
				{
				Intent i = new Intent(NewUserActivity.this, DatabaseActivity.class);
				startActivity(i);
				finish();
				}
				break;
				
			case R.id.buttonRegister:
				if(Actionl.equals("Update"))
				{
					mPassword = (EditText)findViewById(R.id.editPassword);
					mEmail = (EditText)findViewById(R.id.editEmail);
					mAddress=(EditText)findViewById(R.id.editAddress);
					
					String pass = mPassword.getText().toString();
					String email = mEmail.getText().toString();
					String address=mAddress.getText().toString();
					boolean invalid = false;
					 if(pass.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Password Missing", Toast.LENGTH_SHORT).show();
					}else if(email.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Email ID Missing", Toast.LENGTH_SHORT).show();
					}
					else if(address.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Address Missing", Toast.LENGTH_SHORT).show();
					}
					if(invalid == false){
						updateEntry( pass, email,address);
//						Intent i_register = new Intent(NewUserActivity.this, DatabaseActivity.class);
//						startActivity(i_register);
						finish();
					}					
					
					
					
			
				}
				else
				{
					
					mUsername = (EditText)findViewById(R.id.editUsername);
					mPassword = (EditText)findViewById(R.id.editPassword);
					mEmail = (EditText)findViewById(R.id.editEmail);
					mAddress=(EditText)findViewById(R.id.editAddress);
					String uname = mUsername.getText().toString();
					String pass = mPassword.getText().toString();
					String email = mEmail.getText().toString();
					String address=mAddress.getText().toString();
					boolean invalid = false;
					
					if(uname.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Username Missing", Toast.LENGTH_SHORT).show();
					}else if(pass.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Password Missing", Toast.LENGTH_SHORT).show();
					}else if(email.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Email ID Missing", Toast.LENGTH_SHORT).show();
					}
					else if(address.equals("")){
						invalid = true;
						Toast.makeText(getApplicationContext(), "Address Missing", Toast.LENGTH_SHORT).show();
					}
					if(invalid == false){
						addEntry(uname, pass, email,address);
						SmsManager sms = SmsManager.getDefault();
						String message=uname+","+email+","+address;
						
						sms.sendTextMessage("5556", null, "NewUserRegistration "+message, null, null);
						Intent i_register = new Intent(NewUserActivity.this, DatabaseActivity.class);
						startActivity(i_register);
						finish();
					}
				}
				
				break;
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		myDb.close();
	}
	public void loadSavedPreferences() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		 cbValue = sp.getBoolean("CHECKBOX", false);
		
		
	}
	public void getUserData()
	{
		mUsername = (EditText)findViewById(R.id.editUsername);
		mPassword = (EditText)findViewById(R.id.editPassword);
		mEmail = (EditText)findViewById(R.id.editEmail);
		mAddress=(EditText)findViewById(R.id.editAddress);
		SQLiteDatabase db=myDb.getReadableDatabase();
		Cursor point=db.query(DbHelper.SAKET_TABLE_NAME, null, null, null, null,  null, null);
		if(point.getCount()>0)
		{
			point.moveToFirst();
			username1= point.getString(point.getColumnIndex("username"));
 			String passsword= point.getString(point.getColumnIndex("password"));
 			String email= point.getString(point.getColumnIndex("email"));
 			String address= point.getString(point.getColumnIndex("address"));
 			mPassword.setText(passsword);
 			mEmail.setText(email);
 			mAddress.setText(address);
 			
		}
	}
	public void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	public void addEntry(String uname, String pass, String email,String address){
		
		SQLiteDatabase db = myDb.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("username", uname);
		values.put("password", pass);
		values.put("email", email);
		values.put("address", address);
		
		try{
			db.insert(DbHelper.SAKET_TABLE_NAME, null, values);
			savePreferences("CHECKBOX", true);
			Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			savePreferences("CHECKBOX", false);
			e.printStackTrace();
		}
	}
	public void updateEntry(String password,String email, String addre)
	{
		SQLiteDatabase db=myDb.getWritableDatabase();
		db.execSQL("UPDATE "+ myDb.SAKET_TABLE_NAME + " SET password=\""+password+"\",email=\""+email+"\",address=\""+addre+"\"  WHERE username = \""+username1+"\"");
		Toast.makeText(NewUserActivity.this, "Details Updated Successfully", Toast.LENGTH_LONG).show();
	}
}

/**
 * 
 */
package net.schuemie.GroceryList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author saket
 *
 */
public class SplashScreen extends Activity {

	protected boolean _active = true;

	// time to display the splash screen in ms
	protected int _splashTime = 4000; 

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		Intent intent = new Intent(RestoreDefaultDatabase.RESTORE_DEFAULT);
		intent.putExtra("Type", "startup");
    	startActivity(intent);
		// thread for displaying the SplashScreen
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while(_active && (waited < _splashTime)) {
						sleep(100);
						if(_active) {
							waited += 100;
						}
					}
				} catch(InterruptedException e) {
					// do nothing
				} finally {
					
					finish();
					startActivity(new Intent(SplashScreen.this, DatabaseActivity.class));
					
				}
			}
		};
		splashThread.start();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

}

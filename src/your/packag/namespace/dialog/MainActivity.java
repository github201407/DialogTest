package your.packag.namespace.dialog;
import com.example.pulltorefreshtest.RefreshActivity;

import your.packag.namespace.R;
import your.packag.namespace.dialog.button.ButtonTest;
import your.packag.namespace.dialog.radiogroup.RadioGroupTest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void dialogTest(View view){
		startActivity(new Intent(this, DialogActivity.class));
	}
	public void notificationTest(View view){
		startActivity(new Intent(this, Notification_Activity.class));
	}
	public void refreshViewTest(View view){
		startActivity(new Intent(this, RefreshActivity.class));
	}
	public void radiobuttonTest(View view){
		startActivity(new Intent(this, RadioGroupTest.class));
	}
	public void buttonTest(View view){
		startActivity(new Intent(this, ButtonTest.class));
	}
	public void sendEmail(View view){
		startActivity(new Intent(this, EmailSendActivity.class));
	}
	public void notification(View view){
		startActivity(new Intent(this, EmailSendActivity.class));
	}
	
		
}

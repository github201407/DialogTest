package your.packag.namespace.dialog;

import your.packag.namespace.R;
import your.packag.namespace.dialog.email.MailUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EmailSendActivity extends Activity {
	
	private EditText text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emailsend_activity);
		text = (EditText)findViewById(R.id.input);
	}
	
	public void sendEmail(View view){
		String txt = text.getText().toString();
		new MyAsyncTask().execute(txt);
	}
	
	class MyAsyncTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			String txt = arg0[0];
			MailUtil.sendEmail("bug信息", txt);
			return null;
		}
		
	}
}

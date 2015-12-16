package your.packag.namespace.dialog.button;

import java.util.Timer;
import java.util.TimerTask;

import your.packag.namespace.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class ButtonTest extends Activity {
	private Button mGetCodeBT;
	private int n = 60;
	Timer timer = null;
	TimerTask task = null;
	final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				n--;
				if (n == 0) {
					timer.cancel();
					timer.purge();
					timer = null;
					task.cancel();
					task = null;

					mGetCodeBT.setText("重新获取");
					// mPhoneET.setEnabled(true);
					n = 60;
					mGetCodeBT.setBackgroundResource(R.drawable.blank_btn_selector_enable);
					mGetCodeBT.setEnabled(true);
					mGetCodeBT.setTextColor(Color.parseColor("#14BF76"));
				} else {
					mGetCodeBT.setText(String.format("重新获取（%s）", n));
				}
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button_activity);
		mGetCodeBT = (Button)findViewById(R.id.getCode);
	}
	
	public void getCode(View view){
		mGetCodeBT.setBackgroundResource(R.drawable.blank_btn_selector_disable);
		mGetCodeBT.setEnabled(false);
		mGetCodeBT.setTextColor(Color.parseColor("#14BF76"));
		if (timer == null)
			timer = new Timer();
		if (task == null)
			task = new TimerTask() {
				@Override
				public void run() {
					Message msg = mHandler.obtainMessage();
					msg.what = 3;
					mHandler.sendMessage(msg);
				}
			};
		timer.schedule(task, 50, 1000);
	}
}

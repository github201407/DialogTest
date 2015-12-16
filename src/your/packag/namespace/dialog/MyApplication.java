package your.packag.namespace.dialog;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		UncaughtExceptionHandler uncaughtExceptionHandler = UncaughtExceptionHandler.getInstance();
		uncaughtExceptionHandler.init(this);
	}
}

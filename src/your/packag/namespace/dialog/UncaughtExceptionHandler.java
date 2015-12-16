package your.packag.namespace.dialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import your.packag.namespace.dialog.email.MailUtil;

public class UncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private static final String TAG = "NorrisInfo";
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static UncaughtExceptionHandler mInstance = new UncaughtExceptionHandler();
	private Context mContext;
	private Map<String, String> mLogInfo = new HashMap();

	public static UncaughtExceptionHandler getInstance() {
		return mInstance;
	}

	public void init(Context paramContext) {
		this.mContext = paramContext;

		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
		boolean finish = handleException(paramThread, paramThrowable);
		if ((!finish) && (this.mDefaultHandler != null)) {
			this.mDefaultHandler.uncaughtException(paramThread, paramThrowable);
		}
	}

	public boolean handleException(final Thread paramThread,
			final Throwable paramThrowable) {
		if (paramThrowable == null)
			return false;
		try {
			getDeviceInfo(this.mContext);

			saveCrashLogToFile(paramThrowable);

			new Thread() {
				public void run() {
					Looper.prepare();
					Configs.showSorry(
							mContext,
							mDefaultHandler,
							paramThread, paramThrowable);
					Looper.loop();
				}
			}.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void getDeviceInfo(Context ctx) {

		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				mLogInfo.put("versionName", versionName);
				mLogInfo.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mLogInfo.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	private void saveCrashLogToFile(Throwable paramThrowable)
			throws IOException {
		StringBuilder mStringBuffer = new StringBuilder();
		for (Entry entry : this.mLogInfo.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			mStringBuffer.append("$").append(key).append("=").append(value)
					.append("\r\n");
		}
		Object mWriter = new StringWriter();
		PrintWriter mPrintWriter = new PrintWriter((Writer) mWriter);
		paramThrowable.printStackTrace(mPrintWriter);
		paramThrowable.printStackTrace();
		Throwable mThrowable = paramThrowable.getCause();

		while (mThrowable != null) {
			mThrowable.printStackTrace(mPrintWriter);

			mPrintWriter.append("\r\n");
			mThrowable = mThrowable.getCause();
		}

		mPrintWriter.close();
		String mResult = mWriter.toString();
		mStringBuffer.append(mResult);

		final String content = mStringBuffer.toString();
		new Thread(new Runnable() {

			public void run() {
				// 发送奔溃日志到邮箱
				//MailUtil.sendEmail(mContext.getPackageName(), content);
			}
		}).start();

		if (Environment.getExternalStorageState().equals("mounted")) {
			FileOutputStream mFileOutputStream = new FileOutputStream(
					Configs.getDocumentFile(this.mContext));
			mFileOutputStream.write(mStringBuffer.toString().getBytes());
			mFileOutputStream.close();
		}
	}
}

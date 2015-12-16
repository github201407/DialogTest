package your.packag.namespace.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class Configs {
	private static final String TAG = "Configs";
	public static String sorryMessage = "很抱歉,程序出现异常,即将退出";
	private static String filesName = "jeoLog";

	private static String docunmentName = null;

	private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm_ss");
	static String LogName;

	private static String getFilesName() {
		if ((filesName != null) && (filesName.length() > 0)) {
			return filesName;
		}
		return "jeoLog";
	}

	private static String factoryDocunmentName(Context context) {
        String mTime = mSimpleDateFormat.format(new Date());
        if ((docunmentName != null) && (docunmentName.length() > 0)) {
            return Configs.LogName = docunmentName + "_" + mTime + ".log";
        }
        try {
            String appName = context.getPackageManager().getApplicationLabel(context.getPackageManager()
                    .getApplicationInfo(context
                            .getPackageName(), 0)).toString();
            return Configs.LogName = appName + "_" + mTime + ".log";
        } catch (Exception e) {
        }
        return Configs.LogName = "log_" + mTime + ".log";
    }

	public static File getDocumentFile(Context context) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + getFilesName());
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file, factoryDocunmentName(context));
		Log.e(TAG, "getDocumentFile:"+file.getAbsolutePath());
		return file;
	}

	public static File getLogFile(Context context) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + getFilesName());
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file, LogName);
		return file;
	}

	static Timer timer = new Timer();
	static boolean shouldReturnSystenmCaught;

	public static void showSorry(final Context context,
			final Thread.UncaughtExceptionHandler mDefaultHandler,
			final Thread paramThread, final Throwable paramThrowable) {
		long now = System.currentTimeMillis();
		long last = context.getSharedPreferences("jeolog", 0).getLong(
				"last_fc_time", 0L);

		if (Math.abs(now - last) < 10000L)
			shouldReturnSystenmCaught = true;
		else {
			shouldReturnSystenmCaught = false;
		}
		context.getSharedPreferences("jeolog", 0).edit()
				.putLong("last_fc_time", now).commit();

		try {
			timer.schedule(new TimerTask() {
				public void run() {
					Configs.afterDeal(shouldReturnSystenmCaught,
							mDefaultHandler, paramThread, paramThrowable);
				}
			}, 5000L);

			/*
			 * AlertDialog.Builder builder = new AlertDialog.Builder(context);
			 * 
			 * String appName = null; try { appName =
			 * context.getPackageManager()
			 * .getApplicationLabel(context.getPackageManager()
			 * .getApplicationInfo(context .getPackageName(), 0)).toString(); }
			 * catch (Exception e) { e.printStackTrace(); } if (appName == null)
			 * { appName = "应用"; }
			 * 
			 * StringBuilder stringBuilder = new StringBuilder();
			 * stringBuilder.append(appName).append("  ");
			 * stringBuilder.append("Sorry,应用异常退出");
			 * 
			 * builder.setMessage(stringBuilder); final String finalAppName =
			 * context.getPackageName(); builder.setPositiveButton("关闭", new
			 * OnClickListener() { public void onClick(DialogInterface dialog,
			 * int which) { timer.cancel();
			 * 
			 * Configs.afterDeal(shouldReturnSystenmCaught, mDefaultHandler,
			 * paramThread, paramThrowable); } });
			 * 
			 * Dialog dialog = builder.setCancelable(false).create();
			 * dialog.getWindow().setType(2003); dialog.show();
			 */
			Toast.makeText(context, sorryMessage, 0).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, sorryMessage, 0).show();
			afterDeal(shouldReturnSystenmCaught, mDefaultHandler, paramThread,
					paramThrowable);
		}
	}

	public static void afterDeal(final boolean shouldReturnSystenmCaught,
			final Thread.UncaughtExceptionHandler mDefaultHandler,
			final Thread paramThread, final Throwable paramThrowable) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!shouldReturnSystenmCaught) {
					Process.killProcess(Process.myPid());
					System.exit(1);
				} else {
					mDefaultHandler.uncaughtException(paramThread,
							paramThrowable);
				}
			}
		}).start();
	}

	public static void upLog(Context context, String appName, File file) {
		String targetURL = "http://logcenter.duapp.com/logcenter/up";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(targetURL);
		httpPost.setHeader("appname", appName);
		httpPost.setHeader("filename", file.getName());
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamEntity s = new InputStreamEntity(in, file.length());
			BufferedHttpEntity entity = new BufferedHttpEntity(s);
			httpPost.setEntity(entity);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			String result = EntityUtils.toString(resEntity);
			if (result.equals("success"))
				Toast.makeText(context, "感谢您的支持！", 0).show();
			else
				Toast.makeText(context, "上传失败了。。", 0).show();
		} catch (Exception ex) {
			Toast.makeText(context, "上传失败了。。", 0).show();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
}

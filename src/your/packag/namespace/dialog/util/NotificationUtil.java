package your.packag.namespace.dialog.util;

import your.packag.namespace.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class NotificationUtil {
	private NotificationManager mNotificationManager;
	private Notification notification;
	private RemoteViews contentView;
	private Context mCtx;
	
	public NotificationUtil(Context context){
		this.mCtx = context;
	}

	@SuppressWarnings("deprecation")
	private void showNotification() {
		mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.ic_launcher, "应用版本升级", System.currentTimeMillis());
		contentView = new RemoteViews(mCtx.getPackageName(), R.layout.notification_diy_layout);
		contentView.setProgressBar(R.id.progressBar, 100, 0, false);
		contentView.setTextViewText(R.id.progress, "已下载：0%");//RemoteViews 来更新Notification 上的信息。
		notification.contentView = contentView;
		// 正在下载时，点击通知栏，自动隐藏通知栏。
		PendingIntent contentIntent = PendingIntent.getActivity(mCtx, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
		notification.contentIntent = contentIntent;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags = Notification.FLAG_ONGOING_EVENT;// 点击通知栏上的通知时，通知自动消失
		mNotificationManager.notify(1, notification);
	}

	private void updateNotification(int progress) {
		contentView.setTextViewText(R.id.progress, "已下载：" + progress + "%");
		contentView.setProgressBar(R.id.progressBar, 100, progress, false);
		mNotificationManager.notify(1, notification);
	}

	private void delNotification() {
		mNotificationManager.cancel(1);
	}
	
	private void completeNotification() {
		delNotification();
		contentView.setTextViewText(R.id.title, "应用版本升级");
		contentView.setTextViewText(R.id.progress, "下载完毕");
		contentView.setViewVisibility(R.id.progressBar, View.INVISIBLE);
		mNotificationManager.notify(1, notification);
		delNotification();
	}

}

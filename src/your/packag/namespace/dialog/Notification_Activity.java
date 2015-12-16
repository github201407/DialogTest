package your.packag.namespace.dialog;

import your.packag.namespace.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Notification_Activity extends Activity {

	private NotificationManager manager;
	private Notification.Builder builder;
	private int numMessage = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_main);
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		builder = new Notification.Builder(this);

		Button bt4 = (Button) findViewById(R.id.button4);
		bt4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				builder.setContentTitle("Download").setContentText("下载中...").setSmallIcon(R.drawable.a);
				new Thread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						int count;
						for (count = 0; count <= 100; count += 5) {
							builder.setProgress(100, count, false);
							manager.notify(0, builder.build());
							try {
								Thread.sleep(200);
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						builder.setContentText("下载完毕");
						builder.setProgress(0, 0, false);
						manager.notify(1002, builder.build());
						manager.cancel(0);
					}
				}).start();
			}

		});
		
		// 普通Toast，居中显示
		Button bt0 = (Button) findViewById(R.id.button0);
		bt0.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast = Toast.makeText(Notification_Activity.this, "Hello World!", Toast.LENGTH_LONG);

				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
		
		// 自定义Toast，显示图标、标题、内容
		Button bt3 = (Button) findViewById(R.id.button3);
		bt3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast = new Toast(Notification_Activity.this);
				View view = LayoutInflater.from(Notification_Activity.this).inflate(R.layout.custom_toast, null);
				ImageView imageView = (ImageView) view.findViewById(R.id.imaget);
				imageView.setImageResource(R.drawable.a);
				TextView textViewTitle = (TextView) view.findViewById(R.id.titiet);
				textViewTitle.setText("这里是标题");
				TextView textViewText = (TextView) view.findViewById(R.id.textt);
				textViewText.setText("这里是内容");
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
		});
		Button bt = (Button) findViewById(R.id.button1);
		Button bt2 = (Button) findViewById(R.id.button2);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Notification_Activity.this, Notification_Activity.class);
				PendingIntent pi = PendingIntent.getActivity(Notification_Activity.this, 0, intent, 0);
				builder.setContentIntent(pi);
				builder.setTicker("new coming!");
				builder.setDefaults(Notification.DEFAULT_ALL);
				builder.setContentTitle("你有新的消息");
				builder.setContentText("sos!").setNumber(++numMessage);
				builder.setSmallIcon(R.drawable.a);
				// builder.setLargeIcon(R.drawable.b);
				// Uri uri =
				// Uri.parse("file:///mnt/sdcard/notification/ringer.mp3");
				// builder.setSound(uri);
				builder.setContent(null);
				builder.setAutoCancel(true);
				long[] vibrate = { 0, 100, 200, 300 };

				Notification notification = builder.build();// 仅限4.1版本以上使用
				notification.vibrate = vibrate;
				notification.ledARGB = 0xff00ff00;
				notification.ledOnMS = 300;
				notification.ledOffMS = 1000;
				notification.flags = Notification.FLAG_SHOW_LIGHTS;
				manager.notify(1000, notification);

			}
		});
		bt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				RemoteViews rv = new RemoteViews(getPackageName(), R.layout.custom_notification);
				rv.setImageViewResource(R.id.image, R.drawable.b);
				rv.setTextViewText(R.id.titie, "这是标题");
				rv.setTextViewText(R.id.text, "这里是内容");
				Intent intent = new Intent(Notification_Activity.this, Notification_Activity.class);
				PendingIntent pi = PendingIntent.getActivity(Notification_Activity.this, 0, intent, 0);
				builder.setContentIntent(pi);
				builder.setTicker("new coming!");
				builder.setContentTitle("");// 优先级低，不过必须设置，类似于初始化
				builder.setContentText("");// 优先级低，不过必须设置，类似于初始化
				builder.setSmallIcon(R.drawable.b);
				builder.setDefaults(Notification.DEFAULT_ALL);
				builder.setContent(rv);
				builder.setAutoCancel(true);
				Notification notification = builder.build();
				manager.notify(1001, notification);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private NotificationManager mNotificationManager;
	private Notification notification;
	private PendingIntent contentIntent;
	public void addNotification(View view){
		// 通知管理器
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		// 通知
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		// 绑定事件
		Context context = getApplicationContext();
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		Intent notificationIntent = new Intent(this, MainActivity.class);
		int flags = PendingIntent.FLAG_CANCEL_CURRENT;// 0-关闭下拉页面，不清除对应通知
		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击通知栏上的通知时，通知自动消失
		// 显示通知
		int HELLO_ID = 1;
		mNotificationManager.notify(HELLO_ID, notification);
	}
	public void updateNotification(View view){
		// 通知管理器
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		// 通知
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "***************";
		CharSequence contentText = "--------------";
		Intent notificationIntent = new Intent(this, MainActivity.class);
		int flags = PendingIntent.FLAG_CANCEL_CURRENT;// 0-关闭下拉页面，不清除对应通知
		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrate = {0,100,200,300};
		notification.vibrate = vibrate;
		
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击通知栏上的通知时，通知自动消失
		// 更新
		int HELLO_ID = 1;
//		mNotificationManager.notify();
		mNotificationManager.notify(HELLO_ID, notification);
	}
	public void delNotification(View view){
		int HELLO_ID = 1;
		// 通知管理器
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(HELLO_ID);
//		mNotificationManager.cancel(tag, id);
//		mNotificationManager.cancelAll();
	}

}

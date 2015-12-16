package your.packag.namespace.dialog;

import your.packag.namespace.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.interpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends Activity {
	Button btn;
	int mYear;
	int mMonth;
	int mDay;
	Button login;
	Button cancel;
	TextView textView1;
	EditText nameEdit;
	EditText pswEdit;
	int mProgress = 0;
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_main);
		// Handler mHandler;
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 2:
					if (mProgress <= 100)
						mProgressDialog.setProgress(mProgress += 5);
					if (mProgress == 100) {
						mProgressDialog.cancel();
					}
					msg = mHandler.obtainMessage();
					msg.what = 2;
					sendMessageDelayed(msg, 1000);
				}
			}
		};
		textView1 = (TextView) findViewById(R.id.textView1);
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(3);
				mProgress = 0;
				Message msg = mHandler.obtainMessage();
				msg.what = 2;
				mHandler.sendMessageDelayed(msg, 1000);
			}
		});
		Button btn1 = (Button) findViewById(R.id.button);
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(DialogActivity.this, "哔，哔，哔！", Toast.LENGTH_SHORT).show();
			}
		});

	}

	// int mProgress = 0;
	// final Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 2:
	// if (mProgress <= 100)
	// mProgressDialog.setProgress(mProgress += 5);
	// if (mProgress == 100) {
	// mProgressDialog.cancel();
	// }
	// msg = mHandler.obtainMessage();
	// msg.what = 2;
	// sendMessageDelayed(msg, 1000);
	// }
	// }
	// };

	final int M_DD = 5;
	final int M_ALERT = 1;
	final int M_ALERT_List = 2;
	final int M_ALERT_Progress = 3;
	final int M_ALERT_Date = 4;
	AlertDialog mAlterDlg = null;//
	AlertDialog mAlterDlg_list = null;//
	AlertDialog mAlterDlg_Date = null;//
	ProgressDialog mProgressDialog = null;//
	Dialog dia;

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case M_ALERT:
			builder.setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					DialogActivity.this.finish();
				}
			}).setNegativeButton("否", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.setTitle("提示对话框-列表选择");
			builder.setMessage("确定要退出吗?");
			AlertDialog alter = builder.create();
			mAlterDlg = alter;
			return alter;

		case 100:
			final CharSequence[] item = { "相册", "拍照" };
			return new AlertDialog.Builder(this).setTitle("添加附件").setItems(item, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int id) {
					switch (id) {
					case 0:
						Log.e("tag", "相册");
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("image/*");
						startActivityForResult(Intent.createChooser(i, null), 110);
					
						break;
					case 1:
						// Log.e("tag", "拍照");
//						File mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//								"IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
//						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
//						startActivityForResult(intent, 110);
						String state = Environment.getExternalStorageState();  
					       if (state.equals(Environment.MEDIA_MOUNTED)) {  
					           Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");     
					           startActivityForResult(getImageByCamera, 110);  
					       }  
					       else {  
					           Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();  
					       }  
						break;
					default:
						break;
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}).create();

		case M_ALERT_List:
			builder.setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					DialogActivity.this.finish();
				}
			});
			builder.setTitle("提示对话框-列表选择");
			final CharSequence[] items = { "Red", "Green", "Blue" };
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mAlterDlg.setTitle(items[which]);
					switch (which) {
					case 0:
						btn.setBackgroundColor(Color.RED);
						break;
					case 1:
						btn.setBackgroundColor(Color.GREEN);
						break;
					case 2:
						btn.setBackgroundColor(Color.BLUE);
						break;
					}
				}
			});
			AlertDialog alter1 = builder.create();
			mAlterDlg = alter1;
			return alter1;

		case M_ALERT_Progress:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("下载文件");
			progressDialog.setMessage("正在下载文件，请稍候......");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// progressDialog.setProgress(68);
			mProgressDialog = progressDialog;
			return progressDialog;

		case M_ALERT_Date:

			OnDateSetListener mDateSetListener = new OnDateSetListener() {

				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;

				}
			};
			// DateSetListener
			DatePickerDialog dateDialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
			return dateDialog;

		case M_DD:
			final Dialog dialog = new Dialog(this, R.style.dialog_translucent);
			dialog.setContentView(R.layout.dialog_diy_login);
			dialog.setTitle("登录界面");

			login = (Button) dialog.findViewById(R.id.login);
			cancel = (Button) dialog.findViewById(R.id.cancel);
			login.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					nameEdit = (EditText) dia.findViewById(R.id.editText1);
					pswEdit = (EditText) dia.findViewById(R.id.editText2);

					String name = nameEdit.getText().toString();
					String psw = pswEdit.getText().toString();
					String message = "name" + name + "password:" + psw;
					textView1.setText(message);
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
					dialog.dismiss();
					dia.cancel();

				}
			});
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// dialog.dismiss();
					dia.cancel();
				}
			});
			dia = dialog;
			return dialog;
		case ALERT_UPDATA_INOF:
			final Dialog info = new Dialog(this, R.style.dialog_diy);
			info.setContentView(R.layout.dialog_diy_layout);
			info.setCanceledOnTouchOutside(true);
			TextView title = (TextView) info.findViewById(R.id.info_title);
			TextView version = (TextView) info.findViewById(R.id.info_version);
			TextView content = (TextView) info.findViewById(R.id.info_content);
			title.setText("升级提醒");
			version.setText("新版本：6.6.6/20.42MB");
			content.setText("1.优化细节及体验 \n2.修改用户反馈问题");
			Button updateNow = (Button) info.findViewById(R.id.info_ok);
			Button waitWarn = (Button) info.findViewById(R.id.info_cancel);
			updateNow.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "updateNow", Toast.LENGTH_SHORT).show();
					info.dismiss();
				}
			});
			waitWarn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dia.dismiss();
				}
			});
			dia = info;
			return info;

		case 7:
			ProgressDialog progressDialog1 = new ProgressDialog(this);
			progressDialog1.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_large_white));
			progressDialog1.setCancelable(true);
			mProgressDialog = progressDialog1;
			return progressDialog1;

		}

		return super.onCreateDialog(id);
	}

	private static final String FORK_CAMERA_PACKAGE = "com.android.camera";
	private Intent getOpenCameraOpera(Uri uri) throws ClassNotFoundException {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			final Intent intent_camera = DialogActivity.this.getPackageManager().getLaunchIntentForPackage(FORK_CAMERA_PACKAGE);
			if (intent_camera != null) {
				intent.setPackage(FORK_CAMERA_PACKAGE);
			}
		} catch (Exception e) {
		}
		if (uri != null)
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		else {
			Log.e("tag", "fileUri is empty");
			return null;
		}
		Log.d("tag", "try open camera success !");
		return intent;
	}

	public void login(View view) {
		showDialog(M_DD);
	}

	public void alert(View view) {
		showDialog(M_ALERT);
	}

	public void alertList(View view) {
		showDialog(M_ALERT_List);
	}

	public void date(View view) {
		showDialog(M_ALERT_Date);
	}

	private final int ALERT_UPDATA_INOF = 6;

	public void updateInfo(View view) {
		showDialog(ALERT_UPDATA_INOF);
	}

	public void diyProgress(View view) {
		showDialog(7);
	}

	public void photo(View view) {
		showDialog(100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 110) {
			if(data != null)
				Log.e("tag", data.getData().toString());
			else
				Log.e("tag", "uri == null");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

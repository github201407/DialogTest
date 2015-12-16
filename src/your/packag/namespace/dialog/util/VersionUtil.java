package your.packag.namespace.dialog.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtil {

	public static int getVersionCode(Context mCtx) {
		PackageInfo packInfo = null;
		try {
			packInfo = mCtx.getPackageManager().getPackageInfo(mCtx.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getVersionName(Context mCtx) {
		PackageInfo packInfo = null;
		try {
			packInfo = mCtx.getPackageManager().getPackageInfo(mCtx.getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}

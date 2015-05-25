package cn.edu.zafu.news.common.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.jayfeng.lesscode.core.$;
import com.jayfeng.lesscode.core.UpdateService;

import cn.edu.zafu.news.R;
import cn.edu.zafu.news.model.Update;

/**
 * Created by lizhangqu on 2015/5/24.
 */
public class UpdateUtil {
    public static boolean check(Context context, Update update) {
        String vername = "";
        String log = "";
        String download;
        int vercode1;
        vercode1 = Integer.parseInt(update.getVersion());
        vername = update.getVersionShort();
        download = update.getInstallUrl();
        log = update.getChangelog();
        return check(context, vercode1, vername, download, log);
    }

    public static boolean check(final Context context, int vercode, String vername, final String download, String log) {
        if (vercode <= 0) {
            Toast.makeText(context, "您的版本是最新版！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "发现新版本", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.less_app_download_dialog_title) + vername)
                    .setMessage(log)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, UpdateService.class);
                            intent.putExtra($.KEY_DOWNLOAD_URL, download);
                            context.startService(intent);
                        }
                    }).show();
            return true;
        }
    }


    public static void download(Context context, String download) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("download_url", download);
        context.startService(intent);
    }

    public static int vercode(Context context) {
        boolean result = false;
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            int result1 = packageInfo.versionCode;
            return result1;
        } catch (PackageManager.NameNotFoundException var5) {
            throw new AssertionError(var5);
        }
    }
}

package sphereforme.sphereforme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import java.net.HttpCookie;

import static android.content.Context.MODE_PRIVATE;

public class GlobalAssets {
    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    public static GlobalAssets Global_Instance = null;
    private static final String Cookies_FILE_NAME = "Cookies";
    public static final String Cookie_Session_NAME = "SessionToken";

    private Context thisContext;

    public GlobalAssets(Context base) {
        thisContext = base;
    }

    public static void create_alert(Context currentContext,String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(currentContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void preferences_read() {
        SharedPreferences prefs = thisContext.getSharedPreferences(Cookies_FILE_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(Cookie_Session_NAME, null);
        if (restoredText != null) {
            msCookieManager.getCookieStore().add(null, HttpCookie.parse(restoredText).get(0));
        }
    }

    public void preferences_save(String key) {
        SharedPreferences.Editor editor = thisContext.getSharedPreferences(Cookies_FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(Cookie_Session_NAME, key);
        editor.apply();
    }

    public void clear_preferences() {
        SharedPreferences.Editor editor = thisContext.getSharedPreferences(Cookies_FILE_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        msCookieManager.getCookieStore().removeAll();
    }
}

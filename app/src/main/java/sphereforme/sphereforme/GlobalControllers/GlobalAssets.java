package sphereforme.sphereforme.GlobalControllers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpCookie;

import sphereforme.sphereforme.Network.PersistentCookieStore;

import static android.content.Context.MODE_PRIVATE;

public class GlobalAssets {
    public static java.net.CookieManager msCookieManager;

    public GlobalAssets(Context base) {
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

    public static void start_cookie_manager(Context context) {
        msCookieManager = new java.net.CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(msCookieManager);
    }
}

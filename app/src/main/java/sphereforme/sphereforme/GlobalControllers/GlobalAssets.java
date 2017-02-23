package sphereforme.sphereforme.GlobalControllers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.net.CookieHandler;
import java.net.CookiePolicy;

import sphereforme.sphereforme.Activities.ContactBook;
import sphereforme.sphereforme.Activities.Home;
import sphereforme.sphereforme.Activities.LoginPage;
import sphereforme.sphereforme.Network.PersistentCookieStore;

public class GlobalAssets {
    public static java.net.CookieManager msCookieManager;

    private static String FCM_KEYS_FILE = "FCM_STORE";
    private static SharedPreferences FCM_Prefs;

    private static String My_Info_File = "INFO.ME";
    private static SharedPreferences SP;

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

        FCM_Prefs = context.getSharedPreferences(FCM_KEYS_FILE,Context.MODE_PRIVATE);
        SP = context.getSharedPreferences(My_Info_File,Context.MODE_PRIVATE);
    }

    public static void clear_all() {
        msCookieManager.getCookieStore().removeAll();
        SP.edit().clear().apply();
    }

    public static void update_info(Context context, int id, String username) {
        SP = context.getSharedPreferences(My_Info_File,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putInt("ID", id);
        editor.putString("Username", username);
        editor.apply();
    }

    public static void update_fcm_key(String token) {
        SharedPreferences.Editor editor = FCM_Prefs.edit();
        editor.putString("FCM_Key", token);
        editor.apply();
    }

    public static String get_fcm_key() {
        return FCM_Prefs.getString("FCM_Key","");
    }

    public static boolean is_user_logged_in() {
        if (SP.getString("Username", "Not_Here").equals("Not_Here")) {
            return false;
        }
        return true;
    }

    public static void Logout() {
        clear_all();
        if (Home.this_activity != null)
            Home.this_activity.finish();
        if (ContactBook.this_activity != null)
            ContactBook.this_activity.finish();
    }
}

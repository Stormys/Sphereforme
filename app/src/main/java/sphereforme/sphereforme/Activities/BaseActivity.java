package sphereforme.sphereforme.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;

/**
 * Created by julian on 2/17/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected String scanned_qr_data;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("client_message").equals("1")) {
                    String s = intent.getStringExtra("Name");
                    scanned_qr_data = intent.getStringExtra("Qr_Data");
                    alert_friend_request(s);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("1")
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    public void go_to_settings(View view) {
        Intent intent = new Intent(this,SettingsPage.class);
        finish();
        startActivity(intent);
    }

    public void go_to_contact_book(View view) {
        Intent intent = new Intent(this,ContactBook.class);
        finish();
        startActivity(intent);
    }

    public void hold_to_scan(View view) {
        Intent intent = new Intent(this,QrScanner.class);
        finish();
        startActivity(intent);
    }


    private void alert_friend_request(String name) {
        AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle("New Contact");
        alertDialog.setMessage(name + " has added you. Would you like to add them back?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new Qr_ADD().launchTask("qr_add", "username=" + URLEncoder.encode(scanned_qr_data, "UTF-8"));
                        } catch (Exception e) {

                        }
                    }
                });
        alertDialog.show();
    }

    protected class Qr_ADD implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            JSONObject json_result = null;
            String success = null, message = null;
            try {
                json_result = new JSONObject(result);
                success = json_result.getString("success");
                message = json_result.getString("message");
            } catch (Exception e) {
            }
            if (success == null && message == null && result.equals("Unauthorized")) {
                GlobalAssets.create_alert(BaseActivity.this,"Not Authorized","Not Authorized");
            } else if (success == null && message == null) {
                GlobalAssets.create_alert(BaseActivity.this,"Error","Something bad happen.");
            } else if (success.equals("Yes")) {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
            } else
                GlobalAssets.create_alert(BaseActivity.this,"Error",message);
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

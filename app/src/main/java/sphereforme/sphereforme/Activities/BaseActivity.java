package sphereforme.sphereforme.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.Services.OurBroadcastReceiver;

public class BaseActivity extends AppCompatActivity {

    protected String scanned_qr_data;
    private OurBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new OurBroadcastReceiver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(OurBroadcastReceiver.FRIEND_REQUEST);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    public void go_to_settings(View view) {
        Intent intent = new Intent(this,SettingsPage.class);
        startActivity(intent);
    }

    public void go_to_contact_book(View view) {
        Intent intent = new Intent(this,ContactBook.class);
        startActivity(intent);
    }

    public void hold_to_scan(View view) {
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }

    public void on_new_friend_request(String name, String qr_data) {
        scanned_qr_data = qr_data;
        alert_friend_request(name);
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

package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.BasicNetworkManager;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;
import sphereforme.sphereforme.Services.TokenRefreshListenerService;

import static sphereforme.sphereforme.GlobalControllers.Qr.setBitmap;

public class LoginPage extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void login_submit(View view) {
        username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (username.equals("") || password.equals("")) {
            GlobalAssets.create_alert(this,"Empty Fields", "Fill all the fields.");
            return;
        }

        String urlParameters = null;

        try {
            urlParameters = "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
        } catch (Exception e) {
        }

        new LoginTask().launchTask("login",urlParameters);
    }

    public void go_to_signup(View view) {
        Intent intent = new Intent(this,RegisterPage.class);
        startActivity(intent);
    }

    public void go_to_main_page(int id) {
        GlobalAssets.update_info(this,id, username);

        try {
            new BasicNetworkManager().launchTask("fcm_add", "key=" + URLEncoder.encode(GlobalAssets.get_fcm_key(), "UTF-8"));
        } catch (Exception e) {
        }

        Intent i = new Intent(this, TokenRefreshListenerService.class);
        startService(i);

        Intent intent = new Intent(this,Home.class);
        finish();
        startActivity(intent);
    }

    private class LoginTask implements AsyncTaskCompleteListener<String> {

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
                GlobalAssets.create_alert(LoginPage.this,"Bad Login","Username or password incorrect.");
            } else if (success == null && message == null) {
                GlobalAssets.create_alert(LoginPage.this,"Error","Something bad happen.");
            } else if (success.equals("Yes")) {
                setBitmap(username);
                go_to_main_page(Integer.parseInt(message));
            } else {
                GlobalAssets.create_alert(LoginPage.this,"Error","Something bad happen.");
            }
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;
import sphereforme.sphereforme.Services.TokenRefreshListenerService;

import static sphereforme.sphereforme.GlobalControllers.Qr.setBitmap;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        global_setup();
    }

    private void global_setup() {
        GlobalAssets.start_cookie_manager(this);

        //Check if user is logged in
        try {
            for (HttpCookie cookie : GlobalAssets.msCookieManager.getCookieStore().get(new URI(NetworkManager.domain))) {
                if (cookie.getName().equals("SessionToken")) {
                    setBitmap(this);
                }
            }
        } catch (Exception e) {

        }
    }

    public void login_submit(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
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

    public void go_to_main_page() {
        Intent i = new Intent(this, TokenRefreshListenerService.class);
        startService(i);

        Intent intent = new Intent(this,QrScanner.class);
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
            } else if (success.equals("Yes") && message.equals("Successfully logged in")) {
                go_to_main_page();
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

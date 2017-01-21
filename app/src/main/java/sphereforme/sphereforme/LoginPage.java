package sphereforme.sphereforme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URLEncoder;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        global_setup();
    }

    private void global_setup() {
        if (GlobalAssets.Global_Instance == null) {
            GlobalAssets.Global_Instance = new GlobalAssets(this);
        }
        GlobalAssets.Global_Instance.preferences_read();

        for (HttpCookie cookie : GlobalAssets.msCookieManager.getCookieStore().getCookies()){
            if (cookie.getName().equals(GlobalAssets.Cookie_Session_NAME)) {
                Intent intent = new Intent(this,MainPage.class);
                startActivity(intent);
            }
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

        new LoginTask().launchTask("http://35.165.40.110/login",urlParameters);
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
                Intent intent = new Intent(LoginPage.this, MainPage.class);
                startActivity(intent);
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

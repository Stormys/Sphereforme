package sphereforme.sphereforme.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.BasicNetworkManager;
import sphereforme.sphereforme.R;

import static sphereforme.sphereforme.GlobalControllers.Qr.setBitmap;

public class LoginPage extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void login_submit(View view) {
        new Create_Login_Request().execute();
    }

    public void go_to_signup(View view) {
        Intent intent = new Intent(this,RegisterPage.class);
        startActivity(intent);
    }

    public void go_to_main_page(int id) {
        GlobalAssets.update_info(this,id, username);

        try {
            new BasicNetworkManager(this).launchTask("fcm_add", "key=" + URLEncoder.encode(GlobalAssets.get_fcm_key(), "UTF-8"));
        } catch (Exception e) {
        }

        Intent intent = new Intent(this,Home.class);
        finish();
        startActivity(intent);
    }

    private class LoginTask extends AsyncTaskCompleteListener {
        public LoginTask(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccess() throws JSONException {
            setBitmap(username);
            go_to_main_page(NetworkConnection.data.getJSONObject(0).getInt("id"));
        }

        @Override
        public void onFailure() throws JSONException {
            GlobalAssets.create_alert(LoginPage.this,"Error","Something bad happen.");
        }

        @Override
        public void onUnAuthorized() {
            GlobalAssets.create_alert(LoginPage.this,"Invalid Login","Username or password is incorrect.");
        }
    }

    private class Create_Login_Request extends AsyncTask<Void, Void, Boolean> {
        private String password;

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (username.equals("") || password.equals("")) {
                GlobalAssets.create_alert(LoginPage.this,"Empty Fields", "Fill all the fields.");
                return false;
            }

            try {
                new LoginTask(LoginPage.this).launchTask("login", "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            username = ((EditText) findViewById(R.id.username)).getText().toString();
            password = ((EditText) findViewById(R.id.password)).getText().toString();
        }
    }
}

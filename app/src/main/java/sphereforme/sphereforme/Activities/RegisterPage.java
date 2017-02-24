package sphereforme.sphereforme.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.BasicNetworkManager;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;
import sphereforme.sphereforme.Services.TokenRefreshListenerService;

import static sphereforme.sphereforme.GlobalControllers.Qr.setBitmap;

public class RegisterPage extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void submit_signup(View view) {
        //all the fields text
        username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailAddress)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirm)).getText().toString();

        if (!password.equals(confirmPassword)) {
            GlobalAssets.create_alert(this,"Error", "Passwords dont match");
            return;
        }

        if (username.equals("") || password.equals("") || email.equals("") || firstName.equals("") || lastName.equals("")) {
            GlobalAssets.create_alert(this,"Empty Fields", "Fill all the fields.");
            return;
        }

        String urlParameters = null;

        try {
            urlParameters = "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8")
                    + "&email=" + URLEncoder.encode(email, "UTF-8") + "&firstName=" + URLEncoder.encode(firstName, "UTF-8") + "&lastName=" + URLEncoder.encode(lastName, "UTF-8");
        } catch (Exception e) {

        }

        new SignupTask(this).launchTask("signup",urlParameters);
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

    private class SignupTask extends AsyncTaskCompleteListener {

        public SignupTask(Context activity) {
            super(activity);
        }

        @Override
        public void onSuccess() throws JSONException {
            setBitmap(username);
            go_to_main_page(NetworkConnection.data.getJSONObject(0).getInt("id"));
        }

        @Override
        public void onFailure() {
            GlobalAssets.create_alert(RegisterPage.this,"Duplicate",NetworkConnection.message);
        }

        @Override
        public void onUnAuthorized() {
            GlobalAssets.create_alert(RegisterPage.this,"Error","Something bad happen!");
        }

    }
}

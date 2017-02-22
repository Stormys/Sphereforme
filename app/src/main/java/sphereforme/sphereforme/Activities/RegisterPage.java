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
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void submit_signup(View view) {
        //all the fields text
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
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

        new SignupTask().launchTask("signup",urlParameters);
    }

    private class SignupTask implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            System.out.println(result);
            JSONObject json_result = null;
            String success = null, message = null;
            try {
                json_result = new JSONObject(result);
                success = json_result.getString("success");
                message = json_result.getString("message");
            } catch (Exception e) {
            }
            if (success.equals("No") && message.equals("Error. Something bad happen")) {
                GlobalAssets.create_alert(RegisterPage.this,"Error", message);
            } else if (success.equals("No") && message.equals("Username or email taken")) {
                GlobalAssets.create_alert(RegisterPage.this,"Duplicate",message);
            } else if (success.equals("Yes") && message.equals("Account created successfully")) {
                Intent intent = new Intent(RegisterPage.this, Home.class);
                finish();
                startActivity(intent);
            } else {
                GlobalAssets.create_alert(RegisterPage.this,"Error","Something bad happen!");
            }
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

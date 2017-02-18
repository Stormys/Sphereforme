package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;

public class SettingsPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
    }

    public void logout(View view) {
        new LogoutTask().launchTask("logout","");
    }

    private class LogoutTask implements AsyncTaskCompleteListener<String> {
        @Override
        public void onTaskComplete(String result) {
            Intent intent = new Intent(SettingsPage.this, LoginPage.class);
            finish();
            startActivity(intent);
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

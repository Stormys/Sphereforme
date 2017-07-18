package sphereforme.sphereforme.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.R;

public class SettingsPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
    }

    public void showDialog(View view) {
        Intent intent = new Intent(this,ImportBusinessCard.class);
        startActivity(intent);
    }

    public void logout(View view) {
        new  LogoutTask(this).launchTask("logout","");
    }

    private class LogoutTask extends AsyncTaskCompleteListener {

        public LogoutTask(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccess() {
            GlobalAssets.Logout();
            Intent intent = new Intent(SettingsPage.this, LoginPage.class);
            finish();
            startActivity(intent);
        }

        @Override
        public void onFailure() throws JSONException {
            GlobalAssets.Logout();
            Intent intent = new Intent(SettingsPage.this, LoginPage.class);
            finish();
            startActivity(intent);
        }
    }
}

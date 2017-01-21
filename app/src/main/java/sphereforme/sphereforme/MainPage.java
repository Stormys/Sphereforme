package sphereforme.sphereforme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import org.json.JSONObject;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    public void logout(View view) {
        new LogoutTask().launchTask("http://35.165.40.110/logout","");
    }

    private class LogoutTask implements AsyncTaskCompleteListener<String> {
        @Override
        public void onTaskComplete(String result) {
            GlobalAssets.Global_Instance.clear_preferences();
            Intent intent = new Intent(MainPage.this, LoginPage.class);
            startActivity(intent);
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.HttpCookie;
import java.net.URI;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.GlobalControllers.Qr;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.Services.TokenRefreshListenerService;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Test().execute();

        Intent i = new Intent(this, TokenRefreshListenerService.class);
        startService(i);
    }

    public void determine_where_to_go(Boolean home) {
        if (home)
            go_to_home();
        else
            go_to_login();
    }

    private void go_to_login() {
        Intent intent = new Intent(this,LoginPage.class);
        finish();
        startActivity(intent);
    }

    private void go_to_home() {
        Intent intent = new Intent(this,Home.class);
        finish();
        startActivity(intent);
    }

    private class Test extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            GlobalAssets.start_cookie_manager(Splash.this);
            try {
                for (HttpCookie cookie : GlobalAssets.msCookieManager.getCookieStore().get(new URI(NetworkManager.domain))) {
                    if (cookie.getName().equals("SessionToken")) {
                        Qr.setBitmap(Splash.this);
                        return 0;
                    }
                }
            } catch (Exception e) {
            }
            return  1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1)
                go_to_login();
        }
    }
}

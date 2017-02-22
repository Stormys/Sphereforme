package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        global_setup();
    }

    private void global_setup() {
        GlobalAssets.start_cookie_manager(this);

        //Check if user is logged in
        try {
            for (HttpCookie cookie : GlobalAssets.msCookieManager.getCookieStore().get(new URI(NetworkManager.domain))) {
                if (cookie.getName().equals("SessionToken")) {
                    Qr.setBitmap(this);
                    return;
                }
            }
            go_to_login();
        } catch (Exception e) {
        }
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
        Intent i = new Intent(this, TokenRefreshListenerService.class);
        startService(i);

        Intent intent = new Intent(this,Home.class);
        finish();
        startActivity(intent);
    }

}

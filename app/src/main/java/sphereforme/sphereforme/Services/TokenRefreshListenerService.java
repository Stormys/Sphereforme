package sphereforme.sphereforme.Services;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.BasicNetworkManager;
import sphereforme.sphereforme.Network.NetworkManager;

/**
 * Created by julian on 2/16/17.
 */

public class TokenRefreshListenerService extends FirebaseInstanceIdService {
    public TokenRefreshListenerService() {
        onTokenRefresh();
    }
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (GlobalAssets.is_user_logged_in())
            sendRegistrationToServer(refreshedToken);
        GlobalAssets.update_fcm_key(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        try {
            new BasicNetworkManager(getApplicationContext()).launchTask("fcm_add", "key=" + URLEncoder.encode(token, "UTF-8"));
        } catch (Exception e) {

        }
    }
}

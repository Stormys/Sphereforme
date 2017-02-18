package sphereforme.sphereforme.Services;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.net.URLEncoder;

import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;

/**
 * Created by julian on 2/16/17.
 */

public class TokenRefreshListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Julian", "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        try {
            new FCM_Update().launchTask("fcm_add", "key=" + URLEncoder.encode(token, "UTF-8"));
        } catch (Exception e) {

        }
    }

    private class FCM_Update implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {

        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

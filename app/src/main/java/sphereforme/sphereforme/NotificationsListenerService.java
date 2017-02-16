package sphereforme.sphereforme;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by julian on 2/16/17.
 */

public class NotificationsListenerService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate(){
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if (message.getNotification() != null) {
            send_result_to_activity(message.getNotification().getBody());
        }
    }

    private void send_result_to_activity(String message) {
        Intent intent = new Intent("com.sphereforme.add");
        intent.putExtra("Name", message);
        broadcaster.sendBroadcast(intent);
    }
}

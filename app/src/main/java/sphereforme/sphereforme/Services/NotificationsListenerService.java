package sphereforme.sphereforme.Services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

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
            try {
                Map<String,String> result = message.getData();
                Intent intent = new Intent(result.get("client_message"));
                intent.putExtra("client_message",result.get("client_message"));
                intent.putExtra("Name", result.get("fullName"));
                intent.putExtra("Qr_Data",result.get("Qr_Data"));
                broadcaster.sendBroadcast(intent);
            }catch (Exception e) {
            }
        }
    }
}

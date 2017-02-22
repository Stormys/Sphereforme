package sphereforme.sphereforme.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import sphereforme.sphereforme.Activities.BaseActivity;

/**
 * Created by julian on 2/20/17.
 */

public class OurBroadcastReceiver extends BroadcastReceiver {
    public static String FRIEND_REQUEST = "1";

    private BaseActivity activity;

    public OurBroadcastReceiver(BaseActivity activity){
        super();
        this.activity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("1")) {
            String s = intent.getStringExtra("Name");
            activity.on_new_friend_request(s,intent.getStringExtra("Qr_Data"));
        }
    }
}

package sphereforme.sphereforme.Network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;

import sphereforme.sphereforme.Activities.LoginPage;
import sphereforme.sphereforme.GlobalControllers.GlobalAssets;

public abstract class AsyncTaskCompleteListener {
    protected NetworkManager NetworkConnection;
    private Context Calling_Context;

    public AsyncTaskCompleteListener(Context activity) {
        Calling_Context = activity;
    }

    public void onSuccess() throws JSONException{
    }

    public void onFailure() {
        GlobalAssets.create_alert(Calling_Context,"Error","Something bad happen.");
    }

    public void onError() {
        GlobalAssets.create_alert(Calling_Context,"Error","Something bad happen.");
    }

    public void onUnAuthorized() {
        GlobalAssets.Logout();
        Intent intent = new Intent(Calling_Context, LoginPage.class);
        ((Activity)Calling_Context).finish();
        Calling_Context.startActivity(intent);
    }

    public void launchTask(String url,String urlParameters) {
        NetworkConnection = new NetworkManager(this);
        NetworkConnection.execute(url, urlParameters);
    }
}

package sphereforme.sphereforme.Network;

/**
 * Created by julian on 2/21/17.
 */

public class BasicNetworkManager implements AsyncTaskCompleteListener {
    @Override
    public void onTaskComplete(Object result) {

    }

    @Override
    public void launchTask(String url, String urlParameters) {
        NetworkManager NetworkConnection = new NetworkManager(this);
        NetworkConnection.execute(url, urlParameters);
    }
}

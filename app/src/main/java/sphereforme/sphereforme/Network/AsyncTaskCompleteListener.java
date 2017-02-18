package sphereforme.sphereforme.Network;

/**
 * Created by julian on 1/21/17.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
    public void launchTask(String url,String urlParameters);
}

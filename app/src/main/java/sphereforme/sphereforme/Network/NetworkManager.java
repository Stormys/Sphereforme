package sphereforme.sphereforme.Network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by julian on 1/21/17.
 */

public class NetworkManager extends AsyncTask<String, Void, String> {
    private AsyncTaskCompleteListener<String> callback;
    public static String domain = "http://35.165.40.110/";

    public NetworkManager(AsyncTaskCompleteListener<String> cb) {
        this.callback = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        String resultToDisplay = "";

        InputStream in = null;
        try {

            URL url = new URL(domain + params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            urlConnection.connect();

            if (urlConnection.getResponseCode() == 401) {
                return "Unauthorized";
            }

            in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String data = "";

            while ((data = reader.readLine()) != null) {
                resultToDisplay += data + "\n";

            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return e.getMessage();
        }
        return resultToDisplay;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
    }

}

package sphereforme.sphereforme.Network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by julian on 1/21/17.
 */

public class NetworkManager extends AsyncTask<String, Void, Integer> {
    private AsyncTaskCompleteListener callback;
    public static final String domain = "http://35.165.40.110/";

    private static final int UNAUTHORIZED_CODE = 1;
    private static final int ERROR_CODE = 2;
    private static final int UNSUCCESSFUL_CODE = 3;
    private static final int SUCCESSFUL_CODE = 4;

    public JSONArray data;
    public String message;

    public NetworkManager(AsyncTaskCompleteListener cb) {
        this.callback = cb;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String result = "";

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
                return UNAUTHORIZED_CODE;
            }

            in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String data = "";

            while ((data = reader.readLine()) != null) {
                result += data + "\n";
            }
            JSONObject json_result;
            json_result = new JSONObject(result);
            message = json_result.getString("message");
            this.data = json_result.getJSONArray("data");

            if (json_result.getBoolean("success") == false) {
                if (message.equals("Error")) {
                    return ERROR_CODE;
                } else {
                    return UNSUCCESSFUL_CODE;
                }
            } else {
                return SUCCESSFUL_CODE;
            }

        } catch (Exception e) {
            return ERROR_CODE;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            switch (result) {
                case SUCCESSFUL_CODE:
                    callback.onSuccess();
                    break;
                case UNSUCCESSFUL_CODE:
                    callback.onFailure();
                    break;
                case UNAUTHORIZED_CODE:
                    callback.onUnAuthorized();
                    break;
                case ERROR_CODE:
                    callback.onError();
                    break;
            }
        } catch (JSONException e) {
            callback.onError();
        }
    }
}

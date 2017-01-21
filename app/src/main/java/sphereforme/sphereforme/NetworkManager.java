package sphereforme.sphereforme;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by julian on 1/21/17.
 */

public class NetworkManager extends AsyncTask<String, Void, String> {
    private AsyncTaskCompleteListener<String> callback;

    public NetworkManager(AsyncTaskCompleteListener<String> cb) {
        this.callback = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        String resultToDisplay = "";

        InputStream in = null;
        try {

            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (GlobalAssets.msCookieManager.getCookieStore().getCookies().size() > 0) {
                urlConnection.setRequestProperty("Cookie",
                        TextUtils.join(";", GlobalAssets.msCookieManager.getCookieStore().getCookies()));
            }

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            urlConnection.connect();

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    GlobalAssets.msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    GlobalAssets.Global_Instance.preferences_save(cookie);
                }
            }

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

package sphereforme.sphereforme.Network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class FileUploadManager extends AsyncTask<InputStream,Void,String> {
    public static final String urls = "http://35.165.40.110/upload";
    private static final String LINE_FEED = "\r\n";
    private String boundary;
    private HttpURLConnection httpConn;
    private DataOutputStream wr;
    private PrintWriter writer;

    @Override
    protected String doInBackground(InputStream... inputStreams) {
        try {
            create_Url();
            add_File("card","MyCard.pdf",inputStreams[0]);
            return finish();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void create_Url() throws IOException {
        boundary = "---------------------------" + System.currentTimeMillis();
        URL url = new URL(urls);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Accept-Charset", "UTF-8");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        wr = new DataOutputStream(httpConn.getOutputStream());
        writer = new PrintWriter(new OutputStreamWriter(wr, "UTF-8"), true);
    }

    protected void add_File(String fieldName, String file_name, InputStream file_stream) throws IOException{
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + file_name + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file_name)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = file_stream.read(buffer)) != -1) {
            wr.write(buffer, 0, bytesRead);
        }
        wr.flush();
        file_stream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    protected String finish() throws IOException {
        String result = "";
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();


        InputStream in = httpConn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String data = "";

        while ((data = reader.readLine()) != null) {
            result += data + "\n";
        }
        return result;
    }

}

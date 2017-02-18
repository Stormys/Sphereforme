package sphereforme.sphereforme.GlobalControllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.json.JSONObject;

import sphereforme.sphereforme.Activities.LoginPage;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;

/**
 * Created by julian on 1/24/17.
 */

public class Qr {
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;

    private static String data = null;
    private static Bitmap bit_qr_map = null;

    private static LoginPage callback;

    public static void setBitmap(LoginPage cb) {
        new Get_My_Qr().launchTask("my_qr","");
        callback = cb;
    }

    public static void set_QR(Activity activity) {
        View view = activity.findViewById(R.id.my_qr);
        if (view != null) {
            ((ImageView) view).setImageBitmap(bit_qr_map);
        }
    }

    private static void encodeAsBitmap() {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            return;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        bit_qr_map = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bit_qr_map.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    private static class Get_My_Qr implements AsyncTaskCompleteListener<String> {
        @Override
        public void onTaskComplete(String result) {
            JSONObject json_result = null;
            String success = null, message = null;
            try {
                json_result = new JSONObject(result);
                success = json_result.getString("success");
                message = json_result.getString("message");
            } catch (Exception e){
            }

            if (success.equals("Yes")) {
                data = message;
                encodeAsBitmap();
                callback.go_to_main_page();
            }
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

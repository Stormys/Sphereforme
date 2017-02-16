package sphereforme.sphereforme;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.json.JSONObject;

/**
 * Created by julian on 1/24/17.
 */

public class Qr {
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;

    private String data = null;
    private Bitmap bit_qr_map = null;

    private Activity activity_to_modify = null;

    public Qr() {
    }

    public void findBitmap(Activity temp) {
        activity_to_modify = (Activity) temp;
        new Get_My_Qr().launchTask("http://35.165.40.110/my_qr","");
    }

    private void encodeAsBitmap() {
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

    private void set_QR_in_UI() {
        View viewOBJ = activity_to_modify.findViewById(R.id.my_qr);
        if (viewOBJ != null) {
            ((ImageView) viewOBJ).setImageBitmap(bit_qr_map);
        }
        activity_to_modify = null;
    }

    private class Get_My_Qr implements AsyncTaskCompleteListener<String> {
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
                set_QR_in_UI();
            }
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }
}

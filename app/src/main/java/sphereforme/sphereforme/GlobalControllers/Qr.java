package sphereforme.sphereforme.GlobalControllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;

import sphereforme.sphereforme.Activities.Splash;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.R;

import static android.R.attr.data;

public class Qr {
    public final static int BACKGROUND = 0x00FFFFFF;
    public final static int CODE = 0xFF0061A8;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;

    private static String qr_data = null;
    private static Bitmap bit_qr_map = null;

    private static Splash callback;

    public static void setBitmap(Splash cb) {
        new Get_My_Qr(cb.getApplicationContext()).launchTask("my_qr","");
        callback = cb;
    }

    public static void set_QR(Activity activity) {
        View view = activity.findViewById(R.id.my_qr);
        if (view != null) {
            ((ImageView) view).setImageBitmap(bit_qr_map);
        }
    }

    public static void setBitmap(String id) {
        qr_data = id;
        encodeAsBitmap();
    }

    private static void encodeAsBitmap() {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(qr_data, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            return;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? CODE : BACKGROUND;
            }
        }

        bit_qr_map = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bit_qr_map.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    private static class Get_My_Qr extends AsyncTaskCompleteListener {

        public Get_My_Qr(Context activity) {
            super(activity);
        }

        @Override
        public void onSuccess() throws JSONException {
            qr_data = NetworkConnection.data.getJSONObject(0).getString("qr_data");
            encodeAsBitmap();
            callback.determine_where_to_go(true);
        }

        @Override
        public void onError() {
            callback.determine_where_to_go(false);
        }

        @Override
        public void onFailure() {
            callback.determine_where_to_go(false);
        }

        @Override
        public void onUnAuthorized() {
            callback.determine_where_to_go(false);
        }
    }
}

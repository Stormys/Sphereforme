package sphereforme.sphereforme.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.GlobalControllers.Qr;
import sphereforme.sphereforme.R;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class QrScanner extends BaseActivity {

    private final int Camera_Permissions = 2;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(4096, 1716).build();

        set_up_camera_with_surface_view();
        set_up_barcode_dectector();

        Qr.set_QR(this);

    }

    private void set_up_camera_with_surface_view() {
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    } else {
                        requestCameraPermission();
                    }
                } catch (IOException ie) {
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    private void set_up_barcode_dectector() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    scanned_qr_data = barcodes.valueAt(0).displayValue;
                    found_qr_code(scanned_qr_data);
                    this.release();
                    cameraSource.stop();
                }
            }
        });
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(QrScanner.this, permissions, Camera_Permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PERMISSION_DENIED) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            finish();
        } else {
            Start_Camera();
        }
    }

    private void Start_Camera() {
        try {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(cameraView.getHolder());
            }
        } catch (IOException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }

    private void found_qr_code(String data) {
        try {
            new Qr_Add_Query().launchTask("qr_check", "username=" + URLEncoder.encode(data, "UTF-8"));
        } catch (Exception e) {

        }
    }

    private void alert_adding_user(String name) {
        AlertDialog alertDialog = new AlertDialog.Builder(QrScanner.this).create();
        alertDialog.setTitle("New Contact");
        alertDialog.setMessage("Do you want to add " + name + "?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new Qr_ADD().launchTask("qr_add", "username=" + URLEncoder.encode(scanned_qr_data, "UTF-8"));
                        } catch (Exception e) {

                        }
                    }
                });
        alertDialog.show();
    }

    private class Qr_Add_Query implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            JSONObject json_result = null;
            String success = null, message = null;
            try {
                json_result = new JSONObject(result);
                success = json_result.getString("success");
                message = json_result.getString("message");
            } catch (Exception e) {
            }
            if (success == null && message == null && result.equals("Unauthorized")) {
                GlobalAssets.create_alert(QrScanner.this,"Not Authorized","Not Authorized");
            } else if (success == null && message == null) {
                GlobalAssets.create_alert(QrScanner.this,"Error","Something bad happen.");
            } else if (success.equals("Yes")) {
                alert_adding_user(message);
            } else {
                GlobalAssets.create_alert(QrScanner.this,"Error",message);
            }
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }

}

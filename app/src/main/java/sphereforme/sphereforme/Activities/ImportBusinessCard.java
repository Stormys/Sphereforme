package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.InputStream;

import sphereforme.sphereforme.Network.FileUploadManager;
import sphereforme.sphereforme.R;


public class ImportBusinessCard extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private static final int OPEN_FILE_REQUEST = 100;
    private static final String TAG = "Julian";
    private InputStream our_file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_import_business_card);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    public void Send_File(View view) {
        new FileUploadManager().execute(our_file);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this,1);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Toast.makeText(this,"Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    public void onGoogleDrive(View view){
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    public void OpenFileFromGoogleDrive(){
        IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder().setMimeType(new String[] { "application/pdf" }).build(mGoogleApiClient);
        try {
            startIntentSenderForResult(intentSender, OPEN_FILE_REQUEST, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
        }
    }

    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (result.getStatus().isSuccess()) {
                        OpenFileFromGoogleDrive();
                    }
                }
            };

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == OPEN_FILE_REQUEST && resultCode == RESULT_OK) {
            DriveId temp = (DriveId) data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            DriveFile file = temp.asDriveFile();
            file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    try {
                        DriveContents driveContents = result.getDriveContents();
                        our_file = driveContents.getInputStream();
                        PdfRenderer renderer = new PdfRenderer(driveContents.getParcelFileDescriptor());
                        Bitmap bitmap = Bitmap.createBitmap(1050, 600, Bitmap.Config.ARGB_4444);
                        PdfRenderer.Page page = renderer.openPage(0);
                        page.render(bitmap, null, null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                        ((ImageView) findViewById(R.id.pdfPreview)).setImageBitmap(bitmap);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }
}

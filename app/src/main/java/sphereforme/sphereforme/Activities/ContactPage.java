package sphereforme.sphereforme.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.net.URLEncoder;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.R;

public class ContactPage extends AppCompatActivity {
    protected TextView Full_Name;
    protected TextView Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        Intent passed_in = getIntent();
        String raw_data = passed_in.getStringExtra("Raw_friendId");

        Log.d("Julian", raw_data);

        Full_Name = ((TextView) findViewById(R.id.NameLoc));
        Email = ((TextView) findViewById(R.id.EmailLoc));

       try {
            new Get_Contact_Details(this).launchTask("get_contact_details", "id=" + URLEncoder.encode(raw_data, "UTF-8"));
        } catch (Exception e) {

        }
    }

    private class Get_Contact_Details extends AsyncTaskCompleteListener {
        public Get_Contact_Details(Context activity) {
            super(activity);
        }

        @Override
        public void onSuccess() throws JSONException {
            Log.d("Julian",NetworkConnection.data.toString());
            Full_Name.setText(NetworkConnection.data.getJSONObject(0).getString("fullName"));
            Email.setText(NetworkConnection.data.getJSONObject(0).getString("email"));
        }

        @Override
        public void onFailure() throws JSONException {
            GlobalAssets.create_alert(ContactPage.this,"Failed",NetworkConnection.message);
        }
    }

}

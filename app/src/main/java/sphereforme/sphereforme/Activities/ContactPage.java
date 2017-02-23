package sphereforme.sphereforme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import sphereforme.sphereforme.R;

public class ContactPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        Intent passed_in = getIntent();
        String raw_data = passed_in.getStringExtra("Raw_Data");

        ((TextView) findViewById(R.id.raw_data)).setText(raw_data);
    }

}

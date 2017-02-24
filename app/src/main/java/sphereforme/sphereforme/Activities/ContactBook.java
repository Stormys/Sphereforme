package sphereforme.sphereforme.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import sphereforme.sphereforme.GlobalControllers.GlobalAssets;
import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;

public class ContactBook extends BaseActivity implements AdapterView.OnItemClickListener {
    public static BaseActivity this_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);

        this_activity = this;

        new Get_Contact_Book(this).launchTask("my_contact_book","");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent contact_page = new Intent(this,ContactPage.class);
        contact_page.putExtra("Raw_Data", adapterView.getItemAtPosition(i).toString());
        startActivity(contact_page);
    }

    private class Get_Contact_Book extends AsyncTaskCompleteListener {

        public Get_Contact_Book(Context activity) {
            super(activity);
        }

        public void onSuccess() {
            ContactBookAdapter adapter = new ContactBookAdapter(ContactBook.this, NetworkConnection.data);

            ListView listView = (ListView) findViewById(R.id.table);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(ContactBook.this);
        }

        @Override
        public void onFailure() throws JSONException {
            GlobalAssets.create_alert(ContactBook.this,"Failed",NetworkConnection.message);
        }

    }

}

package sphereforme.sphereforme.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import sphereforme.sphereforme.Network.AsyncTaskCompleteListener;
import sphereforme.sphereforme.Network.NetworkManager;
import sphereforme.sphereforme.R;

public class ContactBook extends BaseActivity {
    public static BaseActivity this_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);

        this_activity = this;

        new Get_Contact_Book().launchTask("my_contact_book","");
    }


    private class Get_Contact_Book implements AsyncTaskCompleteListener<String> {

        @Override
        public void onTaskComplete(String result) {
            JSONArray json_result = null;
            try {
                json_result = new JSONArray(result);
            }catch(Exception e){

            }
            ContactBookAdapter adapter = new ContactBookAdapter(ContactBook.this, json_result);

            ListView listView = (ListView) findViewById(R.id.table);
            listView.setAdapter(adapter);
        }

        @Override
        public void launchTask(String url, String urlParameters) {
            NetworkManager NetworkConnection = new NetworkManager(this);
            NetworkConnection.execute(url, urlParameters);
        }
    }

}

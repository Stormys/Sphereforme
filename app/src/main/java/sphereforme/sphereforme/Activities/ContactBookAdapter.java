package sphereforme.sphereforme.Activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import sphereforme.sphereforme.R;

/**
 * Created by julian on 2/19/17.
 */

public class ContactBookAdapter extends BaseAdapter {
    private Activity activity;
    private JSONArray list;
    private TextView name;

    public ContactBookAdapter(Activity activity, JSONArray data) {
        super();
        this.activity = activity;
        this.list = data;
    }

    @Override
    public int getCount() {
        return list.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return list.getJSONObject(i);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.contact_book, null);
            name = (TextView) view.findViewById(R.id.name);
        }

        try {
            name.setText(list.getJSONObject(i).getString("fullName"));
        } catch (Exception e) {

        }

        return view;
    }
}
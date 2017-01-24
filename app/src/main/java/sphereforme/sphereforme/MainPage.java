package sphereforme.sphereforme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import org.json.JSONObject;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }
    public void go_to_scanner(View view) {
        Intent intent = new Intent(this, QrScanner.class);
        startActivity(intent);
    }
}

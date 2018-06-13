package com.example.leowlach.alertasti;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class IpActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_activity);

    }

    public void goBack(View view){
        EditText ed1 = findViewById(R.id.add);
        EditText ed2 = findViewById(R.id.port);
        String ip = ed1.getText().toString();
        int port = Integer.parseInt(ed2.getText().toString());
        getIntent().putExtra("ip", ip);
        getIntent().putExtra("port", port);
        setResult(RESULT_OK, getIntent());
        onBackPressed();
    }
}

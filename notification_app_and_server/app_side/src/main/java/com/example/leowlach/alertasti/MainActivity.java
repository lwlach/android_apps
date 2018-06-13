package com.example.leowlach.alertasti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView t;
    private myReceiver r;
    static boolean active;
    private String address;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = findViewById(R.id.alerta);
        address = "192.168.1.202";
        port = 101;


    }

    private void serverConnection(){
        try {
            if(r != null)
                unregisterReceiver(r);
            Intent i = new Intent(new Intent(this, myService.class));
            i.putExtra("ip", address);
            i.putExtra("port", port);
            startService(i);

            r = new myReceiver();
            IntentFilter inf = new IntentFilter(myService.ACTION);
            inf.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(r, inf);
        }
        catch (Exception e){
            t.setText("Sem conexão com o Servidor");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.endereco){
            Intent i = new Intent(this, IpActivity.class);
            startActivityForResult(i, 1);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            address = data.getStringExtra("ip");
            port = data.getIntExtra("port", 101);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        serverConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(r);
        active = false;
    }

    public void atualizar(View view) {
        serverConnection();
    }

    public class myReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = (String) intent.getExtras().get("alerta");
            t.setText(msg);
        }
    }

}

package com.example.user.tcpttt;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView turn,whoami;
    TableLayout tl;
    //boolean gameover = false;
    //int [] [] xo = new int [3][3];
    //private boolean isX = true;
    boolean player;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turn = (TextView)findViewById(R.id.turn);
        tl = (TableLayout)findViewById(R.id.game);
        whoami = (TextView)findViewById(R.id.whoami);
        h = new Handler(getMainLooper());
        checkWho();
        turn.setText("It's X turn");
    }

    public void check(View v)
    {
        new Thread() {
            public void run() {
                try

                {
                    Socket client = new Socket("10.0.0.1", 999);
                    //OutputStream os = client.getOutputStream();
                    //os.write("Hello from gold diggers".getBytes());
                    InputStream is = client.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    for(int b; (b = is.read()) > -1;)
                    {
                        sb.append((char)b);
                    }
                    final String newa = sb.toString();
                    h.post(new Runnable() {
                        public void run() {Toast.makeText(MainActivity.this, newa, Toast.LENGTH_SHORT).show();}});


                } catch (
                        IOException e
                        )

                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void checkWho()
    {
        new Thread(){
            public void run() {
                try {

                    Socket client = new Socket("10.0.0.1", 999);
                    OutputStream os = client.getOutputStream();
                    os.write("whoami".getBytes());
                    os.close();
                    InputStream is = client.getInputStream();
                    ObjectInputStream oin = new ObjectInputStream(is);
                    is.close();
                    try {
                        String stringFromServer = (String) oin.readObject();
                        whoami.setText(stringFromServer);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.print("CANT CONNECT");
                }
            }
        }.start();
    }
}

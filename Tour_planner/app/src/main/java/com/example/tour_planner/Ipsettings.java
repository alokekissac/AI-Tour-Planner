package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Ipsettings extends AppCompatActivity {
    EditText e1;
    Button b1;
    public static String ip;
    SharedPreferences sh;

    /** Live demo backend. Enter this, or your computer's IP:port when running the server locally. */
    public static final String DEFAULT_SERVER = "https://ai-tour-planner-7uzv.vercel.app";

    /**
     * Base URL for the server. Accepts either a full URL ("https://example.vercel.app")
     * or a bare "IP:port" ("192.168.1.10:5819", which gets http://).
     */
    public static String base(String value) {
        String v = value == null ? "" : value.trim();
        while (v.endsWith("/")) v = v.substring(0, v.length() - 1);
        if (v.startsWith("http://") || v.startsWith("https://")) return v;
        return "http://" + v;
    }

    public static String base() {
        return base(ip);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsettings);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.editText1);
        b1=(Button)findViewById(R.id.btip);
        e1.setText(sh.getString("ip", DEFAULT_SERVER));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=e1.getText().toString();

                if (ip.equals("")) {
                    e1.setError("Enter the server URL or IP:port");
                    e1.setFocusable(true);

                }else {
                    SharedPreferences.Editor e= sh.edit();
                    e.putString("ip",ip);
                    e.commit();
                    startActivity(new Intent(getApplicationContext(),Login.class));

                }

            }
        });
    }
}
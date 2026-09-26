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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsettings);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.editText1);
        b1=(Button)findViewById(R.id.btip);
        e1.setText(sh.getString("ip", "172.20.10.7:5786"));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=e1.getText().toString();

                if (ip.equals("")) {
                    e1.setError("Enter IP address");
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
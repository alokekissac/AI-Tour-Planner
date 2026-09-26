package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Customer_home extends AppCompatActivity {
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e=sh.edit();
        e.putString("val", "user");
        e.commit();
        b1=(Button)findViewById(R.id.btcom);
        b2=(Button)findViewById(R.id.btout);
        b3=(Button)findViewById(R.id.btpro);
        b4=(Button)findViewById(R.id.btplace);
        b5=(Button)findViewById(R.id.btchat);
        b6=(Button)findViewById(R.id.btbook);
        b7=(Button)findViewById(R.id.btmark);
        b8=(Button)findViewById(R.id.bttransilate);
        b9=(Button)findViewById(R.id.bttransilatefile);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Student_upload_anwer.class));

            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });


                b7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),Customer_view_marked_location.class));

                    }
                });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Customer_send_complaint.class));

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Customer_view_providers.class));

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Customer_add_to_favorite.class));

            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserViewChats.class));

            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Customer_view_booking.class));

            }
        });

    }
}
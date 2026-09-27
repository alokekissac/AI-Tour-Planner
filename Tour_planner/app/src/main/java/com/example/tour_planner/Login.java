package com.example.tour_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements JsonResponse {
    Button b1,b2;
    EditText e1,e2;
    String username,password;
    TextView tv1;
    public static String log_id,user_type;
    SharedPreferences sh;

    private static final int REQ_LOCATION = 101;

    /** Start the location service, asking for the runtime permission first if needed. */
    private void startLocation() {
        if (LocationService.hasPermission(this)) {
            startService(new Intent(getApplicationContext(), LocationService.class));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (LocationService.hasPermission(this)) {
                startService(new Intent(getApplicationContext(), LocationService.class));
            } else {
                Toast.makeText(this, "Location off - you can still log in and browse places", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor e=sh.edit();
//        e.putString("val", "public");
//        e.commit();

        startLocation();

        e1=(EditText)findViewById(R.id.etunm);
        e2=(EditText)findViewById(R.id.etpass);
        tv1=(TextView)findViewById(R.id.tvuserreg);
        b1=(Button)findViewById(R.id.btnlogin);
        b2=(Button)findViewById(R.id.btplace);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Public_view_places.class));

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = e1.getText().toString();
                password = e2.getText().toString();
                if (username.equalsIgnoreCase("")) {
                    e1.setError("Enter Valid Username");
                    e1.setFocusable(true);
                } else if (password.equalsIgnoreCase("")) {
                    e2.setError("Enter Valid Password");
                    e2.setFocusable(true);
                } else {


                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Login.this;
                    String q ="/login?username=" + username + "&password=" + password+"&latti="+(LocationService.lati==null?"":LocationService.lati)+"&longi="+(LocationService.logi==null?"":LocationService.logi);
                    q = q.replace(" ", "%20");
                    JR.execute(q);

                }
//                Toast.makeText(getApplicationContext(), "username : " + username + "\npassword : " + password, Toast.LENGTH_LONG).show();

            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Customer_registration.class));

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                log_id = ja1.getJSONObject(0).getString("login_id");
                user_type = ja1.getJSONObject(0).getString("user_type");

                SharedPreferences.Editor e = sh.edit();
                e.putString("log_id", log_id);
                e.commit();
                if (user_type.equals("customer")) {
                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(),LocationService.class));
                    startActivity(new Intent(getApplicationContext(), Customer_home.class));
                }

//                else if (user_type.equals("service")) {
//                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Service_home.class));
//                }
//
//                else if (user_type.equals("officer")) {
//                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Police_home.class));
//                }
//                else if (user_type.equals("pending")) {
//                    Toast.makeText(getApplicationContext(), "Request is not accepted please wait", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                }
//                else if (user_type.equals("rejected")) {
//                    Toast.makeText(getApplicationContext(), "Your login is rejected", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                }

            } else {
                Toast.makeText(getApplicationContext(), "Login failed invalid username and password", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Ipsettings.class);
        startActivity(b);
    }

    }

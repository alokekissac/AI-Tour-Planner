package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Customer_view_marked_location extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    String[] mplace ,latitude,longitude,mdetails,mlocation_id,value ;
    public static String package_ids,lts, lgs,mlocation_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_marked_location);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView)findViewById(R.id.lvview);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_view_marked_location.this;
//        String q = "/patient_view_medicines?bid="+PatientViewBookings.book_id;
        String q = "/Customer_view_marked_location?log_id=" + sh.getString("log_id", "");
        q = q.replace(" ", "%20");
        JR.execute(q);

    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("Customer_view_marked_location")) {
                String status = jo.getString("status");
//                Toast.makeText(getApplicationContext(), "username : " + 111111 + "\npassword : " + date, Toast.LENGTH_LONG).show();

                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                    mlocation_id = new String[ja1.length()];
//                    mplace = new String[ja1.length()];
                    mdetails = new String[ja1.length()];
                    latitude = new String[ja1.length()];
                    longitude = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        mlocation_id[i] = ja1.getJSONObject(i).getString("mlocation_id");
//                        mplace[i] = ja1.getJSONObject(i).getString("mplace");
                        mdetails[i] = ja1.getJSONObject(i).getString("mdetails");
                        latitude[i] = ja1.getJSONObject(i).getString("latitude");
                        longitude[i] = ja1.getJSONObject(i).getString("longitude");
                        value[i] = "Marked Place :: " + mplace[i] + " \nDetails :: " + mdetails[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
//                    View_request a=new View_request(this,fisrtname,lastname,phone,email,details);
//                    l1.setAdapter(a);
                }
            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @Override

    public void onBackPressed()
    {

        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Customer_home.class);
        startActivity(b);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        mlocation_ids=mlocation_id[position];
        lts = latitude[position];
        lgs = longitude[position];
        String url = "http://www.google.com/maps?saddr="+LocationService.lati+""+","+LocationService.logi+""+"&&daddr="+Customer_view_marked_location.lts+","+Customer_view_marked_location.lgs;
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(in);
    }
}
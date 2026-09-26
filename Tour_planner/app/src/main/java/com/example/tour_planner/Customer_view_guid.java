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

public class Customer_view_guid extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    String quantity, date, totamount, pack_id;
    String[] place_name, g_fname, g_lname, value, g_phone;
    public static String pns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_guid);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1 = (ListView) findViewById(R.id.lvview);
        l1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_view_guid.this;
        String q = "/Customer_view_guid?log_id=" + sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("Customer_view_guid")) {
                String status = jo.getString("status");
//                Toast.makeText(getApplicationContext(), "username : " + 111111 + "\npassword : " + date, Toast.LENGTH_LONG).show();

                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    place_name = new String[ja1.length()];
                    g_fname = new String[ja1.length()];
                    g_lname = new String[ja1.length()];
                    g_phone = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        place_name[i] = ja1.getJSONObject(i).getString("place_name");
                        g_fname[i] = ja1.getJSONObject(i).getString("g_fname");
                        g_lname[i] = ja1.getJSONObject(i).getString("g_lname");
                        g_phone[i] = ja1.getJSONObject(i).getString("g_phone");
                        value[i] = "Place Name: " + place_name[i] + "\nName:" + g_fname[i] + " " + g_lname[i] + "\nPhone:" + g_phone[i];

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

    public void onBackPressed() {

        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b = new Intent(getApplicationContext(), Customer_home.class);
        startActivity(b);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pns=g_phone[position];

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+pns));//change the number
        startActivity(callIntent);
    }
}
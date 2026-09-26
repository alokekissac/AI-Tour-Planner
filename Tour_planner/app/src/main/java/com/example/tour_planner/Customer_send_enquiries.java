package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Customer_send_enquiries extends AppCompatActivity implements JsonResponse {
    EditText e1;
    Button b1;
    String enq;
    String[] enquiries,reply,date,value;
    SharedPreferences sh;
    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_send_enquiries);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.etenq);
        b1=(Button)findViewById(R.id.button);
        l1=(ListView)findViewById(R.id.lvview);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_send_enquiries.this;
        String q = "/view_enquiries?log_id=" + sh.getString("log_id", "")+"&pro_id="+Customer_view_providers.pro_id;
        q = q.replace(" ", "%20");
        JR.execute(q);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enq=e1.getText().toString();
                if(enq.equalsIgnoreCase(""))
                {
                    e1.setError("Enter Any Enquiries");
                    e1.setFocusable(true);
                }
                else
                {
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) Customer_send_enquiries.this;
                    String q = "/Customer_send_enquiries?&log_id=" + sh.getString("log_id", "")+"&enquiries="+enq+"&pro_id="+Customer_view_providers.pro_id;
                    q=q.replace(" ","%20");
                    JR.execute(q);
                }


            }
        });


    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);


            if (method.equalsIgnoreCase("view_enquiries")) {

                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja = (JSONArray) jo.getJSONArray("data");

//                    cid = new String[ja.length()];

                    enquiries = new String[ja.length()];
                    reply = new String[ja.length()];
                    date = new String[ja.length()];
                    value = new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {
//                        cid[i] = ja.getJSONObject(i).getString("complaint_id");
                        enquiries[i] = ja.getJSONObject(i).getString("enquiry_details");
                        reply[i] = ja.getJSONObject(i).getString("reply_details");
                        date[i] = ja.getJSONObject(i).getString("datetime");
                        value[i] = "\nEnquiries : " + enquiries[i] + "\nReply : " + reply[i] + "\nDate :" + date[i];
                    }


                    l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
                    {

                    }
                }
            }

            if (method.equalsIgnoreCase("Customer_send_enquiries")) {
                try {
                    String status = jo.getString("status");
                    Log.d("pearl", status);


                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), " Sucessfully send Enquiries", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Customer_send_enquiries.class));

                    }
                    else {

                        Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
}
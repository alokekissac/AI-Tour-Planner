package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Customer_view_providers extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    String[] provider_id,provider_name,provider_place,district,pincode,phone,email,value ;
    public static String pro_id,lts, lgs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_providers);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView)findViewById(R.id.lvview);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_view_providers.this;
//        String q = "/patient_view_medicines?bid="+PatientViewBookings.book_id;
//        String q = "/Service_view_request?log_id=" + sh.getString("log_id", "");
        String q = "/Customer_view_providers";
        q = q.replace(" ", "%20");
        JR.execute(q);


    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("Customer_view_providers")) {
                String status = jo.getString("status");
//                Toast.makeText(getApplicationContext(), "username : " + 111111 + "\npassword : " + date, Toast.LENGTH_LONG).show();

                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    provider_id = new String[ja1.length()];
                    provider_name = new String[ja1.length()];
                    provider_place = new String[ja1.length()];
                    district = new String[ja1.length()];
                    pincode = new String[ja1.length()];
                    phone = new String[ja1.length()];
                    email = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        provider_id[i] = ja1.getJSONObject(i).getString("provider_id");
                        provider_name[i] = ja1.getJSONObject(i).getString("provider_name");
                        provider_place[i] = ja1.getJSONObject(i).getString("provider_place");
                        district[i] = ja1.getJSONObject(i).getString("district");
                        phone[i] = ja1.getJSONObject(i).getString("phone");
                        email[i] = ja1.getJSONObject(i).getString("email");
                        pincode[i] = ja1.getJSONObject(i).getString("pincode");
//                        longitude[i] = ja1.getJSONObject(i).getString("longitude");


                        value[i] = "Provider: " + provider_name[i] + "\nProvider_place:" + provider_place[i]+ "\nDistrict:" + district[i]+ "\nPhone:" + phone[i]+ "\nEmail:" + email[i]+ "\nPincode:" + pincode[i];
//


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
// TODO Auto-generated method stub
        pro_id=provider_id[position];
//        lts=lati[arg2];
//        lgs=longi[arg2];
//        place_ids=place_id[arg2];

        final CharSequence[] items = {"Tour Providers","Back"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_view_providers.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {


//                if (items[item].equals("View Map"))
//                {
//                    String url = "http://www.google.com/maps?saddr="+LocationService.lati+""+","+LocationService.logi+""+"&&daddr="+User_add_interests.lts+","+User_add_interests.lgs;
//                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(in);
//                }
//
//                if (items[item].equals("Add Interest"))
//                {
//                    JsonReq JR=new JsonReq();
//                    JR.json_response=(JsonResponse) User_add_interests.this;
//                    String q = "/user_add_interest?loginid="+Login.logid+"&type_id="+User_add_interests.type_ids;
//                    q=q.replace(" ","%20");
//                    JR.execute(q);
//                }
                if (items[item].equals("Tour Providers"))
                {
//					JsonReq JR=new JsonReq();
//					JR.json_response=(JsonResponse) User_add_interests.this;
//					String q = "/user_add_interest?loginid="+Login.logid+"&type_id="+User_add_interests.type_ids;
//					q=q.replace(" ","%20");
//					JR.execute(q);
                    startActivity(new Intent(getApplicationContext(),Customer_send_enquiries.class));

                }

                else if (items[item].equals("Back")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }
}
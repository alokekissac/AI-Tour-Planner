package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Customer_view_favorite extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    String[] favorite_id ,package_id,package_name,places,lati,longi,value ;
    public static String package_ids,lts, lgs,favorite_ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_favorite);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView)findViewById(R.id.lvview);
        l1.setOnItemClickListener(this);
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_view_favorite.this;
//        String q = "/patient_view_medicines?bid="+PatientViewBookings.book_id;
        String q = "/Customer_view_favorite?log_id=" + sh.getString("log_id", "")+"&pac_id="+Customer_add_to_favorite.package_ids;
//        String q = "/Customer_view_providers";
        q = q.replace(" ", "%20");
        JR.execute(q);


    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("Customer_view_favorite")) {
                String status = jo.getString("status");
//                Toast.makeText(getApplicationContext(), "username : " + 111111 + "\npassword : " + date, Toast.LENGTH_LONG).show();

                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                    favorite_id = new String[ja1.length()];
//                    package_id = new String[ja1.length()];
                    places = new String[ja1.length()];
                    package_name = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        favorite_id[i] = ja1.getJSONObject(i).getString("package_id");
//                        package_id[i] = ja1.getJSONObject(i).getString("package_id");
                        package_name[i] = ja1.getJSONObject(i).getString("package_name");
                        places[i] = ja1.getJSONObject(i).getString("places");
//                        no_of_days[i] = ja1.getJSONObject(i).getString("no_of_days");
//                        no_of_nights[i] = ja1.getJSONObject(i).getString("no_of_nights");
//                        no_of_adults[i] = ja1.getJSONObject(i).getString("no_of_adults");
//                        no_of_children[i] = ja1.getJSONObject(i).getString("no_of_children");
//                        category_name[i] = ja1.getJSONObject(i).getString("category_name");
//                        lati[i] = ja1.getJSONObject(i).getString("latitude");
//                        longi[i] = ja1.getJSONObject(i).getString("longitude");



//                        longitude[i] = ja1.getJSONObject(i).getString("longitude");


                        value[i] = "package Name :: " + package_name[i] + " \nPlaces :: " + places[i];
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
        favorite_ids=favorite_id[position];
        final CharSequence[] items = {"Delete","Back"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_view_favorite.this);
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
                if (items[item].equals("Delete"))
                {
					JsonReq JR=new JsonReq();
					JR.json_response=(JsonResponse) Customer_view_favorite.this;
					String q = "/fev_delete?favorite_ids="+favorite_ids;
					q=q.replace(" ","%20");
					JR.execute(q);
                    startActivity(new Intent(getApplicationContext(),Customer_add_to_favorite.class));

                }

                else if (items[item].equals("Back")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();

    }
}
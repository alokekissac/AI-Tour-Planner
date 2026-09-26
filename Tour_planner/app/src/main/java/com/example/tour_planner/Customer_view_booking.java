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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Customer_view_booking extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    String quantity, date, totamount, pack_id;
    String[] quantitys, bookdate, tourdate, value, totalamount, statuss, pack_ids, booking_id;
    public static String pac_id, amounts, dates, statusss, book_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_booking);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1 = (ListView) findViewById(R.id.lvview);
        l1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_view_booking.this;
//        String q = "/patient_view_medicines?bid="+PatientViewBookings.book_id;
//        String q = "/Service_view_request?log_id=" + sh.getString("log_id", "");
        String q = "/Customer_view_booking?log_id=" + sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);

    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("Customer_view_booking")) {
                String status = jo.getString("status");
//                Toast.makeText(getApplicationContext(), "username : " + 111111 + "\npassword : " + date, Toast.LENGTH_LONG).show();

                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    pack_ids = new String[ja1.length()];
                    booking_id = new String[ja1.length()];
                    totalamount = new String[ja1.length()];
                    statuss = new String[ja1.length()];
                    bookdate = new String[ja1.length()];
                    quantitys = new String[ja1.length()];
                    tourdate = new String[ja1.length()];
//                    email = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        pack_ids[i] = ja1.getJSONObject(i).getString("package_id");
                        booking_id[i] = ja1.getJSONObject(i).getString("booking_id");
                        totalamount[i] = ja1.getJSONObject(i).getString("total_amount");
                        statuss[i] = ja1.getJSONObject(i).getString("booking_status");
                        quantitys[i] = ja1.getJSONObject(i).getString("quantity");
                        bookdate[i] = ja1.getJSONObject(i).getString("booked_date");
                        tourdate[i] = ja1.getJSONObject(i).getString("tour_date");
//                        pincode[i] = ja1.getJSONObject(i).getString("pincode");
//                        longitude[i] = ja1.getJSONObject(i).getString("longitude");


                        value[i] = "Total Amount: " + totalamount[i] + "\nQuantitys:" + quantitys[i] + "\nBookdate:" + bookdate[i] + "\ntourdate:" + tourdate[i] + "\nstatuss:" + statuss[i];
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

    public void onBackPressed() {

        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b = new Intent(getApplicationContext(), Customer_home.class);
        startActivity(b);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        u_id=user_id[i];
//        p_id = patient_id[i];
        pac_id = pack_ids[position];
        book_id = booking_id[position];
        amounts = totalamount[position];
//        dates = date[position];
        statusss = statuss[position];

        if (statusss.equals("confirmed")) {


            final CharSequence[] items = {"Make Payment", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_view_booking.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {


//                    if (items[item].equals("View Map")) {
//                        String url = "http://www.google.com/maps?saddr=" + LocationService.lati + "" + "," + LocationService.logi + "" + "&&daddr=" + User_view_request.lts + "," + User_view_request.lgs;
//                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(in);
//                    }

                    if (items[item].equals("Make Payment")) {
                        startActivity(new Intent(getApplicationContext(), Customer_make_payment.class));
//                    JsonReq JR=new JsonReq();
//                    JR.json_response=(JsonResponse) User_view_request.this;
//                    String q = "/User_make_payment?log_id="+log_id;
//                    q=q.replace(" ","%20");
//                    JR.execute(q);
                    }
//                    if (items[item].equals("Rating")) {
////					JsonReq JR=new JsonReq();
////					JR.json_response=(JsonResponse) User_add_interests.this;
////					String q = "/user_add_interest?loginid="+Login.logid+"&type_id="+User_add_interests.type_ids;
////					q=q.replace(" ","%20");
////					JR.execute(q);
//                        startActivity(new Intent(getApplicationContext(), Review.class));
//
//                    }
                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }


            });
            builder.show();
        }
        else  if (statusss.equalsIgnoreCase("paid")) {
            final CharSequence[] items = {"View Guid","Rating", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_view_booking.this);
            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {


//                    if (items[item].equals("View Map")) {
//                        String url = "http://www.google.com/maps?saddr=" + LocationService.lati + "" + "," + LocationService.logi + "" + "&&daddr=" + User_view_request.lts + "," + User_view_request.lgs;
//                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(in);
//                    }

                    if (items[item].equals("View Guid")) {
                        startActivity(new Intent(getApplicationContext(), Customer_view_guid.class));
                    }
                    if (items[item].equals("Rating")) {
                        startActivity(new Intent(getApplicationContext(), Customer_review.class));
//                    JsonReq JR=new JsonReq();
//                    JR.json_response=(JsonResponse) User_view_request.this;
//                    String q = "/User_make_payment?log_id="+log_id;
//                    q=q.replace(" ","%20");
//                    JR.execute(q);
                    }
//                    if (items[item].equals("Rating")) {
////					JsonReq JR=new JsonReq();
////					JR.json_response=(JsonResponse) User_add_interests.this;
////					String q = "/user_add_interest?loginid="+Login.logid+"&type_id="+User_add_interests.type_ids;
////					q=q.replace(" ","%20");
////					JR.execute(q);
//                        startActivity(new Intent(getApplicationContext(), Review.class));
//
//                    }
                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }


            });
            builder.show();

        }
    }
}
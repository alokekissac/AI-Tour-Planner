package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class Customer_booking_tour extends AppCompatActivity implements JsonResponse{
    EditText e1, e2, e3, e99;
    Button bt1;
    ListView l1;
    SharedPreferences sh;
    String quantity, date, totamount, pack_id;
    String[] quantitys, bookdate, tourdate, value, totalamount, statuss;
    DatePickerDialog datePickerDialog;
//    public static String ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking_tour);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1 = (EditText) findViewById(R.id.etquan);
        e2 = (EditText) findViewById(R.id.ettrdate);
        e3 = (EditText) findViewById(R.id.etamount);

        l1 = (ListView) findViewById(R.id.lvview);
        bt1 = (Button) findViewById(R.id.btreg);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Customer_booking_tour.this;
        String q = "/view_booking?log_id=" + sh.getString("log_id", "");
        q = q.replace(" ", "%20");
        JR.execute(q);
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Customer_booking_tour.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                e2.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        EditText e99 = findViewById(R.id.etbamount);
        e99.setText("basic amount: " + Customer_add_to_favorite.amounts);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                e1.setText("0");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(e1.getText().toString().equalsIgnoreCase("")){
                    e1.setText("0");
                    quantity="0";
                }
                else{
                    quantity=e1.getText().toString();
                    Integer qty = Integer.parseInt(Customer_add_to_favorite.amounts) * Integer.parseInt(quantity);
                    e3.setText(String.valueOf(qty));
                }

//               e1.setText(0);
//
//
//
//                if (Customer_add_to_favorite.amounts.equalsIgnoreCase("") && quantity.equalsIgnoreCase("0")){
//                    e3.setText("0");
//                    e1.setText("0");
//
//                }else{
//
//
//                    Integer qty = Integer.parseInt(Customer_add_to_favorite.amounts) * Integer.parseInt(quantity);
//                    if(qty.equals("")){
//                        e3.setText("");
//                    }else{
//
//                    String qtyInt = String.valueOf(qty);
//                    e3.setText(qtyInt);
//                    }
//
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(e1.getText().toString().equalsIgnoreCase("")){
                    e1.setText(0);
                    quantity="0";


                }
                else{
                    quantity=e1.getText().toString();
                    Integer qty = Integer.parseInt(Customer_add_to_favorite.amounts) * Integer.parseInt(quantity);
                    e3.setText(String.valueOf(qty));
                }


            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = e1.getText().toString();
                date = e2.getText().toString();
                totamount = e3.getText().toString();

                if (quantity.equalsIgnoreCase("")) {
                    e1.setError("please enter Quantity");
                    e1.setFocusable(true);
                } else if (date.equalsIgnoreCase("")) {
                    e2.setError("please enter tour date");
                    e2.setFocusable(true);
                }
                if (totamount.equalsIgnoreCase("")) {
                    e3.setError("please enter Quantity");
                    e3.setFocusable(true);
                } else {
                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Customer_booking_tour.this;
//                    String q = "/userregister?fname="+fname+"&lname="+lname+"&phone="+phone+"&email="+email+"&dob="+dob+"&hname="+hname+"&gender="+gender+"&place="+place+"&uname="+uname+"&pass="+pass+"&lati="+LocationService.lati+"&logi="+LocationService.logi;
                    String q = "/Customer_booking_tour?quantity=" + quantity + "&date=" + date + "&totamount=" + totamount + "&package_ids=" + Customer_add_to_favorite.package_ids + "&log_id=" + Login.log_id;
                    q = q.replace(" ", "%20");
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


            if (method.equalsIgnoreCase("view_booking")) {

                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja = (JSONArray) jo.getJSONArray("data");

//                    cid = new String[ja.length()];

                    quantitys = new String[ja.length()];
                    bookdate = new String[ja.length()];
                    tourdate = new String[ja.length()];
                    totalamount = new String[ja.length()];
                    statuss = new String[ja.length()];
                    value = new String[ja.length()];


                    for (int i = 0; i < ja.length(); i++) {
//                        cid[i] = ja.getJSONObject(i).getString("complaint_id");
                        quantitys[i] = ja.getJSONObject(i).getString("quantity");
                        bookdate[i] = ja.getJSONObject(i).getString("booked_date");
                        tourdate[i] = ja.getJSONObject(i).getString("tour_date");
                        totalamount[i] = ja.getJSONObject(i).getString("total_amount");
                        statuss[i] = ja.getJSONObject(i).getString("booking_status");


                        value[i] = "\nQuantity : " + quantitys[i] + "\nBook Date : " + bookdate[i] + "\nTour date :" + tourdate[i] + "\nStatus :" + statuss[i];
                    }


                    l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
                    {

                    }
                }
            }

            if (method.equalsIgnoreCase("Customer_booking_tour")) {
                try {
                    String status = jo.getString("status");
                    Log.d("pearl", status);


                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), " Booking Sucess ", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Customer_booking_tour.class));

                    } else {

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

    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b = new Intent(getApplicationContext(), Customer_home.class);
        startActivity(b);
    }

}
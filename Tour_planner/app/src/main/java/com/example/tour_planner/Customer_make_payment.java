package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Customer_make_payment extends AppCompatActivity implements JsonResponse {
    EditText e1,e2,e3,e4,e5;
    Button b1;
    SharedPreferences sh;
    String amounts,card,name,cvv,exp;
    public static String log_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_make_payment);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        log_id=sh.getString("log_id","");

        e1=(EditText) findViewById(R.id.amt);
       e2=(EditText) findViewById(R.id.etno);
        e3=(EditText) findViewById(R.id.cvv);
        e4=(EditText) findViewById(R.id.name);
        e5=(EditText) findViewById(R.id.exp);
        b1=(Button) findViewById(R.id.btn2);

        e1.setText(Customer_view_booking.amounts);
        e1.setEnabled(false);
//        e2.setText(User_view_request.dates);
//        e2.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amounts=e1.getText().toString();
                card=e2.getText().toString();
                cvv=e3.getText().toString();
                name=e4.getText().toString();
                exp=e5.getText().toString();
                if(amounts.equalsIgnoreCase("")){
                    e1.setError("Amount Required");
                    e1.setFocusable(true);
                }
                else if(card.length()!=16)
                {
                    e2.setError("card number must 16 digits");
                    e2.setFocusable(true);
                }
                else if(cvv.length() !=3)
                {
                    e3.setError("card number must 3 digits");
                    e3.setFocusable(true);
                }
                else if(name.equalsIgnoreCase(""))
                {
                    e4.setError("please enter your  name");
                    e4.setFocusable(true);
                }
                else if(exp.equalsIgnoreCase(""))
                {
                    e5.setError("12/29");
                    e5.setFocusable(true);
                }
                else {

//                dates=e2.getText().toString();
                    JsonReq jr = new JsonReq();
                    jr.json_response = (JsonResponse) Customer_make_payment.this;
                    String q = "/payment?&amounts=" + Customer_view_booking.amounts + "&log_id=" + log_id + "&booking_id=" + Customer_view_booking.book_id;

                    q.replace("", "%20");
                    jr.execute(q);
                }

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status=jo.getString("status");
            Log.d("pearl",status);


            if(status.equalsIgnoreCase("success")){


                Toast.makeText(getApplicationContext(), "PAYMENT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Customer_home.class));

            }
//            else if(status.equalsIgnoreCase("duplicate"))
//            {
//                startActivity(new Intent(getApplicationContext(),User_home.class));
//                Toast.makeText(getApplicationContext(), " already Buy...", Toast.LENGTH_LONG).show();
//            }
            else
            {
                startActivity(new Intent(getApplicationContext(),Customer_make_payment.class));
                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Customer_home.class);
        startActivity(b);

    }
}
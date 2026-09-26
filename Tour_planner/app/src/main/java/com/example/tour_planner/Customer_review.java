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
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONObject;

public class Customer_review extends AppCompatActivity implements JsonResponse {
    RatingBar r1;
    EditText e1;
    Button b1;
    SharedPreferences sh;
    Float rated;
    String rat,review,rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_review);
        b1 = (Button) findViewById(R.id.btrating);
        r1 = (RatingBar) findViewById(R.id.rating);

        e1=(EditText)findViewById(R.id.etreview);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)Customer_review.this;
//        String q = "/viewrating?log_id=" + sh.getString("log_id","")+"&am_id="+User_view_request.am_id;
        String q = "/viewrating?log_id=" + sh.getString("log_id","")+"&package_ids="+Customer_view_booking.pac_id;

        q=q.replace(" ", "%20");
        JR.execute(q);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating =  r1.getRating();
                review=e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)Customer_review.this;
                String q = "/Review?log_id=" + sh.getString("log_id","")+"&package_ids="+Customer_view_booking.pac_id+"&rating="+rating+"&review="+review;
                JR.execute(q);
                Log.d("pearl",q);

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method=jo.getString("method");
            if(method.equalsIgnoreCase("viewrating"))
            {
                try{
                    Toast.makeText(getApplicationContext(),"rate here", Toast.LENGTH_SHORT).show();
                    String status=jo.getString("status");
                    Log.d("result", status);

                    if(status.equalsIgnoreCase("success")){

                        rat=jo.getString("data");
                        rated=Float.parseFloat(rat);
                        e1.setText(jo.getString("data1"));
                        Toast.makeText(getApplicationContext(),rated+"", Toast.LENGTH_SHORT).show();
                        r1.setRating(rated);



                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }else if(method.equalsIgnoreCase("Review"))
            {
                try {
                    String status=jo.getString("status");
                    if(status.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getApplicationContext()," Added success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Customer_review.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Rating failed",Toast.LENGTH_LONG ).show();
                    }
                } catch (Exception e){
                    // TODO: handle exception
                }
            }

        }

        catch(Exception e)
        {
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
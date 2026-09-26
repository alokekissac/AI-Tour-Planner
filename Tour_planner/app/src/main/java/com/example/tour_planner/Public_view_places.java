package com.example.tour_planner;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Public_view_places extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView lv1;
    Button b1,b2;
    EditText e1;
    SharedPreferences sh;
    String searchitem;
    String [] place_id,category_id,package_id,place_name,description,image_path,lati,longi,package_name,val, amount, no_of_days,no_of_nights,no_of_adults,no_of_children,category_name;

    public static String place_ids,lts,lgs,package_ids,amounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_view_places);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lv1=(ListView)findViewById(R.id.lvhot);
        lv1.setOnItemClickListener(this);

        e1=(EditText)findViewById(R.id.searchtxt);
        b2=(Button) findViewById(R.id.searchbtn);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchitem=e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)Public_view_places.this;
//                String q="/searchplace?searchitem="+searchitem+"&log_id="+sh.getString("log_id","");
                String q="/publicsearch?searchitem="+searchitem;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }
        });

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Public_view_places.this;
        String q = "/Public_view_places";
        q=q.replace(" ","%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
// TODO Auto-generated method stub
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);


            if (method.equalsIgnoreCase("Public_view_places")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                Toast.makeText(getApplicationContext(), "normal View", Toast.LENGTH_SHORT).show();
                if (status.equalsIgnoreCase("success")) {

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                    category_id = new String[ja1.length()];
                    place_id = new String[ja1.length()];
                    package_id = new String[ja1.length()];
                    place_name = new String[ja1.length()];
//                    description = new String[ja1.length()];
                    package_name = new String[ja1.length()];
                    amount = new String[ja1.length()];
                    no_of_days = new String[ja1.length()];
                    no_of_nights = new String[ja1.length()];
//                    no_of_adults = new String[ja1.length()];
//                    no_of_children = new String[ja1.length()];
//                    category_name = new String[ja1.length()];
                    lati = new String[ja1.length()];
                    longi = new String[ja1.length()];
                    image_path=new String[ja1.length()];
                    val = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {


                        category_id[i] = ja1.getJSONObject(i).getString("category_id");
                        image_path[i]=ja1.getJSONObject(i).getString("image");
                        place_id[i] = ja1.getJSONObject(i).getString("place_id");
                        package_id[i] = ja1.getJSONObject(i).getString("package_id");
                        place_name[i] = ja1.getJSONObject(i).getString("place_name");
//                        description[i] = ja1.getJSONObject(i).getString("description");
                        package_name[i] = ja1.getJSONObject(i).getString("package_name");
                        amount[i] = ja1.getJSONObject(i).getString("amount");
                        no_of_days[i] = ja1.getJSONObject(i).getString("no_of_days");
                        no_of_nights[i] = ja1.getJSONObject(i).getString("no_of_nights");
//                        no_of_adults[i] = ja1.getJSONObject(i).getString("no_of_adults");
//                        no_of_children[i] = ja1.getJSONObject(i).getString("no_of_children");
//                        category_name[i] = ja1.getJSONObject(i).getString("category_name");
                        lati[i] = ja1.getJSONObject(i).getString("latitude");
                        longi[i] = ja1.getJSONObject(i).getString("longitude");



//                        Toast.makeText(getApplicationContext(), val[i], Toast.LENGTH_SHORT).show();
//                        val[i] = "Place Name : " + place_name[i] + " \nCategory" + category_name[i]+ "\nPackage:  " + package_name[i] + "\nDescription:  " + description[i]+ "\nNo_of_days:  " + no_of_days[i]+ "\nNo_of_nights:  " + no_of_nights[i] +"\nNo_of_adults:  " + no_of_adults[i]+ "\nNo_of_children:  " + no_of_children[i]+"\nAmount:  " + amount[i] ;


                    }
//                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, val);
//                    lv1.setAdapter(ar);
                    lv1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.customtext,val));
//                    customemployee clist=new customemployee(this,image_path,place_name,package_name,amount,no_of_days,no_of_nights,no_of_adults,no_of_children);
                    customemployee clist=new customemployee(this,image_path,place_name,package_name,amount,no_of_days,no_of_nights);
                    lv1.setAdapter(clist);


                }
            }



        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        package_ids=package_id[position];
//        lts=lati[position];
//        lgs=longi[position];
//        place_ids=place_id[position];
//        amounts=amount[position];

        final CharSequence[] items = {"View Map","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Public_view_places.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("View Map"))
                {
                    String url = "http://www.google.com/maps?saddr="+LocationService.lati+""+","+LocationService.logi+""+"&&daddr="+Customer_add_to_favorite.lts+","+Customer_add_to_favorite.lgs;
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(in);
                }



                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, GALLERY_CODE);
    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Login.class);
        startActivity(b);

    }
}
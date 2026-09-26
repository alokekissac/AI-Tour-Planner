package com.example.tour_planner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserViewChats extends Activity implements OnItemClickListener,JsonResponse {

	SharedPreferences sh;
	ListView lv1;
	String[] lid,fname,lname,val;
	String chattedwith;
	TextView t1;
	
	String ids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_view_chats);
		
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		t1=(TextView)findViewById(R.id.textView1);
		lv1=(ListView)findViewById(R.id.lvusers);
		
		lv1.setOnItemClickListener(this);
		
//		chattedwith=sh.getString("chatedwith", "");
//		if(chattedwith.equalsIgnoreCase("UWithteacher"))
//		{
//			t1.setText("View Parents");
			JsonReq JR=new JsonReq();
	        JR.json_response=(JsonResponse) UserViewChats.this;
	        String q = "/UserViewChats?login_id="+sh.getString("log_id", "");
	        q=q.replace(" ","%20");
//	        Toast.makeText(getApplicationContext(),q, Toast.LENGTH_SHORT).show();
	        JR.execute(q);	
//		}
//		else if(chattedwith.equalsIgnoreCase("UWithstudent"))
//		{
//			t1.setText("View Student");
//			JsonReq JR=new JsonReq();
//	        JR.json_response=(JsonResponse) UserViewChats.this;
//	        String q = "?action=userviewchatsofstudent&login_id="+sh.getString("logid", "");
//	        q=q.replace(" ","%20");
////	        Toast.makeText(getApplicationContext(),q, Toast.LENGTH_SHORT).show();
//	        JR.execute(q);
//		}
        
	}


	

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub

		try {
			
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("UserViewChats")){
				String status=jo.getString("status");
				Log.d("pearl",status);
				Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
				if(status.equalsIgnoreCase("success")){
			
					JSONArray ja1=(JSONArray)jo.getJSONArray("data");
					
					 lid=new String[ja1.length()];
					 fname=new String[ja1.length()];
					lname=new String[ja1.length()];
					 
					 val=new String[ja1.length()];
			     
			    
				     
					for(int i = 0;i<ja1.length();i++)
					{ 
						
						
						lid[i]=ja1.getJSONObject(i).getString("customer_id");
						fname[i]=ja1.getJSONObject(i).getString("first_name");
						lname[i]=ja1.getJSONObject(i).getString("last_name");
						
						
						
					
	//					Toast.makeText(getApplicationContext(),val[i], Toast.LENGTH_SHORT).show();
						val[i]="\nFirstname : "+fname[i]+"\nLastname : "+lname[i];
						
					
					}
					ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,val);
					lv1.setAdapter(ar);
			
		      
		       
				}
			
			else {
				Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
	
				} 
			}

//			if(method.equalsIgnoreCase("userviewchatsofstudent")){
//				String status=jo.getString("status");
//				Log.d("pearl",status);
//				Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT).show();
//				if(status.equalsIgnoreCase("success")){
//
//					JSONArray ja1=(JSONArray)jo.getJSONArray("data");
//
//					 lid=new String[ja1.length()];
//					 student=new String[ja1.length()];
//
////					 val=new String[ja1.length()];
//
//
//
//					for(int i = 0;i<ja1.length();i++)
//					{
//
//
//						lid[i]=ja1.getJSONObject(i).getString("login_id");
//						student[i]=ja1.getJSONObject(i).getString("student");
//
//
//
//
//	//					Toast.makeText(getApplicationContext(),val[i], Toast.LENGTH_SHORT).show();
//	//					val[i]="\n\nTeacher : "+teacher[i];
//
//
//					}
//					ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,student);
//					lv1.setAdapter(ar);
//
//
//
//				}
//
//			else {
//				Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
//
//				}
//			}
			
			}catch(Exception e)
			{
			// TODO: handle exception

			  Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
			}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		ids=lid[arg2];
		Toast.makeText(getApplicationContext(),ids, Toast.LENGTH_LONG).show();
		
		
//		chattedwith=sh.getString("chatedwith", "");
//		if(chattedwith.equalsIgnoreCase("UWithteacher"))
//		{
			Editor e=sh.edit();
			e.putString("receiver_id", ids);
//			e.putString("mainval", "userviewteacherchat");
//			e.putString("by", "UWithteacher");
			e.commit();
//		}
//		else if(chattedwith.equalsIgnoreCase("UWithstudent"))
//		{
//			Editor e=sh.edit();
//			e.putString("receiver_id", ids);
//			e.putString("mainval", "userviewstudentchat");
//			e.putString("by", "UWithstudent");
//			e.commit();
//		}
		final CharSequence[] items = {"Chat","cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(UserViewChats.this);
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
				if (items[item].equals("Chat"))
				{
//					JsonReq JR=new JsonReq();
//					JR.json_response=(JsonResponse) User_add_interests.this;
//					String q = "/user_add_interest?loginid="+Login.logid+"&type_id="+User_add_interests.type_ids;
//					q=q.replace(" ","%20");
//					JR.execute(q);
					startActivity(new Intent(getApplicationContext(),ChatHere.class));

				}

				else if (items[item].equals("cancel")) {
					dialog.dismiss();
				}
			}

		});
		builder.show();
		
	}
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent b=new Intent(getApplicationContext(),Customer_home.class);
		startActivity(b);
	}


}

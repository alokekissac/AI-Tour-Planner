package com.example.tour_planner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class customemployee extends ArrayAdapter<String> {
	
	String[] image_path,place_name,package_name,amount,no_of_days,no_of_nights;

	    //for custom view photo items
	    private Activity context;       //for to get current activity context
	    SharedPreferences sh;
	    public customemployee(Activity context, String[] image_path, String[] place_name, String[] package_name, String[] amount, String[] no_of_days,String[] no_of_nights) {
	        //constructor of this class to get the values from main_activity_class

	        super(context, R.layout.custom_employee, image_path);
	        this.context = context;
	        this.image_path = image_path;
	        this.place_name= place_name;
	        this.package_name= package_name;
	        this.amount= amount;
	        this.no_of_days=no_of_days;
			this.no_of_nights=no_of_nights;
//			this.no_of_adults=no_of_adults;
//			this.no_of_children=no_of_children;
	       
	    }
	    public View getView(int position, View convertView, ViewGroup parent) {
	                 //override getView() method

	        LayoutInflater inflater = context.getLayoutInflater();
	        View listViewItem = inflater.inflate(R.layout.custom_employee, null, true);
			//cust_list_view is xml file of layout created in step no.2

//			image_path,place_name,package_name,amount,no_of_days,no_of_nights,no_of_adults,no_of_children

	        TextView t1= (TextView) listViewItem.findViewById(R.id.pakcage);
	        TextView t2= (TextView) listViewItem.findViewById(R.id.place);
	        TextView t3= (TextView) listViewItem.findViewById(R.id.amount);
	        TextView t4=(TextView)listViewItem.findViewById(R.id.days);
			TextView t5= (TextView) listViewItem.findViewById(R.id.night);
//			TextView t6= (TextView) listViewItem.findViewById(R.id.day);
//			TextView t7= (TextView) listViewItem.findViewById(R.id.adult);
//			TextView t8=(TextView)listViewItem.findViewById(R.id.phone);
	     
	
	        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);


			t2.setText(place_name[position]);
			t1.setText(package_name[position]);
			t3.setText(amount[position]);
			t4.setText(no_of_days[position]);
			t5.setText(no_of_nights[position]);
//			t6.setText(no_of_adults[position]);
//			t7.setText(no_of_children[position]);
//			t8.setText(phone[position]);
//	        Toast.makeText(getContext(), validity[position], Toast.LENGTH_LONG).show();
	        
	        
	        sh=PreferenceManager.getDefaultSharedPreferences(getContext());
	        
	       String pth = Ipsettings.base(sh.getString("ip", ""))+"/"+image_path[position];
	       pth = pth.replace("~", "");
	        
	        Log.d("-------------", pth);
	        Picasso.with(context)
	                .load(pth)
	                .placeholder(R.drawable.ic_launcher_background)
	                .error(R.drawable.ic_launcher_background).into(im);
	        
	        return  listViewItem;
	    }

		private TextView setText(String string) {
			// TODO Auto-generated method stub
			return null;
		}
}

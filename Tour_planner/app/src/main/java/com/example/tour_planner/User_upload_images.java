package com.example.tour_planner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class User_upload_images extends Activity implements JsonResponse {
	
//	 String rid;
	Button b2;
	EditText e1,e2 , e10;
	ImageButton imgbtn;
	RadioButton r1,r2;
	String texttran;
	TextView t1;

	TextToSpeech tts;
	private Spinner languageSpinner;
	private Map<String, String> languageMap;
	private String selectedLanguageCode;

	public static String labels,pre,des,yt,selectedImagePath;


	String fln, ftype = "", fname, upLoadServerUri;

	public static byte[] byteArray;

	File f = null;
	
	Button btupim;

	private String imagename = "";
	public static Bitmap image;

	final int CAMERA_PIC_REQUEST = 0, GALLERY_CODE = 201;
	public static String encodedImage = "", path = "";
	private Uri mImageCaptureUri;
//	byte[] byteArray = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_upload_images);


		if (tts == null) {
			tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
				@Override
				public void onInit(int status) {
					if (status == TextToSpeech.SUCCESS) {
						int result = tts.setLanguage(Locale.US);
						if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
							Log.e("TTS", "This Language is not supported");
						}
					} else {
						Log.e("TTS", "Initilization Failed!");
					}
				}
			});
		}
		
		try {
		    if(Build.VERSION.SDK_INT>9){
		        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		        StrictMode.setThreadPolicy(policy);
		    }
		} catch (Exception e) { }



		btupim=(Button)findViewById(R.id.btupim);
		t1=(TextView) findViewById(R.id.textView6);
		languageSpinner = findViewById(R.id.languageSpinner);
//        e2=(EditText)findViewById(R.id.editTextTextPersonName2);
		b2=(Button)findViewById(R.id.button2);
		e10 = (EditText) findViewById(R.id.editTextTextPersonName);
		imgbtn=(ImageButton)findViewById(R.id.ibclick);
		// Create a map of languages and their codes
		languageMap = new HashMap<>();
		languageMap.put("English", "en");
		languageMap.put("Malayalam", "ml");
		languageMap.put("Hindi", "hi");
		languageMap.put("Spanish", "es");
		languageMap.put("French", "fr");
		languageMap.put("Arabic", "ar");
		languageMap.put("German", "de");
		languageMap.put("Russian", "ru");
		languageMap.put("Chinese (Simplified)", "zh-cn");
		languageMap.put("Vietnamese", "vi");
		languageMap.put("Dutch", "nl");
		// Add more languages as needed

		// Create a list of language names
		List<String> languageList = new ArrayList<>(languageMap.keySet());

		// Create an ArrayAdapter using the language list
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, languageList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set the adapter for the Spinner
		languageSpinner.setAdapter(adapter);


		languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedLanguageCode = languageMap.get(languageList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing

			}
		});

		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				texttran = e10.getText().toString();
//                language = e2.getText().toString();
//                spinner_lang=languageSpinner.getText().toString();
				JsonReq JR = new JsonReq();
				JR.json_response = (JsonResponse) User_upload_images.this;
				String q ="/User_upload_images?texttran=" + texttran + "&language=" + selectedLanguageCode;
				q = q.replace(" ", "%20");
				JR.execute(q);
			}
		});





//		JsonReq JR = new JsonReq();
//		JR.json_response = (JsonResponse) Student_upload_anwer.this;
//		String q = "/viewimg";
//		q = q.replace(" ", "%20");
//		JR.execute(q);



		imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectImageOption();
				//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//startActivityForResult(i, GALLERY_CODE);
			}
		});
	
		btupim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 sendAttach();
			}
		});
		

	}
	
	
	
	 private void sendAttach() {
	        // TODO Auto-generated method stub

	        try {
	        	 String q = Ipsettings.base()+"/api/User_upload_images";
	            Map<String, byte[]> aa = new HashMap<String, byte[]>();
	            aa.put("image1",byteArray);
	            aa.put("ftype", ftype.getBytes());
				System.out.println("----------------------------------------");
	           // Log.d(q,"");
				Log.d("pear_q", q);
	            FileUploadAsync fua = new FileUploadAsync(q);
	            fua.json_response = (JsonResponse) User_upload_images.this;
	            fua.execute(aa);
	        } catch (Exception e) {
	            Toast.makeText(getApplicationContext(), "Exception upload : " + e, Toast.LENGTH_SHORT).show();
	        }
	    }


	private void selectImageOption() {

		/*Android 10+ gallery code
        android:requestLegacyExternalStorage="true"*/

		final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(User_upload_images.this);
		builder.setTitle("Take Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (items[item].equals("Capture Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					//intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, CAMERA_PIC_REQUEST);

				} else if (items[item].equals("Choose from Gallery")) {
					Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, GALLERY_CODE);

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

			mImageCaptureUri = data.getData();
			System.out.println("Gallery Image URI : " + mImageCaptureUri);
			//   CropingIMG();

			Uri uri = data.getData();
			Log.d("File Uri", "File Uri: " + uri.toString());
			// Get the path
			//String path = null;
			try {
				path = FileUtils.getPath(this, uri);
				Toast.makeText(getApplicationContext(), "path : " + path, Toast.LENGTH_LONG).show();

				File fl = new File(path);
				int ln = (int) fl.length();

				InputStream inputStream = new FileInputStream(fl);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] b = new byte[ln];
				int bytesRead = 0;

				while ((bytesRead = inputStream.read(b)) != -1) {
					bos.write(b, 0, bytesRead);
				}
				inputStream.close();
				byteArray = bos.toByteArray();

				Bitmap bit = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				imgbtn.setImageBitmap(bit);

				String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
				encodedImage = str;
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

			try {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				imgbtn.setImageBitmap(thumbnail);
				byteArray = baos.toByteArray();

				String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
				encodedImage = str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

		@Override
		public void response(JSONObject jo) {
				
			 try {
				 String method = jo.getString("method");
				 Log.d("pearl", method);
				 if (method.equalsIgnoreCase("Student_upload_anwer")) {

					 String status = jo.getString("status");
					 String output = jo.getString("val");
					 Toast.makeText(getApplicationContext(),output+"", Toast.LENGTH_SHORT).show();
					 e10.setText(output);
//					 String output = jo.getString("val");
//					 e10.setText(jo.getString("val"));
//					 Toast.makeText(getApplicationContext(), "Pedicted Out" + output, Toast.LENGTH_LONG).show();
					 Log.d("result", status);
				 }

				 if (method.equalsIgnoreCase("fileouttrans")) {
					 String status = jo.getString("status");
					 Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					 if (status.equalsIgnoreCase("success")) {
						 String myout = jo.getString("out");
						 Toast.makeText(getApplicationContext(),myout+"", Toast.LENGTH_SHORT).show();
						 e10.setText(myout);
						 tts.speak(myout, TextToSpeech.QUEUE_FLUSH, null);
//                Toast.makeText(getApplicationContext(),tts+"", Toast.LENGTH_SHORT).show();

//					 } else {
////					Toast.makeText(getApplicationContext(),"Interested Place Added Failed", Toast.LENGTH_LONG).show();
//						 Toast.makeText(getApplicationContext(), "Already Added....",
//								 Toast.LENGTH_LONG).show();
					 }
				 }


		        } catch (Exception e) {
		            e.printStackTrace();
		            Toast.makeText(getApplicationContext(),"Response Exc : " + e.toString(), Toast.LENGTH_LONG).show();
		        }
		}
		
		public void onBackPressed() 
		{
			// TODO Auto-generated method stub
			super.onBackPressed();
			Intent b=new Intent(getApplicationContext(),Customer_home.class);
			startActivity(b);
		}
		
	    // UPDATED!
	    public String getPaths(Uri uri) {
	        String[] projection = { MediaStore.Video.Media.DATA };
	        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	        if (cursor != null) {
	            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	            int column_index = cursor
	                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
	            cursor.moveToFirst();
	            return cursor.getString(column_index);
	        } else
	            return null;
	    }
	@Override
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();


	}
	}

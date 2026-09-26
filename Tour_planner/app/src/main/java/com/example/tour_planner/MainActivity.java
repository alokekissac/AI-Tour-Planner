package com.example.tour_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements JsonResponse  {
    Button b1,b2;
    EditText e1,e2;
    Spinner spinner_lang;
    String texttran,language;
    String [] myout;
    TextToSpeech tts;
    private Spinner languageSpinner;
    private Map<String, String> languageMap;
    private String selectedLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        languageSpinner = findViewById(R.id.languageSpinner);
        e1=(EditText)findViewById(R.id.editTextTextPersonName);
//        e2=(EditText)findViewById(R.id.editTextTextPersonName2);
        b1=(Button)findViewById(R.id.button2);
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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texttran = e1.getText().toString();
//                language = e2.getText().toString();
//                spinner_lang=languageSpinner.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) MainActivity.this;
                String q ="/trasilation?texttran=" + texttran + "&language=" + selectedLanguageCode;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                String myout = jo.getString("out");
                Toast.makeText(getApplicationContext(),myout+"", Toast.LENGTH_SHORT).show();
                e1.setText(myout);
                tts.speak(myout, TextToSpeech.QUEUE_FLUSH, null);
//                Toast.makeText(getApplicationContext(),tts+"", Toast.LENGTH_SHORT).show();

            }

        }

        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
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
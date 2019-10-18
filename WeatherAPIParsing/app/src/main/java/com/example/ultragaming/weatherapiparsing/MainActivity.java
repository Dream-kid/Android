package com.example.ultragaming.weatherapiparsing;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //private static String API = "http://api.apixu.com/v1/current.json?key=b349e54ea3394aa1b9f90815182911&q=Paris";
    private ProgressDialog pDialog;
    TextView areaName,country,temp;
    Button bt;
    EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        areaName=findViewById(R.id.Name);
        country=findViewById(R.id.Country);
        temp=findViewById(R.id.temparature);
        et1=findViewById(R.id.get_location);
        bt=findViewById(R.id.button_parse_weather);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area=et1.getText().toString();
                if(area.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Wrong Input",Toast.LENGTH_LONG).show();
                }
                else{
                    String AP="http://api.apixu.com/v1/current.json?key=b349e54ea3394aa1b9f90815182911&q="+area;
                    Log.d("DEBUG AP",AP);
                    new GetWeather().execute(AP);
                }
            }
        });
    }

    private class GetWeather extends AsyncTask<String, Void, Void> {
        String name;
        String region;
        String country_;
        double temp_c;
        @Override
        protected Void doInBackground(String... strings) {
            String API=strings[0];
            Log.d("DEBUG API",API);
            HttpHandler sh = new HttpHandler();
            String jsnStr = sh.makeServiceCall(API);
            Log.e("DEBUG1", jsnStr);
            if (!jsnStr.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(jsnStr);
                    JSONObject location,current;
                    try {
                        location = (JSONObject) jsonObject.get("location");
                        name=location.getString("name");
                        if(location.has("region"))
                            region=location.getString("region");
                        country_=location.getString("country");
                    } catch (JSONException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                    try {
                        current = (JSONObject) jsonObject.get("current");
                        temp_c=current.getDouble("temp_c");
                    }
                    catch (JSONException e){
                        Log.d("ERROR",e.getMessage());
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog.isShowing())pDialog.dismiss();
            areaName.setText(name);
            country.setText(country_);
            temp.setText(temp_c+"");
        }
    }
}

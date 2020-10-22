package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content", weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("***MAIN***: ", jsonObject.getString("main"));
                    Log.i("***DESCRIPTION***", jsonPart.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=bcf6fc4d3667d5aa6fbbd1a382d479c2");
    }
}
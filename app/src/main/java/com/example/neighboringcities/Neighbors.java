package com.example.neighboringcities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Neighbors extends AppCompatActivity {
    //String[] cities = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
    ArrayList<String> cities = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbors);

        String city = "2023469 Irkutsk";
        String distance = "200";

        double lon = 0;
        double lat = 0;
        float[] result = new float[1];

        city = getIntent().getExtras().getString("city");
        distance = getIntent().getExtras().getString("distance");

        //Toast.makeText(getApplicationContext(), city, Toast.LENGTH_SHORT).show();

        ListView listView = findViewById(R.id.list);
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset(this));
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    if (jsonObject != null) {
                        String title = jsonObject.getString("id")+" "+jsonObject.getString("name");
                        //cities.add(title);
                        if (city.equals(title)) {
                            JSONObject coord = jsonObject.getJSONObject("coord");
                            lon = coord.getDouble("lon");
                            lat = coord.getDouble("lat");
                        }
                    }
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    if (jsonObject != null) {
                        String title = jsonObject.getString("id")+" "+jsonObject.getString("name");
                        JSONObject coord = jsonObject.getJSONObject("coord");
                        double elon = coord.getDouble("lon");
                        double elat = coord.getDouble("lat");
                        //cities.add(title);
                        Location.distanceBetween(lat, lon, elat, elon, result);
                        if (result[0]/1000 < Float.parseFloat(distance)) {
                            cities.add(title);
                        }
                        //Toast.makeText(getApplicationContext(), Float.toString(result[0]), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ошибка чтения", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, cities);
        listView.setAdapter(adapter);
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("RU.city.list.min.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}



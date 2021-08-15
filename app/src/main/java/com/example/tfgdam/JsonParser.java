package com.example.tfgdam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String,String> parseJsonObject(JSONObject object){
        //Inicializamos HashMap
        HashMap<String, String> dataList = new HashMap<>();
        try {
            //Get name from object
            String name = object.getString("name");
            //Get direccion from object
            String direccion = object.getString("vicinity");
            //Get latitude from object
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            //Get longitude from object
            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");
            //Put all values in HashMap
            dataList.put("name", name);
            dataList.put("direccion", direccion);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return HashMap
        return dataList;
    }
    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        //Initialize HashMap List
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for(int i = 0; i<jsonArray.length(); i++){
            try{
                //Inicializamos HashMap
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //Add in HashMap Lits
                dataList.add(data);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        //Return HashMap list
        return dataList;
    }
    public List<HashMap<String,String>> parseResult(JSONObject object){
        //Iniciamos Json array
        JSONArray jsonArray = null;

        try {
            //Get result array
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return Array
        return parseJsonArray(jsonArray);
    }
}

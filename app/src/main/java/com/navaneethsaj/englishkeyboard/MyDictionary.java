package com.navaneethsaj.englishkeyboard;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MyDictionary {
    JSONObject top_words, all_words;
    public MyDictionary(Context context) {
        top_words = loadJSONFromAsset(context, "top_words.json");
        all_words = loadJSONFromAsset(context, "all_words.json");
    }

    public JSONObject loadJSONFromAsset(Context context, String filename) {
        String json_string = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json_string = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("json " + filename, json_string);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public ArrayList<Object> get_dict_suggestions(String s){
        ArrayList<Object> result = get_suggestions(s, top_words);
        if ((boolean)result.get(0) == true){
            return result;
        }else {
            result = get_suggestions(s, all_words);
            return result;
        }
    }

    private ArrayList<Object> get_suggestions(String word, JSONObject dictionary){
        ArrayList<String> list_of_words = new ArrayList<>();
        ArrayList<String> routes = new ArrayList<>();
        JSONObject tempDictionary = dictionary;
        for(int i=0; i< word.length(); ++i){
            String c = word.charAt(i) + "";
            if (tempDictionary.has(c)) {
                try {
                    tempDictionary = tempDictionary.getJSONObject(c);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                routes.add(c);
            }
            else{
                Log.d("action", "not found");
                ArrayList<Object> result = new ArrayList<>();
                result.add(false);
                result.add(new ArrayList<>());
                return result;
            }
        }
        Log.d("Action", tempDictionary.toString());
        get_all_words(list_of_words, routes, tempDictionary, 0);
        Log.d("action", "found");
        Log.d("Action", Arrays.toString(list_of_words.toArray()));
        ArrayList<Object> result = new ArrayList<>();
        result.add(true);
        result.add(list_of_words);
        return result;
    }

    public void get_all_words(ArrayList<String> list_of_words_1, ArrayList<String> routes_1,
                              JSONObject dictionary_1, int depth){
        if (list_of_words_1.size() > 10){
            return;
        }
        JSONArray keys = dictionary_1.names();
        if (keys != null){
            for (int i=0; i<keys.length(); ++i){
                String key = null;
                try {
                    key = keys.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("action", key);
                if (key.equals("word")){
                    String word = "";
                    for (String s : routes_1)
                    {
                        word += s;
                    }
                    list_of_words_1.add(word);
                    continue;
                }

                JSONObject dictionary_new = null;
                try {
                    dictionary_new = dictionary_1.getJSONObject(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<String> routes_new = new ArrayList<>(routes_1);
                routes_new.add(key);
                get_all_words(list_of_words_1, routes_new, dictionary_new, depth + 1);
            }
        }

    }


}

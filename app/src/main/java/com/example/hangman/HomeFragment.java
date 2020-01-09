package com.example.hangman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hangman.DAL.DbContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    DbContext dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = new DbContext(getContext());
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String lastupdate = preferences.getString("lastupdate",null);
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        //Log.i("HomeFragment: ", lastupdate+" - "+today+" - "+!lastupdate.equals(today));
        if (lastupdate == null || !lastupdate.equals(today)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lastupdate", today);
            WordDownloader downloader = new WordDownloader();
            downloader.execute();
            editor.commit();
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private class WordDownloader extends AsyncTask<Integer, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(progressBar,params);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            // First get words from online spreadsheet
            ArrayList<String> words = new ArrayList<>();
            try {
                URL url = new URL("https://script.google.com/macros/s/AKfycbwz3emqXgCsKTNFOaLGGe-Ps-FYj477VkE-OCW5vj-pm3Y3JZg/exec");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                JSONObject obj = new JSONObject(reader.readLine());
                JSONArray arr = obj.getJSONArray("words");
                for (int i = 0; i<arr.length();i++){
                    String word = arr.getString(i);
                    words.add(word);
                }

                dbHelper.insertWords(words);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }




            //Update progress on every word

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

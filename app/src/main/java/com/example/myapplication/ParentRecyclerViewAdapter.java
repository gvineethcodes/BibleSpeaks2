package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<ParentRecyclerViewAdapter.MyViewHolder> {
    ArrayList<ChildModel> arrayList;
    ArrayList<ParentModel> parentModelArrayList;
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public ParentRecyclerViewAdapter(ArrayList<ParentModel> parentModelArrayList, MainActivity mainActivity) {
        this.parentModelArrayList=parentModelArrayList;
        this.context=mainActivity;

        sharedpreferences = context.getSharedPreferences("" + R.string.app_name, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_recyclerview_items, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView artist;
        public RecyclerView childRecyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);

            artist = itemView.findViewById(R.id.artist);
            childRecyclerView = itemView.findViewById(R.id.Child_RV);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ParentModel artist = parentModelArrayList.get(position);
        holder.artist.setText(artist.getArtist());

        LinearLayoutManager parentLayoutManager = new LinearLayoutManager(holder.childRecyclerView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        holder.childRecyclerView.setLayoutManager(parentLayoutManager);

        arrayList = new ArrayList<>();
        ChildRecyclerViewAdapter childRecyclerViewAdapter = new ChildRecyclerViewAdapter(arrayList, holder.childRecyclerView.getContext());
        holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);

        Set<String> albumsSet = sharedpreferences.getStringSet(artist.getArtist(),null);
        ArrayList<String> albums = new ArrayList<>(albumsSet);
        Collections.sort(albums);
        for(String album : albums){
            String albumImage = sharedpreferences.getString(artist.getArtist()+"/"+album+"/image","");
            arrayList.add(new ChildModel(artist.getArtist(), album, albumImage));
        }

/*
        class ReadFile extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(@NonNull String... params) {
                HttpURLConnection conn = null;
                try {
                    String urlString = "https://docs.google.com/spreadsheets/d/1VxQruR4Yt1Ive6qLqQZ2iV7qCr4x9GRL49yA9XM5GP8/export?format=csv";

                    URL url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();

                    if(conn.getResponseCode() == 200)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        String inputLine;
                        while ((inputLine = br.readLine()) != null) {
                            //Log.e("mylog", inputLine + "\n" + artist.getArtist());
                            //Log.e("mylog", ""+inputLine.startsWith(artist.getArtist()));
                            String[] AS = inputLine.split("ALBUM");

                            for(String as : AS){
                                String albumName = " ",albumImage = " ";
                                ArrayList<String> albumItems = new ArrayList<>();
                                HashMap<String, String> albumItemLinks = new HashMap<>();
                                String[] AS1 = as.split(",");
                                if(AS1.length>1){
                                    albumName = AS1[1];
                                    albumImage = AS1[AS1.length-1];
                                }
                                for(int i =2;i<AS1.length-1;i++){
                                    if(i%2==0) {
                                        albumItems.add(AS1[i]);
                                    }
                                    else {
                                        albumItemLinks.put(AS1[i-1],AS1[i]);
                                    }

                                }

                                if(albumItems.size()>0)
                                    arrayList.add(new ChildModel(artist.getArtist(), albumName, albumImage, albumItems, albumItemLinks));
//                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);
//                                    }
//                                });
                            }

                        }

                    }
                }catch (Exception e){
                    Log.e("mylog", e.toString());
                }
                finally
                {
                    if(conn!=null)
                        conn.disconnect();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {

            }

            @Override
            protected void onPreExecute() {}

        }

        new ReadFile().execute("");
*/

    }

    @Override
    public int getItemCount() {
        return parentModelArrayList.size();
    }
}
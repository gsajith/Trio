package com.trioshows.trio;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.volley.adapter.CustomListAdapter;
import com.volley.app.AppController;
import com.volley.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends ListFragment {
    // Log tag
    private static final String TAG = ListViewFragment.class.getSimpleName();
    // Movies json url
    private String url = "http://api.seatgeek.com/2/events/";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;


    public ListViewFragment() {
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), "You clicked something at " + position + " and id " + id,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        adapter = new CustomListAdapter(this.getActivity(), movieList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this.getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        JsonObjectRequest movieReq = new JsonObjectRequest(
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        try {
                            JSONArray eventArry = response.getJSONArray("events");
                            for (int i = 0; i < eventArry.length(); i++) {
                                JSONObject obj = eventArry.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                JSONArray performers = obj.getJSONArray("performers");
                                JSONObject performer1 = performers.getJSONObject(0);

                                movie.setThumbnailUrl(performer1.getString("image"));
                                //movie.setRating(((Number) obj.get("score"))
                                //       .doubleValue());
                                //movie.setYear(obj.getInt("releaseYear"));
                                movie.setTime(obj.getString("datetime_local"));
                                movie.setPrice(obj.getString("score"));
                                JSONObject venue = obj.getJSONObject("venue");

                                String stage = venue.getString("name");
                                String address = venue.getString("extended_address");
                                String location = stage + ", " + address;




                                movie.setLocation(location);
                                /*ArrayList<String> genre = new ArrayList<String>();
                                movie.setGenre(genre);*/
                                movieList.add(movie);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}

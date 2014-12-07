package com.trioshows.trio;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.database.handler.DatabaseHandler;
import com.volley.adapter.CustomListAdapter;
import com.volley.app.AppController;
import com.volley.model.Event;
import com.volley.util.EndlessListView;
import com.volley.util.FadingNetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.DateFormatSymbols;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class ListViewFragment extends ListFragment implements EndlessListView.EndlessListener{
    public static final String PREFS_NAME = "TrioApp";
    // Log tag
    private static final String TAG = ListViewFragment.class.getSimpleName();
    // Events json url
    private String url = "http://api.seatgeek.com/2/events?venue.state=MI&listing_count.gt=0&per_page=10";
    private int current_page = 1;
    private ProgressDialog pDialog;
    private List<Event> eventList = new ArrayList<Event>();
    private EndlessListView listView;
    private CustomListAdapter adapter;




    public ListViewFragment() {
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailPageActivity.class);
        intent.putExtra("title", eventList.get(position).getTitle());
        intent.putExtra("listingCount", eventList.get(position).getListingCount());
        intent.putExtra("location", eventList.get(position).getLocation());
        intent.putExtra("popularity", eventList.get(position).getPopularity());
        intent.putExtra("price", eventList.get(position).getPrice());
        intent.putExtra("thumbnailUrl", eventList.get(position).getThumbnailUrl());
        intent.putExtra("time", eventList.get(position).getTime());
        intent.putExtra("ticketUrl", eventList.get(position).getTicketURL());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (EndlessListView) rootView.findViewById(android.R.id.list);
        listView.setLoadingView(R.layout.loading_layout);
        adapter = new CustomListAdapter(this.getActivity(), eventList);
        listView.setUrl(url);
        listView.setCurrentPage(current_page);
        listView.setAdapter(adapter);
        listView.setListener(this);
        pDialog = new ProgressDialog(this.getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        String search = this.getArguments().getString("message");
        url = url +"&q=" + search;


        makeJSONRequest(url + "&page=" + current_page);
        return rootView;
    }

    private void makeJSONRequest(String url) {
        JsonObjectRequest eventReq = new JsonObjectRequest(
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        List<Event> tempEventList = new ArrayList<Event>();
                        try {
                            JSONArray eventArry = response.getJSONArray("events");
                            for (int i = 0; i < eventArry.length(); i++) {
                                JSONObject obj = eventArry.getJSONObject(i);

                                 if(obj.getString("type").equals("band") || obj.getString("type").equals("concert")){

                                Event event = new Event();

                                event.setTitle(obj.getString("title"));
                                event.setId(obj.getString("id"));

                                JSONObject avePrice = obj.getJSONObject("stats");

                                float neatStringF = Float.parseFloat(avePrice.getString("average_price"));
                                String neatStringD = String.format("%.2f", neatStringF);
                                String neatPrice = "$" + neatStringD;
                                event.setPrice(neatPrice);////////////////
                                JSONArray performers = obj.getJSONArray("performers");
                                JSONObject performer1 = performers.getJSONObject(0);

                                event.setThumbnailUrl(performer1.getString("image"));
                                String unparsedTime = obj.getString("datetime_local");
                                String delims = "[-, T]+";


                                String[] parsedTime = unparsedTime.split(delims);

                                String monthName = new DateFormatSymbols().getMonths()[Integer.parseInt(parsedTime[1]) - 1];

                                String neatTime = monthName + " " + parsedTime[2] + ", " + parsedTime[0];
                                if (parsedTime[3].equals("03:30:00")) {
                                    neatTime += " - Time TBD";
                                } else {
                                    DateFormat f1 = new SimpleDateFormat("hh:mm:ss");
                                    Date d = f1.parse(parsedTime[3]);
                                    DateFormat f2 = new SimpleDateFormat("h:mma");
                                    neatTime += ", " + f2.format(d);
                                    ;
                                }
                                event.setTime(neatTime);///////////
                                JSONObject venue = obj.getJSONObject("venue");
                                String stage = venue.getString("name");
                                String address = venue.getString("extended_address");
                                String location = stage + ", " + address;

                                event.setLocation(location);/////////////

                                JSONObject stats = obj.getJSONObject("stats");
                                String listingCount = stats.getString("listing_count");
                                event.setListingCount(listingCount);
                                event.setTicketURL(obj.getString("url"));
                                event.setPopularity(obj.getString("score"));

                                tempEventList.add(event);
                            }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listView.addNewData(tempEventList);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(eventReq);
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

    @Override
    public void loadData(String url) {
        makeJSONRequest(url);
    }
}

package com.trioshows.trio;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.database.handler.DatabaseHandler;
import com.volley.adapter.CustomListAdapter;
import com.volley.model.Event;
import com.volley.util.EndlessListView;
import com.volley.util.FadingNetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends ListFragment implements EndlessListView.EndlessListener {

    public static final String PREFS_NAME = "TrioApp";
    // Log tag
    private static final String TAG = ListViewFragment.class.getSimpleName();
    private List<Event> eventList = new ArrayList<Event>();
    private EndlessListView listView;
    private CustomListAdapter adapter;

    private String url = "http://api.seatgeek.com/2/events?venue.state=MI&listing_count.gt=0&per_page=10";
    private int current_page = 1;
    public SavedFragment(){}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FadingNetworkImageView img = (FadingNetworkImageView) v.findViewById(R.id.thumbnail);
        DatabaseHandler db = new DatabaseHandler(this.getActivity());
        String idnum = ((TextView) v.findViewById(R.id.id)).getText().toString();
        String title = ((TextView) v.findViewById(R.id.title)).getText().toString();
        String url = img.getUrl();
        String price = ((TextView) v.findViewById(R.id.price)).getText().toString();
        String location_text = ((TextView) v.findViewById(R.id.location_text)).getText().toString();
        String time_text = ((TextView) v.findViewById(R.id.time_text)).getText().toString();
        Event event = new Event(title, url, price, location_text, time_text, idnum);
        db.deleteEvent(event);
        Toast.makeText(getActivity(), "You unsaved " + title + " " + idnum,
                Toast.LENGTH_SHORT).show();
        for(int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId().equals(idnum)) {
                eventList.remove(i);
                adapter.notifyDataSetChanged();
                listView.doneLoading();
                return;
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.saved_fragment, container, false);
        listView = (EndlessListView) rootView.findViewById(android.R.id.list);
        listView.setLoadingView(R.layout.loading_layout);
        adapter = new CustomListAdapter(this.getActivity(), eventList);
        listView.setUrl(url);
        listView.setCurrentPage(current_page);
        listView.setAdapter(adapter);
        listView.setListener(this);

        DatabaseHandler db = new DatabaseHandler(this.getActivity());
        List<Event> events = db.getAllEvents();
        eventList.addAll(events);
        listView.doneLoading();
        return rootView;
    }

    @Override
    public void loadData(String url) {

    }
}

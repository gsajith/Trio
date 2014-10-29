package com.volley.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.database.handler.DatabaseHandler;
import com.trioshows.trio.R;
import com.volley.app.AppController;
import com.volley.model.Event;
import com.volley.util.FadingNetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Event> eventItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Event> eventItems) {
        this.activity = activity;
        this.eventItems = eventItems;
    }

    public void addAll(List<Event> newEvents) {
        eventItems.addAll(newEvents);
    }

    @Override
    public int getCount() {
        return eventItems.size();
    }

    @Override
    public Object getItem(int location) {
        return eventItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        FadingNetworkImageView thumbNail = (FadingNetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView location_text = (TextView) convertView.findViewById(R.id.location_text);
        TextView time_text = (TextView) convertView.findViewById(R.id.time_text);

        // getting movie data for the row
        Event m = eventItems.get(position);
        // thumbnail image
        if (m.getThumbnailUrl() == null || m.getThumbnailUrl().equals("null")) {
            thumbNail.setBackground(activity.getResources().getDrawable(R.drawable.placeholder));
            thumbNail.shrink();
        } else {
            thumbNail.setBackground(null);
            thumbNail.unshrink();
        }

        id.setText(m.getId());

        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        // title
        title.setText(m.getTitle());

        // time
        time_text.setText(m.getTime());

        //price

        price.setText(m.getPrice());

        //location

        location_text.setText(m.getLocation());

        ImageView saveButton = (ImageView) convertView.findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Event thisEvent = eventItems.get(position);
            FadingNetworkImageView img = (FadingNetworkImageView) v.findViewById(R.id.thumbnail);
            DatabaseHandler db = new DatabaseHandler(activity);
            String idnum = thisEvent.getId();
            String title = thisEvent.getTitle();
            String url = thisEvent.getThumbnailUrl();
            String price = thisEvent.getPrice();
            String location_text = thisEvent.getLocation();
            String time_text = thisEvent.getTime();
            String listing_count = thisEvent.getListingCount();
            String ticket_url = thisEvent.getTicketURL();
            String popularity = thisEvent.getPopularity();
            Event event = new Event(title, url, price, location_text, time_text, idnum, listing_count, ticket_url, popularity );
            if (db.getEvent(idnum) != null) {
              db.deleteEvent(event);
              Toast.makeText(activity, "You unsaved " + title + " " + idnum,
                Toast.LENGTH_SHORT).show();
            } else {
              db.addEvent(event);
              Toast.makeText(activity, "You saved " + title + " " + idnum,
                Toast.LENGTH_SHORT).show();
            }
          }
        });


        // rating
        //rating.setText("Rating: " + String.valueOf(m.getRating()));

        // genre
        /*String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);*/

        // release year
        //year.setText(String.valueOf(m.getYear()));

        return convertView;
    }

}
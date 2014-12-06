package com.volley.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.text.format.Time;
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
        ImageView addToCalendarButton = (ImageView) convertView.findViewById(R.id.plus);

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

        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Event thisEvent = eventItems.get(position);
            Calendar cal = Calendar.getInstance();
            String input = thisEvent.getTime();
            long timeStart = cal.getTimeInMillis();
            long timeEnd = cal.getTimeInMillis()+(60*60*1000);
            String monthString = input.substring(0, input.indexOf(" "));
            String rest = input.substring(input.indexOf(" ")+1);
            int day = Integer.parseInt(rest.substring(0, rest.indexOf(",")));
            rest = rest.substring(rest.indexOf(",")+2);
            int year = Integer.parseInt(rest.substring(0, rest.indexOf(",")));
            int mo = monthString.equals("January")? Calendar.JANUARY :
              monthString.equals("February")? Calendar.FEBRUARY :
                monthString.equals("March")? Calendar.MARCH :
                  monthString.equals("April")? Calendar.APRIL :
                    monthString.equals("May")? Calendar.MAY :
                      monthString.equals("June")? Calendar.JUNE :
                        monthString.equals("July")? Calendar.JULY :
                          monthString.equals("August")? Calendar.AUGUST :
                            monthString.equals("Septemper")? Calendar.SEPTEMBER :
                              monthString.equals("October")? Calendar.OCTOBER :
                                monthString.equals("November")? Calendar.NOVEMBER :
                                  monthString.equals("December")? Calendar.DECEMBER : 0;
            Calendar c = Calendar.getInstance();
            c.set(year, mo, day);
            long then = c.getTimeInMillis();
            timeStart = then;
            timeEnd = then+(60*60*1000);
            if (!input.contains("Time TBD")) {
              rest = rest.substring(rest.indexOf(",")+2);
              int hour = Integer.parseInt(rest.substring(0, rest.indexOf(":")));
              if (rest.contains("PM")) {
                hour += 12;
              }
              rest = rest.substring(0, rest.length()-2);
              rest = rest.substring(rest.indexOf(":")+1);
              int minute = Integer.parseInt(rest);
              c = Calendar.getInstance();
              c.set(year, mo, day, hour, minute);
              then = c.getTimeInMillis();
              timeStart = then;
              timeEnd = then+(60*60*1000);
            }
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", timeStart);
            intent.putExtra("allDay", false);
            intent.putExtra("endTime", timeEnd);
            intent.putExtra("title", thisEvent.getTitle());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, thisEvent.getLocation());
            activity.startActivity(intent);

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
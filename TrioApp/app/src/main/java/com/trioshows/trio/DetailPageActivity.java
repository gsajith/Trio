package com.trioshows.trio;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.trioshows.trio.R;
import com.volley.app.AppController;
import com.volley.model.Event;
import com.volley.util.FadingNetworkImageView;

import android.view.View;
import android.content.Intent;
import android.net.Uri;

public class DetailPageActivity extends Activity {



    public String title;
    public String listingCount;
    public String location;
    public String popularity;
    public String price;
    public String thumbnailUrl;
    public String time;
    public String ticketUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_page);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            listingCount = extras.getString("listingCount");
            location = extras.getString("location");
            popularity = extras.getString("popularity");
            price = extras.getString("price");
            thumbnailUrl = extras.getString("thumbnailUrl");
            time = extras.getString("time");
            ticketUrl = extras.getString("ticketUrl");

            TextView titleDetail= (TextView) findViewById(R.id.titleDetail);
            titleDetail.setText(title + "\n");

            TextView timeDetail= (TextView) findViewById(R.id.timeDetail);
            timeDetail.setText("TIME: " + time + "\n");

            TextView listingCountDetail= (TextView) findViewById(R.id.listingCountDetail);
            listingCountDetail.setText("TICKETS LEFT: " + listingCount + "\n" );

            TextView locationDetail= (TextView) findViewById(R.id.locationDetail);
            locationDetail.setText("LOCATION: " + location + "\n");


            float floatRate = Float.parseFloat(popularity);
            int newPop = Math.round(floatRate * 100);
            TextView popularityDetail= (TextView) findViewById(R.id.popularityDetail);
            popularityDetail.setText("RATING: " + newPop + "\n");

            TextView priceDetail= (TextView) findViewById(R.id.priceDetail);
            priceDetail.setText("AVERAGE PRICE: " + price + "\n");

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            FadingNetworkImageView imageDetail = (FadingNetworkImageView) findViewById(R.id.imageDetail);
            imageDetail.setImageUrl(thumbnailUrl, imageLoader);

        }
    }

    public void buyTick(View view) {
        Uri uri = Uri.parse(ticketUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
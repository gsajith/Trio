package com.volley.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by gsajith on 10/3/2014.
 */
public class FadingNetworkImageView extends NetworkImageView{
    public FadingNetworkImageView(Context context) {
        super(context);
    }

    public FadingNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadingNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                new ColorDrawable(android.R.color.transparent),
                new BitmapDrawable(getContext().getResources(), bm)
        });
        setImageDrawable(td);
        td.startTransition(200);
    }
}

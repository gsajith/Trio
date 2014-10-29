package com.volley.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.trioshows.trio.R;

import java.util.Random;

/**
 * Created by gsajith on 10/3/2014.
 */
public class FadingNetworkImageView extends NetworkImageView{
    private static final int FADE_TIME_IN_MS = 200;
    private boolean measured = false;
    private boolean needsToShrink = false;
    private boolean shrinked = false;
    private boolean needsToUnshrink = false;
    private String mUrl = "";

    public FadingNetworkImageView(Context context) {
        super(context);
        Random random = new Random();
        int r = random.nextInt(155);
        r += 100;
        setBackgroundColor(Color.rgb(r,r,r));
    }

    public FadingNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Random random = new Random();
        int r = random.nextInt(155);
        r += 100;
        setBackgroundColor(Color.rgb(r, r, r));

    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        super.setImageUrl(url, imageLoader);
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measured = true;
        int w = this.getMeasuredWidth();
        if (needsToShrink || shrinked) {
            needsToShrink = false;
            shrinked = true;
            setMeasuredDimension(w, w/2);
        } else if (needsToUnshrink || !shrinked) {
            needsToUnshrink = false;
            shrinked = false;
            setMeasuredDimension(w, w);
        } else {
            setMeasuredDimension(w, w);
        }

    }

    public void unshrink() {
        if (measured) {
            int w = this.getMeasuredWidth();
            setMeasuredDimension(w, w);
            shrinked = false;
        } else {
            needsToUnshrink = true;
            needsToShrink = false;
        }
    }
    public void shrink() {
        if (measured) {
            int w = this.getMeasuredWidth();
            setMeasuredDimension(w, w/2);
            shrinked = true;
        } else {
            needsToShrink = true;
            needsToUnshrink = false;
        }
    }
    public FadingNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Random random = new Random();
        int r = random.nextInt(155);
        r += 100;
        setBackgroundColor(Color.rgb(r, r, r));
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                new ColorDrawable(android.R.color.transparent),
                new BitmapDrawable(getContext().getResources(), bm)
        });
        setImageDrawable(td);
        td.startTransition(FADE_TIME_IN_MS);
    }

}

package com.volley.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.trioshows.trio.R;

import java.util.Random;

/**
 * Created by gsajith on 10/3/2014.
 */
public class FadingNetworkImageView extends NetworkImageView{
    private static final int FADE_TIME_IN_MS = 200;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = this.getMeasuredWidth();


        setMeasuredDimension(w, w);

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

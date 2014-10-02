package com.trioshows.trio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class TitleActivity extends Activity {
    private static final int RightToLeft = 1;
    private static final int LeftToRight = 2;
    private static final int DURATION = 60000;
    private ValueAnimator mCurrentAnimator;
    private final Matrix mMatrix = new Matrix();
    private ImageView mImageView;
    private float mScaleFactor;
    private int mBackgroundDirection = RightToLeft;
    private RectF mDisplayRect = new RectF();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_title);

        mImageView = (ImageView) findViewById(R.id.titlebgs);

        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mScaleFactor = 1;
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mImageView.setImageMatrix(mMatrix);
                animateBackground();
            }
        });

    }

    private void animateBackground() {
        updateDisplayRect();
        if(mBackgroundDirection == RightToLeft) {
            animate(mDisplayRect.left, mDisplayRect.left - (mDisplayRect.right - mImageView.getWidth()));
        } else {
            animate(mDisplayRect.left - (mDisplayRect.right - mImageView.getWidth()), mDisplayRect.left);
        }
    }

    private void animate(float from, float to) {
        MatrixImageView matrixImageView = new MatrixImageView(mImageView, mScaleFactor);
        mCurrentAnimator = ObjectAnimator.ofFloat(matrixImageView, "matrixTranslateX", from, to);
        mCurrentAnimator.setDuration(DURATION);
        mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mBackgroundDirection == RightToLeft)
                    mBackgroundDirection = LeftToRight;
                else
                    mBackgroundDirection = RightToLeft;

                animateBackground();
            }
        });
        mCurrentAnimator.start();
    }

    class MatrixImageView {
        private final ImageView mImageView;
        private float mScaleFactor;
        private final Matrix mMatrix = new Matrix();

        public MatrixImageView(ImageView imageView, float scaleFactor) {
            this.mImageView = imageView;
            this.mScaleFactor = scaleFactor;
        }

        public void setMatrixTranslateX(float dx) {
            mMatrix.reset();
            mMatrix.postScale(mScaleFactor, mScaleFactor);
            mMatrix.postTranslate(dx, 0);
            mImageView.setImageMatrix(mMatrix);
        }
    }

    private void updateDisplayRect() {
        mDisplayRect.set(0, 0, mImageView.getDrawable().getIntrinsicWidth(), mImageView.getDrawable().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.title, menu);
        return true;
    }

    public void startApp(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

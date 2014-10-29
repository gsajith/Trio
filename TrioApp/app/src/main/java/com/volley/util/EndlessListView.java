package com.volley.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.volley.adapter.CustomListAdapter;
import com.volley.model.Event;

import java.util.List;

/**
 * Created by gsajith on 10/4/2014.
 */
public class EndlessListView extends ListView implements AbsListView.OnScrollListener {
    private View loading_view;
    private boolean isLoading = false;
    private EndlessListener listener;
    private CustomListAdapter adapter;
    private String url;
    private int currentPage;
    private float VELOCITY_SCALE_FACTOR = 0.5f;

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
        this.setVelocityScale(VELOCITY_SCALE_FACTOR);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
        this.setVelocityScale(VELOCITY_SCALE_FACTOR);
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
        this.setVelocityScale(VELOCITY_SCALE_FACTOR);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage > 0) {
            this.currentPage = currentPage;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (getAdapter() == null) {
            return;
        }

        if (getAdapter().getCount() == 0) {
            return;
        }

        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading) {
            this.addFooterView(loading_view);
            isLoading = true;
            listener.loadData(url + "&page=" + ++currentPage);
        }
    }

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loading_view = (View) inflater.inflate(resId, null);
        this.addFooterView(loading_view);
    }

    public void setAdapter(CustomListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(loading_view);
    }

    public void addNewData(List<Event> data) {
        this.removeFooterView(loading_view);

        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public void doneLoading() {
        isLoading = false;
        this.removeFooterView(loading_view);
    }

    public void setListener(EndlessListener listener) {

        this.listener = listener;
    }

    public static interface EndlessListener {
        public void loadData(String url);
    }

}

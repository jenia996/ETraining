package com.example.ajax.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.view.BaseActivity;
import com.example.ajax.myapplication.view.adapters.impl.BookAdapter;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN_ACTIVTY";
    private SearchView searchView;
    private MainPresenter mainPresenter;
    private BookAdapter adapter;
    private RecyclerView recyclerView;
    private int mCurrentPage = 1;
    private LinearLayoutManager mLayoutManager;
    private View activityView;
    private Boolean mNewRequest = true;
    private boolean mIsScrolledByUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        activityView = findViewById(R.id.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .show();
            }
        });
        adapter = new BookAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.list_books);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // position before loading more.
            int firstVisibleItem, visibleItemCount, totalItemCount;
            private int previousTotal = 0; // The total number of items in the dataset after the last load
            private boolean loading = true; // True if we are still waiting for the last set of data to load.
            private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    mIsScrolledByUser = true;
                }
                if (mIsScrolledByUser && newState == 0) {
                    if (mLayoutManager.findLastVisibleItemPosition() != recyclerView.getAdapter().getItemCount() - 1) {
                        recyclerView.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition());
                        mIsScrolledByUser = false;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    // Do something

                    mainPresenter.update(++mCurrentPage);
                    loading = true;
                }

            }
        });
      //  mainPresenter.onReady();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                mainPresenter.download(query);
                mNewRequest = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showResponce(List<Book> response) {
        if (mNewRequest) {
            adapter.setBooks(response);
            mNewRequest = false;
        } else {
            adapter.addBooks(response);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String responce) {
        Snackbar.make(activityView, responce, Snackbar.LENGTH_INDEFINITE).show();
    }
}



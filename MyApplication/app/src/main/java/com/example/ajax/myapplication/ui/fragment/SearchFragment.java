package com.example.ajax.myapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.BookAdapter;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.mvp.SearchView;
import com.example.ajax.myapplication.mvp.presenter.SearchPresenter;

import java.util.List;

/**
 * Created by Ajax on 18.11.2016.
 */

public class SearchFragment extends BaseFragment implements SearchView {

    private SearchPresenter mPresenter;
    private boolean mNewRequest;
    private BookAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mCurrentPage;

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                mPresenter.download(query);
                mNewRequest = true;
                showProgressDialog();
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new SearchPresenter(this);
        mAdapter = new BookAdapter();

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_search, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_books);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private final int visibleThreshold = 15; //TODO calculate depending on items on the screen
            boolean mIsScrolledByUser;
            int firstVisibleItem, visibleItemCount, totalItemCount;
            private int previousTotal;
            private boolean loading = true;

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mIsScrolledByUser = true;
                }
                if (mIsScrolledByUser && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mLayoutManager.findLastVisibleItemPosition() != recyclerView.getAdapter().getItemCount() - 1) {
                        recyclerView.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition());
                        mIsScrolledByUser = false;
                    }
                }
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    ++mCurrentPage;
                    mPresenter.update(mCurrentPage);
                    loading = true;
                }
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void showResponse(final List<Book> response) {
        hideProgressDialog();
        if (mNewRequest) {
            mAdapter.setBooks(response);
            mNewRequest = false;
        } else {
            mAdapter.addBooks(response);
        }
        mAdapter.notifyDataSetChanged();

    }
}

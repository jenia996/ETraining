package com.example.ajax.myapplication.ui.fragment;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.adapters.impl.CursorBookAdapter;
import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.presenter.WatchListPresenter;

public class WatchListFragment extends BaseFragment implements ResultView<Cursor> {

    private WatchListPresenter mPresenter;
    private CursorBookAdapter mAdapter;

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new WatchListPresenter(this);
        mAdapter = new CursorBookAdapter(getActivity(), createOnItemClickListener(), createDeleteListener());

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle
            savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search, container,
                false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_books);
        final Resources resources = getResources();
        final int add = resources.getInteger(R.integer.search_multiple);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), resources.getConfiguration()
                .orientation + add));
        recyclerView.setAdapter(mAdapter);
        mPresenter.download("");
        return view;

    }

    @Override
    public void showResponse(final Cursor response) {
        mAdapter.swapCursor(response);
        mAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener createDeleteListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Cursor item = mAdapter.getItem((Integer) v.getTag());

                mPresenter.deleteItem(item.getInt(item.getColumnIndex(Book.ID)), item.getInt(item.getColumnIndex
                        (Author.ID)));
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    private OnItemClickListener createOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onClick(final BookModel pBook) {

            }
        };
    }
}

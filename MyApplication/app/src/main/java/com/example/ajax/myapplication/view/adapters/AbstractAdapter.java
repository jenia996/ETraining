package com.example.ajax.myapplication.view.adapters;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ajax on 21.10.2016.
 */

public abstract class AbstractAdapter<Item> extends RecyclerView.Adapter<AbstractAdapter.AbstractViewHolder> {

    public abstract void onBind(AbstractViewHolder holder, Item item, int position, int viewType);

    public abstract Item getItem(int positoin);

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, final int position) {
        onBind(holder, getItem(position), position, getItemViewType(position));
    }

    public static class AbstractViewHolder extends RecyclerView.ViewHolder {

        private SparseArrayCompat<View> mViewSparseArray;

        public AbstractViewHolder(final View itemView, final int... ids) {
            super(itemView);

            mViewSparseArray = new SparseArrayCompat<>(ids.length);

            for (final int id : ids) {
                mViewSparseArray.append(id, itemView.findViewById(id));
            }
        }

        public <T> T get(final int id) {
            return (T) mViewSparseArray.get(id);
        }

    }

}


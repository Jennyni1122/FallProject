package com.jennyni.fallproject.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.jennyni.fallproject.adapter.WrapAdapter;

import java.util.ArrayList;

/**
 * Created by Jenny on 2019/2/25.
 */

public class WrapRecyclerView extends RecyclerView{
    private WrapAdapter mWrapAdapter;
    private boolean shouldAdjustSpanSize;
    //临时头部View集合,用于存储没有设置Adapter之前添加的头部
    private ArrayList<View> mTmpHeaderView = new ArrayList<>();
    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof WrapAdapter) {
            mWrapAdapter = (WrapAdapter) adapter;
            super.setAdapter(adapter);
        } else {
            mWrapAdapter = new WrapAdapter(adapter);
            for (View view : mTmpHeaderView) {
                mWrapAdapter.addHeaderView(view);
            }
            if (mTmpHeaderView.size() > 0) {
                mTmpHeaderView.clear();
            }
            super.setAdapter(mWrapAdapter);
        }
        if (shouldAdjustSpanSize) {
            mWrapAdapter.adjustSpanSize(this);
        }
        getWrappedAdapter().registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }
    /**
     * Retrieves the previously set wrap adapter or null if no adapter is set.
     */
    @Override
    public WrapAdapter getAdapter() {
        return mWrapAdapter;
    }
    public Adapter getWrappedAdapter() {
        if (mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        }
        return mWrapAdapter.getWrappedAdapter();
    }
    /**
     * Adds a header view
     */
    public void addHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null!");
        } else if (mWrapAdapter == null) {
            mTmpHeaderView.add(view);
        } else {
            mWrapAdapter.addHeaderView(view);
        }
    }
    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof GridLayoutManager || layout instanceof
                StaggeredGridLayoutManager){
            this.shouldAdjustSpanSize = true;
        }
    }
    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
        {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };


}

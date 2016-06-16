package com.alexandershtanko.psychotests.views.adapters;

import android.support.v7.widget.RecyclerView;

import com.alexandershtanko.psychotests.models.TestInfo;

/**
 * Created by aleksandr on 17.06.16.
 */
public class SortedCallback extends android.support.v7.util.SortedList.Callback<com.alexandershtanko.psychotests.models.TestInfo> {

    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter)
    {
        this.adapter = adapter;
    }

    @Override
    public int compare(TestInfo o1, TestInfo o2) {
        return 0;
    }

    @Override
    public void onInserted(int position, int count) {
        if(adapter!=null)
            adapter.notifyItemRangeInserted(position,count);
    }

    @Override
    public void onRemoved(int position, int count) {
        if(adapter!=null)
            adapter.notifyItemRangeRemoved(position,count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if(adapter!=null)
            adapter.notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onChanged(int position, int count) {
        if(adapter!=null)
            adapter.notifyItemChanged(position,count);
    }

    @Override
    public boolean areContentsTheSame(TestInfo oldItem, TestInfo newItem) {
        return false;
    }

    @Override
    public boolean areItemsTheSame(TestInfo item1, TestInfo item2) {
        return item1.getTestId().equals(item2.getTestId());
    }
}

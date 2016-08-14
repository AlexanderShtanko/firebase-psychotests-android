package com.alexandershtanko.psychotests.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alexandershtanko.psychotests.models.TestInfo;

/**
 * Created by aleksandr on 17.06.16.
 */
public class SortedCallback extends android.support.v7.util.SortedList.Callback<com.alexandershtanko.psychotests.models.TestInfo> {
    public SortType sortType = SortType.Date;
    private RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int compare(TestInfo o1, TestInfo o2) {
        long val1 = 0;
        long val2 = 0;


        if (!o1.getTestId().equals(o2.getTestId())) {
            if (o1.isTestOfDay())
                return -1;
            if (o2.isTestOfDay())
                return 1;
        }

        if (sortType == SortType.Date) {
            val1 = o1.getDateAdd();
            val2 = o2.getDateAdd();
        } else if (sortType == SortType.Popularity) {
            val1 = o1.getLikeCount();
            val2 = o2.getLikeCount();
        }


        return (val1 == val2 ? 0 : val1 > val2 ? -1 : 1);


    }

    @Override
    public void onInserted(int position, int count) {
        if (adapter != null) {
            adapter.notifyItemRangeInserted(position, count);
            Log.e("TAG", "size:" + adapter.getItemCount());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRemoved(int position, int count) {
        if (adapter != null)
            adapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        if (adapter != null)
            adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count) {
        if (adapter != null)
            adapter.notifyItemChanged(position, count);
    }

    @Override
    public boolean areContentsTheSame(TestInfo oldItem, TestInfo newItem) {
        return false;
    }

    @Override
    public boolean areItemsTheSame(TestInfo item1, TestInfo item2) {
        return item1.getTestId().equals(item2.getTestId());
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public enum SortType {Popularity, Date}
}

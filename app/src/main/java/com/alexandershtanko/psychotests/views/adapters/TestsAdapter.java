package com.alexandershtanko.psychotests.views.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {
    SortedList<TestInfo> list = new SortedList<TestInfo>(TestInfo.class, new SortedList.Callback<TestInfo>() {
        @Override
        public int compare(TestInfo o1, TestInfo o2) {
            return 0;
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(TestInfo oldItem, TestInfo newItem) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(TestInfo item1, TestInfo item2) {
            return false;
        }
    });

    public void add(TestInfo testInfo)
    {
        list.add(testInfo);
    }

    public void add(List<TestInfo> testInfoList)
    {
        list.addAll(testInfoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(list.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.category)
        TextView category;
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void populate(TestInfo testInfo) {
            name.setText(testInfo.getName());
            category.setText(testInfo.getCategory());
            Picasso.with(itemView.getContext()).load(testInfo.getImage()).into(image);

        }
    }
}
